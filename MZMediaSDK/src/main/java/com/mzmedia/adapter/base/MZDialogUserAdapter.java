package com.mzmedia.adapter.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mengzhu.live.sdk.business.dto.MZOnlineUserListDto;
import com.mengzhu.sdk.R;
import com.mzmedia.widgets.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class MZDialogUserAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<MZOnlineUserListDto> dataList;

    private int type = 0;  //0在线用户 1踢出列表 2禁言列表

    public interface OnHandleUserListener {
        void kickUser(MZOnlineUserListDto dto);

        void silenceUser(MZOnlineUserListDto dto);

        void relieveKick(MZOnlineUserListDto dto);

        void relieveSilence(MZOnlineUserListDto dto);
    }

    private OnHandleUserListener onHandleUserListener;

    public void setOnHandleUserListener(OnHandleUserListener onHandleUserListener) {
        this.onHandleUserListener = onHandleUserListener;
    }

    public MZDialogUserAdapter(Context mContext, List<MZOnlineUserListDto> dataList, int type) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.type = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (type == 1) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_dialog_user_kick, viewGroup, false);
            return new MZKickViewHolder(view);
        } else if (type == 2) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_dialog_user_silence, viewGroup, false);
            return new MZSilenceViewHolder(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_dialog_user_online, viewGroup, false);
            return new MZOnlineViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {
        if (type == 1) {
            MZKickViewHolder kickViewHolder = (MZKickViewHolder) viewHolder;
            kickViewHolder.position = i;
            kickViewHolder.item_user_name.setText(dataList.get(i).getNickname() + "");
            ImageLoader.getInstance().displayImage(dataList.get(i).getAvatar(), kickViewHolder.item_user_avatar, new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.mipmap.icon_default_avatar)
                    .showImageOnFail(R.mipmap.icon_default_avatar)
                    .cacheInMemory(true)
                    .build());
        } else if (type == 2) {
            MZSilenceViewHolder silenceViewHolder = (MZSilenceViewHolder) viewHolder;
            silenceViewHolder.position = i;
            silenceViewHolder.item_user_name.setText(dataList.get(i).getNickname() + "");
            ImageLoader.getInstance().displayImage(dataList.get(i).getAvatar(), silenceViewHolder.item_user_avatar, new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.mipmap.icon_default_avatar)
                    .showImageOnFail(R.mipmap.icon_default_avatar)
                    .cacheInMemory(true)
                    .build());
        } else {
            MZOnlineViewHolder onlineViewHolder = (MZOnlineViewHolder) viewHolder;
            onlineViewHolder.position = i;
            onlineViewHolder.item_user_name.setText(dataList.get(i).getNickname() + "");
            ImageLoader.getInstance().displayImage(dataList.get(i).getAvatar(), onlineViewHolder.item_user_avatar, new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.mipmap.icon_default_avatar)
                    .showImageOnFail(R.mipmap.icon_default_avatar)
                    .cacheInMemory(true)
                    .build());
            if (dataList.get(i).getIs_gag() == 1) {
                onlineViewHolder.item_ban_chat.setText("解除禁言");
                onlineViewHolder.item_ban_chat.setBackgroundResource(R.color.white);
                onlineViewHolder.item_ban_chat.setTextColor(mContext.getResources().getColor(R.color.color_ff1f60));
            } else {
                onlineViewHolder.item_ban_chat.setText("禁言");
                onlineViewHolder.item_ban_chat.setBackgroundResource(R.drawable.mz_ban_chat_btn_bg);
                onlineViewHolder.item_ban_chat.setTextColor(mContext.getResources().getColor(R.color.white));
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    class MZOnlineViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView item_user_avatar;
        private TextView item_user_name;
        private TextView item_kick;
        private TextView item_ban_chat;
        private int position;

        public MZOnlineViewHolder(@NonNull View itemView) {
            super(itemView);
            item_user_avatar = itemView.findViewById(R.id.item_user_avatar);
            item_user_name = itemView.findViewById(R.id.item_user_name);
            item_kick = itemView.findViewById(R.id.item_kick);
            item_ban_chat = itemView.findViewById(R.id.item_ban_chat);
            item_kick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onHandleUserListener != null) {
                        onHandleUserListener.kickUser(dataList.get(position));
                    }
                }
            });

            item_ban_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onHandleUserListener != null) {
                        if (dataList.get(position).getIs_gag() == 1) {
                            onHandleUserListener.relieveSilence(dataList.get(position));
                        } else {
                            onHandleUserListener.silenceUser(dataList.get(position));
                        }
                    }
                }
            });
        }
    }

    class MZKickViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView item_user_avatar;
        private TextView item_user_name;
        private TextView item_relieve_kick;
        private int position;

        public MZKickViewHolder(@NonNull View itemView) {
            super(itemView);
            item_user_avatar = itemView.findViewById(R.id.item_user_avatar);
            item_user_name = itemView.findViewById(R.id.item_user_name);
            item_relieve_kick = itemView.findViewById(R.id.item_relieve_kick);
            item_relieve_kick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onHandleUserListener != null) {
                        onHandleUserListener.relieveKick(dataList.get(position));
                    }
                }
            });
        }
    }

    class MZSilenceViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView item_user_avatar;
        private TextView item_user_name;
        private TextView item_relieve_silence;
        private int position;

        public MZSilenceViewHolder(@NonNull View itemView) {
            super(itemView);
            item_user_avatar = itemView.findViewById(R.id.item_user_avatar);
            item_user_name = itemView.findViewById(R.id.item_user_name);
            item_relieve_silence = itemView.findViewById(R.id.item_relieve_silence);

            item_relieve_silence.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onHandleUserListener != null) {
                        onHandleUserListener.relieveSilence(dataList.get(position));
                    }
                }
            });
        }
    }
}
