package com.mzmedia.adapter.base.chat;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mengzhu.live.sdk.R;
import com.mengzhu.live.sdk.business.dto.chat.ChatMessageDto;
import com.mengzhu.live.sdk.business.dto.chat.ChatTextDto;
import com.mengzhu.live.sdk.business.dto.chat.impl.ChatMegTxtDto;
import com.mengzhu.live.sdk.business.presenter.chat.ChatMessageObserver;
import com.mzmedia.adapter.base.BaseViewObtion;
import com.mzmedia.utils.String_Utils;
import com.mzmedia.widgets.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * Created by DELL on 2016/7/8.
 */
public class PlayerChatLeltWrap extends BaseViewObtion {
    private Context mContext;
    private ViewHolder mHolder;
//    private PlayInfoDto mPlayInfoDto;

    public PlayerChatLeltWrap(Context context) {
        mContext = context;
//        mPlayInfoDto = dto;
    }

    private int mContentCount = 0;

    @Override
    public View createView(Object o, int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.mz_player_chat_left_item, null);
            mHolder.mPlayerChatIcon = convertView.findViewById(R.id.player_chat_icon);
            mHolder.mPlayerChatContent = convertView.findViewById(R.id.player_chat_content);
            mHolder.mPlayerChatUsername = convertView.findViewById(R.id.player_chat_username);
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
//        convertView.setOnClickListener(new ChatLeltItemtListener(dto.getText()));
        if (mHolder != null && dto != null && dto.getText().getEvent().equals(ChatMessageObserver.MESSAGE_TYPE)) {
            ChatTextDto textDto = dto.getText();
            ChatMegTxtDto megTxtDto = (ChatMegTxtDto) textDto.getBaseDto();
            if (megTxtDto != null) {
                if (megTxtDto.getImgSrc() != null) {
                    String_Utils.handlerContent("收到一张图片", holder.mPlayerChatContent, R.color.mz_at_name_color);
                } else {
                    String_Utils.handlerContent(megTxtDto.getText(), holder.mPlayerChatContent, R.color.mz_at_name_color);
                }
                holder.mPlayerChatUsername.setText(textDto.getUser_name() + ": ");
//                holder.mPlayerChatUsername.setTextColor(mContext.getResources().getColor(mTextColorArray[mContentCount]));
                holder.mPlayerChatIcon.setTag(textDto);
                holder.mPlayerChatIcon.setOnClickListener(new ChatLeltItemtListener(textDto));
                switch (mContentCount) {
                    case 0:
                        mContentCount++;
                        break;
                    case 1:
                        mContentCount++;
                        break;
                    case 2:
                        mContentCount = 0;
                        break;
                }
//                String_Utils.handlerContent(megTxtDto.getText(), holder.mPlayerChatContent, R.color.mz_at_name_color);

                ImageLoader.getInstance().displayImage(textDto.getAvatar() + String_Utils.getPictureSizeAvatar(), holder.mPlayerChatIcon, new DisplayImageOptions.Builder()
                        .showStubImage(R.mipmap.icon_default_avatar)
                        .showImageForEmptyUri(R.mipmap.icon_default_avatar)
                        .showImageOnFail(R.mipmap.icon_default_avatar)
                        .cacheInMemory(true)
                        .cacheOnDisc(true)
                        .displayer(new RoundedBitmapDisplayer(20))
                        .build());


            }
        }
    }

    class ChatLeltItemtListener implements View.OnClickListener {
        private ChatTextDto mDto;

        public ChatLeltItemtListener(ChatTextDto dto) {
            this.mDto = dto;
        }

        @Override
        public void onClick(View view) {
//            if (isLock()) {
//                return;
//            }
            if (view.getId() == R.id.player_chat_icon) {
//                if ((mPlayInfoDto.getGlobal_right()!=null&&mPlayInfoDto.getGlobal_right().getGlobal_forbiden_check_userinfo()!=1&&!mPlayInfoDto.isForbiden_check_userinfo()) || mPlayInfoDto.getRole_name().equals(PlayInfoDto.ROLE_HOST)||mPlayInfoDto.getRole_name().equals(PlayInfoDto.ROLE_SUB_ACCOUNT)) {
//                    mDto = (ChatTextDto) view.getTag();
//                ((WatchBroadcastActivity)mContext).showUserInfoPopup(mDto.getUser_id());
//                boolean isManager = LoginSystemManage.getInstance().getUserID().equals(mPlayInfoDto.getUser_info().getUid());
//                    boolean isManager =mPlayInfoDto.getRole_name().equals(PlayInfoDto.ROLE_HOST)||mPlayInfoDto.getRole_name().equals(PlayInfoDto.ROLE_SUB_ACCOUNT);

//                     if(isManager){
//                        if (mContext instanceof WatchBroadcastActivity) {
//                            ((WatchBroadcastActivity) mContext).showAnonymousInfoWindow(mDto.getUser_id());
//                        } else if (mContext instanceof LiveBroadcastActivity) {
//                            ((LiveBroadcastActivity) mContext).showAnonymousInfoWindow(mPlayInfoDto,mDto.getUser_id());
//                        }else if(mContext instanceof LiveVoiceActivity){
//                            ((LiveVoiceActivity) mContext).showAnonymousInfoWindow(mPlayInfoDto,mDto.getUser_id());
//                        }
//                    }else{
//                        ToastUtils.popUpToast("管理员才能查看游客");
//                    }
//                } else {
//                    CommonDialog dialog = new CommonDialog(mContext);
//                    dialog.setContentMessage(R.string.forbid_check_user_info);
//                    dialog.setTitle(R.string.notice);
//                    dialog.setSingleType(R.string.confirm);
//                    dialog.show();
//                }

//            } else if (!mDto.getUser_id().equals(LoginSystemManage.getInstance().getUserID())) {
//                if (!StaticStateDto.getInstance().isBanned()) {
//                    if (!StaticStateDto.getInstance().isForbidPublicMsg() || mPlayInfoDto.getRole_name().equals(PlayInfoDto.ROLE_HOST)||mPlayInfoDto.getRole_name().equals(PlayInfoDto.ROLE_SUB_ACCOUNT)) {
//                        LiveBroadcastActivity.isChat = true;
//                        ActivityUtils.startLandscapeTransActivity(true, getActivity(), mPlayInfoDto.getChat_conf(), mDto.getUser_name(), mDto.getUser_id(), mPlayInfoDto.getTicket_id(), mPlayInfoDto);
//                    } else {
//                        Toast.makeText(getActivity(), R.string.forbid_public_hint, Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    DialogManager mDialogManager = new DialogManager(getActivity());
//                    if (!mDialogManager.isShowing()) {
//                        mDialogManager.showDialog(DialogType.PLAY_MANAGER, null, DialogManager.BANNED_TO_POST);
//                    }
//                    Toast.makeText(getActivity(), R.string.mz_banned_to_post, Toast.LENGTH_SHORT).show();
//                }
            }
        }
    }

//    private UserInfoPopupWindow mUserInfoPopup;

    public void showUserInfoPopup(View view, final String uid) {
        if (TextUtils.isEmpty(uid)) {
            return;
        }
//        mUserInfoPopup = new UserInfoPopupWindow(mContext);
//        mUserInfoPopup.loadInfoData(uid);
//        mUserInfoPopup.setListener(new UserInfoPopupWindow.onAttentionClickListener() {
//            @Override
//            public void onAttentionClicked() {
//            }
//
//            @Override
//            public void onHomeClicked() {
//                if (((WatchBroadcastActivity)mContext).getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//                    ((WatchBroadcastActivity)mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                }
//                ActivityUtils.goOtherUserInfoActivity(mContext, uid);
//                mUserInfoPopup.dismiss();
//            }
//        });
//        mUserInfoPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    class ViewHolder {
        CircleImageView mPlayerChatIcon;
        TextView mPlayerChatContent;
        TextView mPlayerChatUsername;
    }

    private boolean isLandscape;

    public void setIsLandscape(boolean isLandscape) {
        this.isLandscape = isLandscape;
    }

    class OnItemtClick implements View.OnClickListener {
        private ChatTextDto mDto;
//        UserInfoPopupWindow mUserInfoPopup;

        public OnItemtClick(ChatTextDto dto) {
            mDto = dto;
        }


        @Override
        public void onClick(View view) {
//            if (mDto.getUser_id().equals(LoginSystemManage.getInstance().getUserID())) {
//                ToastUtils.popUpToast("这是我自己");
//                return;
//            }
//            if (((Activity) mContext).getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE && !isLandscape) {
//                ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//            }
//            mUserInfoPopup = new UserInfoPopupWindow(mContext, mPlayInfoDto.getUser_info().getUid().equals(LoginSystemManage.getInstance().getUserID()));
//            mUserInfoPopup.loadInfoData(mPlayInfoDto.getId(), mDto.getUser_id());
//            mUserInfoPopup.setListener(new UserInfoPopupWindow.onAttentionClickListener() {
//                @Override
//                public void onAttentionClicked() {
//                }
//
//                @Override
//                public void onMsgClicked(UserInfoDto userinfo) {
//                    if (((Activity) mContext).getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//                        if (!isLandscape) {
//                            ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                            ActivityUtils.startChatPrivatelyActivity(mContext, userinfo);
//                            mUserInfoPopup.dismiss();
//                        } else {
//                            ToastUtils.popUpToast("直播中，不能私信用户");
//                        }
//                    } else {
//                        ActivityUtils.startChatPrivatelyActivity(mContext, userinfo);
//                        mUserInfoPopup.dismiss();
//                    }
//                }
//
//                @Override
//                public void onHomeClicked() {
//                    if (((Activity) mContext).getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//                        if (!isLandscape) {
//                            ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                            ActivityUtils.goOtherUserInfoActivity(((Activity) mContext), mDto.getUser_id());
//                            mUserInfoPopup.dismiss();
//                        } else {
//                            ToastUtils.showShortToast(mContext, mContext.getString(R.string.living_broadcast_home_hint));
//                        }
//                    } else {
//                        ActivityUtils.goOtherUserInfoActivity(((Activity) mContext), mDto.getUser_id());
//                        mUserInfoPopup.dismiss();
//                    }
//                }
//
//                @Override
//                public void onOutClicked(boolean isRecover, String uid) {
//                    if (isRecover) {
//                        doRecoverUser(uid);
//                    } else {
//                        doKickUser(uid);
//                    }
//                }
//            });
//            mUserInfoPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
//            if (isLock()) {
//                return;
//            }
//            if (mContext instanceof WatchBroadcastActivity) {
//                ((WatchBroadcastActivity) mContext).showUserInfoPopup(mDto.getUser_id());
//            } else if (mContext instanceof LiveBroadcastActivity) {
//                ((LiveBroadcastActivity) mContext).showUserInfoPopup(mDto.getUser_id());
//            }
        }
    }
}
