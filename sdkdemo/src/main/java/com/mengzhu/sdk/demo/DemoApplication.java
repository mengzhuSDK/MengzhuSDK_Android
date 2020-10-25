package com.mengzhu.sdk.demo;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.mengzhu.live.sdk.core.MZSDKInitManager;

/**
 * Created by DELL on 2018/10/29.
 */
public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        MZSDKInitManager.getInstance().initApplication(this, TestActivity.app_id, true);
    }
}
