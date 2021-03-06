package com.kotlinegitim.residenceadvisor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AnnouncementActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    lateinit var listView: ListView
    val announcements : MutableList<String> = mutableListOf()
    private lateinit var adapter:ArrayAdapter<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_announcement)

        auth = FirebaseAuth.getInstance()
        val apartmentID=intent.getStringExtra("ApartmentID")
        if (apartmentID != null) {
            getAnnouncementData(apartmentID)
        }
        adapter= ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, announcements)
        listView = findViewById(R.id.announcementListView)
        listView.adapter = adapter

    }
    fun getAnnouncementData(apartmentID:String){
        db.collection("Announcements"+apartmentID)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        announcements.add(document.data.get("context") as String)
                        adapter.notifyDataSetChanged()
                    }

                }
                .addOnFailureListener { exception ->
                    Log.d("userdata", "Error getting documents: ", exception)
                }

    }
}