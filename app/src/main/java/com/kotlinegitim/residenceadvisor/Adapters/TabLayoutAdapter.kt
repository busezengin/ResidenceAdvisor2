package com.kotlinegitim.residenceadvisor.Adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.kotlinegitim.residenceadvisor.fragments.FoodFragment

class TabLayoutAdapter (private val myContext: Context, fm: FragmentManager, internal var totalTabs: Int) : FragmentPagerAdapter(fm) {

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()
                return FoodFragment()
            }
            1 -> {
                return FoodFragment()
            }
            2 -> {
                // val movieFragment = MovieFragment()
                return FoodFragment()
            }
            else -> return FoodFragment()
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}