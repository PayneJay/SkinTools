package com.leather.skindemo.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;
import com.leather.skindemo.R;
import com.leather.skindemo.skin.ISkinViewSupport;
import com.leather.skindemo.skin.utils.SkinResources;

public class MyTabLayout extends TabLayout implements ISkinViewSupport {

    private int skinTypefaceId;
    private int tabTextColorId;
    private int tabIndColorId;

    public MyTabLayout(@NonNull Context context) {
        this(context, null);
    }

    public MyTabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyTabLayout, defStyleAttr, 0);
        skinTypefaceId = typedArray.getResourceId(R.styleable.MyTabLayout_skinTypeface, 0);
        tabTextColorId = typedArray.getResourceId(R.styleable.MyTabLayout_tabTextColor, 0);
        tabIndColorId = typedArray.getResourceId(R.styleable.MyTabLayout_tabIndicatorColor, 0);
        typedArray.recycle();
    }

    @Override
    public void applySkin() {
        if (skinTypefaceId != 0) {
            Typeface typeface = SkinResources.getInstance().getTypeface(skinTypefaceId);

        }

        if (tabIndColorId != 0) {
            int indColor = SkinResources.getInstance().getColor(tabIndColorId);
            setSelectedTabIndicatorColor(indColor);
        }

        if (tabTextColorId != 0) {
            ColorStateList colorStateList = SkinResources.getInstance().getColorStateList(tabTextColorId);
            setTabTextColors(colorStateList);
        }
    }
}
