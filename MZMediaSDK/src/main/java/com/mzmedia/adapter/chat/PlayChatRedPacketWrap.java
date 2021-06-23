package com.mzmedia.adapter.chat;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mengzhu.live.sdk.business.dto.chat.ChatMessageDto;
import com.mengzhu.live.sdk.business.dto.chat.ChatTextDto;
import com.mengzhu.live.sdk.business.dto.chat.impl.ChatCompleteDto;
import com.mengzhu.sdk.R;
import com.mzmedia.adapter.base.BaseViewObtion;
import com.mzmedia.fragment.PlayerChatListFragment;
import com.mzmedia.widgets.CircularImage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 红包item消息
 */
public class PlayChatRedPacketWrap extends BaseViewObtion {
    private Context mContext;
    private ViewHolder mHolder;

    public PlayChatRedPacketWrap(Context context) {
        mContext = context;
    }

    @Override
    public View createView(Object o, int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = View.inflate(mContext, isHalfPlayer ? R.layout.mz_red_packet_chat_item_half : R.layout.mz_red_packet_chat_item, null);
            mHolder.mPlayerChatIcon = convertView.findViewById(R.id.player_chat_icon);
            mHolder.mPlayerChatLayout = convertView.findViewById(R.id.player_chat_avatar_layout);
            mHolder.mPlayerChatUsername = convertView.findViewById(R.id.player_chat_username);
            mHolder.mPlayerRedPacketTitle = convertView.findViewById(R.id.red_packet_title);
            mHolder.mPlayerChatRedPacketLayout = convertView.findViewById(R.id.mz_chat_red_packet_layout);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    @Override
    public void updateView(Object o, int position, View convertView) {
        ChatMessageDto dto = (ChatMessageDto) o;
        ViewHolder holder = (ViewHolder) convertView.getTag();
        ChatTextDto textDto = dto.getText();
        holder.mPlayerChatUsername.setText(textDto.getUser_name() + ": ");
        holder.mPlayerChatIcon.setTag(textDto);
        holder.mPlayerChatIcon.setOnClickListener(new ChatIconListener(textDto));

        ImageLoader.getInstance().displayImage(textDto.getAvatar(), holder.mPlayerChatIcon, new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.icon_default_avatar)
                .showImageOnFail(R.mipmap.icon_default_avatar)
                .cacheInMemory(true)
                .build());
        ChatCompleteDto chatCompleteDto = (ChatCompleteDto) textDto.getBaseDto();
        holder.mPlayerRedPacketTitle.setText(chatCompleteDto.getSlogan() + "");
        holder.mPlayerChatRedPacketLayout.setOnClickListener(new ChatIconListener(textDto));
    }

    private PlayerChatListFragment.OnRedPacketClickListener onRedPacketClickListener;

    public void setOnRedPacketClickListener(PlayerChatListFragment.OnRedPacketClickListener onRedPacketClickListener) {
        this.onRedPacketClickListener = onRedPacketClickListener;
    }

    public interface OnChatIconClickListener {

        void onChatIconClick(ChatTextDto dto);

    }

    private PlayerChatLeltWrap.OnChatIconClickListener mOnChatIconClickListener;

    public void setOnChatIconClickListener(PlayerChatLeltWrap.OnChatIconClickListener listener) {
        mOnChatIconClickListener = listener;
    }

    class ChatIconListener implements View.OnClickListener {
        private ChatTextDto mDto;

        public ChatIconListener(ChatTextDto dto) {
            this.mDto = dto;
        }

        @Override
        public void onClick(View view) {

            if (view.getId() == R.id.player_chat_icon) {
                if (mOnChatIconClickListener != null) {
                    mOnChatIconClickListener.onChatIconClick(mDto);
                }
            }
            if (view.getId() == R.id.mz_chat_red_packet_layout) {
                if (onRedPacketClickListener != null) {
                    onRedPacketClickListener.onRedPacketClick(mDto);
                }
            }
        }
    }


    class ViewHolder {
        LinearLayout mPlayerChatLayout;
        CircularImage mPlayerChatIcon;
        TextView mPlayerChatUsername;
        TextView mPlayerRedPacketTitle;
        LinearLayout mPlayerChatRedPacketLayout;
    }

    private boolean isLandscape;
    private boolean isHalfPlayer;

    public void setIsLandscape(boolean isLandscape) {
        this.isLandscape = isLandscape;
    }

    public void setHalfPlayer(boolean halfPlayer) {
        isHalfPlayer = halfPlayer;
    }
}
