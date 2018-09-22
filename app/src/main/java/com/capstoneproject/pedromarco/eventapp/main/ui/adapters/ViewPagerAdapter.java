package com.capstoneproject.pedromarco.eventapp.main.ui.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Adapter class of the ViewPager
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private String[] titles;
    private Fragment[] fragments;

    public ViewPagerAdapter(FragmentManager fm, Fragment[] fragments, String[] titles) {
        super(fm);
        this.titles = titles;
        this.fragments = fragments;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return fragments.length;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return titles[position];
    }

}