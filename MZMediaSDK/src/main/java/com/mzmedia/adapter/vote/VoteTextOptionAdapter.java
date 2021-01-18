package com.mzmedia.adapter.vote;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.mengzhu.live.sdk.business.dto.vote.VoteInfoDto;
import com.mengzhu.live.sdk.business.dto.vote.VoteOptionDto;
import com.mengzhu.sdk.R;
import com.mzmedia.adapter.base.BaseRecycleHeaderAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 投票文字适配
 */
public class VoteTextOptionAdapter extends BaseRecycleHeaderAdapter<VoteOptionDto> {

    private Context mContext;
    private List<VoteOptionDto> mList = new ArrayList<>();
    private VoteInfoDto mVoteInfo;
    private List<VoteOptionDto> mSelectedList = new ArrayList<>();

    public interface OnVoteItemCheckChangeListener{
        void checkChange();
    }

    private OnVoteItemCheckChangeListener onVoteItemCheckChangeListener;

    public void setOnVoteItemCheckChangeListener(OnVoteItemCheckChangeListener onVoteItemCheckChangeListener) {
        this.onVoteItemCheckChangeListener = onVoteItemCheckChangeListener;
    }

    public VoteTextOptionAdapter(Context context, VoteInfoDto dto) {
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
            return new VoteOptionViewHolder(mHeaderView);
        return new VoteOptionViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_vote_text_option, viewGroup, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (getItemViewType(i) == TYPE_HEADER) return;
        VoteOptionViewHolder voteHolder = (VoteOptionViewHolder) viewHolder;
        int position = getRealPosition(voteHolder);
        VoteOptionDto item = mList.get(position);
        voteHolder.mTvContent.setText(item.getTitle());
        if (mVoteInfo.getIs_vote() == 1 || mVoteInfo.getIs_expired() == 1) {
            voteHolder.mRlLayout.setClickable(false);
            voteHolder.mTvNumVotes.setVisibility(View.VISIBLE);
            voteHolder.mCheck.setVisibility(View.GONE);
            voteHolder.mTvNumVotes.setText(item.getVote_num() + "票");
            if (item.getIs_vote() == 1) {
                voteHolder.mProgressChecked.setProgressDrawable(mContext.getDrawable(R.drawable.bg_progress_item_vote_me));
            } else {
                voteHolder.mProgressChecked.setProgressDrawable(mContext.getDrawable(R.drawable.bg_progress_item_vote));
            }
            voteHolder.mProgressChecked.setProgress(item.getPercentage());
        } else {
            voteHolder.mRlLayout.setClickable(true);
            voteHolder.mTvNumVotes.setVisibility(View.GONE);
            voteHolder.mCheck.setVisibility(View.VISIBLE);
            voteHolder.mProgressChecked.setProgress(0);
            voteHolder.mCheck.setChecked(item.isSelect);
            voteHolder.mRlLayout.setOnClickListener(new CheckClickListener(position , item));
        }

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

    class VoteOptionViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout mRlLayout;
        ProgressBar mProgressChecked;
        TextView mTvContent;
        TextView mTvNumVotes;
        CheckBox mCheck;

        public VoteOptionViewHolder(@NonNull View itemView) {
            super(itemView);
            if (itemView == mHeaderView) return;
            mProgressChecked = itemView.findViewById(R.id.progress_item_vote_checked);
            mTvContent = itemView.findViewById(R.id.tv_item_vote_content);
            mTvNumVotes = itemView.findViewById(R.id.tv_item_vote_num_votes);
            mCheck = itemView.findViewById(R.id.check_item_vote_checkbox);
            mRlLayout = itemView.findViewById(R.id.rl_item_vote_layout);
        }
    }
}
