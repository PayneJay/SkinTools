package com.leather.skindemo.skin.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 用sp缓存当前选中的皮肤，以便下次启动使用
 */
public class SkinPreference {
    private static final String SKIN_SHARED = "skins";

    private static final String KEY_SKIN_PATH = "skin-path";
    private static volatile SkinPreference instance;
    private final SharedPreferences pref;

    public static void init(Context context) {
        if (instance == null) {
            synchronized (SkinPreference.class) {
                if (instance == null) {
                    instance = new SkinPreference(context.getApplicationContext());
                }
            }
        }
    }

    public static SkinPreference getInstance() {
        return instance;
    }

    private SkinPreference(Context context) {
        pref = context.getSharedPreferences(SKIN_SHARED, Context.MODE_PRIVATE);
    }

    public void setSkin(String skinPath) {
        pref.edit().putString(KEY_SKIN_PATH, skinPath).apply();
    }

    public String getSkin() {
        return pref.getString(KEY_SKIN_PATH, null);
    }

}
