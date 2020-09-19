package com.leather.skindemo.skin.model;

import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import com.leather.skindemo.skin.ISkinViewSupport;
import com.leather.skindemo.skin.utils.SkinResources;

import java.util.List;

/**
 * 用来存放需要换肤的View以及这个View上需要换的具体属性集合
 */
public class SkinView {
    private View view;
    private List<SkinPain> skinPains;

    public SkinView(View view, List<SkinPain> skinPains) {
        this.view = view;
        this.skinPains = skinPains;
    }

    public void applySkin(Typeface typeface) {
        applyTypeface(typeface);//全局换字体
        applySkinSupport();//自定义View换肤
        for (SkinPain skinPain : skinPains) {
            Drawable left = null, top = null, right = null, bottom = null;
            switch (skinPain.getAttributeName()) {
                case "background":
                    Object background = SkinResources.getInstance().getBackground(skinPain.getResId());
                    //Color
                    if (background instanceof Integer) {
                        view.setBackgroundColor((Integer) background);
                    } else {
                        ViewCompat.setBackground(view, (Drawable) background);
                    }
                    break;
                case "src":
                    background = SkinResources.getInstance().getBackground(skinPain.getResId());
                    if (background instanceof Integer) {
                        ((ImageView) view).setImageDrawable(new ColorDrawable((Integer) background));
                    } else {
                        ((ImageView) view).setImageDrawable((Drawable) background);
                    }
                    break;
                case "textColor":
                    ColorStateList color = SkinResources.getInstance().getColorStateList(skinPain.getResId());
                    ((TextView) view).setTextColor(color);
                    break;
                case "drawableLeft":
                case "drawableStart":
                    left = SkinResources.getInstance().getDrawable(skinPain.getResId());
                    break;
                case "drawableRight":
                case "drawableEnd":
                    top = SkinResources.getInstance().getDrawable(skinPain.getResId());
                    break;
                case "drawableTop":
                    right = SkinResources.getInstance().getDrawable(skinPain.getResId());
                    break;
                case "drawableBottom":
                    bottom = SkinResources.getInstance().getDrawable(skinPain.getResId());
                    break;
                case "skinTypeface"://换指定控件的字体
                    applyTypeface(SkinResources.getInstance().getTypeface(skinPain.getResId()));
                    break;
            }

            if (left != null || top != null || right != null || bottom != null) {
                ((TextView) view).setCompoundDrawablesRelativeWithIntrinsicBounds(left, top, right, bottom);
            }
        }
    }

    /**
     * 自定义控件换肤
     */
    private void applySkinSupport() {
        if (view instanceof ISkinViewSupport) {
            ((ISkinViewSupport) view).applySkin();
        }
    }

    /**
     * 换字体
     *
     * @param typeface 字体
     */
    private void applyTypeface(Typeface typeface) {
        if (view instanceof TextView) {
            ((TextView) view).setTypeface(typeface);
        }
    }
}
