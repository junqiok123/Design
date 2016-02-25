package com.example.design.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.design.R;
import com.example.design.adapter.FragAdapter;
import com.example.design.fragment.ArtFragment;
import com.example.design.fragment.DesignFragment;
import com.example.design.fragment.LatestFragment;
import com.example.design.fragment.MyFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Seven on 2016/2/16.
 *
 */
public class MainActivity extends BaseFragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private FragmentManager fm = getSupportFragmentManager();
    private LatestFragment latestFragment = new LatestFragment();
    private DesignFragment designFragment = new DesignFragment();
    private ArtFragment artFragment = new ArtFragment();
    private MyFragment myFragment = new MyFragment();
    private ViewPager viewPager;
    private RadioButton latest, design, art, my;

    private long exitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

//        AdManager.getInstance(getApplicationContext()).init("dad7e9f379621a86", "6a465fa58538216c", false);
    }

    private void initViews() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        latest = (RadioButton) findViewById(R.id.radio_latest);
        design = (RadioButton) findViewById(R.id.radio_design);
        art = (RadioButton) findViewById(R.id.radio_art);
        my = (RadioButton) findViewById(R.id.radio_my);

        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(latestFragment);
        fragments.add(designFragment);
        fragments.add(artFragment);
        fragments.add(myFragment);

        viewPager.setAdapter(new FragAdapter(fm, fragments));
        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.addOnPageChangeListener(this);
        latest.setOnClickListener(this);
        design.setOnClickListener(this);
        art.setOnClickListener(this);
        my.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.radio_latest:
                viewPager.setCurrentItem(0, false);
                break;
            case R.id.radio_design:
                viewPager.setCurrentItem(1, false);
                break;
            case R.id.radio_art:
                viewPager.setCurrentItem(2, false);
                break;
            case R.id.radio_my:
                viewPager.setCurrentItem(3, false);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        updateRadio(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void updateRadio(int currentItem) {
        latest.setChecked(false);
        design.setChecked(false);
        art.setChecked(false);
        my.setChecked(false);
        switch (currentItem) {
            case 0:
                latest.setChecked(true);
                break;
            case 1:
                design.setChecked(true);
                break;
            case 2:
                art.setChecked(true);
                break;
            case 3:
                my.setChecked(true);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
            return;
        }
        finish();
        System.exit(0);
    }
}
