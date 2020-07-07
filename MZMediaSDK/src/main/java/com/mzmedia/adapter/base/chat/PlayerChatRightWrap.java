package com.mzmedia.adapter.base.chat;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mengzhu.sdk.R;
import com.mengzhu.live.sdk.business.dto.chat.ChatMessageDto;
import com.mengzhu.live.sdk.business.dto.chat.ChatTextDto;
import com.mengzhu.live.sdk.business.dto.chat.impl.ChatMegTxtDto;
import com.mengzhu.live.sdk.business.presenter.chat.ChatMessageObserver;
import com.mzmedia.adapter.base.BaseViewObtion;
import com.mzmedia.utils.String_Utils;
import com.mzmedia.widgets.CircularImage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * Created by DELL on 2016/7/8.
 */
public class PlayerChatRightWrap extends BaseViewObtion {
    private Context mContext;
    private ViewHolder mHolder;

    public PlayerChatRightWrap(Context context) {
        mContext = context;
    }

    @Override
    public View createView(Object o, int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = View.inflate(mContext, isHalfPlayer?R.layout.mz_halfplayer_chat_right_item : R.layout.mz_player_chat_right_item, null);
            mHolder.mPlayerChatRightLayout = convertView.findViewById(R.id.player_chat_right_layout);
            mHolder.mPlayerChatRightIcon = convertView.findViewById(R.id.player_chat_right_icon);
            mHolder.mPlayerChatRightContent = convertView.findViewById(R.id.player_chat_right_content);
            mHolder.mPlayerChatRightUsername = convertView.findViewById(R.id.player_chat_right_username);
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
        if (mHolder != null && dto != null && dto.getText().getEvent().equals(ChatMessageObserver.MESSAGE_TYPE)) {
            ChatTextDto textDto = dto.getText();
            ChatMegTxtDto megTxtDto = (ChatMegTxtDto) textDto.getBaseDto();
            if (megTxtDto != null) {
                holder.mPlayerChatRightContent.setText(megTxtDto.getText());
                holder.mPlayerChatRightUsername.setText(textDto.getUser_name() + ": ");
//                String_Utils.handlerContent(megTxtDto.getText(), holder.mPlayerChatRightContent, R.color.mz_at_name_color);
                holder.mPlayerChatRightContent.setText(megTxtDto.getText());
                ImageLoader.getInstance().displayImage(megTxtDto.getAvatar() + String_Utils.getPictureSizeAvatar(), holder.mPlayerChatRightIcon, new DisplayImageOptions.Builder()
                        .showStubImage(R.mipmap.icon_default_avatar)
                        .showImageForEmptyUri(R.mipmap.icon_default_avatar)
                        .showImageOnFail(R.mipmap.icon_default_avatar)
                        .cacheInMemory(true)
                        .cacheOnDisc(true)
                        .displayer(new RoundedBitmapDisplayer(20))
                        .build());
                holder.mPlayerChatRightLayout.setOnClickListener(new OnItemtClick(textDto));
            }
        }
    }

    class ViewHolder {
        LinearLayout mPlayerChatRightLayout;
        CircularImage mPlayerChatRightIcon;
        TextView mPlayerChatRightContent;
        TextView mPlayerChatRightUsername;
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

//            if (mDto.getUser_id().equals(LoginSystemManage.getInstance().getUserID())) {
//                ToastUtils.popUpToast("这是我自己");
//                return;
//            }

//            if (((Activity) mContext).getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE&&!isLandscape) {
//                ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//            }
//            mUserInfoPopup = new UserInfoPopupWindow(mContext);
//            mUserInfoPopup.loadInfoData(mDto.getUser_id());
//            mUserInfoPopup.setListener(new UserInfoPopupWindow.onAttentionClickListener() {
//                @Override
//                public void onAttentionClicked() {
//                }
//
//                @Override
//                public void onHomeClicked() {
//                    if (((Activity) mContext).getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//                        if(!isLandscape) {
//                            ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                            ActivityUtils.goOtherUserInfoActivity(((Activity) mContext), mDto.getUser_id());
//                            mUserInfoPopup.dismiss();
//                        }else {
//                            ToastUtils.showShortToast(mContext,mContext.getString(R.string.living_broadcast_home_hint));
//                        }
//                    }else {
//                        ActivityUtils.goOtherUserInfoActivity(((Activity) mContext), mDto.getUser_id());
//                        mUserInfoPopup.dismiss();
//                    }
//
//                }
//            });
//            mUserInfoPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
        }
    }
}
