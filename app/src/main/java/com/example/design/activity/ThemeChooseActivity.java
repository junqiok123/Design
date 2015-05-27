package com.example.design.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.design.R;
import com.example.design.adapter.ThemeChooseAdapter;
import com.example.design.control.ThemeControl;
import com.example.design.util.ThemeUtil;

public class ThemeChooseActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageView back;
    private RelativeLayout action_bar;
    private GridView theme_grid;
    private ThemeChooseAdapter adapter;
    private int[] colors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_choose);
        getActionBar().hide();
        initViews();
    }

    private void initViews() {
        back = (ImageView) findViewById(R.id.back);
        action_bar = (RelativeLayout) findViewById(R.id.action_bar);
        theme_grid = (GridView) findViewById(R.id.theme_grid);

        themeChoose();

        back.setOnClickListener(this);

        colors = new int[]{R.color.material_deep_teal_500, R.color.text_color_red
                , R.color.blue, R.color.material_blue_grey_900, R.color.red1
                , R.color.background_color_green};
        adapter = new ThemeChooseAdapter(this, colors);
        theme_grid.setAdapter(adapter);
        theme_grid.setOnItemClickListener(this);
    }

    private void themeChoose() {
        View[] views = new View[]{action_bar};
        ThemeControl.setTheme(ThemeChooseActivity.this, views, ThemeUtil.getThemeChoose(ThemeChooseActivity.this));
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ThemeUtil.setThemeChoose(ThemeChooseActivity.this, position);
        themeChoose();
    }
}
