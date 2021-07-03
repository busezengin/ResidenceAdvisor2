package com.kotlinegitim.residenceadvisor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class QuestionnaireActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    lateinit var listView: ListView
    val questionnaires : MutableList<String> = mutableListOf()
    val questionnaireIDs : MutableList<String> = mutableListOf()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionnaire)
        auth = FirebaseAuth.getInstance()
        val apartmentID=intent.getStringExtra("ApartmentID")
        if (apartmentID != null) {
            getQuestionnaire(apartmentID)
        }
        adapter= ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, questionnaires)
        listView = findViewById(R.id.questionnaireListView)
        listView.adapter = adapter
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            intent = Intent(this, VoteQuestionnaireActivity::class.java)
            intent.putExtra("ApartmentID",apartmentID)
            intent.putExtra("QuestionnaireID",questionnaireIDs[position])
            startActivity(intent)
        }


    }
    fun getQuestionnaire(apartmentID:String){
        db.collection("Questionnaires"+apartmentID)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        questionnaires.add(document.data.get("question") as String)
                        adapter.notifyDataSetChanged()
                        questionnaireIDs.add(document.id)
                    }

                }
                .addOnFailureListener { exception ->
                    Log.d("userdata", "Error getting documents: ", exception)
                }

    }

}