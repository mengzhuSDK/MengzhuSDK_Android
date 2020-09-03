package com.mzmedia.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.mengzhu.live.sdk.business.dto.qa.QAModelDto;
import com.mengzhu.live.sdk.business.dto.qa.ReplyModelDto;
import com.mengzhu.sdk.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import tv.mengzhu.sdk.widgets.CircleImageView;

/**
 * 问答列表adapter
 */
public class QAExpandableAdapter extends BaseExpandableListAdapter {

    private List<QAModelDto> groupList;
    private Context mContext;

    public QAExpandableAdapter(Context mContext, List<QAModelDto> groupList) {
        this.groupList = groupList;
        this.mContext = mContext;
    }

    public void setData(List<QAModelDto> data){
        groupList = data;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return groupList != null ? groupList.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (groupList != null && groupList.get(groupPosition).getReplys() != null) ? groupList.get(groupPosition).getReplys().size() : 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groupList.get(groupPosition).getReplys().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_qa_ask_layout , parent , false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.civAskAvatar = convertView.findViewById(R.id.item_ask_avatar);
            groupViewHolder.tvAskContent = convertView.findViewById(R.id.item_ask_content);
            groupViewHolder.tvAskDate = convertView.findViewById(R.id.item_ask_date);
            groupViewHolder.tvAskName = convertView.findViewById(R.id.item_ask_name);
            groupViewHolder.divider = convertView.findViewById(R.id.group_divider);
            convertView.setTag(groupViewHolder);
        }else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(groupList.get(groupPosition).getAvatar() , groupViewHolder.civAskAvatar);
        groupViewHolder.tvAskName.setText(groupList.get(groupPosition).getNickname());
        groupViewHolder.tvAskDate.setText(groupList.get(groupPosition).getDatetime());
        groupViewHolder.tvAskContent.setText(groupList.get(groupPosition).getContent());
        if (groupList.get(groupPosition).getReplys() == null || groupList.get(groupPosition).getReplys().isEmpty()){
            groupViewHolder.divider.setVisibility(View.VISIBLE);
        }else {
            groupViewHolder.divider.setVisibility(View.GONE);
        }
        return convertView;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_qa_answer_layout , parent , false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.tvAnswerContent = convertView.findViewById(R.id.item_answer_content);
            convertView.setTag(childViewHolder);
        }else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        List<ReplyModelDto> childList = groupList.get(groupPosition).getReplys();
        if (childList != null){
            String nickName = childList.get(childPosition).getNickname() + "：";
            SpannableString spannableString = new SpannableString(nickName + childList.get(childPosition).getContent());
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#DF1435"));
            spannableString.setSpan(foregroundColorSpan, 0, nickName.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            childViewHolder.tvAnswerContent.setText(spannableString);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class GroupViewHolder {
        private CircleImageView civAskAvatar;
        private TextView tvAskName;
        private TextView tvAskDate;
        private TextView tvAskContent;
        private View divider;
    }

    class ChildViewHolder {
        private TextView tvAnswerContent;
    }
}
