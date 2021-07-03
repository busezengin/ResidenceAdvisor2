package com.kotlinegitim.residenceadvisor

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kotlinegitim.residenceadvisor.Adapters.OrderAdapter
import com.kotlinegitim.residenceadvisor.classes.Order
import com.kotlinegitim.residenceadvisor.classes.Product

class OrderActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    private var orderList: ArrayList<Order> = ArrayList()

    lateinit var apartmentID : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        auth = FirebaseAuth.getInstance()

        apartmentID=intent.getStringExtra("ApartmentID").toString()

        val recyclerView : RecyclerView = findViewById(R.id.orderRecyclerView)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        var adapter = OrderAdapter(orderList,apartmentID)
        recyclerView.adapter = adapter

        getOrders(adapter)
    }

    fun getOrders(adapter: OrderAdapter){
        db.collection("Orders"+apartmentID)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if(document.toObject(Order::class.java).productStatus=="wait"){
                            var tempOrder = document.toObject(Order::class.java)
                            tempOrder.shopList = ArrayList()
                            for(x in 0..document.get("productCount").toString().toInt()-1){
                                var tempProduct = Product((document.get("product$x") as Map<String, *>).get("id")as String?,
                                        (document.get("product$x") as Map<String, *>).get("name") as String?,
                                        (document.get("product$x") as Map<String, *>).get("price").toString().toInt(),
                                        (document.get("product$x") as Map<String, *>).get("count").toString().toInt(),
                                        (document.get("product$x") as Map<String, *>).get("type")as String?)
                                tempOrder.shopList.add(tempProduct)
                            }
                            orderList.add(tempOrder)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("userdata", "Error getting documents: ", exception)
                }
    }
}