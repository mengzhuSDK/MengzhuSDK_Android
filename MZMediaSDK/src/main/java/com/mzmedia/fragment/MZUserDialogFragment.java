package com.mzmedia.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;

import com.mengzhu.sdk.R;
import com.mzmedia.adapter.base.MZUserDialogTitleAdapter;
import com.mzmedia.fragment.user.MZPushOnlineUserFragment;
import com.mzmedia.widgets.HackyViewPager;
import com.mzmedia.widgets.dialog.BaseDialogFragment;
import com.mzmedia.widgets.magicindicator.MagicIndicator;
import com.mzmedia.widgets.magicindicator.ViewPagerHelper;
import com.mzmedia.widgets.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 主播端用户列表，在线，禁言，踢出
 */
public class MZUserDialogFragment extends BaseDialogFragment {

    private Context mActivity;
    private String channel_id;
    private String ticket_id;

    private static final String[] titles = {"在线观众" , "踢出管理" ,"禁言管理"};

    private HackyViewPager viewPager;
    private MagicIndicator mMagicIndicator;

    public static MZUserDialogFragment newInstance(String channel_id , String ticket_id){
        MZUserDialogFragment userDialogFragment = new MZUserDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("channel_id" , channel_id);
        bundle.putString("ticket_id" , ticket_id);
        userDialogFragment.setArguments(bundle);
        userDialogFragment.setGravity(Gravity.BOTTOM);
        return userDialogFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_user_list;
    }

    @Override
    protected void initViews(View v) {
        this.channel_id = getArguments().getString("channel_id");
        this.ticket_id = getArguments().getString("ticket_id");
        mMagicIndicator = v.findViewById(R.id.tab_dialog_user_list);
        viewPager = v.findViewById(R.id.vp_dialog_user_list);
        viewPager.setLocked(true);
        initMagicIndicator(viewPager);
        v.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    private void initMagicIndicator(ViewPager viewPager) {
        CommonNavigator commonNavigator = new CommonNavigator(mActivity);
        commonNavigator.setAdjustMode(true);
        MZUserDialogTitleAdapter adapter = new MZUserDialogTitleAdapter(mActivity, viewPager);

        adapter.setDataList(Arrays.asList(titles));
        commonNavigator.setAdapter(adapter);
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, viewPager);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(MZPushOnlineUserFragment.newInstance(channel_id , ticket_id , 0));
        fragmentList.add(MZPushOnlineUserFragment.newInstance(channel_id , ticket_id , 1));
        fragmentList.add(MZPushOnlineUserFragment.newInstance(channel_id , ticket_id , 2));
        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getChildFragmentManager() , fragmentList);
        viewPager.setOffscreenPageLimit(fragmentList.size());
        viewPager.setAdapter(tabPagerAdapter);
        viewPager.setCurrentItem(0);
    }

    public class TabPagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragments;

        public TabPagerAdapter(FragmentManager fm, List<Fragment> mHasTabsFragments) {
            super(fm);
            this.fragments = mHasTabsFragments;
        }

        @Override
        public int getCount() {
            return null == fragments ? 0 : fragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

    }
}
