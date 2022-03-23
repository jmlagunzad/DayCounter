package com.example.myfirstapp.Views.Adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myfirstapp.Views.Fragments.*
import kotlinx.android.synthetic.main.fragment_educate.*

class EducatePagerAdapter(fa: EducateViewPagerFragment) : FragmentStateAdapter(fa) {

    var educateFragment = EducateFragment()

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                educateFragment
            }
            else -> {
                EducateSummaryFragment()
            }
        }
    }

    override fun getItemCount(): Int{
        return 2
    }


}