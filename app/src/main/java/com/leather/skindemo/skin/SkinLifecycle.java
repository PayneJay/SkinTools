package com.leather.skindemo.skin;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.leather.skindemo.skin.utils.SkinThemeUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class SkinLifecycle implements Application.ActivityLifecycleCallbacks {
    private Map<Activity, SkinLayoutFactory> factoryMap = new HashMap<>();

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        SkinThemeUtils.updateStatusBarColor(activity);
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        try {
            //如果不通过反射修改mFactorySet的值，则正常运行的时候会抛异常。
            //throw new IllegalStateException("A factory has already been set on this LayoutInflater");
            //这个回调是在onCreate里执行的，还有一种方案是把我们setFactory2的时机提前，在调super.onCreate()之前调也行
            Field factorySet = LayoutInflater.class.getDeclaredField("mFactorySet");
            factorySet.setAccessible(true);
            factorySet.setBoolean(layoutInflater, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //添加自定义的创建View的工厂
        SkinLayoutFactory skinLayoutFactory = new SkinLayoutFactory(activity);
        layoutInflater.setFactory2(skinLayoutFactory);

        //注册观察者
        SkinManager.getInstance().addObserver(skinLayoutFactory);
        //需要把这些观察者存起来，在当前activity生命周期结束的时候去注销或者回收
        factoryMap.put(activity, skinLayoutFactory);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        //移除当前activity对应的观察者，并且从观察者集合中移除
        SkinLayoutFactory factory = factoryMap.remove(activity);
        SkinManager.getInstance().deleteObserver(factory);
    }
}
