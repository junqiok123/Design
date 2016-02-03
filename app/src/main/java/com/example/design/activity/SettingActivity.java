package com.example.design.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.design.R;
import com.example.design.control.ThemeControl;
import com.example.design.tool.CacheClear;
import com.example.design.util.ThemeUtil;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.flyco.dialog.widget.NormalDialog;
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
                break;
            case R.id.theme_choose:
                startActivity(new Intent(SettingActivity.this, ThemeChooseActivity.class));
                break;
            case R.id.clear_disk:
                final MaterialDialog dialog = new MaterialDialog(this);
                dialog.content("如果缓存太多，清理时可能会顿卡")
                        .isTitleShow(false)
                        .btnNum(2)
                        .btnText("算了", "清理")
                        .show();
                dialog.setOnBtnClickL(new OnBtnClickL() {
                                          @Override
                                          public void onBtnClick() {
                                              dialog.dismiss();
                                          }
                                      },
                        new OnBtnClickL() {
                            @Override
                            public void onBtnClick() {
                                imageLoader.clearDiscCache();
                                cacheSize = CacheClear.FormetFileSize(CacheClear.getFileSize(new File(cacheDir)));
                                cache_size.setText(cacheSize);
                                dialog.dismiss();
                            }
                        });
                break;
            case R.id.clear_mer:
                imageLoader.clearMemoryCache();
                final MaterialDialog dialog1 = new MaterialDialog(this);
                dialog1.content("清理完成")
                        .isTitleShow(false)
                        .btnNum(1)
                        .btnText("OK")
                        .show();
                dialog1.setOnBtnClickL(new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog1.dismiss();
                    }
                });
                break;
        }
    }
}
