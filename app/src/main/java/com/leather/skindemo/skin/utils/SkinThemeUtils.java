package com.leather.skindemo.skin.utils;

import android.content.Context;
import android.content.res.TypedArray;

public class SkinThemeUtils {
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
}
