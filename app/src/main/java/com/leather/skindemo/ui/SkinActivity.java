package com.leather.skindemo.ui;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.leather.skindemo.R;
import com.leather.skindemo.skin.SkinManager;
import com.leather.skindemo.skin.utils.NightModeConfig;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;

import java.io.File;
import java.util.List;

public class SkinActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin_layout);
    }

    public void change(View view) {
        Toast.makeText(this, "换肤", Toast.LENGTH_SHORT).show();
        requestPermissions();
    }

    public void reset(View view) {
        Toast.makeText(this, "还原", Toast.LENGTH_SHORT).show();
        SkinManager.getInstance().loadSkin(null);
    }

    /**
     * 夜间模式
     */
    public void night(View view) {
        Toast.makeText(this, "夜间模式", Toast.LENGTH_SHORT).show();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        NightModeConfig.getInstance().setNightMode(this, true);
        recreate();//必须让当前的Activity重建才能生效
    }

    /**
     * 日间模式
     */
    public void day(View view) {
        Toast.makeText(this, "日间模式", Toast.LENGTH_SHORT).show();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        NightModeConfig.getInstance().setNightMode(this, false);
        recreate();
    }

    private void requestPermissions() {
        PermissionX.init(this).permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onExplainRequestReason(new ExplainReasonCallback() {
                    @Override
                    public void onExplainReason(ExplainScope scope, List<String> deniedList) {
                        scope.showRequestReasonDialog(deniedList, "换肤需要您开启以下权限", "确认", "取消");
                    }
                })
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {
                            changeSkin();
                        }
                    }
                });
    }

    private void changeSkin() {
        String path = Environment.getExternalStorageDirectory() + File.separator + "skinmaker-debug.apk";
        SkinManager.getInstance().loadSkin(path);
    }
}
