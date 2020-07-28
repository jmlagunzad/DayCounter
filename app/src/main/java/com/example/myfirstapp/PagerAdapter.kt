package com.example.myfirstapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> {
                EndureFragment()
            }
            else -> {
                return ExploreFragment()
            }
        }
    }

    override fun getCount(): Int{
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "ENDURE"
            1 -> "EXPLORE"
            else -> ""
        }
    }

}