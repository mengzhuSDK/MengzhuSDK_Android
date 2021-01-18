package com.mzmedia.adapter.vote;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mengzhu.live.sdk.business.dto.vote.VoteInfoDto;
import com.mengzhu.live.sdk.business.dto.vote.VoteOptionDto;
import com.mengzhu.live.sdk.core.utils.DensityUtil;
import com.mengzhu.sdk.R;
import com.mzmedia.adapter.base.BaseRecycleHeaderAdapter;
import com.mzmedia.widgets.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class MultipleVoteAdapter extends BaseRecycleHeaderAdapter<VoteOptionDto> {

    private Context mContext;
    private List<VoteOptionDto> mList = new ArrayList<>();
    private VoteInfoDto mVoteInfo;
    private List<VoteOptionDto> mSelectedList = new ArrayList<>();

    private VoteTextOptionAdapter.OnVoteItemCheckChangeListener onVoteItemCheckChangeListener;

    public void setOnVoteItemCheckChangeListener(VoteTextOptionAdapter.OnVoteItemCheckChangeListener onVoteItemCheckChangeListener) {
        this.onVoteItemCheckChangeListener = onVoteItemCheckChangeListener;
    }

    public MultipleVoteAdapter(Context context, VoteInfoDto dto) {
        this.mContext = context;
        this.mVoteInfo = dto;
    }

    @Override
    public void clearData() {
        mList.clear();
    }

    @Override
    public void setData(List<VoteOptionDto> list) {
        this.mList = list;
    }

    @Override
    public List<VoteOptionDto> getSelectedList() {
        return mSelectedList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER)
            return new MultipleViewHolder(mHeaderView);
        return new MultipleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_multiple_vote, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (getItemViewType(i) == TYPE_HEADER) return;
        MultipleViewHolder mViewHolder = (MultipleViewHolder) viewHolder;
        VoteOptionDto mVoteOptionDto = mList.get(getRealPosition(mViewHolder));
        if (mVoteInfo.getIs_vote() == 1 || mVoteInfo.getIs_expired() == 1) {//已经投过票
            mViewHolder.mLayoutChoose.setClickable(false);
            mViewHolder.mCbChoose.setVisibility(View.GONE);
            mViewHolder.mTvNum.setVisibility(View.VISIBLE);
            mViewHolder.mTvNum.setText(mVoteOptionDto.getVote_num() + "票");
            if (mVoteOptionDto.getIs_vote() == 1) {
                mViewHolder.mTvNum.setBackgroundColor(Color.parseColor("#ccff2145"));
            } else {
                mViewHolder.mTvNum.setBackgroundColor(Color.parseColor("#80999999"));
            }
        } else {
            mViewHolder.mLayoutChoose.setClickable(true);
            mViewHolder.mCbChoose.setVisibility(View.VISIBLE);
            mViewHolder.mTvNum.setVisibility(View.GONE);
        }
        ImageLoader.getInstance().displayImage(mVoteOptionDto.getImage(), mViewHolder.mRiv, new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.icon_goods_default)
                .showImageForEmptyUri(R.mipmap.icon_goods_default)
                .showImageOnFail(R.mipmap.icon_goods_default)
                .cacheInMemory(true)
                .build());
        mViewHolder.mCbChoose.setChecked(mVoteOptionDto.isSelect);
        mViewHolder.mTvDecs.setText(mVoteOptionDto.getTitle());
        mViewHolder.mLayoutChoose.setOnClickListener(new CheckClickListener(getRealPosition(mViewHolder) , mVoteOptionDto));
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mViewHolder.mLayoutChoose.getLayoutParams();
        if ((getRealPosition(mViewHolder) % 2) == 0){
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT , 1);
        }else {
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT , 1);
        }
        layoutParams.bottomMargin = DensityUtil.dip2px(mContext.getResources().getDimension(R.dimen.dimen_11));
        mViewHolder.mLayoutChoose.setLayoutParams(layoutParams);
    }

    class CheckClickListener implements View.OnClickListener {
        VoteOptionDto mOptionDto;
        int position;

        public CheckClickListener(int position, VoteOptionDto mOptionDto) {
            this.mOptionDto = mOptionDto;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (mVoteInfo.getSelect_type() == VoteInfoDto.SINGLE_TYPE) {
                if (!mSelectedList.isEmpty() && !mSelectedList.contains(mOptionDto)){
                    return;
                }
                if (mOptionDto.isSelect){
                    mOptionDto.isSelect = false;
                    mSelectedList.clear();
                }else {
                    mOptionDto.isSelect = true;
                    mSelectedList.add(mOptionDto);
                }
            }else {
                if (mSelectedList.size() == mVoteInfo.getMax_select() && !mOptionDto.isSelect) {
                    Toast.makeText(mContext ,String.format("最多可以选择%d项", mVoteInfo.getMax_select()) , Toast.LENGTH_LONG).show();
                    return;
                }
                mOptionDto.isSelect = !mOptionDto.isSelect;
                if (mOptionDto.isSelect){
                    mSelectedList.add(mOptionDto);
                }else {
                    mSelectedList.remove(mOptionDto);
                }
            }
            notifyDataSetChanged();
            if (onVoteItemCheckChangeListener != null){
                onVoteItemCheckChangeListener.checkChange();
            }
        }
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mList.size() : mList.size() + 1;
    }

    class MultipleViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView mRiv;
        TextView mTvDecs;
        RelativeLayout mLayoutChoose;
        CheckBox mCbChoose;
        TextView mTvNum;
        int position;

        public MultipleViewHolder(View view) {
            super(view);
            mLayoutChoose = (RelativeLayout) view.findViewById(R.id.layout_item_multiple_vote);
            mRiv = (RoundedImageView) view.findViewById(R.id.item_multiple_vote_iv);
            mTvDecs = (TextView) view.findViewById(R.id.item_multiple_vote_decs);
            mCbChoose = (CheckBox) view.findViewById(R.id.item_multiple_vote_cb);
            mTvNum = (TextView) view.findViewById(R.id.item_multiple_vote_result_num);
        }
    }
}
