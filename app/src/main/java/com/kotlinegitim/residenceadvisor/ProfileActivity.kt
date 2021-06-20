package com.kotlinegitim.residenceadvisor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kotlinegitim.residenceadvisor.classes.User
import java.io.Console

class ProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    lateinit var nameSurname: TextView
    lateinit var eMail: TextView
    lateinit var apartmentName: TextView
    lateinit var apartmentNo: TextView
    lateinit var changePassword: Button

    lateinit var usr: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()

        nameSurname = findViewById(R.id.nameSurnameProfileTextView)
        eMail = findViewById(R.id.eMailProfileTextView)
        apartmentName = findViewById(R.id.apartmentNameProfileTextView)
        apartmentNo = findViewById(R.id.apartmentNoProfileTextView)
        changePassword = findViewById(R.id.changePasswordProfileButton)

        changePassword.setOnClickListener{
            //TODO
        }

        getUserProfileData()
    }
    fun getUserProfileData(){
        if(auth.currentUser != null){

            val docRef = db.collection("Users").document(auth.currentUser!!.uid)
            docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            usr = document.toObject(User::class.java)!!
                            showData()
                        } else {
                            Log.d("userdata", "No such document")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("userdata", "get failed with ", exception)
                    }

        }

    }

    fun showData(){
        var apartmentId= usr.apartmentID.toString()
        var apartmentNameString=""
        val docRef = db.collection("Apartments").document(apartmentId)
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        apartmentNameString = document.get("apartmentName") as String
                        apartmentName.text = "Apartment Name"+ apartmentNameString
                    } else {
                        Log.d("userdata", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("userdata", "get failed with ", exception)
                }




        nameSurname.text= "Name Surname:"+usr.firstname+ " "+usr.lastname
        eMail.text = "Email" + usr.email
        apartmentNo.text= "Apartment No" + usr.apartmentNumber

    }

}