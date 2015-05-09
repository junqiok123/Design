package com.example.design.util;

import android.content.Context;

import com.example.design.control.SharedPreferencesConstant;

/**
 * Created by Administrator on 2015/5/9.
 */
public class ThemeUtil {
    /**
     * 设置上次选择的皮肤
     *
     * @return void
     */
    public static Integer getThemeChoose(Context context) {
        int colorInt = PreferenceUtil.readInt(context, SharedPreferencesConstant.THEME, SharedPreferencesConstant.THEME);
        return colorInt;
    }

    /**
     * 设置上次选择的皮肤
     *
     * @return void
     */
    public static void setThemeChoose(Context context, int type) {
        PreferenceUtil.write(context, SharedPreferencesConstant.THEME, SharedPreferencesConstant.THEME, type);
    }
}
