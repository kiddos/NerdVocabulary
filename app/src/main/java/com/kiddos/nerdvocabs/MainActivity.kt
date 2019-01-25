package com.kiddos.nerdvocabs

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager

    private val navigationListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                viewPager.currentItem = 0
                viewPager.adapter?.notifyDataSetChanged()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                viewPager.currentItem = 1
                viewPager.adapter?.notifyDataSetChanged()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                viewPager.currentItem = 2
                viewPager.adapter?.notifyDataSetChanged()
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager = findViewById(R.id.vpContent)
        viewPager.adapter = ContentPagerAdapter(supportFragmentManager)

        navigation.setOnNavigationItemSelectedListener(navigationListener)
    }

    inner class ContentPagerAdapter(fragmentManger: FragmentManager) : FragmentStatePagerAdapter(fragmentManger) {
        override fun getCount(): Int = 3

        override fun getItemPosition(`object`: Any): Int {
            return PagerAdapter.POSITION_NONE
        }

        override fun getItem(position: Int): Fragment {
            when (position) {
                0 -> {
                    return ReviewFragment()
                }
                1 -> {
                    return AddWordFragment()
                }
                2 -> {
                    return QuizFragment()
                }
            }
            return ReviewFragment()
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> {
                    return getString(R.string.title_review)
                }
                1 -> {
                    return getString(R.string.title_add_word)
                }
                2 -> {
                    return getString(R.string.title_quiz)
                }
            }
            return "Page_$position"
        }
    }
}
