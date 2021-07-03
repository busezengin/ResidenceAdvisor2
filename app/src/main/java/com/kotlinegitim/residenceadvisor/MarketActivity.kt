package com.kotlinegitim.residenceadvisor

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kotlinegitim.residenceadvisor.Adapters.RecyclerViewAdapter
import com.kotlinegitim.residenceadvisor.classes.Product
import com.kotlinegitim.residenceadvisor.classes.User
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


class MarketActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    var tabLayout: TabLayout? = null
    lateinit var shopListButton: Button
    //var viewPager: ViewPager? = null
    private var foodList: ArrayList<Product> = ArrayList()
    private var drinkList: ArrayList<Product> = ArrayList()
    private var shopList: ArrayList<Product> = ArrayList()

    lateinit var apartmentID : String
    var tabLayoutPositon = 0
    lateinit var usr: User

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market)

        auth = FirebaseAuth.getInstance()

        apartmentID=intent.getStringExtra("ApartmentID").toString()

        tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        //viewPager = findViewById<ViewPager>(R.id.viewPager)
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Food"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Drink"))
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL

        shopListButton = findViewById(R.id.shopListButton)

        //shopList.add(Product("elma",4,1,"yiyecek"))
        val recyclerView : RecyclerView = findViewById(R.id.marketRecyclerView)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        var adapter = RecyclerViewAdapter(foodList)
        recyclerView.adapter = adapter

        val itemSwipe = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var position : Int = viewHolder.adapterPosition
                adapter.notifyDataSetChanged()
                addProductShopList(position)
                if(tabLayoutPositon==0){
                    Toast.makeText(applicationContext,foodList.get(position).name+" added", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(applicationContext,drinkList.get(position).name+" added", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                TODO("Not yet implemented")
            }
        }

        val swap = ItemTouchHelper(itemSwipe)
        swap.attachToRecyclerView(recyclerView)

        //val adapter = TabLayoutAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
        //viewPager!!.adapter = adapter
        //viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                //viewPager!!.currentItem = tab.position
                if(tab.position==0){
                    tabLayoutPositon=0
                    adapter = RecyclerViewAdapter(foodList)
                    recyclerView.adapter = adapter
                }else{
                    tabLayoutPositon=1
                    adapter = RecyclerViewAdapter(drinkList)
                    recyclerView.adapter = adapter
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        shopListButton.setOnClickListener {
            var items: ArrayList<String> = ArrayList()

            for(x in 0..shopList.size-1){
                items.add(shopList.get(x).name+" "+shopList.get(x).count)
            }

            MaterialAlertDialogBuilder(this)
                    .setTitle(resources.getString(R.string.app_name))
                    .setItems(items.toTypedArray()) { dialog, which ->
                        // Respond to item chosen
                    }.setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                        // Respond to negative button press
                    }.setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->
                        sendShopListDatabase()
                    }
                    .show()
        }

        getProducts(adapter)
        getUserProfileData()
    }

    fun getProducts(adapter: RecyclerViewAdapter){
        db.collection("Products")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if(document.toObject(Product::class.java).type=="food"){
                            foodList.add(document.toObject(Product::class.java))
                            adapter.notifyDataSetChanged()
                        }else{
                            drinkList.add(document.toObject(Product::class.java))
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("userdata", "Error getting documents: ", exception)
                }
    }

    fun addProductShopList(position: Int){
        var isFind: Boolean = false
        if(tabLayoutPositon==0){
            for(x in 0..shopList.size-1){
                if (shopList.get(x).id==foodList.get(position).id){
                    shopList.get(x).count=shopList.get(x).count+1
                    isFind=true
                }
            }
            if (!isFind){
                shopList.add(foodList.get(position))
            }
        }else{
            for(x in 0..shopList.size-1){
                if (shopList.get(x).id==drinkList.get(position).id){
                    shopList.get(x).count=shopList.get(x).count+1
                    isFind=true
                }
            }
            if (!isFind){
                shopList.add(drinkList.get(position))
            }
        }
        isFind=false
        shopListButton.text = "SEPET("+shopList.size.toString()+")"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendShopListDatabase(){

        val now = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneOffset.UTC)
                .format(Instant.now())


        val docData = hashMapOf(
                "ownerID" to auth.currentUser!!.uid,
                "ownerNameSurname" to usr.firstname+" "+usr.lastname,
                "productCount" to shopList.size,
                "productStatus" to "wait",
                "date" to now
        )

        db.collection("Orders"+apartmentID).document(now).set(docData)

        for (x in 0..shopList.size-1){
            val docData = hashMapOf(
                    "product$x" to shopList.get(x)
            )

            db.collection("Orders"+apartmentID).document(now).update(docData as Map<String, Any>)
        }

        finish()

    }

    fun getUserProfileData(){
        if(auth.currentUser != null){

            val docRef = db.collection("Users").document(auth.currentUser!!.uid)
            docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            usr = document.toObject(User::class.java)!!
                        } else {
                            Log.d("userdata", "No such document")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("userdata", "get failed with ", exception)
                    }

        }

    }
}