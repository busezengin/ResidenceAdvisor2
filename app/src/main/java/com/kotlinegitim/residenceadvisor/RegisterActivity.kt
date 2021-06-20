package com.kotlinegitim.residenceadvisor

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var name: EditText
    lateinit var surname: EditText
    lateinit var apartmentId: EditText
    lateinit var apartmentNumber: EditText
    lateinit var register: Button


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        email = findViewById(R.id.editTextRegisterEmail)
        password = findViewById(R.id.editTextRegisterPassword)
        name = findViewById(R.id.editTextRegisterName)
        surname = findViewById(R.id.editTextRegisterSurname)
        apartmentId = findViewById(R.id.editTextRegisterApartmentId)
        apartmentNumber = findViewById(R.id.editTextRegisterApartmentNumber)

        register = findViewById(R.id.registerButton)

        register.setOnClickListener{
            createUser()
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createUser(){
        auth.createUserWithEmailAndPassword(email.text.toString(),password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        sendUserData()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("deneme", "registerInWithEmail:failure", task.exception)

                    }
                }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendUserData(){
        val now = DateTimeFormatter
                .ofPattern("dd-MM-yyyy")
                .withZone(ZoneOffset.UTC)
                .format(Instant.now())

        val user = hashMapOf(
                //TODO User obje yapılacak
                "apartmentID" to apartmentId.text.toString(),
                "email" to email.text.toString(),
                "firstname" to name.text.toString(),
                "lastname" to surname.text.toString(),
                "apartmentNumber" to apartmentNumber.text.toString(),
                "role" to "resident",
                "budget" to Random.nextInt(500, 1500),
                "lastDuesPaymentDate" to now
        )
       if(auth.currentUser != null){
           db.collection("Users").document(auth.currentUser!!.uid)
                   .set(user)
                   .addOnSuccessListener { Log.d("deneme", "DocumentSnapshot successfully written!") }
                   .addOnFailureListener { e -> Log.w("deneme", "Error writing document", e) }
       }
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}