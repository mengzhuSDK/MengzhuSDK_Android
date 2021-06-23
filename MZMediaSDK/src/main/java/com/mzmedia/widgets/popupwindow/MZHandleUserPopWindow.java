package com.mzmedia.widgets.popupwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mengzhu.live.sdk.business.dto.MZUserChatStateDto;
import com.mengzhu.live.sdk.business.dto.chat.ChatTextDto;
import com.mengzhu.live.sdk.ui.api.MZApiDataListener;
import com.mengzhu.live.sdk.ui.api.MZApiRequest;
import com.mengzhu.sdk.R;
import com.mzmedia.widgets.CircleImageView;
import com.mzmedia.widgets.dialog.AbstractPopupWindow;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import tv.mengzhu.core.wrap.netwock.Page;

/**
 * 主播端点击聊天头像弹出操作用户弹窗
 */
public class MZHandleUserPopWindow extends AbstractPopupWindow {

    private Context mContext;
    private ChatTextDto dto;
    private boolean isAnchor;

    private ImageView iv_close;
    private CircleImageView civ_avatar;
    private TextView tv_user_name;
    private TextView tv_kick_out;
    private TextView tv_ban_chat;

    private String channel_id;
    private String ticket_id;

    MZApiRequest stateApiRequest;
    MZApiRequest kickApiRequest;
    MZApiRequest silenceApiRequest;

    private int silence_type = 0;

    public MZHandleUserPopWindow(Context context, ChatTextDto dto, boolean isAnchor, String channel_id , String ticket_id) {
        super(context);
        this.mContext = context;
        this.dto = dto;
        this.isAnchor = isAnchor;
        this.channel_id = channel_id;
        this.ticket_id = ticket_id;
        initView();
        initListener();
        initRequest();
        initData();
        stateApiRequest.startData(MZApiRequest.API_GET_CHAT_STATE , ticket_id , dto.getUser_id());
    }

    public void initView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.mz_handle_user_pop_layout, null);
        iv_close = rootView.findViewById(R.id.iv_close);
        civ_avatar = rootView.findViewById(R.id.civ_user_avatar);
        tv_user_name = rootView.findViewById(R.id.tv_user_name);
        tv_kick_out = rootView.findViewById(R.id.tv_kick_out);
        tv_ban_chat = rootView.findViewById(R.id.tv_ban_chat);
        setContentView(rootView);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.transparent)));
    }

    void initListener() {
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tv_kick_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kickApiRequest.startData(MZApiRequest.API_KICK_USER , channel_id , dto.getUser_id() , ticket_id);
            }
        });

        tv_ban_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                silenceApiRequest.startData(MZApiRequest.API_TYPE_ROOM_FORBIDDEN, dto.getUser_id(), ticket_id, silence_type);
            }
        });
    }

    void initData() {
        if (isAnchor) {
            tv_kick_out.setVisibility(View.GONE);
            tv_ban_chat.setVisibility(View.GONE);
        }
        if (dto != null) {
            ImageLoader.getInstance().displayImage(dto.getAvatar(), civ_avatar, new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.mipmap.icon_default_avatar)
                    .showImageOnFail(R.mipmap.icon_default_avatar)
                    .cacheInMemory(true)
                    .build());
            tv_user_name.setText("" + dto.getUser_name());
        }
    }

    void updateButton(){
        if (silence_type == 1){
            tv_ban_chat.setText("禁言");
        }else {
            tv_ban_chat.setText("解除禁言");
        }
    }

    void initRequest(){
        stateApiRequest = new MZApiRequest();
        stateApiRequest.createRequest(mContext , MZApiRequest.API_GET_CHAT_STATE);
        stateApiRequest.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String apiType, Object dto, Page page, int status) {
                MZUserChatStateDto userChatStateDto = (MZUserChatStateDto) dto;
                //state 1正常发言 0 被禁言
                silence_type = userChatStateDto.getState();
                if (silence_type == 0){
                    silence_type = 2;
                }
                updateButton();
            }

            @Override
            public void errorResult(String apiType, int code, String msg) {

            }
        });

        kickApiRequest = new MZApiRequest();
        kickApiRequest.createRequest(mContext , MZApiRequest.API_KICK_USER);
        kickApiRequest.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String apiType, Object dto, Page page, int status) {
                if (status == 200){
                    Toast.makeText(mContext, "踢出成功", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void errorResult(String apiType, int code, String msg) {

            }
        });

        silenceApiRequest = new MZApiRequest();
        silenceApiRequest.createRequest(mContext , MZApiRequest.API_TYPE_ROOM_FORBIDDEN);
        silenceApiRequest.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String apiType, Object dto, Page page, int status) {
                if (silence_type == 1){
                    silence_type = 2;
                    updateButton();
                    Toast.makeText(mContext, "禁言成功", Toast.LENGTH_SHORT).show();
                }else {
                    silence_type = 1;
                    updateButton();
                    Toast.makeText(mContext, "取消禁言成功", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void errorResult(String apiType, int code, String msg) {

            }
        });
    }
}
