package com.example.myfirstapp.Views.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager2.widget.ViewPager2
import com.example.myfirstapp.R
import com.example.myfirstapp.Views.Adapters.EducatePagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EducateViewPagerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EducateViewPagerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var fragmentAdapter : EducatePagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_educate_view_pager, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //TAB LAYOUT
        var tabLayout = view!!.findViewById<TabLayout>(R.id.educateTabMenu)
        var viewPager = view!!.findViewById<ViewPager2>(R.id.educateViewPager)

        fragmentAdapter = EducatePagerAdapter(this)
        viewPager.adapter = fragmentAdapter
        viewPager.offscreenPageLimit = 2

        TabLayoutMediator(tabLayout, viewPager){ tab, position ->
            when(position){
                0 -> {
                    tab.text = "Transactions"
                }
                else -> {
                    tab.text = "Summary"
                }
            }
        }.attach()

        class callback : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                fragmentAdapter!!.educateFragment.educateRecyclerAdapter?.endActionMode()
            }
        }

        viewPager.registerOnPageChangeCallback(callback())


//        viewPager.registerOnPageChangeCallback(ViewPager2.OnPageChangeCallback{
//
//        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EducateViewPagerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EducateViewPagerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}