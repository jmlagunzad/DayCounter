package com.example.myfirstapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TAB LAYOUT
        var tabLayout = findViewById<TabLayout>(R.id.tabMenu)
        var tabExplore = findViewById<TabItem>(R.id.tabExplore)
        var tabEndure = findViewById<TabItem>(R.id.tabEndure)
        var viewPager = findViewById<ViewPager>(R.id.viewPager)

        val fragmentAdapter = PagerAdapter(supportFragmentManager)
        viewPager.adapter = fragmentAdapter

        tabLayout.setupWithViewPager(viewPager)







    }

}