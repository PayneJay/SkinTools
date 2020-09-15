package com.leather.skindemo.skin;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class SkinLayoutFactory implements LayoutInflater.Factory2, Observer {
    private static final String[] mClassPrefixList = {//系统自带的组件的包名只有这三种
            "android.widget.",
            "android.view.",
            "android.webkit."
    };
    //存放View的名称和构造器
    private static final HashMap<String, Constructor<? extends View>> sConstructorMap =
            new HashMap<>();
    //创建View的Class文件，反射创建时用
    private static final Class<?>[] mConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};
    //控件属性处理类
    private SkinAttribute skinAttribute;

    public SkinLayoutFactory() {
        skinAttribute = new SkinAttribute();
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context,
                             @NonNull AttributeSet attrs) {
        View view = createViewFromTag(parent, name, context, attrs);
        if (view == null) {
            view = createView(context, name, attrs);
        }
        //筛选符合属性View
        skinAttribute.filter(view, attrs);
        return view;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return null;
    }

    private View createViewFromTag(View parent, String name, Context context, AttributeSet attrs) {
        /**
         * 自定义的View是不能通过反射拿到的，需要单独处理，具体参见{@link LayoutInflater}
         */
        if (-1 != name.indexOf('.')) {//通过这个判断是否只自定义的View
            return onCreateView(name, context, attrs);
        }
        View view = null;
        for (String s : mClassPrefixList) {//遍历系统的view通过反射去创建
            view = createView(context, s + name, attrs);
            if (view != null) {
                break;
            }
        }
        return view;
    }

    /**
     * 通过反射真正创建View。参考LayoutInflater源码，也是这么实现的
     *
     * @param context 上下文
     * @param name    控件名称，Button、TextView。。。
     * @param attrs   控件属性
     * @return View
     */
    private View createView(Context context, String name, AttributeSet attrs) {
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        Class<? extends View> clazz;
        if (constructor == null) {
            try {
                clazz = Class.forName(name, false, context.getClassLoader()).asSubclass(View.class);
                constructor = clazz.getConstructor(mConstructorSignature);
                sConstructorMap.put(name, constructor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (constructor != null) {
            try {
                return constructor.newInstance(context, attrs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 通过观察者模式通知各个页面进行换肤。实际上这里也可以用广播、EventBus通知
     *
     * @param o   观察者对象
     * @param arg 传给notifyObservers方法的参数
     */
    @Override
    public void update(Observable o, Object arg) {
        if (skinAttribute != null) {
            skinAttribute.applySkin();
        }
    }
}
