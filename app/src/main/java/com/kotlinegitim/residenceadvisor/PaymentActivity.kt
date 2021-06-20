package com.kotlinegitim.residenceadvisor

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kotlinegitim.residenceadvisor.classes.User
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

class PaymentActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    lateinit var usr: User
    lateinit var  budget: TextView
    lateinit var payment: TextView
    lateinit var payButton: Button

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        auth = FirebaseAuth.getInstance()

        getUserData()
        budget = findViewById(R.id.budgetPaymentTextView)
        payment =findViewById(R.id.totalPaymentTextView)
        payButton = findViewById(R.id.payPaymentButton)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getUserData(){
        if(auth.currentUser != null){
            val docRef = db.collection("Users").document(auth.currentUser!!.uid)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        usr = document.toObject(User::class.java)!!
                        budget.text= "BUDGET: "+usr.budget.toString()
                        getApartmentDues()
                    } else {
                        Log.d("userdata", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("userdata", "get failed with ", exception)
                }

        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getApartmentDues(){
        val docRef = db.collection("Apartments").document(usr.apartmentID)
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        calculateDue(document.data?.get("duesAmount") as Any)
                    } else {
                        Log.d("userdata", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("userdata", "get failed with ", exception)
                }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateDue(dues: Any) {
        val dateStr = usr.lastDuesPaymentDate
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val date: Date = sdf.parse(dateStr)

        val now = DateTimeFormatter
                .ofPattern("dd-MM-yyyy")
                .withZone(ZoneOffset.UTC)
                .format(Instant.now())

        val date2: Date = sdf.parse(now)
        val diff: Long = date2.getTime() - date.getTime()
        payment.text= "TOTAL PAYMENT: "+(dues.toString().toInt()*((TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)/30).toInt())).toString()


    }
}