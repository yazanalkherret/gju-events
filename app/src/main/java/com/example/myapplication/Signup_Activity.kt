package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Signup_Activity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val alreadyGotAcc = findViewById<TextView>(R.id.already_got_acc)
        alreadyGotAcc.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        mAuth = FirebaseAuth.getInstance()

        val email = findViewById<TextView>(R.id.signemail)
        val pass = findViewById<TextView>(R.id.signpass)
        val confirmPass = findViewById<TextView>(R.id.signconfirmpass)
        val createBtn = findViewById<Button>(R.id.createacc)

        createBtn.setOnClickListener {
            val semail = email.text.toString().trim()
            val spass = pass.text.toString().trim()
            val sconf = confirmPass.text.toString().trim()

            if (semail.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!semail.endsWith("@gju.edu.jo")) {
                Toast.makeText(this, "Only GJU emails allowed", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (spass.isEmpty()) {
                Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (spass != sconf) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            mAuth.createUserWithEmailAndPassword(semail, spass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser
                        user?.sendEmailVerification()
                            ?.addOnCompleteListener { emailTask ->
                                if (emailTask.isSuccessful) {
                                    Toast.makeText(this, "Account created. Verification email sent.", Toast.LENGTH_LONG).show()
                                    val db = FirebaseFirestore.getInstance()
                                    val safeEmail = semail.replace(".", "_")

                                    val userInfo = hashMapOf(
                                        "email" to semail,
                                        "role" to "user",
                                        "certificate" to 1


                                    )

                                    db.collection("users").document(safeEmail).set(userInfo)

                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(this, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(this, "Sign up failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}
