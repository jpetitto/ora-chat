package com.johnpetitto.orachat;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountAccessActivity extends AppCompatActivity {
    @BindView(R.id.account_access_tab_layout) TabLayout tabLayout;
    @BindView(R.id.account_access_view_pager) ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        viewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    private class TabPagerAdapter extends FragmentPagerAdapter {
        static final int REGISTER = 0;
        static final int LOGIN = 1;

        TabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case REGISTER:
                    return new RegisterFragment();
                case LOGIN:
                    return new LoginFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case REGISTER:
                    return getString(R.string.register);
                case LOGIN:
                    return getString(R.string.login);
            }
            return null;
        }
    }
}
