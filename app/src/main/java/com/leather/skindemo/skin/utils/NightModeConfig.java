package com.leather.skindemo.skin.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 夜间模式切换配置
 */
public class NightModeConfig {
    private static final String IS_NIGHT_MODE = "_is_night_mode";
    private static final String NIGHT_MODE = "_night_mode";

    private static NightModeConfig instance;
    //用sp把当前的设置保存起来
    private SharedPreferences mPreferences;

    private NightModeConfig() {
    }

    public static NightModeConfig getInstance() {
        if (null == instance) {
            instance = new NightModeConfig();
        }
        return instance;
    }

    //是否是夜间模式
    public boolean isNightMode(Context context) {
        if (mPreferences == null) {
            mPreferences = context.getSharedPreferences(NIGHT_MODE, Context.MODE_PRIVATE);
        }
        return mPreferences.getBoolean(IS_NIGHT_MODE, false);
    }

    public void setNightMode(Context context, boolean isNightMode) {
        if (mPreferences == null) {
            mPreferences = context.getSharedPreferences(NIGHT_MODE, Context.MODE_PRIVATE);
        }
        mPreferences.edit().putBoolean(IS_NIGHT_MODE, isNightMode).apply();
    }
}
