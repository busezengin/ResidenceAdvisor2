package com.kotlinegitim.residenceadvisor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kotlinegitim.residenceadvisor.classes.User

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    lateinit var  toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var listView: ListView
    lateinit var usr: User
    lateinit var items: List<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,0,0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        getUserData()
        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("ApartmentID",usr.apartmentID)
            startActivity(intent)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId)   {
            R.id.nav_profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_signOut -> {
                auth.signOut()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    fun getUserData(){
        if(auth.currentUser != null){
            val docRef = db.collection("Users").document(auth.currentUser!!.uid)
            docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            usr = document.toObject(User::class.java)!!
                            if(usr.role!="janitor"){
                                items = listOf("Announcements", "Payments", "Questionnaire", "Market", "Fault and Complaint Notification", "Open Door With QR Code")
                            }
                            else{
                                items = listOf("Announcements", "Payments", "Questionnaire", "Orders", "Fault and Complaint Notification", "Open Door With QR Code")
                            }
                            val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items)
                            listView = findViewById(R.id.homeListView)
                            listView.adapter = adapter

                            listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

                                openSelectedActivity(position)
                            }
                        } else {
                            Log.d("userdata", "No such document")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("userdata", "get failed with ", exception)
                    }

        }

    }

    fun openSelectedActivity(position: Int){
        val intent: Intent
        if(position==0){
            intent = Intent(this, AnnouncementActivity::class.java)
            intent.putExtra("ApartmentID",usr.apartmentID)
        }
        else if(position==1){
            intent = Intent(this, PaymentActivity::class.java)
        }
        else if(position==2){
            intent = Intent(this, QuestionnaireActivity::class.java)
            intent.putExtra("ApartmentID",usr.apartmentID)
        }
        else if(position==3){
            if(usr.role != "janitor"){
                intent = Intent(this, MarketActivity::class.java)
            }
            else{
                intent = Intent(this, AnnouncementActivity::class.java)
            }

        }
        else{
            intent = Intent(this, AnnouncementActivity::class.java)
        }
        startActivity(intent)
    }
}