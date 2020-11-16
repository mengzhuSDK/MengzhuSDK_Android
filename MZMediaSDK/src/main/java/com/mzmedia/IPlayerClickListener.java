package com.mzmedia;

import android.widget.ImageView;
import android.widget.TextView;

import com.mengzhu.live.sdk.business.dto.AnchorInfoDto;
import com.mengzhu.live.sdk.business.dto.MZGoodsListDto;
import com.mengzhu.live.sdk.business.dto.MZOnlineUserListDto;
import com.mengzhu.live.sdk.business.dto.chat.ChatTextDto;
import com.mengzhu.live.sdk.business.dto.play.PlayInfoDto;

public interface IPlayerClickListener {
    /**
     * 主播头像回调
     *
     * @param dto 主播信息
     */
    void onAvatarClick(AnchorInfoDto dto);

    /**
     * 关注回调
     *
     * @param dto       直播间信息
     * @param Attention 关注view
     */
    void onAttentionClick(PlayInfoDto dto, TextView Attention);

    /**
     * 在线人数回调
     */
    void onOnlineClick(MZOnlineUserListDto onlineUserDto);

    /**
     * 退出回调
     *
     * @param dto 直播间信息
     */
    void onCloseClick(PlayInfoDto dto);

    /**
     * 举报回调
     *
     * @param dto 直播间信息
     */
    void onReportClick(PlayInfoDto dto);

    /**
     * 分享回调
     *
     * @param dto 直播间信息
     */
    void onShareClick(PlayInfoDto dto);

    /**
     * 点赞回调
     *
     * @param dto  直播间信息
     * @param Like 点赞view
     */
    void onLikeClick(PlayInfoDto dto, ImageView Like);

    /**
     * 推荐商品回调
     *
     * @param dto 商品信息
     */
    void onRecommendGoods(MZGoodsListDto dto);

    /**
     * 商品列表回调
     *
     * @param dto 商品信息
     */
    void onGoodsListItem(MZGoodsListDto dto);

    /**
     * 聊天头像回调
     *
     * @param dto 聊天头像信息
     */
    void onChatAvatar(ChatTextDto dto);

    void onNotLogin(PlayInfoDto dto);

    /**
     *  主播信息回调
     */
    void resultAnchorInfo(AnchorInfoDto anchorInfoDto);
}
