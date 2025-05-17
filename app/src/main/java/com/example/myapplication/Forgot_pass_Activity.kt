package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

import android.widget.EditText



class Forgot_pass_Activity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_pass)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        mAuth = FirebaseAuth.getInstance()

        var forgotemail = findViewById<TextView>(R.id.email_forgot)
        var resetBtn = findViewById<Button>(R.id.send_link)

        resetBtn.setOnClickListener {
            var input_forgot_email = forgotemail.text.toString().trim()
            if (input_forgot_email.isEmpty()) {
                Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            if (!input_forgot_email.endsWith("@gju.edu.jo")) {
                Toast.makeText(this, "Only GJU emails allowed", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            mAuth.sendPasswordResetEmail(input_forgot_email).addOnCompleteListener { resettask ->
                if (resettask.isSuccessful) {
                    Toast.makeText(this, "Reset email sent. Check your inbox.", Toast.LENGTH_LONG)
                        .show()
                    var intent = Intent(this , MainActivity::class.java)
                    startActivity(intent)
                    finish()

                }   else {
                                Toast.makeText(this, "Reset failed: ${resettask.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }


        }
    }
}