package com.example.design.control;

import android.content.Context;
import android.view.View;

import com.example.design.R;

/**
 * Created by Administrator on 2015/5/20.
 */
public class ThemeControl {

    private static int color = 0;

    public static Integer getThemeFromSharedPreferences(Context context, int type){
        switch (type) {
            case 0:
                color = R.color.material_deep_teal_500;
                break;
            case 1:
                color = R.color.text_color_red;
                break;
            case 2:
                color = R.color.blue;
                break;
            case 3:
                color = R.color.material_blue_grey_900;
                break;
            case 4:
                color = R.color.red1;
                break;
            case 5:
                color = R.color.background_color_green;
                break;
        }
        return context.getResources().getColor(color);
    }

    public static void setTheme(Context context, View[] view, int type){
        for (int i = 0; i < view.length; i++) {
            view[i].setBackgroundColor(getThemeFromSharedPreferences(context, type));
        }
    }
}
