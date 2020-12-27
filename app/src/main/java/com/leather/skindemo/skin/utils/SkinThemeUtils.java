package com.leather.skindemo.skin.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;

import com.leather.skindemo.R;

public class SkinThemeUtils {
    private static final int[] APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS = {androidx.appcompat.R.attr.colorPrimaryDark};
    //状态栏和导航栏属性列表
    private static final int[] STATUS_BAR_COLOR_ATTRS = {android.R.attr.statusBarColor, android.R.attr.navigationBarColor};
    //字体属性
    private static final int[] TYPEFACE_ATTRS = {R.attr.skinTypeface};

    /**
     * 通过属性数组获取到对应资源id
     *
     * @param context 上下文
     * @param attrs   属性集合
     * @return 资源id集合
     */
    public static int[] getResId(Context context, int[] attrs) {
        int[] ints = new int[attrs.length];
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        for (int i = 0; i < typedArray.length(); i++) {
            ints[i] = typedArray.getResourceId(i, 0);
        }
        typedArray.recycle();
        return ints;
    }

    /**
     * 更新状态栏颜色
     * 只有在5。0以上才能修状态栏颜色
     *
     * @param context activity
     */
    public static void updateStatusBarColor(Activity context) {
        //获取状态栏和导航栏的色值
        int[] statusBarId = getResId(context, STATUS_BAR_COLOR_ATTRS);
        if (statusBarId[0] != 0) {//如果状态栏配置颜色了，就换肤
            context.getWindow().setStatusBarColor(SkinResources.getInstance().getColor(statusBarId[0]));
        } else {
            //获取兼容包中的colorPrimaryDark，兼容版本
            int[] compatResId = getResId(context, APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS);
            if (compatResId[0] != 0) {
                context.getWindow().setStatusBarColor(SkinResources.getInstance().getColor(compatResId[0]));
            }
        }
        if (statusBarId[1] != 0) {//如果配置了导航栏的颜色了，也换肤
            context.getWindow().setNavigationBarColor(SkinResources.getInstance().getColor(statusBarId[1]));
        }
    }

    /**
     * 获取要换肤的字体
     *
     * @param context activity
     * @return 字体
     */
    public static Typeface getSkinTypeface(Context context) {
        int[] resId = getResId(context, TYPEFACE_ATTRS);
        return SkinResources.getInstance().getTypeface(resId[0]);
    }
}