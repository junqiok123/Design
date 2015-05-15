package com.example.design.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.design.R;
import com.example.design.adapter.PopupWindowAdapter;
import com.example.design.adapter.ViewPagerAdapter;
import com.example.design.util.ThemeUtil;
import com.example.design.view.SlidingTabLayout;

public class SampleActivity extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView mDrawerList;
    private ViewPager pager;
    private Toolbar toolbar;
    private SlidingTabLayout slidingTabLayout;
    private ImageView pop_arrow;
    private PopupWindow popupWindow;
    private GridView pop_grid;
    private View layout;
    private PopupWindowAdapter adapterPop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        init();
    }

    private void init() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navdrawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        pop_arrow = (ImageView) findViewById(R.id.pop_arrow);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_ab_drawer);
        }
        pager = (ViewPager) findViewById(R.id.viewpager);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.tabTitles)));

        slidingTabLayout.setViewPager(pager);
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.WHITE;
            }
        });
        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(drawerToggle);
        String[] values = new String[]{"DEFAULT", "RED", "BLUE", "MATERIAL GREY"};
        String[] value = new String[]{"模块选择"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, value);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                themeChoose(position);
                startActivity(new Intent(SampleActivity.this, CardsActivity.class));
            }
        });
        initPopupWindow();
        themeChoose(ThemeUtil.getThemeChoose(SampleActivity.this));
        pop_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAsDropDown(toolbar);
                adapterPop.setSelectItem(pager.getCurrentItem());
                adapterPop.notifyDataSetChanged();
            }
        });
    }

    private void initPopupWindow() {
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.popupwindow, null);
        pop_grid = (GridView) layout.findViewById(R.id.pop_grid);
        popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        adapterPop = new PopupWindowAdapter(SampleActivity.this, getResources().getStringArray(R.array.tabTitles));
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

    private void themeChoose(int id) {
        ThemeUtil.setThemeChoose(SampleActivity.this, id);
        switch (id) {
            case 0:
                mDrawerList.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                toolbar.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                pop_arrow.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                layout.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                mDrawerLayout.closeDrawer(Gravity.START);
                break;
            case 1:
                mDrawerList.setBackgroundColor(getResources().getColor(R.color.red));
                toolbar.setBackgroundColor(getResources().getColor(R.color.red));
                slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.red));
                pop_arrow.setBackgroundColor(getResources().getColor(R.color.red));
                layout.setBackgroundColor(getResources().getColor(R.color.red));
                mDrawerLayout.closeDrawer(Gravity.START);
                break;
            case 2:
                mDrawerList.setBackgroundColor(getResources().getColor(R.color.blue));
                toolbar.setBackgroundColor(getResources().getColor(R.color.blue));
                slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.blue));
                pop_arrow.setBackgroundColor(getResources().getColor(R.color.blue));
                layout.setBackgroundColor(getResources().getColor(R.color.blue));
                mDrawerLayout.closeDrawer(Gravity.START);
                break;
            case 3:
                mDrawerList.setBackgroundColor(getResources().getColor(R.color.material_blue_grey_800));
                toolbar.setBackgroundColor(getResources().getColor(R.color.material_blue_grey_800));
                slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.material_blue_grey_800));
                pop_arrow.setBackgroundColor(getResources().getColor(R.color.material_blue_grey_800));
                layout.setBackgroundColor(getResources().getColor(R.color.material_blue_grey_800));
                mDrawerLayout.closeDrawer(Gravity.START);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.START);
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
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawer(Gravity.START);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawer(Gravity.START);
        }
        super.onStop();
    }
}
