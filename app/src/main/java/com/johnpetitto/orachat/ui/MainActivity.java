package com.johnpetitto.orachat.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.johnpetitto.orachat.R;
import com.johnpetitto.orachat.ui.chats.ChatsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    private static final int CHAT_POSITION = 0;
    private static final int ACCOUNT_POSITION = 1;

    @BindView(R.id.main_toolbar) Toolbar toolbar;
    @BindView(R.id.main_view_pager) ViewPager viewPager;
    @BindView(R.id.main_navigation) BottomNavigationView navigation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        viewPager.setAdapter(new NavigationPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(this);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public void onPageSelected(int position) {
        navigation.getMenu().getItem(position).setChecked(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chat:
                viewPager.setCurrentItem(CHAT_POSITION);
                return true;
            case R.id.account:
                viewPager.setCurrentItem(ACCOUNT_POSITION);
                return true;
        }
        return false;
    }

    private class NavigationPagerAdapter extends FragmentPagerAdapter {
        NavigationPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case CHAT_POSITION:
                    return new ChatsFragment();
                case ACCOUNT_POSITION:
                    return new AccountFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageScrollStateChanged(int state) {}
}
