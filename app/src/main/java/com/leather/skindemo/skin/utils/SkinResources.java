package com.leather.skindemo.skin.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import androidx.core.content.res.ResourcesCompat;

public class SkinResources {
    private static volatile SkinResources instance;
    private final Resources mAppResources;
    private Resources skinResources;
    private String skinPkgName;
    private boolean isDefaultSkin = true;

    public SkinResources(Context context) {
        mAppResources = context.getResources();
    }

    public static SkinResources getInstance() {
        return instance;
    }

    public static void init(Context context) {
        if (null == instance) {
            synchronized (SkinResources.class) {
                if (null == instance) {
                    instance = new SkinResources(context);
                }
            }
        }
    }

    public int getColor(int resId) {
        int identifier = getIdentifier(resId);
        if (!isDefaultSkin && identifier != 0) {
            return ResourcesCompat.getColor(skinResources, identifier, null);
        }
        return ResourcesCompat.getColor(mAppResources, resId, null);
    }

    /**
     * 设置字体颜色用
     */
    public ColorStateList getColorStateList(int resId) {
        int identifier = getIdentifier(resId);
        if (!isDefaultSkin && identifier != 0) {
            return ResourcesCompat.getColorStateList(skinResources, identifier, null);
        }
        return ResourcesCompat.getColorStateList(mAppResources, resId, null);
    }

    public Drawable getDrawable(int resId) {
        int identifier = getIdentifier(resId);
        if (!isDefaultSkin && identifier != 0) {
            return ResourcesCompat.getDrawable(skinResources, identifier, null);
        }
        return ResourcesCompat.getDrawable(mAppResources, resId, null);
    }

    /**
     * 可能是Color 也可能是drawable
     */
    public Object getBackground(int resId) {
        String resourceTypeName = mAppResources.getResourceTypeName(resId);

        if (resourceTypeName.equals("color")) {
            return getColor(resId);
        } else {
            // drawable
            return getDrawable(resId);
        }
    }

    public int getIdentifier(int resId) {
        if (isDefaultSkin) {
            return resId;
        }
        //在皮肤包中不一定就是 当前程序的 id
        //获取对应id 在当前的名称 colorPrimary
        //R.drawable.ic_launcher
        String resName = mAppResources.getResourceEntryName(resId);//ic_launcher   /colorPrimaryDark
        String resType = mAppResources.getResourceTypeName(resId);//drawable
        return skinResources.getIdentifier(resName, resType, skinPkgName);
    }

    /**
     * 恢复默认皮肤设置
     */
    public void reset() {
        skinResources = null;
        skinPkgName = "";
        isDefaultSkin = true;
    }

    public void applySkin(Resources appResources, String packageName) {
        this.skinResources = appResources;
        this.skinPkgName = packageName;
        isDefaultSkin = TextUtils.isEmpty(skinPkgName) || null == skinResources;
    }
}
