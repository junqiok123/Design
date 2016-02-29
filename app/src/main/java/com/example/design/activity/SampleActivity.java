package com.example.design.activity;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.design.R;
import com.example.design.adapter.NavdrawerAdapter;
import com.example.design.adapter.PopupWindowAdapter;
import com.example.design.adapter.ViewPagerAdapter;
import com.example.design.control.Constant;
import com.example.design.control.ThemeControl;
import com.example.design.fragment.MainFragment;
import com.example.design.ldrawer.ActionBarDrawerToggle;
import com.example.design.ldrawer.DrawerArrowDrawable;
import com.example.design.util.ThemeUtil;
import com.example.design.util.TitlesUtil;
import com.example.design.view.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SampleActivity extends BaseFragmentActivity implements AdapterView.OnItemClickListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView mDrawerList;
    private LinearLayout navdrawer, sliding_layout;
    private ViewPager pager;
//    private Toolbar toolbar;
    private SlidingTabLayout slidingTabLayout;
//    private ImageView pop_arrow;
    private PopupWindow popupWindow;
    private GridView pop_grid;
    private View layout;
    private PopupWindowAdapter adapterPop;
    private String[] spTitles, navdrawer_values;
    private List<Integer> titleID;
    private ViewPagerAdapter viewPagerAdapter;
    private ArrayList<Fragment> fragments;
    private DrawerArrowDrawable arrowDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        ActionBar ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.popupwindow, null);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navdrawer_list);
        navdrawer = (LinearLayout) findViewById(R.id.navdrawer);
        sliding_layout = (LinearLayout) findViewById(R.id.sliding_layout);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_ab_drawer);
//        toolbar.setTitle("Design");
//        pop_arrow = (ImageView) findViewById(R.id.pop_arrow);

        pager = (ViewPager) findViewById(R.id.viewpager);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        getData();
        viewPagerAdapter = new ViewPagerAdapter(SampleActivity.this, getSupportFragmentManager(), fragments);
        pager.setAdapter(viewPagerAdapter);
        pager.setOffscreenPageLimit(0);

        slidingTabLayout.setViewPager(pager);
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.WHITE;
            }
        });
        arrowDrawable = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };
        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, arrowDrawable, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(drawerToggle);
        navdrawer_values = new String[]{"设置"};
        NavdrawerAdapter adapter = new NavdrawerAdapter(this, spTitles);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(this);
        themeChoose();
    }

    private void getData() {
        String[] titles = getResources().getStringArray(R.array.tabTitles);
        String checkedTitles = TitlesUtil.getTitleChecked(SampleActivity.this);
        List<String> spList = new ArrayList<>();
        if (checkedTitles != null) {
            StringTokenizer token = new StringTokenizer(checkedTitles, ",");
            while (token.hasMoreTokens()) {
                spList.add(token.nextToken());
            }
            spTitles = new String[spList.size() + 1];
            spTitles[0] = "最近更新";
            for (int i = 0; i < spList.size(); i++) {
                spTitles[i + 1] = spList.get(i);
            }
        } else
            spTitles = titles;

        titleID = new ArrayList<>();
        titleID.add(0, 1);
        for (int i = 1; i < titles.length; i++) {
            if (checkedTitles != null) {
                if (checkedTitles.contains(titles[i].toString())) {
                    titleID.add(i + 1);
                }
            } else
                titleID.add(i + 1);
        }

        fragments = new ArrayList<>();
//        for (int i = 0; i < titleID.size(); i++) {
//            fragments.add(new MainFragment(titleID.get(i)));
//        }
        fragments.add(MainFragment.getInstance(titleID.get(0)));

        if (Constant.isCardsChange) {
            pager.removeAllViews();
            fragments.clear();
            Constant.isCardsChange = false;
        }

    }

    private void initPopupWindow() {
        pop_grid = (GridView) layout.findViewById(R.id.pop_grid);
        popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        adapterPop = new PopupWindowAdapter(SampleActivity.this, spTitles);
        adapterPop.setSelectItem(pager.getCurrentItem());
        pop_grid.setAdapter(adapterPop);
        pop_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                slidingTabLayout.setViewPager(pager, position);
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        });
    }

    private void themeChoose() {
        View[] views = new View[]{sliding_layout, navdrawer, mDrawerList, layout};
        ThemeControl.setTheme(SampleActivity.this, views, ThemeUtil.getThemeChoose(SampleActivity.this));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getData();
        viewPagerAdapter.getTitleID();
        viewPagerAdapter = new ViewPagerAdapter(SampleActivity.this, getSupportFragmentManager(), fragments);
        pager.setAdapter(viewPagerAdapter);

        slidingTabLayout.setViewPager(pager);
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.WHITE;
            }
        });
    }

    @Override
    protected void onResume() {
        viewPagerAdapter.notifyDataSetChanged();
        slidingTabLayout.notifyDataSetChanged();
        getData();
        initPopupWindow();
        slidingTabLayout.setViewPager(pager);
//        pop_arrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popupWindow.showAsDropDown(slidingTabLayout);
//                adapterPop.setSelectItem(pager.getCurrentItem());
//                adapterPop.notifyDataSetChanged();
//            }
//        });
        themeChoose();
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        super.onStop();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        startActivity(new Intent(SampleActivity.this, SettingActivity.class));
//        overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
//        new MainFragment(titleID.get(position)).onRefresh();
    }
}