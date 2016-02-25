package com.example.design.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.example.design.R;
import com.example.design.fragment.MainFragment;
import com.example.design.util.TitlesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private String titles[];
    private List<Integer> titleID;
    private Context mContext;
    private ArrayList<Fragment> fragments;
    private FragmentManager fm;

    public ViewPagerAdapter(Context context, FragmentManager fm, ArrayList<Fragment> fragmentArrayList) {
        super(fm);
        this.mContext = context;
        this.fm = fm;
        this.fragments = fragmentArrayList;
        getTitleID();
    }

    public void setFragments(ArrayList<Fragment> fragments) {
        if(this.fragments != null){
            FragmentTransaction ft = fm.beginTransaction();
            for(Fragment f:this.fragments){
                ft.remove(f);
            }
            ft.commitAllowingStateLoss();
            ft=null;
            fm.executePendingTransactions();
        }
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    public void getTitleID() {
        String[] titlesAll = mContext.getResources().getStringArray(R.array.tabTitles);
        String checkedTitles = TitlesUtil.getTitleChecked(mContext);
        titleID = new ArrayList<>();
        titleID.add(0, 1);
        for (int i = 1; i < titlesAll.length; i++) {
            if (checkedTitles != null) {
                if (checkedTitles.contains(titlesAll[i].toString())) {
                    titleID.add(i + 1);
                }
            } else
                titleID.add(i + 1);
        }

        List<String> spList = new ArrayList<String>();
        if (checkedTitles != null) {
            StringTokenizer token = new StringTokenizer(checkedTitles, ",");
            while (token.hasMoreTokens()) {
                spList.add(token.nextToken());
            }
            titles = new String[spList.size() + 1];
            titles[0] = "最近更新";
            for (int i = 0; i < spList.size(); i++) {
                titles[i + 1] = spList.get(i);
            }
        } else
            titles = titlesAll;
    }

    @Override
    public Fragment getItem(int position) {
        return MainFragment.getInstance(titleID.get(position));
    }

    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}