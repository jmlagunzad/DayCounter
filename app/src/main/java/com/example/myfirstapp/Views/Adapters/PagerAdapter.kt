package com.example.myfirstapp.Views.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myfirstapp.Views.Fragments.*

class PagerAdapter (fa: FragmentActivity) : FragmentStateAdapter(fa) {

    public var educateViewPager = EducateViewPagerFragment()

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                EndureFragment()
            }
            1 -> {
                ExploreFragment()
            }
            2 -> {
                educateViewPager
            }
            3 -> {
                EvolveFragment()
            }
            else -> {
                AboutFragment()
            }
        }
    }

    override fun getItemCount(): Int{
        return 5
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