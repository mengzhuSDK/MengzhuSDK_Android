package com.mzmedia.presentation.presenter.stop;

import android.content.Context;

import com.mengzhu.live.sdk.business.presenter.IBasePresenter;
import com.mengzhu.live.sdk.business.presenter.IBasePresenterLinstener;
import com.mzmedia.presentation.biz.IBaseBiz;
import com.mzmedia.presentation.biz.IBaseBizListener;

import tv.mengzhu.core.wrap.netwock.Page;

/**
 * Created by sunjiale
 * on 2016/6/21.
 */
public class StopBroadcastPresenter implements IBasePresenter<IBasePresenterLinstener>, IBaseBizListener {

    private IBaseBiz mBiz;
    private IBasePresenterLinstener mListener;

    @Override
    public void initPresenter(Context context) {
        mBiz = new StopBrocastBiz();
        mBiz.initBiz(context);
    }

    @Override
    public void onExecute(Object... obj) {
        mBiz.startData(obj);
    }

    @Override
    public void registerListener(IBasePresenterLinstener listener) {
        //内容提供者回调
        mListener = listener;
        mBiz.registerListener(this);
    }

    @Override
    public void dataResult(Object obj, Page page, int status) {
        mListener.dataResult(obj, page, status);
    }

    @Override
    public void errerResult(int code, String msg) {
        mListener.errorResult(code, msg);
    }
}
