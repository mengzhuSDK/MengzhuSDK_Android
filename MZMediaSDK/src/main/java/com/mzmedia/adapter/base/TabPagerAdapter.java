package com.mzmedia.adapter.base;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by sunjiale
 * on 2016/6/28.
 */
public class TabPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private float ratio = 0;

    public TabPagerAdapter(FragmentManager fm, List<Fragment> mHasTabsFragments) {
        super(fm);
        this.fragments = mHasTabsFragments;
    }
    public TabPagerAdapter(FragmentManager fm, List<Fragment> mHasTabsFragments, float ratio ) {
        super(fm);
        this.fragments = mHasTabsFragments;
        this.ratio = ratio;
    }

    @Override
    public int getCount() {
        return null == fragments ? 0 : fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public float getPageWidth(int position) {
        if (ratio != 0) {
            return ratio;
        }
        return super.getPageWidth(position);
    }
}
