package com.leather.skindemo.skin.model;

/**
 * 用来封装控件属性和对应资源id的数据结构
 */
public class SkinPain {
    private String attributeName;
    private int resId;

    public SkinPain(String attributeName, int resId) {
        this.attributeName = attributeName;
        this.resId = resId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
