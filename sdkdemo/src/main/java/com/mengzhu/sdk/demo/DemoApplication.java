package com.mengzhu.sdk.demo;

import android.app.Application;

import com.mengzhu.live.sdk.core.MZSDKInitManager;

import tv.mengzhu.core.frame.coreutils.URLParamsUtils;

/**
 * Created by DELL on 2018/10/29.
 */
public class DemoApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        URLParamsUtils.setDebug(true);
        MZSDKInitManager.getInstance().initApplication(this);
    }
}
