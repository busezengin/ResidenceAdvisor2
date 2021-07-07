package com.kotlinegitim.residenceadvisor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    lateinit var email: EditText
    lateinit var password: EditText

    lateinit var login: Button
    lateinit var register: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        if(auth.currentUser != null){
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        email = findViewById(R.id.editTextEmailAddress)
        password = findViewById(R.id.editTextPassword)
        login = findViewById(R.id.buttonLogin)
        register = findViewById(R.id.createnewac)

        login.setOnClickListener{
            loginUser()
        }

        register.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    private fun loginUser(){
        auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("deneme", "signInWithEmail:failure", task.exception)

                }
            }
    }
}