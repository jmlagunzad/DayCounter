package com.example.myfirstapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter (fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                EndureFragment()
            }
            1 -> {
                ExploreFragment()
            }
            2 -> {
                EducateFragment()
            }
            else -> {
                AboutFragment()
            }
        }
    }

    override fun getItemCount(): Int{
        return 4
    }

    /*
    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "ENDURE"
            1 -> "EXPLORE"
            2 -> "EDUCATE"
            else -> ""
        }
    }*/

}