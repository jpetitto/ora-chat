package com.johnpetitto.orachat.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.johnpetitto.orachat.OraChatApplication
import com.johnpetitto.orachat.R
import com.johnpetitto.orachat.data.user.UserModel
import com.johnpetitto.orachat.startActivity
import com.johnpetitto.orachat.ui.login.LoginFragment
import com.johnpetitto.orachat.ui.register.RegisterFragment
import kotlinx.android.synthetic.main.activity_account_access.*

private const val REGISTER_POSITION = 0
private const val LOGIN_POSITION = 1

class AccountAccessActivity: AppCompatActivity() {
    lateinit var userModel: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userModel = (application as OraChatApplication).userModel

        // automatically sign in if user is authorized
        if (userModel.isAuthorized) {
            startMainActivity()
        }

        setContentView(R.layout.activity_account_access)

        viewPager.adapter = TabPagerAdapter(supportFragmentManager)
        viewPager.currentItem = LOGIN_POSITION
        tabLayout.setupWithViewPager(viewPager)
    }

    inner class TabPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                REGISTER_POSITION -> RegisterFragment()
                else -> LoginFragment()
            }
        }

        override fun getCount() = 2

        override fun getPageTitle(position: Int): CharSequence {
            return when (position) {
                REGISTER_POSITION -> getString(R.string.register)
                else -> getString(R.string.login)
            }
        }
    }

    fun showProgressBar(show: Boolean) {
        progress.visibility = if (show) View.VISIBLE else View.INVISIBLE
        viewPager.visibility = if (show) View.INVISIBLE else View.VISIBLE
    }

    fun startMainActivity() = startActivity(MainActivity::class.java, true)
}
