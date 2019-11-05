package com.mengzhu.live.sdk.demo;

import android.app.Application;

import com.mengzhu.core.coreutils.URLParamsUtils;
import com.mengzhu.live.sdk.core.MZSDKInitManager;

/**
 * Created by DELL on 2018/10/29.
 */
public class DemoApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        URLParamsUtils.setDebug(false);
        MZSDKInitManager.getInstance().initApplication(this);
    }
}
