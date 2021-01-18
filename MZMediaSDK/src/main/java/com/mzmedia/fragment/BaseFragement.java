package com.mzmedia.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import androidx.fragment.app.Fragment;

import com.mengzhu.live.sdk.core.utils.viewparse.IViewOperater;

public abstract class BaseFragement extends Fragment implements IViewOperater {
    protected View mView;
    protected LayoutInflater mInflater;
    private Bundle mSavedInstanceState;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = getView(inflater);
        mInflater = inflater;
        mView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        return mView;
    }

    public Bundle getSavedInstanceState() {
        return mSavedInstanceState;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mSavedInstanceState = savedInstanceState;
        initView();
        initData();
        setListener();
        loadData();
        super.onActivityCreated(savedInstanceState);
    }

    private View getView(LayoutInflater inflater) {
        int mLayoutId = getLayoutId();
        return inflater.inflate(mLayoutId, null);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * <p>获得布局view</p><br/>
     *
     * @return
     * @author sunxianhao
     * @since 1.0.0
     */
    public View getContentView() {
        return mView;
    }

    public View findViewById(int id) {
        return mView == null ? null : mView.findViewById(id);
    }

    public Context getContext() {
        return getActivity();
    }

    public String getMString(int id) {
        if (id != 0) {
            try {
                return getContext().getResources().getString(id);
            } catch (NullPointerException e) {
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * <p>在绑定Activiy时候，是否存在这样的Activity</p><br/>
     *
     * @return true:存在
     * @author sunxianhao
     * @since 1.0.0
     */
    public boolean isActivityContains() {
        return getActivity() != null && isAdded();
    }
}
