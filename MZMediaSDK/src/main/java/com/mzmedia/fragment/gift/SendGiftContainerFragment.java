package com.mzmedia.fragment.gift;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mengzhu.live.sdk.adapter.CommonListAdapter;
import com.mengzhu.live.sdk.business.dto.gift.GiftDto;
import com.mengzhu.live.sdk.business.dto.gift.PushWrapperDto;
import com.mengzhu.sdk.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tv.mengzhu.core.module.base.view.fragment.BaseFragement;

/**
 * 赠送礼物详情页面
 */
public class SendGiftContainerFragment extends BaseFragement {
    public static Map<String, OnItemListener> mItemListener;
    private GridView mGvEight;
    public static String PUSHDATA = "pushData";
    private static final String INDEX_KEY = "index_key";
    private int mIndex;

    public static SendGiftContainerFragment newInstance(ArrayList<PushWrapperDto> mDatas, int index) {
        SendGiftContainerFragment eightFragment = new SendGiftContainerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PUSHDATA, mDatas);
        bundle.putInt(INDEX_KEY, index);
        eightFragment.setArguments(bundle);
        if (mItemListener == null) {
            mItemListener = new HashMap<>();
        }
        return eightFragment;
    }

    public interface OnItemListener {
        public void onItemClick(int index, int position);
    }


    @Override
    public void initView() {
        mGvEight = (GridView) findViewById(R.id.gv_fragment_gift_container);
    }

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        if (null != bundle && bundle.containsKey(PUSHDATA)) {
            ArrayList<PushWrapperDto> data = (ArrayList<PushWrapperDto>) bundle.getSerializable(PUSHDATA);
            mIndex = bundle.getInt(INDEX_KEY, 0);
            mDatas = data;
        }

    }
    class GiftItemListener implements OnItemListener {

        @Override
        public void onItemClick(int index, int position) {
            if (index == mIndex) {
                for (int i = 0; i < mDatas.size(); i++) {
                    PushWrapperDto item = mDatas.get(i);
                    if (position == i) {
                        item.isChecked = true;
                        if (null != listener) {
                            listener.onColumnItemClicked(item);
                        }
                    } else {
                        item.isChecked = false;
                    }
                }
                mAdapter.notifyDataSetChanged();
            } else {
                for (int i = 0; i < mDatas.size(); i++) {
                    PushWrapperDto item = mDatas.get(i);
                    item.isChecked = false;
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void setListener() {
        if(mItemListener!=null){
            mItemListener.put(SendGiftContainerFragment.class.getSimpleName() + "" + mIndex, new GiftItemListener());
        }
        mGvEight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mDatas.get(position).isChecked) {
                    return;
                }

                for (int i = 0; i < mItemListener.size(); i++) {
                    mItemListener.get(SendGiftContainerFragment.class.getSimpleName() + "" + i).onItemClick(mIndex, position);
                }
//                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private List<PushWrapperDto> mDatas = new ArrayList<PushWrapperDto>();
    private CommonListAdapter<PushWrapperDto> mAdapter;

    @Override
    public void loadData() {
        mAdapter = new CommonListAdapter<PushWrapperDto>(getActivity(), R.layout.item_send_gift, mDatas) {
            PushViewHolder mPushViewHolder = null;
            DisplayImageOptions mDisplayImageOptions = null;

            @Override
            public ViewHolder initView(int position, View container) {
                mPushViewHolder = new PushViewHolder();
                mPushViewHolder.layout_item_gift = container.findViewById(R.id.layout_item_gift);
                mPushViewHolder.mIVGiftIcon = (ImageView) container.findViewById(R.id.iv_item_gift_icon_label);
                mPushViewHolder.mTvName = (TextView) container.findViewById(R.id.tv_item_gift_name);
                mPushViewHolder.mTvCost = (TextView) container.findViewById(R.id.tv_item_gift_price);
                return mPushViewHolder;
            }

            @Override
            public void initData(int position, ViewHolder holder, PushWrapperDto item) {
                mPushViewHolder = (PushViewHolder) holder;

                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
                ImageLoader.getInstance().displayImage(((GiftDto) item).getIcon() , mPushViewHolder.mIVGiftIcon , options);
                if (item.isChecked) {
                    mPushViewHolder.layout_item_gift.setBackgroundResource(R.drawable.bg_item_gift_select);
                } else {
                    mPushViewHolder.layout_item_gift.setBackgroundResource(R.color.color_4a4a4a);
                    if(TextUtils.isEmpty(mDatas.get(position).getName())){
                        mPushViewHolder.layout_item_gift.setVisibility(View.GONE);
                    }
                }
                mPushViewHolder.mTvName.setText(((GiftDto) item).name);
                if (((GiftDto) item).getPrice().equals("0")) {
                    mPushViewHolder.mTvCost.setText("免费");
                }else {
                    mPushViewHolder.mTvCost.setText("¥" + ((GiftDto) item).getPay_amount());
                }
            }
        };
        mGvEight.setAdapter(mAdapter);
    }

    private onColumnItemListener listener;

    public interface onColumnItemListener {
        void onColumnItemClicked(PushWrapperDto item);
    }

    public void setOnColumnItemListener(onColumnItemListener listener) {
        this.listener = listener;
    }

    class PushViewHolder extends CommonListAdapter.ViewHolder {
        RelativeLayout layout_item_gift;
        ImageView mIVGiftIcon;
        TextView mTvName;
        TextView mTvCost;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mItemListener != null) {
            mItemListener.clear();
            mItemListener = null;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_gift_container;
    }
}
