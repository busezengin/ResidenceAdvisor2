package com.kotlinegitim.residenceadvisor

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kotlinegitim.residenceadvisor.Adapters.MessageAdapter
import com.kotlinegitim.residenceadvisor.classes.Message
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    lateinit var messageBox: EditText
    lateinit var sendButton: Button
    lateinit var listView: ListView
    val messages : MutableList<com.kotlinegitim.residenceadvisor.classes.Message> = mutableListOf()
    lateinit var adapter: MessageAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        messageBox=findViewById(R.id.chatEditText)
        sendButton=findViewById(R.id.sendMessageButton)
        listView=findViewById(R.id.chatListView)



        auth = FirebaseAuth.getInstance()

        val apartmentID=intent.getStringExtra("ApartmentID")

        sendButton.setOnClickListener {
            if (messageBox.text!=null && !messageBox.text.toString().equals("") && apartmentID != null){
                sendMessageToDatabase(messageBox.text.toString(),apartmentID)
                messageBox.text.clear()
            }
        }

        getData(apartmentID.toString())
    }

    fun getData(apartmentID:String){
        db.collection("Messages"+apartmentID)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("deneme2", "listen:error", e)
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        messages.add(dc.document.toObject(Message::class.java))
                        adapter= MessageAdapter(messages as ArrayList<Message>?, this)
                        listView.adapter = adapter
                    }
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessageToDatabase(message:String, apartmentID:String){
        val now = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneOffset.UTC)
            .format(Instant.now())

        val tempMessage = com.kotlinegitim.residenceadvisor.classes.Message(message,
            auth.currentUser?.uid.toString(),now)
        db.collection("Messages"+apartmentID).document(now).set(tempMessage)
    }
}