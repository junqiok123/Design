package com.example.design.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.design.R;
import com.example.design.activity.AboutActivity;
import com.example.design.activity.MainActivity;
import com.example.design.tool.CacheClear;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

/**
 * Created by Seven on 2016/2/16.
 */
public class MyFragment extends LazyFragment implements View.OnClickListener {

    private View view;
    private LinearLayout clear_mer, about, connect, exit;
    private RelativeLayout clear_disk;
    private TextView cache_size;
    private ImageLoader imageLoader;
    private String cacheDir;
    private String cacheSize;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_setting, container, false);
        initViews();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lazyLoad();
    }

    private void initViews() {
        imageLoader = ImageLoader.getInstance();
        cacheDir = imageLoader.getDiscCache().get("0").getParent();
        cacheSize = CacheClear.FormetFileSize(CacheClear.getFileSize(new File(cacheDir)));
        clear_disk = (RelativeLayout) view.findViewById(R.id.clear_disk);
        clear_mer = (LinearLayout) view.findViewById(R.id.clear_mer);
        about = (LinearLayout) view.findViewById(R.id.about);
        connect = (LinearLayout) view.findViewById(R.id.connect);
        exit = (LinearLayout) view.findViewById(R.id.exit);
        cache_size = (TextView) view.findViewById(R.id.cache_size);
        cache_size.setText(cacheSize);

        clear_disk.setOnClickListener(this);
        clear_mer.setOnClickListener(this);
        about.setOnClickListener(this);
        connect.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
                break;
            case R.id.connect:
                final MaterialDialog dialog3 = new MaterialDialog(getActivity());
                dialog3.content("随机获取一句有关艺术的名人名言")
                        .isTitleShow(false)
                        .btnNum(1)
                        .btnText("好吧")
                        .show();
                dialog3.setOnBtnClickL(new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog3.dismiss();
                    }
                });
                break;
            case R.id.clear_disk:
                final MaterialDialog dialog = new MaterialDialog(getActivity());
                dialog.content("如果缓存太多，清理时可能会顿卡")
                        .isTitleShow(false)
                        .btnNum(2)
                        .btnText("取消", "清理")
                        .show();
                dialog.setOnBtnClickL(
                        new OnBtnClickL() {
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
                final MaterialDialog dialog1 = new MaterialDialog(getActivity());
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
            case R.id.exit:
                final MaterialDialog dialog2 = new MaterialDialog(getActivity());
                dialog2.content("退出程序")
                        .isTitleShow(false)
                        .btnNum(2)
                        .btnText("取消", "退出")
                        .show();
                dialog2.setOnBtnClickL(
                        new OnBtnClickL() {
                            @Override
                            public void onBtnClick() {
                                dialog2.dismiss();
                            }
                        },
                        new OnBtnClickL() {
                            @Override
                            public void onBtnClick() {
                                getActivity().finish();
                                System.exit(0);
                            }
                        });
                break;
        }
    }

    @Override
    public void lazyLoad() {
        imageLoader = ImageLoader.getInstance();
        cacheDir = imageLoader.getDiscCache().get("0").getParent();
        cacheSize = CacheClear.FormetFileSize(CacheClear.getFileSize(new File(cacheDir)));
        cache_size.setText(cacheSize);
    }
}
