package com.mzmedia.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.mzmedia.activity.LandscapeTransActivity;

/**
 * <p>Activity跳转注册列表</p><br/>
 * <p>TODO (类的详细的功能描述)</p>
 *
 * @author sunxianhao
 * @since 1.0.0
 */
public class ActivityUtils {
    public static final int TRANS_REQUEST_CODE = 104;
    public static final int INFO_PERFECT_REQUEST = 105;
    public static final String BALANCE_KEY = "balance_key";
    public static final String APPROVE_NAME = "approveName";
    public static final String APPROVE_PHONE = "approvePhone";
    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;
    /**
     * 线上订单
     */
    public static final int CHANNEL_GOODSORDER_TYPE_ONLINE = 1;
    /**
     * 线下订单
     */
    public static final int CHANNEL_GOODSORDER_TYPE_OFFLINE = 2;
    /**
     * 商城订单
     */
    public static final int TRADE_ORDER_PAY_REQUEST = 121;
    /**
     * 绑定手机号
     */
    public static final int SAFE_BIND_ACQUIRE_REQUEST = 122;
    /**
     * 绑定手机号
     */
    public static final int BIND_ACQUIRE_REQUEST = 123;

    /**
     * 启动聊天输入栏
     *
     * @param context
     * @author sunxianhao
     * created at 2016/7/8 11:44
     */
    public static void startLandscapeTransActivity(Context context) {

        Intent in = new Intent(context, LandscapeTransActivity.class);
//        in.putExtra(LandscapeTransActivity.IS_CROSSWISE, isCrosswise);
//        in.putExtra(LandscapeTransActivity.TRANS_TOKEN, token);
//        in.putExtra(LandscapeTransActivity.TRANS_AT, at);
//        in.putExtra(LandscapeTransActivity.PLAYINFO, dto);
//        in.putExtra(RequestParamConstants.UID, uid);
//        in.putExtra(RequestParamConstants.TICKET_ID, ticketId);
        ((Activity) context).startActivityForResult(in, TRANS_REQUEST_CODE);
    }
}