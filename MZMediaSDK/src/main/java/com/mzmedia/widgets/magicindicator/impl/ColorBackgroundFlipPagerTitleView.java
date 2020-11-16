package com.mzmedia.widgets.magicindicator.impl;

import android.content.Context;

import com.mzmedia.widgets.magicindicator.buildins.commonnavigator.titles.SimpleBackgroundPagerTitleView;

public class ColorBackgroundFlipPagerTitleView extends SimpleBackgroundPagerTitleView {

    private float mChangePercent = 0.5f;
    public ColorBackgroundFlipPagerTitleView(Context context) {
        super(context);
    }
    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
        if (leavePercent >= mChangePercent) {
            setTextColor(mNormalColor);
            setBackgroundResource(mNormalBackground);
        } else {
            setTextColor(mSelectedColor);
            setBackgroundResource(mSelectedBackground);
        }
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        if (enterPercent >= mChangePercent) {
            setTextColor(mSelectedColor);
            setBackgroundResource(mSelectedBackground);
        } else {
            setTextColor(mNormalColor);
            setBackgroundResource(mNormalBackground);
        }
    }

    @Override
    public void onSelected(int index, int totalCount) {
    }

    @Override
    public void onDeselected(int index, int totalCount) {
    }

    public float getChangePercent() {
        return mChangePercent;
    }

    public void setChangePercent(float changePercent) {
        mChangePercent = changePercent;
    }
}
