package com.mzmedia.fragment;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.mengzhu.live.sdk.business.dto.AnchorInfoDto;
import com.mengzhu.live.sdk.business.dto.MZAllSettingDto;
import com.mengzhu.live.sdk.business.dto.MZGoodsListDto;
import com.mengzhu.live.sdk.business.dto.MZGoodsListExternalDto;
import com.mengzhu.live.sdk.business.dto.MZOnlineUserListDto;
import com.mengzhu.live.sdk.business.dto.chat.ChatMessageDto;
import com.mengzhu.live.sdk.business.dto.chat.ChatTextDto;
import com.mengzhu.live.sdk.business.dto.chat.ContentBean;
import com.mengzhu.live.sdk.business.dto.chat.RightBean;
import com.mengzhu.live.sdk.business.dto.chat.impl.ChatCmdDto;
import com.mengzhu.live.sdk.business.dto.chat.impl.ChatCompleteDto;
import com.mengzhu.live.sdk.business.dto.chat.impl.ChatMegTxtDto;
import com.mengzhu.live.sdk.business.dto.chat.impl.ChatOnlineDto;
import com.mengzhu.live.sdk.business.dto.play.PlayInfoDto;
import com.mengzhu.live.sdk.business.presenter.chat.ChatMessageObserver;
import com.mengzhu.live.sdk.business.presenter.chat.ChatPresenter;
import com.mengzhu.live.sdk.core.MZSDKInitManager;
import com.mengzhu.live.sdk.core.SDKInitListener;
import com.mengzhu.live.sdk.core.utils.ToastUtils;
import com.mengzhu.live.sdk.ui.api.MZApiDataListener;
import com.mengzhu.live.sdk.ui.api.MZApiRequest;
import com.mengzhu.live.sdk.ui.chat.MZChatManager;
import com.mengzhu.live.sdk.ui.chat.MZChatMessagerListener;
import com.mengzhu.live.sdk.ui.widgets.ChannelDlnaDialogFragment;
import com.mengzhu.live.sdk.ui.widgets.popupwindow.MZLottoWebFragment;
import com.mengzhu.live.sdk.ui.widgets.popupwindow.SignInWebFragment;
import com.mengzhu.live.sdk.ui.widgets.popupwindow.SpeedBottomDialogFragment;
import com.mengzhu.sdk.R;
import com.mzmedia.IPlayerClickListener;
import com.mzmedia.fragment.gift.SendGiftDialogFragment;
import com.mzmedia.fragment.redpacket.MZRedPacketDialogFragment;
import com.mzmedia.utils.ActivityUtils;
import com.mzmedia.utils.String_Utils;
import com.mzmedia.widgets.ChatOnlineView;
import com.mzmedia.widgets.CircleImageView;
import com.mzmedia.widgets.DanmakuViewCacheStuffer;
import com.mzmedia.widgets.LoveLayout;
import com.mzmedia.widgets.dialog.MessageDialog;
import com.mzmedia.widgets.player.PlayerGoodsPushView;
import com.mzmedia.widgets.player.PlayerGoodsView;
import com.mzmedia.widgets.popupwindow.PersonListPopupWindow;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import tv.mengzhu.core.frame.coreutils.DensityUtil;
import tv.mengzhu.core.frame.coreutils.PreferencesUtils;
import tv.mengzhu.core.module.model.dto.BaseDto;
import tv.mengzhu.core.wrap.library.utils.CommonUtil;
import tv.mengzhu.core.wrap.netwock.Page;
import tv.mengzhu.core.wrap.user.modle.UserDto;
import tv.mengzhu.core.wrap.user.presenter.MyUserInfoPresenter;
import tv.mengzhu.dlna.DLNAController;
import tv.mengzhu.dlna.entity.RemoteItem;
import tv.mengzhu.sdk.danmaku.ui.widget.DanmakuView;
import tv.mengzhu.sdk.module.IMZPlayerManager;
import tv.mengzhu.sdk.module.MZPlayerManager;
import tv.mengzhu.sdk.module.MZPlayerView;
import tv.mengzhu.sdk.module.PlayerEventListener;
import tv.mengzhu.sdk.module.player.PlayerController;
import tv.mengzhu.sdk.module.player.callback.IMediaStateListener;
import tv.mengzhu.sdk.widgets.MZDefaultPlayerControlView;

public class PlayerFragment extends Fragment implements View.OnClickListener, MZApiDataListener {

    private static final String USER_INFO = "userDto";
    public static final String APP_ID = "appid";
    public static final String AVATAR = "avatar";
    public static final String NICKNAME = "nickName";
    public static final String UNIQUE_ID = "unique_id";
    public static final String TICKET_ID = "ticket_id";

    private static String CLASSNAME = PlayerFragment.class.getSimpleName();
    private static String GIFT_KEY = "gift";
    private static String KICK_OUT_KEY = "kick_out";
    private MZPlayerManager mManager;
    private MZPlayerView mzPlayerView;
    private AppBarLayout mAppBarLayout;
    private CircleImageView mIvAvatar; //头像
    private CircleImageView mOnlinePersonIv1, mOnlinePersonIv2, mOnlinePersonIv3; //在线人数头像
    private TextView mTvNickName; //昵称
    private TextView mTvPopular; //人气
    private TextView mTvLiveType;
    private TextView mTVLiveTypeText; //直播状态
    public TextView mTvAttention; //关注
    private TextView mTvOnline; //在线人数
    private ImageView mIvClose; //退出
    private ImageView mIvConfig; //配置
    private ImageView mIvGift; //礼物
    private ImageView mIvRedPacket; //红包
    private ImageView mIvShare; //分享
    public ImageView mIvLike; //点赞
    private TextView mTvReport;
    private TextView mTvDanmakuSwtich;
    private LinearLayout mConfigLayout; //举报 弹幕 设置
    private RelativeLayout mRlLiveOver; //主播离开
    private RelativeLayout mRlChatAndBottom; //聊天区域和底部工具
    private RelativeLayout mRlSendChat; //发消息
    private TextView mLiveContent; //蒙层提示文字
    private TextView mTvHitChat; //发消息提示文字
    private LoveLayout mLoveLayout; //飘心
    private boolean isConfig;
    private PlayerGoodsView mPlayerGoodsLayout;
    private PlayerGoodsPushView mPlayerGoodsPushLayout;
    private TextView mGoodsIv; //店铺
    private String mVideoUrl; //观看地址
    private String mUid;
    //    private String mAppid;
//    private String mAvatr;
//    private String mNickName;
//    private String mAccountNo;
    private Activity mActivity;
    private DisplayImageOptions avatarOptions;
    private MZApiRequest mzApiRequest;
    private MZApiRequest mzApiRequestGoods;
    private MZApiRequest mzApiRequestOnline;
    private MZApiRequest mzApiRequestAnchorInfo;
    private MZApiRequest mzGetSettingRequest;
    private String ticketId; //活动id
    private ArrayList<MZGoodsListDto> mGoodsListDtos;
    private int mPosition;
    private String mTotalPerson;
    private List<MZOnlineUserListDto> personAvatars = new ArrayList<>();
    private ChatOnlineView mChatOnlineView;
    private ArrayList<ChatCompleteDto> mChatCompleteDtos = new ArrayList<>();
    private PlayInfoDto mPlayInfoDto;
    private AnchorInfoDto anchorInfoDto; //主播信息
    private PlayerChatListFragment mChatFragment;
    private boolean isLooping;
    private GoodsCountDown goodsCountDown;
    private MZGoodsListDto presentGoodsListDto;
    private MZApiRequest mPraiseRequest;
    private boolean isPraise = true;

    private int liveStatus; //直播状态

    private int mSpeedIndex = 1; //倍速值索引
    private float[] speeds = new float[]{0.75f, 1, 1.25f, 1.5f, 2};
    SpeedBottomDialogFragment speedDialog;
    SendGiftDialogFragment giftDialogFragment;

    private DLNAController controller; //投屏控制

    private ImageView mIvVote;
    private ImageView mIvSignIn;
    private ImageView mIvDraw; //抽奖入口
    //签到抽奖等
    private ContentBean signInfoBean;
    private ContentBean lottoBean;
    private CountDownTimer signCountDown;
    private boolean signDialogShowing = false;

    public static PlayerFragment newInstance(String Appid, String avatar, String nickName, String unique_id, String ticketId) {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putSerializable(APP_ID, Appid);
        args.putSerializable(AVATAR, avatar);
        args.putSerializable(NICKNAME, nickName);
        args.putSerializable(UNIQUE_ID, unique_id);
        args.putString(TICKET_ID, ticketId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ticketId = getArguments().getString(TICKET_ID);
        }

        MZSDKInitManager.getInstance().initLive();
        MZSDKInitManager.getInstance().registerInitListener(new SDKInitListener() {
            @Override
            public void dataResult(int i) {
                //请求获取观看信息api
                mzApiRequest.startData(MZApiRequest.API_TYPE_PLAY_INFO, ticketId);
            }

            @Override
            public void errorResult(int i, String s) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_player, container, false);
        mTvOnline = rootView.findViewById(R.id.tv_playerfragment_person);
        mIvClose = rootView.findViewById(R.id.iv_playerfragment_close);
        mIvAvatar = rootView.findViewById(R.id.civ_playerfragment_avatar);
        mOnlinePersonIv1 = rootView.findViewById(R.id.civ_activity_live_online_person1);
        mOnlinePersonIv2 = rootView.findViewById(R.id.civ_activity_live_online_person2);
        mOnlinePersonIv3 = rootView.findViewById(R.id.civ_activity_live_online_person3);
        mTvNickName = rootView.findViewById(R.id.tv_playerfragment_nickname);
        mTvPopular = rootView.findViewById(R.id.tv_playerfragment_popularity);
        mTvLiveType = rootView.findViewById(R.id.tv_playerfragment_livetype_tag);
        mTVLiveTypeText = rootView.findViewById(R.id.tv_playerfragment_livetype);
        mTvAttention = rootView.findViewById(R.id.tv_playerfragment_attention);
        mIvConfig = rootView.findViewById(R.id.iv_playerfragment_config);
        mIvGift = rootView.findViewById(R.id.iv_playerfragment_gift);
        mIvRedPacket = rootView.findViewById(R.id.iv_playerfragment_red_packet);
        mIvShare = rootView.findViewById(R.id.iv_playerfragment_share);
        mIvLike = rootView.findViewById(R.id.iv_playerfragment_zan);
        mConfigLayout = rootView.findViewById(R.id.iv_playerfragment_config_layout);
        mTvReport = rootView.findViewById(R.id.tv_playerfragment_report);
        mTvDanmakuSwtich = rootView.findViewById(R.id.tv_playerfragment_danmaku_switch);
        mLoveLayout = rootView.findViewById(R.id.love_playerfragment_layout);
        mzPlayerView = rootView.findViewById(R.id.video_playerfragment_view);
        mAppBarLayout = rootView.findViewById(R.id.sv_activity_content_appbar);
        mRlChatAndBottom = rootView.findViewById(R.id.rl_activity_chat_bottom);
        mRlSendChat = rootView.findViewById(R.id.rl_playerfragment_send_chat);
        mRlLiveOver = rootView.findViewById(R.id.rl_activity_broadcast_live_over);
        mTvHitChat = rootView.findViewById(R.id.tv_playerfragment_chat);
        mPlayerGoodsLayout = rootView.findViewById(R.id.live_broadcast_goods_view);
        mPlayerGoodsPushLayout = rootView.findViewById(R.id.live_broadcast_goods_push_view);
        mGoodsIv = rootView.findViewById(R.id.iv_player_fragment_goods);
        mChatOnlineView = rootView.findViewById(R.id.player_chat_list_online_view);
        mLiveContent = rootView.findViewById(R.id.tv_activity_broadcast_live_over);

        mIvVote = (ImageView) rootView.findViewById(R.id.iv_vote);
        mIvSignIn = (ImageView) rootView.findViewById(R.id.iv_sign_in);
        mIvDraw = (ImageView) rootView.findViewById(R.id.iv_draw);

        DanmakuView danmakuView = mzPlayerView.findViewById(R.id.sv_danmaku);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) danmakuView.getLayoutParams();
        params.topMargin = DensityUtil.dip2px(mActivity, 68);
        danmakuView.setLayoutParams(params);

        LottieAnimationView audioView = mzPlayerView.findViewById(R.id.mz_audio_anim_view);
        RelativeLayout.LayoutParams audioViewParams = (RelativeLayout.LayoutParams) audioView.getLayoutParams();
        audioViewParams.topMargin = DensityUtil.dip2px(mActivity, 128);
        audioView.setLayoutParams(audioViewParams);
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //头像加载失败默认处理
        avatarOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.icon_default_avatar)
                .showImageForEmptyUri(R.mipmap.icon_default_avatar)
                .showImageOnFail(R.mipmap.icon_default_avatar)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        //初始化view数据
        initView();
        //初始化监听
        initListener();
        //请求api接口数据
        loadData();

    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        mzPlayerView._hideAllView(false, false);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));
        mChatFragment = new PlayerChatListFragment();
        goodsCountDown = new GoodsCountDown(10000 * 5000, 5000);
        mManager = new MZPlayerManager();
        mManager.init(mzPlayerView);
        setControllerNoAutoGone();
        //自定义播控组件
        mzPlayerView.setPlayerControlView(new MZDefaultPlayerControlView(getContext() , mzPlayerView));
    }

    /**
     * 设置控制栏不自动隐藏
     */
    public void setControllerNoAutoGone() {
        if (mzPlayerView != null)
            mzPlayerView.setDEFAULT_HIDE_TIMEOUT(Integer.MAX_VALUE);
    }

    private void initListener() {
        mTvOnline.setOnClickListener(this);
        mIvAvatar.setOnClickListener(this);
        mTvAttention.setOnClickListener(this);
        mIvConfig.setOnClickListener(this);
        mIvGift.setOnClickListener(this);
        mIvRedPacket.setOnClickListener(this);
        mIvShare.setOnClickListener(this);
        mIvLike.setOnClickListener(this);
        mTvReport.setOnClickListener(this);
        mTvDanmakuSwtich.setOnClickListener(this);
        mIvClose.setOnClickListener(this);
        mRlSendChat.setOnClickListener(this);
        mGoodsIv.setOnClickListener(this);
        mOnlinePersonIv1.setOnClickListener(this);
        mOnlinePersonIv2.setOnClickListener(this);
        mOnlinePersonIv3.setOnClickListener(this);
        mIvVote.setOnClickListener(this);
        mIvSignIn.setOnClickListener(this);
        mIvDraw.setOnClickListener(this);

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
        //各类型消息回调
        MZChatManager.getInstance(mActivity).registerListener(CLASSNAME, new MZChatMessagerListener() {
            @Override
            public void dataResult(Object obj, Page page, int status) {

            }

            @Override
            public void errorResult(int code, String msg) {

            }

            @Override
            public void monitorInformResult(String type, Object obj, Object extend) {
                if (ChatMessageObserver.MESSAGE_TYPE.equals(type) && !(boolean) extend) { //文本消息 弹幕
                    if (mPlayInfoDto.isInputDanmuku() && PreferencesUtils.loadPrefBoolean(mActivity, PreferencesUtils.DANMAKU_SWITCH_KEY, true))
                        receiveSendDanmaku(obj);
                }
            }

            @Override
            public void monitorInformResult(String s, Object o) {
                ChatMessageDto mChatMessage = (ChatMessageDto) o;
                ChatTextDto mChatText = mChatMessage.getText();
                BaseDto mBase = mChatText.getBaseDto();
                switch (s) {
                    case ChatMessageObserver.ONLINE_TYPE: {//上下线消息
                        ChatOnlineDto mChatOnline = (ChatOnlineDto) mBase;
                        mTotalPerson = mTvOnline.getText().toString();
                        mChatOnlineView.startOnline(mActivity, mChatMessage);
                        long popular = Integer.valueOf(mPlayInfoDto.getPopular()) + mChatOnline.getLast_pv();
                        mPlayInfoDto.setPopular(popular + "");
                        //加载人气
                        mTvPopular.setText("人气" + String_Utils.convert2W0_0(popular + ""));
                        if (String_Utils.isNumeric(mTotalPerson) && mChatOnline.getIs_hidden() != 1 && !personAvatars.isEmpty()) {
                            boolean isContainsMe = false;
                            for (int i = 0; i < personAvatars.size(); i++) {
                                if (personAvatars.get(i).getUid().equals(mChatMessage.getText().getUser_id())) {
                                    isContainsMe = true;
                                }
                            }
                            if (isContainsMe) {
                                break;
                            }
                            if (Long.parseLong(mChatMessage.getText().getUser_id()) < Long.parseLong("5000000000")) {
                                MZOnlineUserListDto dto = new MZOnlineUserListDto();
                                dto.setUid(mChatMessage.getText().getUser_id());
                                dto.setAvatar(mChatText.getAvatar());
                                dto.setIs_gag(mChatOnline.getIs_gag());
                                dto.setRole_name(mChatOnline.getRole());
                                dto.setNickname(mChatText.getUser_name());
                                personAvatars.add(dto);
                                initOnlineAvatar();
                                int current = Integer.valueOf(mTotalPerson) + 1;
                                mTvOnline.setText(String_Utils.convert2W0_0(current + ""));
                            }
                        }
                        break;
                    }
                    case ChatMessageObserver.OFFLINE_TYPE:
                        mTotalPerson = mTvOnline.getText().toString();
                        if (String_Utils.isNumeric(mTotalPerson) && !personAvatars.isEmpty()) {
                            int current = Integer.valueOf(mTotalPerson) - 1;
                            if (current <= 0) {
                                current = 1;
                            }
                            mTvOnline.setText(String_Utils.convert2W0_0(current + ""));

                            Iterator<MZOnlineUserListDto> iterator = personAvatars.iterator();
                            while (iterator.hasNext()) {
                                MZOnlineUserListDto userListDto = iterator.next();
                                if (userListDto.getUid().equals(mChatText.getUser_id()) && !mChatText.getUser_id().equals(mPlayInfoDto.getChat_uid())) {
                                    iterator.remove();
                                }
                            }
                            initOnlineAvatar();
                        }
                        if (personAvatars.size() < 4) {
                            mzApiRequestOnline.startData(MZApiRequest.API_TYPE_ONLINE_USER_LIST, true, ticketId);
                        }
                        break;
                    case ChatMessageObserver.COMPLETE:
                        ChatCompleteDto mChatComplete = (ChatCompleteDto) mBase;

                        mChatCompleteDtos.add(mChatComplete);
                        mPlayerGoodsPushLayout.setVisibility(View.VISIBLE);
                        if (mChatComplete.getType().equals(ChatPresenter.STORE_GENERALIZE)) {
//                            if (mPlayerGoodsPushLayout.getVisibility() == View.VISIBLE) {
//                                return;
//                            }
                            if (mPlayerGoodsPushLayout != null) {
                                if (mChatCompleteDtos.size() > 0) {
                                    mPlayerGoodsPushLayout.startPlayerGoods();
                                    mPlayerGoodsPushLayout.setGoodsData(mChatCompleteDtos.get(0));
                                    mPlayerGoodsPushLayout.setOnPushGoodsAnimatorListener(new PlayerGoodsPushView.OnPushGoodsAnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animator) {
                                            mPlayerGoodsLayout.setVisibility(View.GONE);
                                            presentGoodsListDto = new MZGoodsListDto();
                                            presentGoodsListDto.setId(mChatCompleteDtos.get(0).getId());
                                            presentGoodsListDto.setBuy_url(mChatCompleteDtos.get(0).getUrl());
                                            presentGoodsListDto.setName(mChatCompleteDtos.get(0).getName());
                                            presentGoodsListDto.setPrice(mChatCompleteDtos.get(0).getPrice());
                                            presentGoodsListDto.setPic(mChatCompleteDtos.get(0).getPic());
                                            presentGoodsListDto.setType(mChatCompleteDtos.get(0).getType());
                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {

                                            mChatCompleteDtos.remove(0);
                                            if (mChatCompleteDtos.size() > 0) {
                                                mPlayerGoodsPushLayout.startPlayerGoods();
                                                mPlayerGoodsPushLayout.setGoodsData(mChatCompleteDtos.get(0));
                                            } else {
                                                mPlayerGoodsPushLayout.setVisibility(View.GONE);
                                                mPlayerGoodsLayout.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    });
                                }
                            }
                        }
                        break;

                    case ChatMessageObserver.CMD_TYPE:
                        ChatCmdDto mChatCmd = (ChatCmdDto) mBase;
                        switch (mChatCmd.getType()) {
                            case ChatCmdDto.DISABLE_CHAT:
                                //禁言消息
                                if (mPlayInfoDto.getChat_uid().equals(((ChatCmdDto) mBase).getUser_id())) {
                                    mPlayInfoDto.setUser_status(3);
                                    initBanChat(true);
                                }
                                break;
                            case ChatCmdDto.PERMIT_CHAT:
                                //取消禁言消息
                                if (mPlayInfoDto.getChat_uid().equals(((ChatCmdDto) mBase).getUser_id())) {
                                    mPlayInfoDto.setUser_status(1);
                                    initBanChat(false);
                                }
                                break;
                            case ChatCmdDto.LIVE_OVER:
                                //主播离开
                                mRlLiveOver.setVisibility(View.VISIBLE);
                                mLiveContent.setText("主播暂时离开，\n稍等一下马上回来");
                                mManager.stop();
                                mManager.reset();
                                break;
                            case ChatCmdDto.LIVE_END:
                                //直播结束
                                mRlLiveOver.setVisibility(View.VISIBLE);
                                mLiveContent.setText("直播已结束");
                                mManager.stop();
                                break;
                            case ChatCmdDto.LIVE_PUBLISH_PAUSE:
                                break;
                            case ChatCmdDto.LIVE_PUBLISH_START:
                                mRlLiveOver.setVisibility(View.GONE);
                                if (TextUtils.isEmpty(mPlayInfoDto.getVideo().getUrl())) {
                                    mzApiRequest.startData(MZApiRequest.API_TYPE_PLAY_INFO, ticketId);
                                } else {
                                    mManager.reset();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mManager.reload();
                                        }
                                    }, 500);
                                }
                                break;
                            case ChatCmdDto.LIVE_CHANNEL_START:
                                if (ticketId.equals(mChatCmd.getTicket_id()))
                                    mzApiRequest.startData(MZApiRequest.API_TYPE_PLAY_INFO, ticketId);
                                mRlLiveOver.setVisibility(View.GONE);
                                break;
                            case ChatCmdDto.DO_SWITCH_PAGE:
                                mChatCmd.getWebinar_id();
                                break;
                            case ChatPresenter.WEBINAR_VIEW_CONFIG_UPDATE: //弹幕，禁言，防录屏,等配置开关
                                updateConfigs(mChatCmd.getWebinar_content());
                                break;
                        }
                        break;
                }
            }

            @Override
            public void monitorInformError(String s, int i, String s1) {

            }
        });

        MZChatManager.getInstance(getContext()).registerListener(GIFT_KEY , new ChatMessageObserver.MesGiftCallback() {
            @Override
            public void monitorGiftResult(ChatMessageDto chatMessageDto) {
                Log.e("Gift", "monitorGiftResult: " + ((ChatCompleteDto)chatMessageDto.getText().getBaseDto()).getName());
            }
        });

        MZChatManager.getInstance(getActivity()).registerListener(KICK_OUT_KEY, new ChatMessageObserver.MesKickOutCallback() {
            @Override
            public void monitorKickOutResult(ChatMessageDto chatMessageDto) {
                Log.e("Kick_out", "monitorKickOutResult: " + ((ChatCmdDto)chatMessageDto.getText().getBaseDto()).getType());
                //踢出自己自行处理
                MessageDialog messageDialog = new MessageDialog(getContext());
                messageDialog.setTitle("提示");
                messageDialog.setContentMessage("您已被踢出直播间！");
                messageDialog.setSingleType("确定");
                messageDialog.show();
            }
        });

        mzApiRequest = new MZApiRequest();
        mzApiRequestOnline = new MZApiRequest();
        mzApiRequestGoods = new MZApiRequest();
        mzApiRequestAnchorInfo = new MZApiRequest();
        mzGetSettingRequest = new MZApiRequest();

        //设置获取观看信息回调监听
        mzApiRequest.setResultListener(this);
        //在线人数列表回调
        mzApiRequestOnline.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String apiType, Object dto, Page page, int status) {
                personAvatars.clear();
                boolean isContainsMe = false;
                List<MZOnlineUserListDto> mzOnlineUserListDto = (List<MZOnlineUserListDto>) dto;
                for (int i = 0; i < mzOnlineUserListDto.size(); i++) {
                    if (Long.parseLong(mzOnlineUserListDto.get(i).getUid()) < Long.parseLong("5000000000")) {
                        personAvatars.add(mzOnlineUserListDto.get(i));
                    }
                    if (mzOnlineUserListDto.get(i).getUid().equals(mPlayInfoDto.getChat_uid())) {
                        isContainsMe = true;
                    }
                }
                if (!isContainsMe) {
                    MZOnlineUserListDto userListDto = new MZOnlineUserListDto();
                    UserDto userInfoDto = MyUserInfoPresenter.getInstance().getUserInfo();
                    userListDto.setUid(mPlayInfoDto.getChat_uid());
                    userListDto.setAvatar(userInfoDto.getAvatar());
                    userListDto.setNickname(userInfoDto.getNickname());
                    personAvatars.add(userListDto);
                }
                initOnlineAvatar();

            }

            @Override
            public void errorResult(String apiType, int code, String msg) {

            }
        });
        //商品列表回调
        mzApiRequestGoods.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String s, Object o, Page page, int status) {
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
        //主播信息回调
        mzApiRequestAnchorInfo.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String s, Object o, Page page, int status) {
                anchorInfoDto = (AnchorInfoDto) o;
                if (mListener != null) {
                    mListener.resultAnchorInfo(anchorInfoDto);
                }
                mTvNickName.setText(anchorInfoDto.getNickname());
                MZChatManager.getInstance(mActivity).setAnchorUid(anchorInfoDto.getUid());
                ImageLoader.getInstance().displayImage(anchorInfoDto.getAvatar() + String_Utils.getPictureSizeAvatar(), mIvAvatar, avatarOptions);
            }

            @Override
            public void errorResult(String s, int i, String s1) {

            }
        });

        //配置相关回调
        mzGetSettingRequest.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String apiType, Object dto, Page page, int status) {
                if (dto instanceof MZAllSettingDto){
                    MZAllSettingDto mzAllSettingDto = (MZAllSettingDto) dto;
                    if (mzAllSettingDto.getTools() != null && mzAllSettingDto.getTools().size() > 0){
                        List<MZAllSettingDto.SettingDto> settingDtoList = mzAllSettingDto.getTools();
                        List<RightBean> configList = new ArrayList<>();
                        for (int i = 0; i < settingDtoList.size(); i++) {
                            RightBean rightBean = new RightBean();
                            rightBean.setType(settingDtoList.get(i).getTools());
                            rightBean.setIs_open(settingDtoList.get(i).getIs_open());
                            configList.add(rightBean);
                        }
                        updateConfigs(configList);
                    }
                }
            }

            @Override
            public void errorResult(String apiType, int code, String msg) {

            }
        });

        //聊天用户头像点击的回调
        mChatFragment.setOnChatAvatarClickListener(new ChatAvatarClick());

        //红包消息点击事件回调
        mChatFragment.setOnRedPacketClickListener(new RedPacketClickListener());

    }

    //消息弹幕监听
    public void receiveSendDanmaku(Object obj) {
        ChatMessageDto mChatMessage = (ChatMessageDto) obj;
        ChatTextDto mChatText = mChatMessage.getText();
        ChatMegTxtDto megTxtDto = (ChatMegTxtDto) mChatText.getBaseDto();
        boolean isSelf;
        if (TextUtils.isEmpty(megTxtDto.getUniqueID())){
            isSelf = false;
        }else {
            isSelf = megTxtDto.getUniqueID().equals(MyUserInfoPresenter.getInstance().getUserInfo().getUniqueID());
        }
        if (isSelf) {
            mzPlayerView.setDanmakuCustomTextColor(getResources().getColor(R.color.color_fff45c));
        } else {
            mzPlayerView.setDanmakuCustomTextColor(getResources().getColor(R.color.white));
        }
        if (MZChatManager.getInstance(mActivity).isOnlyAnchor()){
            if (isSelf || mChatText.getUser_id().equals(anchorInfoDto.getUid())){
                mzPlayerView.sendDanmaku(mChatText.getUser_name() + ":  " + megTxtDto.getText(), liveStatus == 1, mChatText.getAvatar(), new DanmakuViewCacheStuffer(mActivity, mzPlayerView.getDanmakuView()));
            }
        }else {
            mzPlayerView.sendDanmaku(mChatText.getUser_name() + ":  " + megTxtDto.getText(), liveStatus == 1, mChatText.getAvatar(), new DanmakuViewCacheStuffer(mActivity, mzPlayerView.getDanmakuView()));
        }
    }

    class ChatAvatarClick implements PlayerChatListFragment.OnChatAvatarClickListener {

        @Override
        public void onChatAvatarClick(ChatTextDto dto) {
            if (null != mListener) {
                mListener.onChatAvatar(dto);
            }
        }
    }

    class RedPacketClickListener implements PlayerChatListFragment.OnRedPacketClickListener{

        @Override
        public void onRedPacketClick(ChatTextDto dto) {
            MZRedPacketDialogFragment mzRedPacketDialogFragment = MZRedPacketDialogFragment.newInstance(dto);
            mzRedPacketDialogFragment.setOnHistoryClickListener(new MZRedPacketDialogFragment.OnHistoryClickListener() {
                @Override
                public void onHistoryClick(ChatTextDto dto) {
                    if (mListener != null){
                        mListener.onRedPacketHistoryClick(dto);
                    }
                }
            });
            mzRedPacketDialogFragment.show(getChildFragmentManager() , "MZRedPacketDialogFragment");
        }
    }

    private void loadData() {

        mzApiRequest.createRequest(mActivity, MZApiRequest.API_TYPE_PLAY_INFO);
        mzApiRequestOnline.createRequest(mActivity, MZApiRequest.API_TYPE_ONLINE_USER_LIST);
        mzApiRequestGoods.createRequest(mActivity, MZApiRequest.API_TYPE_GOODS_LIST);
        mzApiRequestAnchorInfo.createRequest(mActivity, MZApiRequest.API_TYPE_ANCHOR_INFO);

        //请求商店列表api
        mzApiRequestGoods.startData(MZApiRequest.API_TYPE_GOODS_LIST, true, ticketId);
        //请求主播信息api
        mzApiRequestAnchorInfo.startData(MZApiRequest.API_TYPE_ANCHOR_INFO, ticketId);

        mzGetSettingRequest.createRequest(mActivity , MZApiRequest.API_GET_ALL_SETTING);
    }

    //需要加载该fragment的activity实现点击回调接口
    private IPlayerClickListener mListener;
    private PlayerEventListener mPlayerEventListener = new PlayerEventListener() {
        @Override
        public void hideAllEvent() {
            PlayerFragment.this.showAllEvent();
        }

        @Override
        public void showAllEvent() {
            PlayerFragment.this.hideAllEvent();
        }

        @Override
        public void onBackClick(boolean isBack) {

        }

        @Override
        public void onPausePlayer() {
            MZChatManager.getInstance(mActivity).sendMessageEndEvent(ticketId);
        }

        @Override
        public void onStartPlayer() {
            MZChatManager.getInstance(mActivity).sendMessagePlayEvent(ticketId, speeds[mSpeedIndex] + "");
        }

        @Override
        public void onForbid(boolean isForbid) {

        }

        @Override
        public void onSwitchFullBtnClick(int orientation) {

        }

        @Override
        public void onTvClick() {
            showDlanDialog();
        }

        @Override
        public void onSpeedClick() {
            showSpeedSelectDialog();
        }
    };

    public void setIPlayerClickListener(IPlayerClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.civ_playerfragment_avatar) { //点击主播头像
            if (mListener != null) {
                mListener.onAvatarClick(anchorInfoDto);
            }

        }
        if (view.getId() == R.id.tv_playerfragment_attention) { //点击关注
            if (mListener != null) {
                mListener.onAttentionClick(mPlayInfoDto, mTvAttention);
            }
        }
        if (view.getId() == R.id.tv_playerfragment_person || view.getId() == R.id.civ_activity_live_online_person1
                || view.getId() == R.id.civ_activity_live_online_person2 || view.getId() == R.id.civ_activity_live_online_person3) { //点击在线人数
            if (mListener != null) {
                PersonListPopupWindow personListPopupWindow = new PersonListPopupWindow(mActivity, mPlayInfoDto);
                personListPopupWindow.showAtLocation(mzPlayerView, Gravity.CENTER, 0, 0);
                personListPopupWindow.setOnPersonListClickCallBack(new PersonListPopupWindow.PersonListClickedCallBack() {
                    @Override
                    public void onPersonItemClicked(MZOnlineUserListDto onlineUserListDto) {
                        mListener.onOnlineClick(onlineUserListDto);
                    }
                });
            }
        }
        if (view.getId() == R.id.iv_playerfragment_close) { //点击退出
            if (mListener != null) {
                mListener.onCloseClick(mPlayInfoDto);
            }
        }

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
            if (PreferencesUtils.loadPrefBoolean(mActivity, PreferencesUtils.DANMAKU_SWITCH_KEY, true)) {
                PreferencesUtils.savePrefBoolean(mActivity, PreferencesUtils.DANMAKU_SWITCH_KEY, false);
                Drawable drawableLeft = getResources().getDrawable(R.mipmap.ic_danmaku_closed);
                mTvDanmakuSwtich.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
            } else {
                PreferencesUtils.savePrefBoolean(mActivity, PreferencesUtils.DANMAKU_SWITCH_KEY, true);
                Drawable drawableLeft = getResources().getDrawable(R.mipmap.ic_danmaku_open);
                mTvDanmakuSwtich.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
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
                        public void dataResult(String s, Object o, Page page, int status) {
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
                if (mPlayInfoDto.isDisable_chat()) {
                    Toast.makeText(mActivity, "主播开启了全体禁言", Toast.LENGTH_SHORT).show();
                    return;
                }
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
            showGoodsDialog();
        }
        if (view.getId() == R.id.iv_playerfragment_gift){
            showSendGiftDialog();
        }
        if (view.getId() == R.id.iv_playerfragment_red_packet){ //点击创建红包
            if (mListener != null)
                mListener.onRedPacketClick(mPlayInfoDto);
        }
        if (view.getId() == R.id.iv_vote){ //投票
            assert getFragmentManager() != null;
            VoteDialogFragment voteDialogFragment = (VoteDialogFragment) getFragmentManager().findFragmentByTag("VOTEDIALOGFRAGMENT");
            if (null == voteDialogFragment) {
                voteDialogFragment = VoteDialogFragment.newInstance(mPlayInfoDto);
            }
            if (!voteDialogFragment.isAdded() && !voteDialogFragment.isVisible() && !voteDialogFragment.isRemoving()) {
                voteDialogFragment.show(getFragmentManager(), "VOTEDIALOGFRAGMENT");
            }
        }
        if (view.getId() == R.id.iv_sign_in){
            if (signInfoBean == null)return;
            if (signInfoBean.getIs_expired() == 1) {
                ToastUtils.popUpToast("签到过期无法签到");
            } else {
                if (!signInfoBean.isIs_sign()) {
                    if (signInfoBean.getStatus() == 1) {
                        if (signCountDown != null)
                            signCountDown.cancel();
                        showSignDialog();
                    } else if (signInfoBean.getStatus() == 0) {
                        ToastUtils.popUpToast("签到活动未开始");
                    } else {
                        ToastUtils.popUpToast("签到活动已结束");
                    }
                } else {
                    showSignDialog();
                }
            }
        }
        if(view.getId() == R.id.iv_draw){
            if(!CommonUtil.isFastDoubleClick()){
                showLottoDialog();
            }
        }
    }

    /**
     * 获取观看信息的回调
     */
    @SuppressLint("WrongConstant")
    @Override
    public void dataResult(String s, Object o, Page page, int status) {
        mPlayInfoDto = (PlayInfoDto) o;
        updateConfigs(mPlayInfoDto.getRight());
        for (int i = 0; i < mPlayInfoDto.getRight().size(); i++) {
            if ("sign".equals(mPlayInfoDto.getRight().get(i).getType())){
                signInfoBean = mPlayInfoDto.getRight().get(i).getContent();
            }
            if ("prize".equals(mPlayInfoDto.getRight().get(i).getType())){
                lottoBean = mPlayInfoDto.getRight().get(i).getContent();
            }
        }
        if (mPlayInfoDto.isVoteShow()){
            mIvVote.setVisibility(View.VISIBLE);
        }
        if (mPlayInfoDto.isSignShow()){
            mIvSignIn.setVisibility(View.VISIBLE);
        }
        if(mPlayInfoDto.isLottoShow()){
            mIvDraw.setVisibility(View.VISIBLE);
        }
        initSign();
        MyUserInfoPresenter.getInstance().getUserInfo().setUid(mPlayInfoDto.getChat_uid());
        //请求配置相关数据
        mzGetSettingRequest.startData(MZApiRequest.API_GET_ALL_SETTING, ticketId);
        liveStatus = mPlayInfoDto.getStatus();
        //获取播放地址
        mVideoUrl = mPlayInfoDto.getVideo().getUrl();
        if (!TextUtils.isEmpty(mVideoUrl)) {
            controller = DLNAController.getInstance(mActivity.getApplication());
            controller.initPresenter(mActivity.getApplication());
            controller.registerListener(mActivity.getClass().getSimpleName(), new DLNAListener());
            RemoteItem itemurl1 = new RemoteItem("name", "1111", "wwwww",
                    0, "0", "", mVideoUrl);
            controller.setUrl(itemurl1);
            int liveType = PlayerController.TYPE_LIVE;
            if (liveStatus == 2) {
                liveType = PlayerController.TYPE_VIDEO;
            }
            mManager.setBroadcastType(liveType, mPlayInfoDto.getLive_type() == 1).setMediaQuality(IMZPlayerManager.MZ_MEDIA_QUALITY_HIGH);
            if (mPlayerEventListener != null) {
                mManager.setEventListener(mPlayerEventListener);
            }
            mManager.setVideoPath(mVideoUrl);
            mManager.showPreviewImage(mPlayInfoDto.getCover());
            mManager.setIsOpenRandom(true);
            mManager.setRandomData("test", 80, getResources().getColor(R.color.color_3F95FD));
            mzPlayerView.enableDanmaku(true);
            mzPlayerView.hideFullIV();
            mManager.setPlayerReConnectViewShow(true);
            mManager.setNetWorkConnectReload(true);
            mManager.start();
            MZChatManager.getInstance(mActivity).setPlayinfo(mPlayInfoDto);
            MZChatManager.getInstance(mActivity).sendMessagePlayEvent(ticketId, speeds[mSpeedIndex] + "");
        } else {
            if (liveStatus == 3) {
                mRlLiveOver.setVisibility(View.VISIBLE);
                mLiveContent.setText("主播暂时离开，\n稍等一下马上回来");
            } else {
                mzPlayerView.showPreviewImage(mPlayInfoDto.getCover());
            }
        }
        if (liveStatus != 2 && mPlayInfoDto.getLive_type() == 1) {
            mzPlayerView.hideDlnaIV();
        }
        //请求在线人数api
        mzApiRequestOnline.startData(MZApiRequest.API_TYPE_ONLINE_USER_LIST, true, ticketId);
        //加载人气
        mTvPopular.setText("人气" + String_Utils.convert2W0_0(mPlayInfoDto.getPopular()));
        //是否禁言
        initBanChat(mPlayInfoDto.getUser_status() == 3 || mPlayInfoDto.getUser_status() == 2);
        //加载直播状态
        initLiveStatus();
        //添加聊天fragment
        if (mChatFragment.isAdded()) {
            return;
        }
        mTvOnline.setText(mPlayInfoDto.getWebinar_onlines() + "");
        Bundle bundle = new Bundle();
        bundle.putBoolean(PlayerChatListFragment.PLAY_TYPE_KEY, true);
        bundle.putSerializable(PlayerChatListFragment.PLAY_INFO_KEY, mPlayInfoDto);
        mChatFragment.setArguments(bundle);
        getChildFragmentManager().beginTransaction().replace(R.id.layout_activity_live_broadcast_chat, mChatFragment).commitAllowingStateLoss();

    }

    @Override
    public void errorResult(String s, int i, String s1) {
        Log.e("gm", "errorResult: " + s);
    }

    //初始化右侧观看用户的头像
    private void initOnlineAvatar() {
        if (personAvatars.size() >= 3) {
            mOnlinePersonIv1.setVisibility(View.VISIBLE);
            mOnlinePersonIv2.setVisibility(View.VISIBLE);
            mOnlinePersonIv3.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(personAvatars.get(personAvatars.size() - 1).getAvatar(), mOnlinePersonIv1, avatarOptions);
            ImageLoader.getInstance().displayImage(personAvatars.get(personAvatars.size() - 2).getAvatar(), mOnlinePersonIv2, avatarOptions);
            ImageLoader.getInstance().displayImage(personAvatars.get(personAvatars.size() - 3).getAvatar(), mOnlinePersonIv3, avatarOptions);
        } else if (personAvatars.size() == 2) {
            mOnlinePersonIv1.setVisibility(View.VISIBLE);
            mOnlinePersonIv2.setVisibility(View.VISIBLE);
            mOnlinePersonIv3.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage(personAvatars.get(personAvatars.size() - 1).getAvatar(), mOnlinePersonIv1, avatarOptions);
            ImageLoader.getInstance().displayImage(personAvatars.get(personAvatars.size() - 2).getAvatar(), mOnlinePersonIv2, avatarOptions);

        } else if (personAvatars.size() == 1) {
            mOnlinePersonIv1.setVisibility(View.VISIBLE);
            mOnlinePersonIv2.setVisibility(View.GONE);
            mOnlinePersonIv3.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage(personAvatars.get(personAvatars.size() - 1).getAvatar(), mOnlinePersonIv1, avatarOptions);
        }
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
     * 更新配置消息
     */
    private void updateConfigs(List<RightBean> rightBeans) {
        for (int i = 0; i < rightBeans.size(); i++) {
            boolean isOpen = rightBeans.get(i).getIs_open() == 1;
            switch (rightBeans.get(i).getType()) {
                case PlayInfoDto.DISABLE_CHAT: //禁言
                    mPlayInfoDto.setDisable_chat(isOpen);
//                    StaticStateDto.getInstance().setForbidPublicMsg(isOpen);
                    break;
                case PlayInfoDto.BARRAGE:
                    mPlayInfoDto.setInputDanmuku(isOpen);
                    break;
                case PlayInfoDto.RECORD_SCREEN:
                    if (mActivity != null) {
                        mPlayInfoDto.setRecordScreen(isOpen);
                        mzPlayerView.setIsOpenRandom(isOpen);
                    }
                    break;
                case PlayInfoDto.VOTE:
                    mPlayInfoDto.setVoteShow(isOpen);
                    break;
                case PlayInfoDto.SIGN:
                    mPlayInfoDto.setSignShow(isOpen);
                    break;
                case PlayInfoDto.PRIZE:
                    mPlayInfoDto.setLottoShow(isOpen);
                    break;
                case PlayInfoDto.DOCUMENTS:
                    mPlayInfoDto.setDocumentShow(isOpen);
                    break;
                case PlayInfoDto.CHAT_HISTORY:
                    mPlayInfoDto.setHide_chat_history(isOpen);
                    break;
                case PlayInfoDto.PAY_GIFT:
                    mPlayInfoDto.setPay_gift_open(isOpen);
                    mIvGift.setVisibility(mPlayInfoDto.isPay_gift_open() ? View.VISIBLE : View.GONE);
                    break;
                case PlayInfoDto.OPEN_LIKE:
                    mPlayInfoDto.setLike_open(isOpen);
                    mLoveLayout.setVisibility(mPlayInfoDto.isLike_open() ? View.VISIBLE : View.GONE);
                    mIvLike.setVisibility(mPlayInfoDto.isLike_open() ? View.VISIBLE : View.GONE);
                    break;
                case PlayInfoDto.TIMES_SPEED:
                    mPlayInfoDto.setTimes_speed_open(isOpen);
                    if (mPlayInfoDto.isTimes_speed_open()){
                        mzPlayerView.showSpeedIV();
                    }else {
                        mzPlayerView.hideSpeedIV();
                    }
                    break;
            }
        }
        updateButtons(mPlayInfoDto);
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
        goodsListPopupWindow.showAtLocation(mzPlayerView, Gravity.BOTTOM, 0, 0);
    }

    public void showSendGiftDialog(){
        if (mPlayInfoDto != null){
            giftDialogFragment = SendGiftDialogFragment.newInstance(mPlayInfoDto);
            giftDialogFragment.show(getFragmentManager() , "gift_dialog");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MZChatManager.getInstance(mActivity).removeListener(CLASSNAME);
        MZChatManager.getInstance(mActivity).removeListener(GIFT_KEY);
        MZChatManager.getInstance(mActivity).removeListener(KICK_OUT_KEY);
        MZChatManager.destroyChat();
        //释放飘心资源
        if (null != mLoveLayout) {
            mLoveLayout.removeView();
        }
        mManager.onDestroy();
        if (controller != null) {
            controller.onDestroy();
            controller.unRegisterListener(mActivity.getClass().getSimpleName());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (controller != null)
            controller.onExecute(DLNAController.STOP_DLNA);
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    public void hideAllEvent() {
        if (liveStatus == 2) {
            mzPlayerView.setIsCanTouch(true);
            mRlChatAndBottom.setVisibility(View.GONE);
        }
    }

    public void showAllEvent() {
        mzPlayerView.setIsCanTouch(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRlChatAndBottom.setVisibility(View.VISIBLE);
            }
        }, 500);
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

    private void initLiveStatus() {
        if (liveStatus == 2) {
            mTvLiveType.setBackgroundResource(R.drawable.shape_video_type_ball);
            mTVLiveTypeText.setText("回放");
        } else if (liveStatus == 1) {
            mTvLiveType.setBackgroundResource(R.drawable.shape_live_type_ball);
            mTVLiveTypeText.setText("直播");
        } else if (liveStatus == 0) {
            mTvLiveType.setBackgroundResource(R.drawable.shape_video_type_ball);
            mTVLiveTypeText.setText("未开播");
        }
    }

    private void showSpeedSelectDialog() {
        speedDialog = new SpeedBottomDialogFragment(mActivity, false);
        speedDialog.showAtLocation(mzPlayerView, mSpeedIndex);
        speedDialog.setOnSelectSpeedListener(new SpeedBottomDialogFragment.OnSelectSpeedListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void OnSelectSpeed(int type) {
                mSpeedIndex = type;
                MZChatManager.getInstance(mActivity).sendMessageChangeSpeedEvent(ticketId, speeds[mSpeedIndex] + "");
                mzPlayerView.setSpeed(speeds[mSpeedIndex]);
                switch (type) {
                    case 0:
                        mzPlayerView.setSpeedDrawable(getResources().getDrawable(R.mipmap.icon_speed_075));
                        break;
                    case 1:
                        mzPlayerView.setSpeedDrawable(getResources().getDrawable(R.mipmap.icon_speed_100));
                        break;
                    case 2:
                        mzPlayerView.setSpeedDrawable(getResources().getDrawable(R.mipmap.icon_speed_125));
                        break;
                    case 3:
                        mzPlayerView.setSpeedDrawable(getResources().getDrawable(R.mipmap.icon_speed_150));
                        break;
                    case 4:
                        mzPlayerView.setSpeedDrawable(getResources().getDrawable(R.mipmap.icon_speed_200));
                        break;
                }
            }
        });
    }

    public void updateButtons(PlayInfoDto playInfoDto){
        if (playInfoDto.isVoteShow()){
            mIvVote.setVisibility(View.VISIBLE);
        }else {
            mIvVote.setVisibility(View.GONE);
        }
        if (!playInfoDto.isSignShow()){
            mIvSignIn.setVisibility(View.GONE);
        }
        mLoveLayout.setVisibility(mPlayInfoDto.isLike_open() ? View.VISIBLE : View.GONE);
        mIvDraw.setVisibility(playInfoDto.isLottoShow()?View.VISIBLE:View.GONE);
    }

    //初始化签到数据
    private void initSign(){
        if (signInfoBean == null) return;
        if (signInfoBean.isIs_sign()){
            mIvSignIn.setImageResource(R.mipmap.icon_mz_signed);
        }else {
            mIvSignIn.setImageResource(R.mipmap.icon_mz_sign_in);
        }
        if (signInfoBean.isIs_force() &&
                signInfoBean.getIs_expired() == 0
                && signInfoBean.getRedirect_sign() == 1
                && signInfoBean.getDelay_time()==0
                && !signInfoBean.isIs_sign()
                && signInfoBean.getStatus() == 1) {
            showSignDialog();
        }else if(signInfoBean.isIs_force()
                && signInfoBean.getRedirect_sign() == 1
                && signInfoBean.getDelay_time()>0
                && !signInfoBean.isIs_sign()){
            if(PreferencesUtils.loadPrefBoolean(mActivity,mPlayInfoDto.getTicket_id() +signInfoBean.getSign_id() + mPlayInfoDto.getUnique_id(),false)){
                showSignDialog();
            }else {
                countdownSign(signInfoBean.getDelay_time()*6000);
            }
        }
    }

    private void countdownSign(long l) {
        signCountDown = new CountDownTimer(l, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                showSignDialog();
            }
        }.start();
    }

    //签到弹窗
    public void showSignDialog(){
        if (signInfoBean == null || mActivity == null || signDialogShowing)
            return;
        final SignInWebFragment signInWebFragment = new SignInWebFragment(mActivity , mPlayInfoDto , signInfoBean);
        signInWebFragment.setSignInOutSideDismiss(false);
        signInWebFragment.setOnDismissListener(new SignInWebFragment.OnDismissListener() {
            @Override
            public void onDismiss(boolean isSign) {
                signDialogShowing = false;
                signInWebFragment.onDestroy();
                if (isSign){
                    mIvSignIn.setImageResource(R.mipmap.icon_mz_signed);
                }
            }
        });
        PreferencesUtils.savePrefBoolean(mActivity,mPlayInfoDto.getTicket_id() +signInfoBean.getSign_id() + mPlayInfoDto.getUnique_id(),true);
        signInWebFragment.showAtLocation(mActivity.findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
        signDialogShowing = true;
    }

    /**
     * 抽奖弹窗
     */
    public void showLottoDialog(){
        MZLottoWebFragment lottoWebFragment = new MZLottoWebFragment(mActivity,lottoBean,"抽奖");
        lottoWebFragment.setLottoOutSideDismiss(true);
        lottoWebFragment.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        lottoWebFragment.showAtLocation(mActivity.findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
    }

    //开始投屏
    public void startDlan() {
        if (controller != null)
            controller.onExecute(DLNAController.PLAYER_DLNA);
    }

    /**
     * 底面搜索投屏列表
     */
    public void showDlanDialog() {

        try {
            assert getFragmentManager() != null;
            ChannelDlnaDialogFragment dlanDialogFragment = (ChannelDlnaDialogFragment) getFragmentManager().findFragmentByTag("DLANDIALOGFRAGMENT");
            if (null == dlanDialogFragment) {
                ChannelDlnaDialogFragment.Builder builder = new ChannelDlnaDialogFragment.Builder(mActivity);
                builder.setOnDeviceItemSelectListener(new ChannelDlnaDialogFragment.OnDeviceItemSelectListener() {
                    @Override
                    public void onSelectDevice() {
                        startDlan();
                    }
                });
                dlanDialogFragment = builder.build();
            }
            if (!dlanDialogFragment.isAdded() && !dlanDialogFragment.isVisible() && !dlanDialogFragment.isRemoving()) {
                dlanDialogFragment.show(getFragmentManager(), "DLANDIALOGFRAGMENT");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class DLNAListener implements DLNAController.DLNAStateListener {

        @Override
        public void onSuccess(final DLNAController.DLNAState state) {

        }

        @Override
        public void onError(DLNAController.DLNAError error) {


        }
    }

}
