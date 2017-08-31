package com.johnpetitto.orachat.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.johnpetitto.orachat.R
import com.johnpetitto.orachat.ui.account.AccountFragment
import com.johnpetitto.orachat.ui.chats.ChatsFragment
import kotlinx.android.synthetic.main.activity_main.*

private const val CHAT_POSITION = 0
private const val ACCOUNT_POSITION = 1

class MainActivity: AppCompatActivity(), ViewPager.OnPageChangeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        viewPager.adapter = NavigationPagerAdapter()
        viewPager.addOnPageChangeListener(this)

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.chat -> viewPager.currentItem = CHAT_POSITION
                R.id.account -> viewPager.currentItem = ACCOUNT_POSITION
            }

            true
        }
    }

    override fun onPageSelected(position: Int) {
        invalidateOptionsMenu()
        bottomNavigation.menu.getItem(position).isChecked = true
    }

    private inner class NavigationPagerAdapter: FragmentStatePagerAdapter(supportFragmentManager) {
        override fun getItem(position: Int): Fragment = when(position) {
            CHAT_POSITION -> ChatsFragment()
            else -> AccountFragment()
        }

        override fun getCount() = 2
    }

    override fun onPageScrollStateChanged(state: Int) {}
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
}
