package com.example.myfirstapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabItem
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
                else -> {
                    tab.text = "About"
                }
            }
        }.attach()
        //tabLayout(viewPager)

    }

}