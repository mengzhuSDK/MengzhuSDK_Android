package com.mzmedia;

import com.mengzhu.live.sdk.business.dto.MZGoodsListDto;
import com.mengzhu.live.sdk.business.dto.chat.ChatTextDto;
import com.mengzhu.live.sdk.business.dto.play.PlayInfoDto;

public interface IPlayerClickListener {
    void onAvatarClick(PlayInfoDto dto);
    void onAttentionClick(PlayInfoDto dto);
    void onOnlineClick();
    void onCloseClick(PlayInfoDto dto);
    void onReportClick(PlayInfoDto dto);
    void onShareClick(PlayInfoDto dto);
    void onLikeClick(PlayInfoDto dto);
    void onRecommendGoods(PlayInfoDto dto);
    void onGoodsListItem(MZGoodsListDto dto);
    void onChatAvatar(ChatTextDto dto);
    void onNotLogin(PlayInfoDto dto);
}
