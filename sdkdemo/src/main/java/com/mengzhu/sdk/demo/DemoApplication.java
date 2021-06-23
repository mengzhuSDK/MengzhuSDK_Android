package com.mengzhu.sdk.demo;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.mengzhu.live.sdk.business.model.Paths;
import com.mengzhu.live.sdk.core.MZSDKInitManager;
import com.mzmedia.utils.MZLogUtils;
import com.squareup.leakcanary.LeakCanary;

import tv.mengzhu.restreaming.tools.LogTools;

/**
 * Created by DELL on 2018/10/29.
 */
public class DemoApplication extends Application {

    public static String live_tk = "";
    public static String ticket_Id = "";
    public static String app_id = "分配的appid";
    public static String unique_id_test = "";
    public static String channel_id = "";
    public static String secretKey = "分配的秘钥";

    private boolean isDebug = false;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        MZLogUtils.setMZLogDebug(true);
        MZSDKInitManager.getInstance().initApplication(this, DemoApplication.app_id, isDebug);
    }
}
