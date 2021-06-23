package com.mzmedia.fragment.redpacket;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mengzhu.live.sdk.adapter.CommonListAdapter;
import com.mengzhu.live.sdk.business.dto.chat.ChatTextDto;
import com.mengzhu.live.sdk.business.dto.chat.impl.ChatCompleteDto;
import com.mengzhu.live.sdk.business.dto.redpacket.MZRedPacketHistoryDto;
import com.mengzhu.live.sdk.business.dto.redpacket.MZRedPacketItemDto;
import com.mengzhu.live.sdk.core.utils.DateUtils;
import com.mengzhu.live.sdk.ui.api.MZApiDataListener;
import com.mengzhu.live.sdk.ui.api.MZApiRequest;
import com.mengzhu.sdk.R;
import com.mzmedia.fragment.BaseFragement;
import com.mzmedia.pullrefresh.PullToRefreshBase;
import com.mzmedia.pullrefresh.PullToRefreshListView;
import com.mzmedia.widgets.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import tv.mengzhu.core.wrap.netwock.Page;
import tv.mengzhu.core.wrap.user.presenter.MyUserInfoPresenter;

/**
 * 红包领取记录
 */
public class MZRedPacketHistoryFragment extends BaseFragement {

    private ImageView mAvatar;
    private PullToRefreshListView mListView;
    private TextView mNickname;
    private TextView mDescTv;
    private TextView mStatusTv;
    //领取金额
    private TextView mGetMoney;
    private LinearLayout mLayoutGetMoney;
    private MZRedPacketHistoryDto mRedPacketDto;
    private List<MZRedPacketItemDto> mList = new ArrayList<>();
    private boolean isShowNoMoreLabel;
    private boolean isRefresh = true;

    private ChatTextDto chatTextDto;
    private ChatCompleteDto chatCompleteDto;

    private int page_size = 50;

    MZApiRequest mzApiRequest;

    public static MZRedPacketHistoryFragment newInstance(ChatTextDto dto) {
        MZRedPacketHistoryFragment fragment = new MZRedPacketHistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("ChatTextDto", dto);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initView() {
        chatTextDto = (ChatTextDto) getArguments().getSerializable("ChatTextDto");
        if (chatTextDto != null) {
            chatCompleteDto = (ChatCompleteDto) chatTextDto.getBaseDto();
        }
        mAvatar = (ImageView) findViewById(R.id.iv_activit_rp_avatar);
        mNickname = (TextView) findViewById(R.id.tv_activity_rp_nikename);
        mDescTv = (TextView) findViewById(R.id.tv_activity_rp_desc);
        mStatusTv = (TextView) findViewById(R.id.tv_activity_rp_status);
        mGetMoney = (TextView) findViewById(R.id.tv_activity_rp_money);
        mLayoutGetMoney = (LinearLayout) findViewById(R.id.ll_activity_rp_money);
        mListView = (PullToRefreshListView) findViewById(R.id.rp_records_listview);
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        if (chatTextDto == null || chatCompleteDto == null) {
            getActivity().finish();
            return;
        }
    }

    @Override
    public void initData() {
        mzApiRequest = new MZApiRequest();
        mzApiRequest.createRequest(getContext() , MZApiRequest.API_GET_RED_PACKET_HISTORY);
    }

    @Override
    public void setListener() {
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isRefresh = true;
                String unique_id = MyUserInfoPresenter.getInstance().getUserInfo().getUniqueID();
                mzApiRequest.startData(MZApiRequest.API_GET_RED_PACKET_HISTORY , chatCompleteDto.getBonus_id() , unique_id , page_size , isRefresh);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (isShowNoMoreLabel) {
                    mListView.getLoadingLayoutProxy(false, true).setNoMoreLabel(true);
                    mListView.onRefreshComplete();
                } else {
                    isRefresh = false;
                    String unique_id = MyUserInfoPresenter.getInstance().getUserInfo().getUniqueID();
                    mzApiRequest.startData(MZApiRequest.API_GET_RED_PACKET_HISTORY , chatCompleteDto.getBonus_id() , unique_id , page_size , isRefresh);
                }
            }
        });

        mzApiRequest.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String apiType, Object dto, Page page, int status) {
                mRedPacketDto = (MZRedPacketHistoryDto) dto;

                if(isRefresh){
                    mList.clear();
                }
                if (mRedPacketDto != null) {
                    mList.addAll(mRedPacketDto.getObtainList());
                    isShowNoMoreLabel = mRedPacketDto.getObtainList().size()< 50;
                    setAdapter();
                }
                if(mListView.isRefreshing()){
                    mListView.onRefreshComplete();
                }
            }

            @Override
            public void errorResult(String apiType, int code, String msg) {
                if(mListView.isRefreshing()){
                    mListView.onRefreshComplete();
                }
            }
        });
    }

    @Override
    public void loadData() {
        String unique_id = MyUserInfoPresenter.getInstance().getUserInfo().getUniqueID();
        mzApiRequest.startData(MZApiRequest.API_GET_RED_PACKET_HISTORY , chatCompleteDto.getBonus_id() , unique_id , page_size , isRefresh);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_red_packet_records;
    }

    private void setData() {
        if (mRedPacketDto != null) {
            ImageLoader.getInstance().displayImage(mRedPacketDto.getSendInfo().getAvatar(), mAvatar, new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.mipmap.icon_default_avatar)
                    .showImageOnFail(R.mipmap.icon_default_avatar)
                    .cacheInMemory(true)
                    .build());
            mNickname.setText(mRedPacketDto.getSendInfo().getNickname());
            mDescTv.setText(mRedPacketDto.getLotteryInfo().getSlogan());
            if (TextUtils.isEmpty(mRedPacketDto.getUser_receive().getAmount())) {
                mLayoutGetMoney.setVisibility(View.GONE);
            } else {
                mGetMoney.setText(mRedPacketDto.getUser_receive().getAmount());
            }
            String str;
            str = String.format("已领取%s/%s个，共%s/%s元", mRedPacketDto.getLotteryInfo().getAlreadyQuantity(), mRedPacketDto.getLotteryInfo().getQuantity()
                    , mRedPacketDto.getLotteryInfo().getRemainAmount(), mRedPacketDto.getLotteryInfo().getAmount());

            mStatusTv.setText(str);
        }
    }

    private CommonListAdapter<MZRedPacketItemDto> adapter;

    private void setAdapter() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();

            return;
        }
        setData();
        adapter = new CommonListAdapter<MZRedPacketItemDto>(getActivity(), R.layout.item_redpacket_records, mList) {

            DateViewHolder mViewHolder = null;

            @Override
            public boolean isEnabled(int position) {
                return false;
            }

            @Override
            public ViewHolder initView(int position, View container) {
                mViewHolder = new DateViewHolder();
                mViewHolder.iv_item_rp_records_avatar = (RoundedImageView) container.findViewById(R.id.iv_item_rp_records_avatar);
                mViewHolder.tv_item_rp_nike = (TextView) container.findViewById(R.id.tv_item_rp_nike);
                mViewHolder.tv_item_rp_time = (TextView) container.findViewById(R.id.tv_item_rp_time);
                mViewHolder.tv_item_rp_money = (TextView) container.findViewById(R.id.tv_item_rp_money);
                mViewHolder.tv_item_rp_icon = (TextView) container.findViewById(R.id.tv_item_rp_icon);
                return mViewHolder;
            }

            @Override
            public void initData(final int position, ViewHolder holder, final MZRedPacketItemDto dto) {

                mViewHolder = (DateViewHolder) holder;
                mViewHolder.tv_item_rp_nike.setText(dto.getNickname() + "");
                if ("1".equals(dto.getIsTop())) {
                    mViewHolder.tv_item_rp_icon.setVisibility(View.VISIBLE);
                } else {
                    mViewHolder.tv_item_rp_icon.setVisibility(View.GONE);
                }
                mViewHolder.tv_item_rp_time.setText(DateUtils.stringToDateNoymd(dto.getActionTime() + "000"));
                mViewHolder.tv_item_rp_money.setText(dto.getAmount() + "");
                ImageLoader.getInstance().displayImage(dto.getAvatar(), mViewHolder.iv_item_rp_records_avatar, new DisplayImageOptions.Builder()
                        .showImageForEmptyUri(R.mipmap.icon_default_avatar)
                        .showImageOnFail(R.mipmap.icon_default_avatar)
                        .cacheInMemory(true)
                        .build());
            }

        };
        mListView.setAdapter(adapter);
    }


    class DateViewHolder extends CommonListAdapter.ViewHolder {
        RoundedImageView iv_item_rp_records_avatar;
        private TextView tv_item_rp_nike;
        private TextView tv_item_rp_time;
        private TextView tv_item_rp_money;
        private TextView tv_item_rp_icon;
    }

}
