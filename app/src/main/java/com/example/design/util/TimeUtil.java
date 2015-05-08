package com.example.design.util;

import android.content.Context;
import android.text.TextUtils;

import com.example.design.R;
import com.example.design.control.SharedPreferencesConstant;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class TimeUtil {
	/**
	 * 根据类型获取上次更新的时间
	 *
	 * @param type
	 * @return timeString
	 */
	public static String getRefreashTime(Context context, int type) {
		String timeString = PreferenceUtil.readString(context, SharedPreferencesConstant.VARIABLE, "INFOS_" + type);
		if (TextUtils.isEmpty(timeString)) {
			return R.string.not_update + "...";
		}
		return timeString;
	}

	/**
	 * 根据类型设置上次更新的时间
	 *
	 * @param type
	 * @return void
	 */
	public static void setRefreashTime(Context context, int type) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		PreferenceUtil.write(context, SharedPreferencesConstant.VARIABLE, "INFOS_" + type, simpleDateFormat.format(new Date()));
	}

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
