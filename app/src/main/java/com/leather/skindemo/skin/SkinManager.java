package com.leather.skindemo.skin;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;

import com.leather.skindemo.skin.utils.SkinPreference;
import com.leather.skindemo.skin.utils.SkinResources;

import java.lang.reflect.Method;
import java.util.Observable;

/**
 * 换肤管理类，需要在Application中初始化
 */
public class SkinManager extends Observable {
    private static volatile SkinManager instance;
    private Application application;

    private SkinManager(Application application) {
        this.application = application;
        SkinResources.init(application);
        SkinPreference.init(application);
        application.registerActivityLifecycleCallbacks(new SkinLifecycle());
    }

    public static void init(Application application) {
        if (null == instance) {
            synchronized (SkinManager.class) {
                if (instance == null) {
                    instance = new SkinManager(application);
                }
            }
        }
    }

    public static SkinManager getInstance() {
        return instance;
    }

    /**
     * 加载皮肤资源,可能是从服务器下在的皮肤资源apk
     *
     * @param path 资源路径
     */
    public void loadSkin(String path) {
        if (TextUtils.isEmpty(path)) {
            //路径为空表示不想换肤或者恢复默认，则重置
            SkinPreference.getInstance().setSkin("");
            //资源管理器、皮肤资源属性等重置
            SkinResources.getInstance().reset();

        } else {
            try {
                //反射创建AssetManager，用来加载资源
                AssetManager assetManager = AssetManager.class.newInstance();
                //调用addAssetPath方法加载指定路径的资源文件，可以是压缩包
                Method method = assetManager.getClass().getMethod("addAssetPath", String.class);
                method.invoke(assetManager, path);

                Resources appResources = application.getResources();
                Resources skinResources = new Resources(assetManager, appResources.getDisplayMetrics(), appResources.getConfiguration());

                //需要把加载的皮肤保存一下，在应用重启后才能继续生效
                SkinPreference.getInstance().setSkin(path);
                //获取外部apk（皮肤包）的包名
                PackageInfo packageInfo = application.getPackageManager().getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
                SkinResources.getInstance().applySkin(skinResources, packageInfo != null ? packageInfo.packageName : null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //皮肤加载完后通知各页面即时生效
        setChanged();
        notifyObservers();
    }
}
