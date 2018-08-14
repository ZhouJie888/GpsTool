package com.bear.yzx110.gpstool;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.TimerTask;


/**
 * Created by yzx110 on 2018/7/17.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView t = (TextView)findViewById(R.id.falsh_text);
        t.setText("版本号：" + getVersion());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }

    // 获取版本号
    private String getVersion() {
        PackageInfo pkg;
        String versionName = "";
        try {
            pkg = getPackageManager().getPackageInfo(getApplication().getPackageName(), 0);
//			String appName = pkg.applicationInfo.loadLabel(getPackageManager()).toString();
            versionName = pkg.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
}
