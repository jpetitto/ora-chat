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
    private static final int REGISTER_POSITION = 0;
    private static final int LOGIN_POSITION = 1;

    @BindView(R.id.account_access_tab_layout) TabLayout tabLayout;
    @BindView(R.id.account_access_view_pager) ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        viewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(LOGIN_POSITION);
        tabLayout.setupWithViewPager(viewPager);
    }

    private class TabPagerAdapter extends FragmentPagerAdapter {
        TabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case REGISTER_POSITION:
                    return new RegisterFragment();
                case LOGIN_POSITION:
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
                case REGISTER_POSITION:
                    return getString(R.string.register);
                case LOGIN_POSITION:
                    return getString(R.string.login);
            }
            return null;
        }
    }
}
