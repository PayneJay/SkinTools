package com.leather.skindemo.skin.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import androidx.core.content.res.ResourcesCompat;

public class SkinResources {
    private static volatile SkinResources instance;
    //用来加载默认的资源
    private Resources mAppResources;
    //用来加载皮肤包中的资源
    private Resources mSkinResources;
    //皮肤包包名
    private String skinPkgName;
    //是否是默认皮肤
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
            return ResourcesCompat.getColor(mSkinResources, identifier, null);
        }
        return ResourcesCompat.getColor(mAppResources, resId, null);
    }

    /**
     * 设置字体颜色用
     */
    public ColorStateList getColorStateList(int resId) {
        int identifier = getIdentifier(resId);
        if (!isDefaultSkin && identifier != 0) {
            return ResourcesCompat.getColorStateList(mSkinResources, identifier, null);
        }
        return ResourcesCompat.getColorStateList(mAppResources, resId, null);
    }

    public Drawable getDrawable(int resId) {
        int identifier = getIdentifier(resId);
        if (!isDefaultSkin && identifier != 0) {
            return ResourcesCompat.getDrawable(mSkinResources, identifier, null);
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

    /**
     * 通过资源id去对应的皮肤包中去匹配资源
     *
     * @param resId 资源id
     * @return 匹配到的资源id，0表示没有匹配到
     */
    public int getIdentifier(int resId) {
        if (isDefaultSkin) {
            return resId;
        }
        //在皮肤包中不一定就是 当前程序的 id
        //获取对应id 在当前的名称 colorPrimary
        //R.drawable.ic_launcher
        String resName = mAppResources.getResourceEntryName(resId);//ic_launcher   /colorPrimaryDark
        String resType = mAppResources.getResourceTypeName(resId);//drawable
        return mSkinResources.getIdentifier(resName, resType, skinPkgName);
    }

    /**
     * 恢复默认皮肤设置
     */
    public void reset() {
        mSkinResources = null;
        skinPkgName = "";
        isDefaultSkin = true;
    }

    public void apply(Resources skinResources, String packageName) {
        this.mSkinResources = skinResources;
        this.skinPkgName = packageName;
        isDefaultSkin = TextUtils.isEmpty(skinPkgName) || null == this.mSkinResources;
    }

    /**
     * 获取字体属性
     *
     * @param typefaceId 属性id
     * @return 字体
     */
    public Typeface getTypeface(int typefaceId) {
        if (typefaceId == 0) {
            return Typeface.DEFAULT;
        }
        String typefacePath = getTypefacePath(typefaceId);
        if (TextUtils.isEmpty(typefacePath)) {//如果是默认皮肤，则返回默认字体
            return Typeface.DEFAULT;
        }

        if (isDefaultSkin) {//不需要更换字体,用默认的即可
            return Typeface.createFromAsset(mAppResources.getAssets(), typefacePath);
        }
        return Typeface.createFromAsset(mSkinResources.getAssets(), typefacePath);
    }

    /**
     * 根据属性id获取字体的路径
     * <string name="typeface_global">font/global.ttf</string>
     *
     * @param typefaceId 属性id（即typeface_global）
     * @return 通过这个方法拿到的其实是font/global.ttf,还需要用createFromAsset加载
     */
    private String getTypefacePath(int typefaceId) {
        if (isDefaultSkin) {//如果是默认皮肤，则返回默认字体
            return mAppResources.getString(typefaceId);
        }

        int identifier = getIdentifier(typefaceId);
        if (identifier == 0) {//没有设置字体，不需要更换，用默认的即可
            return mAppResources.getString(typefaceId);
        }
        //返回要更换的字体路径
        return mSkinResources.getString(identifier);
    }
}
