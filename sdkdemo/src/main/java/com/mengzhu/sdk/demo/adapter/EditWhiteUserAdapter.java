package com.mengzhu.sdk.demo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mengzhu.live.sdk.business.dto.Config.MZWhiteUserDto;
import com.mengzhu.sdk.demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 编辑用户adapter
 */
public class EditWhiteUserAdapter extends RecyclerView.Adapter {

    public static final int TYPE_ADD = 1;
    public static final int TYPE_ITEM = 2;

    private Context mContext;
    private List<MZWhiteUserDto.WhiteUserItem> dataList = new ArrayList<>();

    public interface OnItemClickListener {
        void itemClick(MZWhiteUserDto.WhiteUserItem whiteUserItem);

        void delClick(MZWhiteUserDto.WhiteUserItem whiteUserItem);

        void addClick();
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public EditWhiteUserAdapter(Context mContext, List<MZWhiteUserDto.WhiteUserItem> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == TYPE_ADD) {
            return new AddViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_edit_user_add, viewGroup, false));
        } else if (i == TYPE_ITEM) {
            return new ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_edit_user_item, viewGroup, false));
        }

        return new ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_edit_user_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {
        if (viewHolder instanceof AddViewHolder) {

        }
        if (viewHolder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
            itemViewHolder.position = i;
            itemViewHolder.tv_phone.setText(dataList.get(i).getPhone());
        }
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == dataList.size() ? TYPE_ADD : TYPE_ITEM;
    }

    public class AddViewHolder extends RecyclerView.ViewHolder {

        public AddViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.addClick();
                    }
                }
            });
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private int position;
        private TextView tv_phone;
        private ImageView iv_Del;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_phone = itemView.findViewById(R.id.tv_phone);
            iv_Del = itemView.findViewById(R.id.iv_del);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.itemClick(dataList.get(position));
                    }
                }
            });
            iv_Del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.delClick(dataList.get(position));
                    }
                }
            });
        }
    }
}
