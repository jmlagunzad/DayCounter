package com.example.myfirstapp.Views

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myfirstapp.Views.AboutFragment
import com.example.myfirstapp.Views.EducateFragment
import com.example.myfirstapp.Views.EndureFragment
import com.example.myfirstapp.Views.ExploreFragment

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