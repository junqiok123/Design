package com.example.design.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.design.R;
import com.example.design.control.ThemeControl;
import com.example.design.tool.CacheClear;
import com.example.design.util.ThemeUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;
    private LinearLayout card_choose, theme_choose, clear_mer;
    private RelativeLayout clear_disk, action_bar;
    private TextView cache_size;
    private ImageLoader imageLoader;
    private String cacheDir;
    private String cacheSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getActionBar().hide();
        initViews();

    }

    private void initViews() {
        imageLoader = ImageLoader.getInstance();
        cacheDir = imageLoader.getDiscCache().get("0").getParent();
        cacheSize = CacheClear.FormetFileSize(CacheClear.getFileSize(new File(cacheDir)));
        back = (ImageView) findViewById(R.id.back);
        card_choose = (LinearLayout) findViewById(R.id.card_choose);
        theme_choose = (LinearLayout) findViewById(R.id.theme_choose);
        clear_disk = (RelativeLayout) findViewById(R.id.clear_disk);
        action_bar = (RelativeLayout) findViewById(R.id.action_bar);
        clear_mer = (LinearLayout) findViewById(R.id.clear_mer);
        cache_size = (TextView) findViewById(R.id.cache_size);
        cache_size.setText(cacheSize);

        themeChoose();

        back.setOnClickListener(this);
        card_choose.setOnClickListener(this);
        theme_choose.setOnClickListener(this);
        clear_disk.setOnClickListener(this);
        clear_mer.setOnClickListener(this);
    }

    private void themeChoose() {
        View[] views = new View[]{action_bar};
        ThemeControl.setTheme(SettingActivity.this, views, ThemeUtil.getThemeChoose(SettingActivity.this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.card_choose:
                startActivity(new Intent(SettingActivity.this, CardsActivity.class));
                overridePendingTransition(R.anim.activity_translate_right_in, R.anim.activity_translate_right_out);
                break;
            case R.id.theme_choose:
                startActivity(new Intent(SettingActivity.this, ThemeChooseActivity.class));
                overridePendingTransition(R.anim.activity_translate_right_in, R.anim.activity_translate_right_out);
                break;
            case R.id.clear_disk:
                new MaterialDialog.Builder(this)// compile 'com.robbypond:material-dialogs:1.0.0'
                        .content("如果缓存太多，清理时可能会顿卡")
                        .positiveText("清理")
                        .negativeText("算了")
                        .callback(new MaterialDialog.Callback() {
                            @Override
                            public void onPositive(MaterialDialog materialDialog) {
                                imageLoader.clearDiscCache();
                                cacheSize = CacheClear.FormetFileSize(CacheClear.getFileSize(new File(cacheDir)));
                                cache_size.setText(cacheSize);
                            }

                            @Override
                            public void onNegative(MaterialDialog materialDialog) {
                                materialDialog.dismiss();
                            }
                        }).build().show();
                break;
            case R.id.clear_mer:
                imageLoader.clearMemoryCache();
                new MaterialDialog.Builder(this)
                        .content("清理完成")
                        .positiveText("OK")
                        .callback(new MaterialDialog.SimpleCallback() {
                            @Override
                            public void onPositive(MaterialDialog materialDialog) {
                                materialDialog.dismiss();
                            }
                        })
                        .build().show();
                break;
        }
    }
}
