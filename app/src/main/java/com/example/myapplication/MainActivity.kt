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

class MainActivity : AppCompatActivity() {

    lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mAuth = FirebaseAuth.getInstance()

        val email = findViewById<TextView>(R.id.login_email)
        val password = findViewById<TextView>(R.id.login_password)
        val loginButton = findViewById<Button>(R.id.login_button)
        var signbutton = findViewById<Button>(R.id.sign_up_button)
        var fpass = findViewById<TextView>(R.id.forgetpass)

        fpass.setOnClickListener { val intent = Intent(this , Forgot_pass_Activity::class.java)
            startActivity(intent)
            finish()
        }
        signbutton.setOnClickListener {
            val intent = Intent(this, Signup_Activity::class.java)
            startActivity(intent)
            finish()
        }

        loginButton.setOnClickListener {
            val semail = email.text.toString().trim()
            val spass = password.text.toString().trim()

            if (semail.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (spass.isEmpty()) {
                Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }



            mAuth.signInWithEmailAndPassword(semail, spass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser
                        if (user != null && user.isEmailVerified) {

                            val db = FirebaseFirestore.getInstance()
                            val safeEmail = semail.replace(".", "_")

                            db.collection("users").document(safeEmail).get()
                                .addOnSuccessListener { document ->
                                    if (document.exists()) {
                                        val role = document.getString("role")

                                        if (role == "admin") {

                                            val intent = Intent(this, AdminActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        } else {

                                            val intent = Intent(this, HomePage::class.java)
                                            startActivity(intent)
                                            finish()
                                        }

                                    } else {
                                        Toast.makeText(
                                            this,
                                            "User role not found.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        } else {

                            Toast.makeText(
                                this,
                                "Please verify your email first",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {

                        Toast.makeText(
                            this,
                            "Login failed: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

        }
    }
}