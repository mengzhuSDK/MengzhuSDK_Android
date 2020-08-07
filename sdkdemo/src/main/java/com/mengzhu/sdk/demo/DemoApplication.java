package com.mengzhu.sdk.demo;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.mengzhu.live.sdk.core.MZSDKInitManager;

import tv.mengzhu.core.frame.coreutils.URLParamsUtils;

/**
 * Created by DELL on 2018/10/29.
 */
public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        URLParamsUtils.setDebug(true);
        MZSDKInitManager.getInstance().initApplication(this);
    }
}
