package com.mzmedia;

import com.mengzhu.live.sdk.business.dto.MZOnlineUserListDto;
import com.mengzhu.live.sdk.business.dto.chat.ChatTextDto;
import com.mengzhu.live.sdk.business.dto.play.PlayInfoDto;

import java.util.List;

public interface IPushClickListener {
    /**
     * 结束直播
     */
    void onStopLive();
    /**
     * 点击聊天用户头像回调
     */
    void onChatAvatar(ChatTextDto chatTextDto);
    /**
     * 全体禁言
     */
    void onALLBanChat();
    /**
     * 单体禁言
     */
    void onBanChat();
    /**
     * 分享
     */
    void onShare(PlayInfoDto dto);
    /**
     * 点击主播头像
     */
    void onLiveAvatar();
    /**
     * 点击在线人数
     */
    void onOnlineNum(List<MZOnlineUserListDto> mzOnlineUserListDto);
}
