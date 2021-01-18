package com.mzmedia.adapter.base;

import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.viewpager.widget.ViewPager;

import com.mengzhu.sdk.R;
import com.mzmedia.widgets.HomeTitleSizeBarView;
import com.mzmedia.widgets.magicindicator.buildins.UIUtil;
import com.mzmedia.widgets.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.mzmedia.widgets.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.mzmedia.widgets.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.mzmedia.widgets.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by max on 2019/3/11.
 */

public class WatchTitleBarAdapter extends CommonNavigatorAdapter {
    private ViewPager mViewPager;
    private List<String> mDataList = new ArrayList<>();
    private Context mContext;
    private boolean isNewYear;

    public WatchTitleBarAdapter(Context context, ViewPager pager, boolean isNewYear) {
        mViewPager = pager;
        mContext = context;
        this.isNewYear = isNewYear;
    }

    public WatchTitleBarAdapter(Context context, ViewPager pager) {
        mViewPager = pager;
        mContext = context;
        this.isNewYear = false;
    }

    public void setDataList(List<String> list) {
        mDataList = list;
    }

    public void addDataList(List<String> list) {
        mDataList = list;
    }

    public void clearDataList() {
        mDataList.clear();
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        HomeTitleSizeBarView simplePagerTitleView = new HomeTitleSizeBarView(context);
        simplePagerTitleView.setText(mDataList.get(index));
        int padding = UIUtil.dip2px(context, 0);
        simplePagerTitleView.setPadding(padding, 0, padding, 0);
        simplePagerTitleView.setSelectedSize(16);
        simplePagerTitleView.setNormalSize(12);
        simplePagerTitleView.setNormalColor(mContext.getResources().getColor(isNewYear ? R.color.white_60 : R.color.white_60));
        simplePagerTitleView.setSelectedColor(mContext.getResources().getColor(isNewYear ? R.color.color_ff2145 : R.color.color_ff2145));
        simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(index);
            }
        });
        return simplePagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
        indicator.setLineHeight(UIUtil.dip2px(context, 2));
        indicator.setLineWidth(UIUtil.dip2px(context, 16));
        indicator.setRoundRadius(UIUtil.dip2px(context, 3));
        indicator.setStartInterpolator(new AccelerateInterpolator());
        indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
//        indicator.setColors(R.color.recommend_circle_dot_playback_color);
        indicator.setColors(mContext.getResources().getColor(isNewYear ? R.color.color_ff2145 : R.color.color_ff2145));
        return indicator;
    }
}

