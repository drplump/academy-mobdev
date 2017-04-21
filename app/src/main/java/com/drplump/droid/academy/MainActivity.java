package com.drplump.droid.academy;


import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    Fragment translateFragment;
    Fragment favouritesFragment;
    Fragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        Drawable icon = getDrawable(R.drawable.ic_translate_black_24dp);
        icon.setColorFilter(getColor(R.color.tabSelected), PorterDuff.Mode.SRC_IN);
        tabLayout.addTab(tabLayout.newTab().setIcon(icon));

        icon = getDrawable(R.drawable.ic_turned_in_black_24dp);
        icon.setColorFilter(getColor(R.color.tabUnselected), PorterDuff.Mode.SRC_IN);
        tabLayout.addTab(tabLayout.newTab().setIcon(icon));

        icon = getDrawable(R.drawable.ic_settings_black_24dp);
        icon.setColorFilter(getColor(R.color.tabUnselected), PorterDuff.Mode.SRC_IN);
        tabLayout.addTab(tabLayout.newTab().setIcon(icon));

        CustomPagerAdapter pagerAdapter = new CustomPagerAdapter(getSupportFragmentManager());
        if (savedInstanceState != null) {
            translateFragment = getSupportFragmentManager().getFragment(savedInstanceState, TranslateFragment.class.getName());
            favouritesFragment = getSupportFragmentManager().getFragment(savedInstanceState, FavouritesFragment.class.getName());
            settingsFragment = getSupportFragmentManager().getFragment(savedInstanceState, SettingsFragment.class.getName());
        } else {
            translateFragment = new TranslateFragment();
            favouritesFragment = new FavouritesFragment();
            settingsFragment = new SettingsFragment();
        }
        pagerAdapter.addFragment(translateFragment);
        pagerAdapter.addFragment(favouritesFragment);
        pagerAdapter.addFragment(settingsFragment);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                int tabIconColor = getColor(R.color.tabSelected);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
                int tabIconColor = getColor(R.color.tabUnselected);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //getSupportFragmentManager().putFragment(outState,TranslateFragment.class.getName(),translateFragment);
        //getSupportFragmentManager().putFragment(outState,FavouritesFragment.class.getName(),favouritesFragment);
        //getSupportFragmentManager().putFragment(outState,SettingsFragment.class.getName(),settingsFragment);
    }


    class CustomPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragments = new ArrayList<>();

        public CustomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment) {
            mFragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }


}
