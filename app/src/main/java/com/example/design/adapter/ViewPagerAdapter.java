package com.example.design.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.design.fragment.MainFragment;
import com.example.design.fragment.SampleFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private String titles[] ;

    public ViewPagerAdapter(FragmentManager fm, String[] titles2) {
        super(fm);
        titles=titles2;
    }

    @Override
    public Fragment getItem(int position) {
//        switch (position) {
//            // Open FragmentTab1.java
//            case 0:
//                return SampleFragment.newInstance(position);
//            case 1:
//                return SampleFragment.newInstance(position);
//            case 2:
//                return SampleFragment.newInstance(position);
//            case 3:
//                return SampleFragment.newInstance(position);
//            case 4:
//                return SampleFragment.newInstance(position);
//            case 5:
//                return SampleFragment.newInstance(position);
//            case 6:
//                return SampleFragment.newInstance(position);
//            case 7:
//                return SampleFragment.newInstance(position);
//
//        }
//        return null;
        MainFragment fragment = new MainFragment(position + 1);
        return fragment;
    }

    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }

}