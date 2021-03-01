package com.example.myfirstapp.Views


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.myfirstapp.Handlers.DatabaseHandler
import com.example.myfirstapp.R
import com.example.myfirstapp.Views.Adapters.PagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var dbHandler = DatabaseHandler(this)
        dbHandler.writableDatabase
        //TAB LAYOUT
        var tabLayout = findViewById<TabLayout>(R.id.tabMenu)
        //var tabExplore = findViewById<TabItem>(R.id.tabExplore)
        //var tabEndure = findViewById<TabItem>(R.id.tabEndure)
        var viewPager = findViewById<ViewPager2>(R.id.viewPager)

        val fragmentAdapter = PagerAdapter(this)
        viewPager.adapter = fragmentAdapter
        viewPager.offscreenPageLimit = 2
//
//        tabLayout.setupWithViewPager(viewPager)

            //val adapter = PagerAdapter(EndureFragment())
        //viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager){ tab, position ->
            when(position){
                0 -> {
                    tab.text = "Endure"
                }
                1 -> {
                    tab.text = "Explore"
                }
                2 -> {
                    tab.text = "Educate"
                }
                3 -> {
                    tab.text = "Evolve"
                }
                else -> {
                    tab.text = "About"
                }
            }
        }.attach()
        //tabLayout(viewPager)

    }

}