package com.leather.skindemo.skin;

import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.leather.skindemo.skin.model.SkinPain;
import com.leather.skindemo.skin.model.SkinView;
import com.leather.skindemo.skin.utils.SkinThemeUtils;

import java.util.ArrayList;
import java.util.List;

public class SkinAttribute {
    /**
     * 定义换肤时需要替换的资源属性
     */
    private static final List<String> ATTRIBUTES = new ArrayList<>();
    /**
     * 需要换肤的控件集合
     */
    private List<SkinView> skinViews = new ArrayList<>();
    /**
     * 字体
     */
    private Typeface typeface;

    static {
        ATTRIBUTES.add("background");
        ATTRIBUTES.add("src");
        ATTRIBUTES.add("textColor");
        ATTRIBUTES.add("drawableStart");
        ATTRIBUTES.add("drawableLeft");
        ATTRIBUTES.add("drawableEnd");
        ATTRIBUTES.add("drawableRight");
        ATTRIBUTES.add("drawableTop");
        ATTRIBUTES.add("drawableBottom");
        //自定义属性，局部换肤
        ATTRIBUTES.add("skinTypeface");
    }

    public SkinAttribute(Typeface typeface) {
        this.typeface = typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    /**
     * 过滤需要换肤的属性
     */
    public void filter(View view, AttributeSet attrs) {
        List<SkinPain> skinPains = new ArrayList<>();
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            //获取属性的名字
            String attributeName = attrs.getAttributeName(i);
            if (ATTRIBUTES.contains(attributeName)) {
                //如果该属性在我们定义的集合中，则获取对应的属性值进行处理
                java.lang.String attributeValue = attrs.getAttributeValue(i);
                if (attributeValue.startsWith("#")) {
                    //比如写死的色值#ffffff，这种一般都是不希望通过换肤改的，可以忽略掉
                    continue;
                }

                int resId;
                if (attributeValue.startsWith("?")) {
                    //这种就是用到了系统属性的，比如：android:background="?colorPrimary"
                    //比如：android:background="?attr/colorPrimary",这个跟上面是一样的效果，系统会将attr/colorPrimary作为一个整体，跟color/colorAccent是一样的道理
                    //我们需要得到它对应的值,属性值是从属性下标1的位置开始的，这些值在R文件中都是以int类型存放的，需要转为int
                    int attrId = Integer.parseInt(attributeValue.substring(1));
                    resId = SkinThemeUtils.getResId(view.getContext(), new int[]{attrId})[0];
                } else {
                    //还有一种就是我们自定义的值，比如：android:textColor="@color/colorAccent"
                    resId = Integer.parseInt(attributeValue.substring(1));
                }

                if (resId != 0) {//说明有属性值
                    SkinPain skinPain = new SkinPain(attributeName, resId);
                    skinPains.add(skinPain);
                }
            }
        }

        //如果有需要替换的属性，则加到换肤控件集合中
        if (!skinPains.isEmpty() || view instanceof TextView) {
            SkinView skinView = new SkinView(view, skinPains);
            skinView.applySkin(typeface);
            skinViews.add(skinView);
        }
    }

    public void applySkin() {
        for (SkinView skinView : skinViews) {
            skinView.applySkin(typeface);
        }
    }
}
