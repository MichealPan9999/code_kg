package com.ktc.launcher;

import android.app.Application;

import com.ktc.launcher.utils.ScreenParamsConfig;

/**
 * Created by xiacf on 2017/9/7.
 */

public class LauncherApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ScreenParamsConfig.setResolutionAndDpiDiv(getApplicationContext());
    }
}
