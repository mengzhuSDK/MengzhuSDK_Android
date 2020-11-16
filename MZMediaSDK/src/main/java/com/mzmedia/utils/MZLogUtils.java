package com.mzmedia.utils;

import com.mengzhu.sdk.download.library.publics.util.ALog;

import tv.mengzhu.core.frame.logger.SDKLogUtils;
import tv.mengzhu.restreaming.tools.LogTools;

public class MZLogUtils {
    /**
     * 设置SDK log debug模式
     *
     * @param isDebug true 打开
     */
    public static void setMZLogDebug(boolean isDebug) {
        if (isDebug) {
            SDKLogUtils.setLogLevel(SDKLogUtils.LOG_LEVEL_ALL);
            LogTools.setEnableLog(true);
            ALog.LOG_LEVEL = ALog.LOG_LEVEL_VERBOSE;
        } else {
            SDKLogUtils.setLogLevel(SDKLogUtils.LOG_LEVEL_NONE);
            LogTools.setEnableLog(false);
            ALog.LOG_LEVEL = ALog.LOG_CLOSE;
        }
    }
}
