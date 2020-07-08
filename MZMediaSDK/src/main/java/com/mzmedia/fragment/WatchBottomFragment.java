package com.mzmedia.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mengzhu.live.sdk.business.dto.MZGoodsListDto;
import com.mengzhu.live.sdk.business.dto.MZGoodsListExternalDto;
import com.mengzhu.live.sdk.business.dto.chat.ChatMessageDto;
import com.mengzhu.live.sdk.business.dto.chat.ChatTextDto;
import com.mengzhu.live.sdk.business.dto.chat.impl.ChatCompleteDto;
import com.mengzhu.live.sdk.business.dto.chat.impl.ChatOnlineDto;
import com.mengzhu.live.sdk.business.dto.play.PlayInfoDto;
import com.mengzhu.live.sdk.business.presenter.chat.ChatMessageObserver;
import com.mengzhu.live.sdk.ui.api.MZApiDataListener;
import com.mengzhu.live.sdk.ui.api.MZApiRequest;
import com.mengzhu.live.sdk.ui.chat.MZChatManager;
import com.mengzhu.sdk.R;
import com.mzmedia.IPlayerClickListener;
import com.mzmedia.utils.ActivityUtils;
import com.mzmedia.widgets.ChatOnlineView;
import com.mzmedia.widgets.LoveLayout;
import com.mzmedia.widgets.player.PlayerGoodsPushView;
import com.mzmedia.widgets.player.PlayerGoodsView;

import java.util.ArrayList;

import tv.mengzhu.core.frame.coreutils.PreferencesUtils;
import tv.mengzhu.core.module.model.dto.BaseDto;
import tv.mengzhu.core.wrap.user.presenter.MyUserInfoPresenter;

/**
 * 观看端 互动fragment
 */
public class WatchBottomFragment extends BaseFragement implements View.OnClickListener {

    private Activity mActivity;
    private String ticketId; //活动id

    public static final String PLAY_INFO_KEY = "PLAY_INFO_KEY";

    private ImageView mIvConfig; //配置
    private ImageView mIvShare; //分享
    public ImageView mIvLike; //点赞
    private TextView mTvReport;
    private TextView mTvDanmakuSwtich;
    private LinearLayout mConfigLayout; //举报 弹幕 设置
    private RelativeLayout mRlChatAndBottom; //聊天区域和底部工具
    private RelativeLayout mRlSendChat; //发消息
    private TextView mTvHitChat; //发消息提示文字
    private LoveLayout mLoveLayout; //飘心
    private boolean isConfig;
    private ChatOnlineView mChatOnlineView;
    private PlayerGoodsView mPlayerGoodsLayout;
    private PlayerGoodsPushView mPlayerGoodsPushLayout;
    private TextView mGoodsIv; //店铺

    private MZApiRequest mzApiRequestGoods;
    private MZApiRequest mPraiseRequest;
    private boolean isPraise = true;

    private PlayInfoDto mPlayInfoDto;
    private MZGoodsListDto presentGoodsListDto;
    private boolean isLooping;
    private GoodsCountDown goodsCountDown;
    private int mPosition;
    private ArrayList<MZGoodsListDto> mGoodsListDtos;
    private ArrayList<ChatCompleteDto> mChatCompleteDtos = new ArrayList<>();

    private PlayerChatListFragment mChatFragment;


    private IPlayerClickListener mListener;

    public void setIPlayerClickListener(IPlayerClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ChatMessageObserver.getInstance().cancelRegister(WatchBottomFragment.class.getSimpleName());
        //释放飘心资源
        if (null != mLoveLayout) {
            mLoveLayout.removeView();
        }
    }

    @Override
    public void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mPlayInfoDto = (PlayInfoDto) getArguments().getSerializable(PLAY_INFO_KEY);
            if (mPlayInfoDto != null)
            ticketId = mPlayInfoDto.getTicket_id();
        }

        mIvConfig = (ImageView) findViewById(R.id.iv_playerfragment_config);
        mIvShare = (ImageView) findViewById(R.id.iv_playerfragment_share);
        mIvLike = (ImageView) findViewById(R.id.iv_playerfragment_zan);
        mConfigLayout = (LinearLayout) findViewById(R.id.iv_playerfragment_config_layout);
        mTvReport = (TextView) findViewById(R.id.tv_playerfragment_report);
        mTvDanmakuSwtich = (TextView) findViewById(R.id.tv_playerfragment_danmaku_switch);
        mLoveLayout = (LoveLayout) findViewById(R.id.love_playerfragment_layout);
        mRlChatAndBottom = (RelativeLayout) findViewById(R.id.rl_activity_chat_bottom);
        mRlSendChat = (RelativeLayout) findViewById(R.id.rl_playerfragment_send_chat);
        mTvHitChat = (TextView) findViewById(R.id.tv_playerfragment_chat);
        mPlayerGoodsLayout = (PlayerGoodsView) findViewById(R.id.live_broadcast_goods_view);
        mPlayerGoodsPushLayout = (PlayerGoodsPushView) findViewById(R.id.live_broadcast_goods_push_view);
        mGoodsIv = (TextView) findViewById(R.id.iv_player_fragment_goods);
        mChatOnlineView = (ChatOnlineView) findViewById(R.id.player_chat_list_online_view);
        goodsCountDown = new GoodsCountDown(10000 * 5000, 5000);
    }

    @Override
    public void initData() {
        mChatFragment = new PlayerChatListFragment();
        //是否禁言
        initBanChat(mPlayInfoDto.getUser_status() == 3);

        //添加聊天fragment
        Bundle bundle = new Bundle();
        bundle.putBoolean(PlayerChatListFragment.PLAY_TYPE_KEY, true);
        bundle.putBoolean(PlayerChatListFragment.UI_TYPE_KEY, true);
        bundle.putSerializable(PlayerChatListFragment.PLAY_INFO_KEY, mPlayInfoDto);
        mChatFragment.setArguments(bundle);
        getChildFragmentManager().beginTransaction().replace(R.id.layout_activity_live_broadcast_chat, mChatFragment).commitAllowingStateLoss();
    }

    @Override
    public void setListener() {
        mIvConfig.setOnClickListener(this);
        mIvShare.setOnClickListener(this);
        mIvLike.setOnClickListener(this);
        mTvReport.setOnClickListener(this);
        mTvDanmakuSwtich.setOnClickListener(this);
        mRlSendChat.setOnClickListener(this);
        mGoodsIv.setOnClickListener(this);

        mPlayerGoodsPushLayout.setOnGoodsPushItemClickListener(new PlayerGoodsView.OnGoodsItemClickListener() {
            @Override
            public void onGoodsItemClick() {
                if (mListener != null) {
                    mListener.onRecommendGoods(presentGoodsListDto);
                }
            }
        });
        mPlayerGoodsLayout.setOnGoodsItemClickListener(new PlayerGoodsView.OnGoodsItemClickListener() {
            @Override
            public void onGoodsItemClick() {
                if (mListener != null) {
                    mListener.onRecommendGoods(presentGoodsListDto);
                }
            }
        });

        mzApiRequestGoods = new MZApiRequest();
        //商品列表回调
        mzApiRequestGoods.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String s, Object o) {
                MZGoodsListExternalDto mzGoodsListExternalDto = (MZGoodsListExternalDto) o;
                mGoodsListDtos = (ArrayList<MZGoodsListDto>) mzGoodsListExternalDto.getList();
                if (mGoodsListDtos.size() > 0) {
                    //循环商品
                    mGoodsIv.setText(mzGoodsListExternalDto.getTotal() + "");
                    goodsCountDown.start();
                    isLooping = true;
                }
            }

            @Override
            public void errorResult(String s, int i, String s1) {

            }
        });

        //聊天用户头像点击的回调
        mChatFragment.setOnChatAvatarClickListener(new ChatAvatarClick());

        ChatMessageObserver.getInstance().register(new ChatMessageObserver.InformMonitorCallback() {
            @Override
            public void monitorInformResult(String type, Object obj) {
                ChatMessageDto mChatMessage = (ChatMessageDto) obj;
                ChatTextDto mChatText = mChatMessage.getText();
                BaseDto mBase = mChatText.getBaseDto();
                switch (type) {
                    case ChatMessageObserver.ONLINE_TYPE://上下线消息
                        ChatOnlineDto mChatOnline = (ChatOnlineDto) mBase;
                        mChatOnlineView.startOnline(mActivity, mChatMessage);
                        break;
                    }
            }

            @Override
            public void monitorInformErrer(String type, int state, String msg) {

            }
        } , WatchBottomFragment.class.getSimpleName());
    }

    class ChatAvatarClick implements PlayerChatListFragment.OnChatAvatarClickListener {

        @Override
        public void onChatAvatarClick(ChatTextDto dto) {
            if (null != mListener) {
                mListener.onChatAvatar(dto);
            }
        }
    }

    @Override
    public void loadData() {
        mzApiRequestGoods.createRequest(mActivity, MZApiRequest.API_TYPE_GOODS_LIST);

        //请求商店列表api
        mzApiRequestGoods.startData(MZApiRequest.API_TYPE_GOODS_LIST, true, ticketId);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_watchbottom;
    }

    //商品循环显示倒计时
    private class GoodsCountDown extends CountDownTimer {

        public GoodsCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            if (mPosition < mGoodsListDtos.size() - 1) {
                mPosition++;
            } else {
                mPosition = 0;
            }
            if (mPlayerGoodsLayout != null) {
                if (mGoodsListDtos.size() > 0) {
                    presentGoodsListDto = mGoodsListDtos.get(mPosition);

                    mPlayerGoodsLayout.setGoodsData(mGoodsListDtos.get(mPosition));
                    mPlayerGoodsLayout.startPlayerGoods();
                }
            }
        }

        @Override
        public void onFinish() {
            if (mPlayerGoodsLayout != null) {
                mPlayerGoodsLayout.setVisibility(View.GONE);
            }
            isLooping = false;
        }

    }

    /**
     * 底面商店弹窗
     */
    public void showGoodsDialog() {
        GoodsListPopupWindow goodsListPopupWindow = new GoodsListPopupWindow(mActivity, 1, ticketId, new GoodsListPopupWindow.OnGoodsLoadListener() {
            //商品列表回调
            @Override
            public void onGoodsLoad(ArrayList<MZGoodsListDto> mzGoodsListDtos, int totalNum) {
                mGoodsListDtos = mzGoodsListDtos;
                mGoodsIv.setText(mGoodsListDtos.size() > 0 ? totalNum + "" : "");
                if (mGoodsListDtos.size() > 0) {
                    mPlayerGoodsLayout.setVisibility(View.VISIBLE);
                    if (!isLooping) {
//                        GoodsCountDown goodsCountDown = new GoodsCountDown(10000 * 5000, 5000);
                        goodsCountDown.start();
                        isLooping = true;
                    }
                } else {
                    mPlayerGoodsLayout.setVisibility(View.GONE);
                }
            }
        });
        goodsListPopupWindow.setOnGoodsListItemClickListener(new GoodsListPopupWindow.OnGoodsListItemClickListener() {
            @Override
            public void onGoodsListItemClick(MZGoodsListDto dto) {
                if (mListener != null) {
                    mListener.onGoodsListItem(dto);
                }
            }
        });
        goodsListPopupWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);
    }

    private void initBanChat(boolean isBan) {
        if (isBan) {
            mTvHitChat.setText(mActivity.getResources().getString(R.string.ban_talk_to_up));
            mTvHitChat.setTextColor(mActivity.getResources().getColor(R.color.white));
            Drawable drawable = mActivity.getResources().getDrawable(R.drawable.icon_playerfragment_ban_chat);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTvHitChat.setCompoundDrawables(drawable, null, null, null);
        } else {
            mTvHitChat.setText(mActivity.getResources().getString(R.string.talk_to_up));
            mTvHitChat.setTextColor(mActivity.getResources().getColor(R.color.crop_99FFFFFF));
            mTvHitChat.setCompoundDrawables(null, null, null, null);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_playerfragment_config) { //点击底部三个点显示举报
            isConfig = !isConfig;
            mIvConfig.setImageResource(isConfig ? R.mipmap.gm_icon_config_true : R.mipmap.mz_icon_config);
            mConfigLayout.setVisibility(isConfig ? View.VISIBLE : View.GONE);
        }
        if (view.getId() == R.id.tv_playerfragment_report) { //点击举报
            if (mListener != null) {
                mListener.onReportClick(mPlayInfoDto);
            }
        }
        if (view.getId() == R.id.tv_playerfragment_danmaku_switch) { //点击弹幕开关
            if (PreferencesUtils.loadPrefBoolean(mActivity  ,PreferencesUtils.DANMAKU_SWITCH_KEY, true)){
                PreferencesUtils.savePrefBoolean(mActivity , PreferencesUtils.DANMAKU_SWITCH_KEY , false);
                Drawable drawableLeft = getResources().getDrawable(R.mipmap.ic_danmaku_closed);
                mTvDanmakuSwtich.setCompoundDrawablesWithIntrinsicBounds(drawableLeft , null , null , null);
            }else {
                PreferencesUtils.savePrefBoolean(mActivity , PreferencesUtils.DANMAKU_SWITCH_KEY , true);
                Drawable drawableLeft = getResources().getDrawable(R.mipmap.ic_danmaku_open);
                mTvDanmakuSwtich.setCompoundDrawablesWithIntrinsicBounds(drawableLeft , null , null , null);
            }
        }
        if (view.getId() == R.id.iv_playerfragment_share) { //点击分享
            if (mListener != null) {
                mListener.onShareClick(mPlayInfoDto);
            }
        }
        if (view.getId() == R.id.iv_playerfragment_zan) { //点击点赞
            if (mListener != null) {
                if (mPraiseRequest == null) {
                    mPraiseRequest = new MZApiRequest();
                    mPraiseRequest.createRequest(mActivity, MZApiRequest.API_TYPE_ROOT_PRAISE);
                    mPraiseRequest.setResultListener(new MZApiDataListener() {
                        @Override
                        public void dataResult(String s, Object o) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    isPraise = true;
                                }
                            }).start();

                        }

                        @Override
                        public void errorResult(String s, int i, String s1) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    isPraise = true;
                                }
                            }).start();
                        }
                    });
                }
                if (isPraise) {
                    mPraiseRequest.startData(MZApiRequest.API_TYPE_ROOT_PRAISE, ticketId, mPlayInfoDto.getChannel_id(), 1, mPlayInfoDto.getChat_uid());
                    isPraise = false;
                }
                mListener.onLikeClick(mPlayInfoDto, mIvLike);
            }
            //飘心
            mLoveLayout.addLoveView();
        }
        if (view.getId() == R.id.rl_playerfragment_send_chat) { //点击发送消息
            if (!TextUtils.isEmpty(MyUserInfoPresenter.getInstance().getUserInfo().getUniqueID())) {
                if (mPlayInfoDto.getUser_status() == 1) {
                    ActivityUtils.startLandscapeTransActivity(mActivity);
                } else if (mPlayInfoDto.getUser_status() == 3) {
                    Toast.makeText(mActivity, "您已被禁言", Toast.LENGTH_SHORT).show();
                }
            } else {
                mListener.onNotLogin(mPlayInfoDto);
            }
        }
        if (view.getId() == R.id.iv_player_fragment_goods) { //点击商店
//            if (mGoodsListDtos != null && mGoodsListDtos.size() > 0) {
            showGoodsDialog();
//            } else {
//                Toast.makeText(mActivity, "暂无商品", Toast.LENGTH_SHORT).show();
//            }
        }
    }
}
