package com.mzmedia.adapter.chat;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mengzhu.live.sdk.business.dto.chat.ChatMessageDto;
import com.mengzhu.live.sdk.business.dto.chat.ChatTextDto;
import com.mengzhu.live.sdk.business.dto.chat.impl.ChatCompleteDto;
import com.mengzhu.live.sdk.business.dto.chat.impl.ChatMegTxtDto;
import com.mengzhu.live.sdk.business.presenter.chat.ChatMessageObserver;
import com.mengzhu.sdk.R;
import com.mzmedia.adapter.base.BaseViewObtion;
import com.mzmedia.utils.String_Utils;
import com.mzmedia.widgets.CircularImage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * Created by DELL on 2016/7/8.
 */
public class PlayerChatGiftRightWrap extends BaseViewObtion {
    private Context mContext;
    private ViewHolder mHolder;

    public PlayerChatGiftRightWrap(Context context) {
        mContext = context;
    }

    @Override
    public View createView(Object o, int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = View.inflate(mContext, isHalfPlayer ? R.layout.mz_halfplayer_chat_right_item : R.layout.mz_player_chat_right_item, null);
            mHolder.mPlayerChatRightLayout = convertView.findViewById(R.id.player_chat_right_layout);
            mHolder.mPlayerChatRightIcon = convertView.findViewById(R.id.player_chat_right_icon);
            mHolder.mPlayerChatRightContent = convertView.findViewById(R.id.player_chat_right_content);
            mHolder.mPlayerChatRightUsername = convertView.findViewById(R.id.player_chat_right_username);
            mHolder.mPlayerGiftLayout = convertView.findViewById(R.id.player_chat_gift_layout);
            mHolder.mPlayerGiftIcon = convertView.findViewById(R.id.player_chat_gift_icon);
            mHolder.mPlayerGiftNum = convertView.findViewById(R.id.player_chat_gift_num);
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
        holder.mPlayerChatRightUsername.setText(textDto.getUser_name() + ": ");
        holder.mPlayerChatRightLayout.setOnClickListener(new OnItemtClick(textDto));
        ImageLoader.getInstance().displayImage(textDto.getAvatar() + String_Utils.getPictureSizeAvatar(), holder.mPlayerChatRightIcon, new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.icon_default_avatar)
                .showImageForEmptyUri(R.mipmap.icon_default_avatar)
                .showImageOnFail(R.mipmap.icon_default_avatar)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .displayer(new RoundedBitmapDisplayer(20))
                .build());

        mHolder.mPlayerGiftLayout.setVisibility(View.VISIBLE);
        ChatCompleteDto chatCompleteDto = (ChatCompleteDto) textDto.getBaseDto();
        holder.mPlayerChatRightContent.setText("送上" + chatCompleteDto.getName());
        holder.mPlayerGiftNum.setText("x " + chatCompleteDto.getContinuous());
        ImageLoader.getInstance().displayImage(chatCompleteDto.getIcon(), holder.mPlayerGiftIcon, new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.icon_default_avatar)
                .showImageForEmptyUri(R.mipmap.icon_default_avatar)
                .showImageOnFail(R.mipmap.icon_default_avatar).build());
    }

    class ViewHolder {
        LinearLayout mPlayerChatRightLayout;
        CircularImage mPlayerChatRightIcon;
        TextView mPlayerChatRightContent;
        TextView mPlayerChatRightUsername;
        RelativeLayout mPlayerGiftLayout;
        ImageView mPlayerGiftIcon;
        TextView mPlayerGiftNum;
    }

    private boolean isLandscape;
    private boolean isHalfPlayer;

    public void setIsLandscape(boolean isLandscape) {
        this.isLandscape = isLandscape;
    }

    public void setHalfPlayer(boolean halfPlayer) {
        isHalfPlayer = halfPlayer;
    }

    class OnItemtClick implements View.OnClickListener {
        private ChatTextDto mDto;

        public OnItemtClick(ChatTextDto dto) {
            mDto = dto;
        }


        @Override
        public void onClick(View view) {

        }
    }
}
