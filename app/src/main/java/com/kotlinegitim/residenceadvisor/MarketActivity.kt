package com.kotlinegitim.residenceadvisor

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.kotlinegitim.residenceadvisor.Adapters.RecyclerViewAdapter
import com.kotlinegitim.residenceadvisor.classes.Product


class MarketActivity : AppCompatActivity() {

    var tabLayout: TabLayout? = null
    //var viewPager: ViewPager? = null
    private var shopList: ArrayList<Product> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market)

        tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        //viewPager = findViewById<ViewPager>(R.id.viewPager)
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Home"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Sport"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Movie"))
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL

        shopList.add(Product("elma",4,1,"yiyecek"))
        shopList.add(Product("asdf",4,1,"vbcbcv"))
        shopList.add(Product("aaaaaa",4,1,"yiyecek"))
        shopList.add(Product("elma",4,1,"yiyecek"))
        shopList.add(Product("asdf",4,1,"vbcbcv"))
        shopList.add(Product("aaaaaa",4,1,"yiyecek"))
        shopList.add(Product("elma",4,1,"yiyecek"))
        shopList.add(Product("asdf",4,1,"vbcbcv"))
        shopList.add(Product("aaaaaa",4,1,"yiyecek"))
        shopList.add(Product("aaaaaa",4,1,"yiyecek"))
        shopList.add(Product("elma",4,1,"yiyecek"))
        shopList.add(Product("asdf",4,1,"vbcbcv"))
        shopList.add(Product("aaaaaa",4,1,"yiyecek"))

        //val adapter = TabLayoutAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
        //viewPager!!.adapter = adapter
        //viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        val recyclerView : RecyclerView = findViewById(R.id.marketRecyclerView)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = RecyclerViewAdapter(shopList)
        recyclerView.adapter = adapter

        val itemSwipe = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Log.d("kaydirma",viewHolder.adapterPosition.toString())
                var position : Int = viewHolder.adapterPosition
                adapter.notifyDataSetChanged()
                Toast.makeText(applicationContext,shopList.get(position).name+" added",Toast.LENGTH_SHORT).show()
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                TODO("Not yet implemented")
            }
        }

        val swap = ItemTouchHelper(itemSwipe)
        swap.attachToRecyclerView(recyclerView)

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                //viewPager!!.currentItem = tab.position
                Log.d("pagee",tab.position.toString())
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }
}