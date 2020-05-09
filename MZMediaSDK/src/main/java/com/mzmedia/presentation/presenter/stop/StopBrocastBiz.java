package com.mzmedia.presentation.presenter.stop;

import android.content.Context;

import com.mengzhu.core.base.datainterface.BaseLoadListener;
import com.mengzhu.core.base.datainterface.IDao;
import com.mengzhu.core.base.datainterface.impl.support.Result;
import com.mengzhu.live.sdk.core.netwock.BazaarPostDao;
import com.mengzhu.live.sdk.core.netwock.Paths;
import com.mengzhu.sdk.download.config.CommonConstant;
import com.mzmedia.presentation.biz.IBaseBiz;
import com.mzmedia.presentation.biz.IBaseBizListener;

/**
 * Created by sunjiale
 * on 2016/6/21.
 */
public class StopBrocastBiz extends BaseLoadListener implements IBaseBiz<IBaseBizListener> {

    private BazaarPostDao mPostDao;
    private IBaseBizListener mListener;

    @Override
    public void initBiz(Context context) {
        mPostDao = new BazaarPostDao<EndBroadcastInfoDto>("http://b.t.zmengzhu.com", EndBroadcastInfoDto.class, BazaarPostDao.ARRAY_DATA_CHUNK);
        mPostDao.isRequestCache(false);
        mPostDao.isCacheData(false);
    }

    @Override
    public void registerListener(IBaseBizListener listener) {
        mPostDao.registerListener(this);
        mListener = listener;
    }

    @Override
    public void startData(Object... obj) {
        mPostDao.putUrlStern("/api/live/stop");
        //此处处理业务执行（场景举例：执行数据请求处理请求参数或本地数据获取等等）
        mPostDao.putParams("ticket_id", String.valueOf(obj[0]));
        mPostDao.putParams("appid", "2019101019585068343");
        mPostDao.putAllURLParams(null);
        mPostDao.asyncDoAPI();
    }

    //数据请求错误回调
    @Override
    public void onError(Result result) {
        super.onError(result);
        mListener.errerResult(result.getCode(), result.getErrmsg());
    }

    //数据请求结果回调
    @Override
    public void onComplete(IDao.ResultType resultType) {
        super.onComplete(resultType);
        mListener.dataResult(mPostDao.getData(), mPostDao.getPage(), mPostDao.getStatus());
    }

    //数据请求取消回调
    @Override
    public void onCancel() {
        super.onCancel();
    }
}
