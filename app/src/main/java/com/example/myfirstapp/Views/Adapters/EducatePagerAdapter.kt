package com.example.myfirstapp.Views.Adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myfirstapp.Views.Fragments.*

class EducatePagerAdapter(fa: EducateViewPagerFragment) : FragmentStateAdapter(fa) {

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                EducateFragment()
            }
            else -> {
                EducateSummaryFragment()
            }
        }
    }

    override fun getItemCount(): Int{
        return 2
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