package com.kotlinegitim.residenceadvisor

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class VoteQuestionnaireActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    lateinit var question: TextView
    lateinit var voteButton: Button
    lateinit var checkBox1: CheckBox
    lateinit var checkBox2: CheckBox
    lateinit var checkBox3: CheckBox
    lateinit var checkBox4: CheckBox
    lateinit var checkBox5: CheckBox
    lateinit var selectedAnswer: String

    val answers : MutableList<String> = mutableListOf()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vote_questionnaire)

        val apartmentID=intent.getStringExtra("ApartmentID")
        val questionnaireID=intent.getStringExtra("QuestionnaireID")

        question = findViewById(R.id.questionTextView)
        voteButton=findViewById(R.id.voteButton)
        checkBox1 = findViewById(R.id.checkBox1)
        checkBox2 = findViewById(R.id.checkBox2)
        checkBox3 = findViewById(R.id.checkBox3)
        checkBox4 = findViewById(R.id.checkBox4)
        checkBox5 = findViewById(R.id.checkBox5)
        adapter= ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, answers)

        if (apartmentID != null) {
            if (questionnaireID != null) {
                getAnswers(apartmentID,questionnaireID)
            }
        }
        checkBox1.setOnCheckedChangeListener { buttonView, isChecked ->
            if(checkBox1.isChecked==true){
                checkBox2.isChecked=false
                checkBox3.isChecked=false
                checkBox4.isChecked=false
                checkBox5.isChecked=false
                selectedAnswer = "answer1"
            }
        }
        checkBox2.setOnCheckedChangeListener { buttonView, isChecked ->
            if(checkBox2.isChecked==true){
                checkBox1.isChecked=false
                checkBox3.isChecked=false
                checkBox4.isChecked=false
                checkBox5.isChecked=false
                selectedAnswer = "answer2"
            }
        }
        checkBox3.setOnCheckedChangeListener { buttonView, isChecked ->
            if(checkBox3.isChecked==true){
                checkBox1.isChecked=false
                checkBox2.isChecked=false
                checkBox4.isChecked=false
                checkBox5.isChecked=false
                selectedAnswer = "answer3"
            }
        }
        checkBox4.setOnCheckedChangeListener { buttonView, isChecked ->
            if(checkBox4.isChecked==true){
                checkBox1.isChecked=false
                checkBox2.isChecked=false
                checkBox3.isChecked=false
                checkBox5.isChecked=false
                selectedAnswer = "answer4"
            }
        }
        checkBox5.setOnCheckedChangeListener { buttonView, isChecked ->
            if(checkBox5.isChecked==true){
                checkBox1.isChecked=false
                checkBox2.isChecked=false
                checkBox3.isChecked=false
                checkBox4.isChecked=false
                selectedAnswer = "answer5"
            }
        }

        voteButton.setOnClickListener{
            if (apartmentID != null) {
                if (questionnaireID != null) {
                    increaseCount(apartmentID, questionnaireID, selectedAnswer)
                    checkBox1.isEnabled=false
                    checkBox2.isEnabled=false
                    checkBox3.isEnabled=false
                    checkBox4.isEnabled=false
                    checkBox5.isEnabled=false
                    voteButton.visibility = View.INVISIBLE
                    onStop()
                }
            }
        }

    }

    fun getAnswers(apartmentID:String, questionnaireID:String){

        if (questionnaireID != null) {
            db.collection("Questionnaires"+apartmentID).document(questionnaireID)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        question.text = document.get("question") as String
                        val docMap = document.get("answers") as Map<String, *>
                        if(docMap.size>=1){
                            answers.add((docMap.get("answer1") as String).toString())
                            checkBox1.text=answers[0]
                            checkBox1.isVisible=true
                        }
                        if(docMap.size>=2){
                            answers.add((docMap.get("answer2") as String).toString())
                            checkBox2.text=answers[1]
                            checkBox2.isVisible=true
                        }
                        if(docMap.size>=3){
                            answers.add((docMap.get("answer3") as String).toString())
                            checkBox3.text=answers[2]
                            checkBox3.isVisible=true
                        }
                        if(docMap.size>=4){
                            answers.add((docMap.get("answer4") as String).toString())
                            checkBox4.text=answers[3]
                            checkBox4.isVisible=true
                        }
                        if(docMap.size>=5){
                            answers.add((docMap.get("answer5") as String).toString())
                            checkBox5.text=answers[4]
                            checkBox5.isVisible=true
                        }
                    }

                }
                .addOnFailureListener { exception ->
                    Log.d("userdata", "Error getting documents: ", exception)
                }
        }
    }
    fun increaseCount(apartmentID: String, questionnaireID: String, selectedAnswer: String){

        db.collection("Questionnaires"+apartmentID).document(questionnaireID)
            .update(mapOf(
                "answersVoteCount."+selectedAnswer+"Count" to FieldValue.increment(1)
            ))

    }
}


