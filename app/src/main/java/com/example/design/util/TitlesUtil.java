package com.example.design.util;

import android.content.Context;

import com.example.design.control.SharedPreferencesConstant;

/**
 * Created by Administrator on 2015/5/15.
 */
public class TitlesUtil {

    /**
     * 获取上次选择的标题卡
     *
     * @return void
     */
    public static String getTitleChecked(Context context) {
        String titles = PreferenceUtil.readString(context, SharedPreferencesConstant.TITLES, SharedPreferencesConstant.TITLES);
        return titles;
    }

    /**
     * 设置选择的标题卡
     *
     * @return void
     */
    public static void setTitleChecked(Context context, String type) {
        PreferenceUtil.write(context, SharedPreferencesConstant.TITLES, SharedPreferencesConstant.TITLES, type);
    }
}
