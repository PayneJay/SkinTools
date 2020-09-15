package com.leather.skindemo;

import android.app.Application;

import com.leather.skindemo.skin.SkinManager;

public class SkinApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.init(this);
    }
}
