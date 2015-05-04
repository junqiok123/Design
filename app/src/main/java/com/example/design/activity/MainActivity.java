package com.example.design.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.design.CSDN;
import com.example.design.R;
import com.example.design.adapter.TabAdapter;
import com.example.design.control.SharedPreferencesConstant;
import com.example.design.control.ThemeControl;
import com.example.design.tool.LogTool;
import com.example.design.util.PreferenceUtil;
import com.example.design.viewpagerindicator.TabPageIndicator;

public class MainActivity extends BaseFragmentActivity {
    public static final String tag = "MainActivity";
    public TabPageIndicator tabPageIndicator;// TabPageIndicator的实例
    public ViewPager viewPager;// ViewPager实例
    private FragmentPagerAdapter fragmentPagerAdapter;// ViewPager的适配器实例
    public View action_bar;
    public LinearLayout top_layout;
    private TextView mode;
    private LinearLayout mainActivityRoot;
    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeControl.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        res = getResources();
        initView();
        initData();
        mainActivityRoot.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        tabPageIndicator = (TabPageIndicator) findViewById(R.id.tabPageIndicator);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        action_bar = findViewById(R.id.action_bar);
        top_layout = (LinearLayout) findViewById(R.id.top_layout);
        mode = (TextView) findViewById(R.id.mode);
        mainActivityRoot = (LinearLayout) findViewById(R.id.mainActivityRoot);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        fragmentPagerAdapter = new TabAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.tabTitles));// 获取Tab的适配器
        viewPager.setAdapter(fragmentPagerAdapter);// 为ViewPager设置适配器
        tabPageIndicator.setViewPager(viewPager, 0);// 选择当前展示的界面为第一个
        mode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LogTool.e(tag, "mode.onClick");
                if (mode.getText().equals(res.getString(R.string.night))) {
                    PreferenceUtil.write(CSDN.getContext(), SharedPreferencesConstant.CONFIG, "theme", 0);
                    ThemeControl.changeToTheme(MainActivity.this, ThemeControl.THEME_NIGHT);
                } else {
                    PreferenceUtil.write(CSDN.getContext(), SharedPreferencesConstant.CONFIG, "theme", 1);
                    ThemeControl.changeToTheme(MainActivity.this, ThemeControl.THEME_DAY);
                }
            }
        });
    }
}
