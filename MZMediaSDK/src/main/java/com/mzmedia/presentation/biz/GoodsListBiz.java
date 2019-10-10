package com.mzmedia.presentation.biz;

import android.content.Context;

import com.mengzhu.core.base.datainterface.BaseLoadListener;
import com.mengzhu.core.base.datainterface.IDao;
import com.mengzhu.core.base.datainterface.impl.support.Result;
import com.mengzhu.live.sdk.core.netwock.BazaarGetDao;
import com.mzmedia.presentation.dto.MallGoodsListDto;

/**
 * @author max
 * @description 商品入口
 */

public class GoodsListBiz extends BaseLoadListener implements IBaseBiz<IBaseBizListener> {

    private BazaarGetDao<MallGoodsListDto> mDao;
    private BazaarGetDao mGoodsListDao;
    private IBaseBizListener mListener;
    private IBaseBizListener mGoodsListener;
    private Context mContext;

    @Override
    public void initBiz(Context context) {
        this.mContext = context;
//        mDao = new BazaarGetDao<MallGoodsListDto>(Paths.getInstance().getBusiness() + Paths.GOODS_LISTBYTICKETID, MallGoodsListDto.class, BazaarGetDao.ARRAY_DATA);
        mDao.registerListener(this);
        mDao.isRequestCache(false);
        mDao.isCacheData(false);
//        mGoodsListDao = new BazaarGetDao(Paths.getInstance().getBusiness(), MallGoodsListDto.class, BazaarGetDao.ARRAY_DATA);
        mGoodsListDao.setNoCache();
    }

    @Override
    public void registerListener(IBaseBizListener listener) {
        mListener = listener;
    }

    @Override
    public void startData(Object... obj) {
//        mDao.putParams(RequestParamConstants.TICKET_ID, (String) obj[0]);
//        mDao.putParams(RequestParamConstants.CHANNEL_ID, (String) obj[0]);
        if ((Boolean) obj[1]) {
            mDao.startTask();
        } else {
            mDao.nextTask();
        }
    }

    @Override
    public void onPrepare() {
        super.onPrepare();
    }

    @Override
    public void onCancel() {
        super.onCancel();
    }

    @Override
    public void onProgress(long progress, long len) {
        super.onProgress(progress, len);
    }

    @Override
    public void onError(Result result) {
        super.onError(result);
        mListener.errerResult(result.getCode(), result.getErrmsg());
    }

    @Override
    public void onComplete(IDao.ResultType resultType) {
        super.onComplete(resultType);
        mListener.dataResult(mDao.getList(), mDao.getPage(), mDao.getStatus());
    }

    public void onGoodsListStartData(Object... obj) {
//        mGoodsListDao.putUrlStern(Paths.GOODS_LIST);
        if ((Boolean) obj[0]) {
            mGoodsListDao.startTask();
        } else {
            mGoodsListDao.nextTask();
        }
    }

    public void registerGoodsListListener(IBaseBizListener listener) {
        mGoodsListener = listener;
        mGoodsListDao.registerListener(new GoodsListener());
    }

    public class GoodsListener extends BaseLoadListener {
        @Override
        public void onComplete(IDao.ResultType resultType) {
            super.onComplete(resultType);
            mGoodsListener.dataResult(mGoodsListDao.getList(), mGoodsListDao.getPage(), mGoodsListDao.getStatus());
        }

        @Override
        public void onError(Result result) {
            super.onError(result);
            mGoodsListener.errerResult(result.getCode(), result.getErrmsg());
        }
    }
}
