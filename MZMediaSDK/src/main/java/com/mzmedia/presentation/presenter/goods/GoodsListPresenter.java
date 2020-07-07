package com.mzmedia.presentation.presenter.goods;

import android.content.Context;

import com.mengzhu.live.sdk.business.presenter.IBasePresenter;
import com.mengzhu.live.sdk.business.presenter.IBasePresenterLinstener;
import com.mzmedia.presentation.biz.GoodsListBiz;
import com.mzmedia.presentation.biz.IBaseBizListener;

import tv.mengzhu.core.wrap.netwock.Page;

/**
 * @author max
 * @description
 */

public class GoodsListPresenter implements IBasePresenter<IBasePresenterLinstener>, IBaseBizListener {
    private GoodsListBiz mBiz;
    private IBasePresenterLinstener mListener;
    private IBasePresenterLinstener mGoodsListListener;

    @Override
    public void initPresenter(Context context) {
        mBiz = new GoodsListBiz();
        mBiz.initBiz(context);
    }

    @Override
    public void onExecute(Object... obj) {
        mBiz.startData(obj);
    }

    @Override
    public void registerListener(IBasePresenterLinstener listener) {
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

    public void onGoodsListExecute(Object... obj) {
        mBiz.onGoodsListStartData(obj);
    }

    public void registerGoodsListListener(IBasePresenterLinstener linstener) {
        mGoodsListListener = linstener;
        mBiz.registerGoodsListListener(new GoodsList());
    }

    public class GoodsList implements IBaseBizListener {

        @Override
        public void dataResult(Object obj, Page page, int status) {
            mGoodsListListener.dataResult(obj, page, status);
        }

        @Override
        public void errerResult(int code, String msg) {
            mGoodsListListener.errorResult(code, msg);
        }
    }
}
