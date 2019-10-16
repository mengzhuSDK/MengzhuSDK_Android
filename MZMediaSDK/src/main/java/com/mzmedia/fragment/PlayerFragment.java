package com.mzmedia.fragment;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mengzhu.live.sdk.R;
import com.mengzhu.live.sdk.business.dto.AnchorInfoDto;
import com.mengzhu.live.sdk.business.dto.BaseDto;
import com.mengzhu.live.sdk.business.dto.MZGoodsListDto;
import com.mengzhu.live.sdk.business.dto.MZGoodsListExternalDto;
import com.mengzhu.live.sdk.business.dto.MZOnlineUserListDto;
import com.mengzhu.live.sdk.business.dto.UserDto;
import com.mengzhu.live.sdk.business.dto.chat.ChatMessageDto;
import com.mengzhu.live.sdk.business.dto.chat.ChatTextDto;
import com.mengzhu.live.sdk.business.dto.chat.impl.ChatCmdDto;
import com.mengzhu.live.sdk.business.dto.chat.impl.ChatCompleteDto;
import com.mengzhu.live.sdk.business.dto.chat.impl.ChatOnlineDto;
import com.mengzhu.live.sdk.business.dto.play.PlayInfoDto;
import com.mengzhu.live.sdk.business.presenter.MyUserInfoPresenter;
import com.mengzhu.live.sdk.business.presenter.chat.ChatMessageObserver;
import com.mengzhu.live.sdk.business.presenter.chat.ChatPresenter;
import com.mengzhu.live.sdk.business.presenter.player.media.IRenderView;
import com.mengzhu.live.sdk.business.view.widgets.MZVideoView;
import com.mengzhu.live.sdk.core.MZSDKInitManager;
import com.mengzhu.live.sdk.core.SDKInitListener;
import com.mengzhu.live.sdk.core.netwock.Page;
import com.mengzhu.live.sdk.ui.api.MZApiDataListener;
import com.mengzhu.live.sdk.ui.api.MZApiRequest;
import com.mengzhu.live.sdk.ui.chat.MZChatManager;
import com.mengzhu.live.sdk.ui.chat.MZChatMessagerListener;
import com.mzmedia.IPlayerClickListener;
import com.mzmedia.utils.ActivityUtils;
import com.mzmedia.utils.String_Utils;
import com.mzmedia.widgets.ChatOnlineView;
import com.mzmedia.widgets.CircleImageView;
import com.mzmedia.widgets.LoveLayout;
import com.mzmedia.widgets.player.PlayerGoodsPushView;
import com.mzmedia.widgets.player.PlayerGoodsView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

public class PlayerFragment extends Fragment implements View.OnClickListener, MZApiDataListener {

    private static final String USER_INFO = "userDto";
    public static final String APP_ID = "appid";
    public static final String AVATAR = "avatar";
    public static final String NICKNAME = "nickName";
    public static final String ACCOUNTNO = "accountNo";
    public static final String TICKET_ID = "ticket_id";
    private MZVideoView mzVideoView;
    private CircleImageView mIvAvatar; //头像
    private CircleImageView mOnlinePersonIv1, mOnlinePersonIv2, mOnlinePersonIv3; //在线人数头像
    private TextView mTvNickName; //昵称
    private TextView mTvPopular; //人气
    private TextView mTvAttention; //关注
    private TextView mTvOnline; //在线人数
    private ImageView mIvClose; //退出
    private ImageView mIvConfig; //配置
    private ImageView mIvShare; //分享
    private ImageView mIvLike; //点赞
    private ImageView mIvReport; //举报
    private RelativeLayout mRlLiveOver; //主播离开
    private RelativeLayout mRlSendChat; //发消息
    private TextView mTvHitChat; //发消息提示文字
    private LoveLayout mLoveLayout; //飘心
    private boolean isConfig;
    private PlayerGoodsView mPlayerGoodsLayout;
    private PlayerGoodsPushView mPlayerGoodsPushLayout;
    private TextView mGoodsIv; //店铺
    private String mVideoUrl; //观看地址
    private UserDto mUserDto; //主播信息
    private String mUid;
    private String mAppid;
    private String mAvatr;
    private String mNickName;
    private String mAccountNo;
    private Activity mActivity;
    private DisplayImageOptions avatarOptions;
    private MZApiRequest mzApiRequest;
    private MZApiRequest mzApiRequestGoods;
    private MZApiRequest mzApiRequestOnline;
    private MZApiRequest mzApiRequestAnchorInfo;
    private String ticketId; //活动id
    private ArrayList<MZGoodsListDto> mGoodsListDtos;
    private int mPosition;
    private String mTotalPerson;
    private List<String> personAvatars = new ArrayList<>();
    private ChatOnlineView mChatOnlineView;
    private ArrayList<ChatCompleteDto> mChatCompleteDtos = new ArrayList<>();
    private PlayInfoDto mPlayInfoDto;
    private PlayerChatListFragment mChatFragment;
    private boolean isLooping;
    private GoodsCountDown goodsCountDown;

    public static PlayerFragment newInstance(String Appid, String avatar, String nickName, String accountNo, String ticketId) {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putSerializable(APP_ID, Appid);
        args.putSerializable(AVATAR, avatar);
        args.putSerializable(NICKNAME, nickName);
        args.putSerializable(ACCOUNTNO, accountNo);
        args.putString(TICKET_ID, ticketId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAppid = getArguments().getString(APP_ID);
            mAvatr = getArguments().getString(AVATAR);
            mNickName = getArguments().getString(NICKNAME);
            mAccountNo = getArguments().getString(ACCOUNTNO);
            ticketId = getArguments().getString(TICKET_ID);
        }
        mUserDto = new UserDto();
        mUserDto.setAppid(mAppid);
        mUserDto.setAvatar(mAvatr);
        mUserDto.setNickname(mNickName);
        mUserDto.setAccountNo(mAccountNo);
        //保存观看用户信息
        MyUserInfoPresenter.getInstance().saveUserinfo(mUserDto);
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
        mTvAttention = rootView.findViewById(R.id.tv_playerfragment_attention);
        mIvConfig = rootView.findViewById(R.id.iv_playerfragment_config);
        mIvShare = rootView.findViewById(R.id.iv_playerfragment_share);
        mIvLike = rootView.findViewById(R.id.iv_playerfragment_zan);
        mIvReport = rootView.findViewById(R.id.iv_playerfragment_report);
        mLoveLayout = rootView.findViewById(R.id.love_playerfragment_layout);
        mzVideoView = rootView.findViewById(R.id.video_playerfragment_view);
        mRlSendChat = rootView.findViewById(R.id.rl_playerfragment_send_chat);
        mRlLiveOver = rootView.findViewById(R.id.rl_activity_broadcast_live_over);
        mTvHitChat = rootView.findViewById(R.id.tv_playerfragment_chat);
        mPlayerGoodsLayout = rootView.findViewById(R.id.live_broadcast_goods_view);
        mPlayerGoodsPushLayout = rootView.findViewById(R.id.live_broadcast_goods_push_view);
        mGoodsIv = rootView.findViewById(R.id.iv_player_fragment_goods);
        mChatOnlineView = rootView.findViewById(R.id.player_chat_list_online_view);

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
        //处理播放器黑边问题
        mzVideoView.setAspectRatio(IRenderView.AR_ASPECT_FILL_PARENT);
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
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));
        mChatFragment = new PlayerChatListFragment();
        goodsCountDown = new GoodsCountDown(10000 * 5000, 5000);
    }

    private void initListener() {
        mTvOnline.setOnClickListener(this);
        mIvAvatar.setOnClickListener(this);
        mTvAttention.setOnClickListener(this);
        mIvConfig.setOnClickListener(this);
        mIvShare.setOnClickListener(this);
        mIvLike.setOnClickListener(this);
        mIvReport.setOnClickListener(this);
        mIvClose.setOnClickListener(this);
        mRlSendChat.setOnClickListener(this);
        mGoodsIv.setOnClickListener(this);

        mPlayerGoodsPushLayout.setOnGoodsPushItemClickListener(new PlayerGoodsView.OnGoodsItemClickListener() {
            @Override
            public void onGoodsItemClick() {
                if (mListener != null) {
                    mListener.onRecommendGoods(mPlayInfoDto);
                }
            }
        });
        mPlayerGoodsLayout.setOnGoodsItemClickListener(new PlayerGoodsView.OnGoodsItemClickListener() {
            @Override
            public void onGoodsItemClick() {
                if (mListener != null) {
                    mListener.onRecommendGoods(mPlayInfoDto);
                }
            }
        });
        //各类型消息回调
        MZChatManager.getInstance(mActivity).registerListener(PlayerFragment.class.getSimpleName(), new MZChatMessagerListener() {
            @Override
            public void dataResult(Object o, Page page, int i) {

            }

            @Override
            public void errorResult(int i, String s) {

            }

            @Override
            public void monitorInformResult(String s, Object o) {
                Log.e("wzh", "消息类型=" + s);
                ChatMessageDto mChatMessage = (ChatMessageDto) o;
                ChatTextDto mChatText = mChatMessage.getText();
                BaseDto mBase = mChatText.getBaseDto();
                switch (s) {
                    case ChatMessageObserver.ONLINE_TYPE: {//上下线消息
                        ChatOnlineDto mChatOnline = (ChatOnlineDto) mBase;
                        mTotalPerson = mChatOnline.getConcurrent_user();
                        mChatOnlineView.startOnline(mActivity, mChatMessage);
                        int current = Integer.valueOf(mTotalPerson) + Integer.valueOf(mPlayInfoDto.getUv());
                        try {
                            mTvOnline.setText(String_Utils.convert2W0_0(current + ""));
                            personAvatars.add(mChatText.getAvatar());
                            initOnlineAvatar();
                        } catch (Exception e) {
                        }
                        break;
                    }
                    case ChatMessageObserver.OFFLINE_TYPE:
                        ChatOnlineDto mChatOnline = (ChatOnlineDto) mBase;
                        mTotalPerson = mChatOnline.getConcurrent_user();
                        int current = Integer.valueOf(mTotalPerson) + Integer.valueOf(mPlayInfoDto.getUv());
                        try {
                            mTvOnline.setText(String_Utils.convert2W0_0(current + ""));
                            personAvatars.remove(mChatText.getAvatar());
                            initOnlineAvatar();
                        } catch (Exception e) {
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
                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {
                                            mChatCompleteDtos.remove(0);
                                            if (mChatCompleteDtos.size() > 0) {
                                                mPlayerGoodsPushLayout.startPlayerGoods();
                                                mPlayerGoodsPushLayout.setGoodsData(mChatCompleteDtos.get(0));
                                            } else {
                                                mPlayerGoodsPushLayout.setVisibility(View.GONE);
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
                                mzVideoView.pause();
                                break;
                        }
                        break;
                }
            }

            @Override
            public void monitorInformErrer(String s, int i, String s1) {

            }
        });

        mzApiRequest = new MZApiRequest();
        mzApiRequestOnline = new MZApiRequest();
        mzApiRequestGoods = new MZApiRequest();
        mzApiRequestAnchorInfo = new MZApiRequest();

        //设置获取观看信息回调监听
        mzApiRequest.setResultListener(this);
        //在线人数列表回调
        mzApiRequestOnline.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String apiType, Object dto) {
                List<MZOnlineUserListDto> mzOnlineUserListDto = (List<MZOnlineUserListDto>) dto;
                mTvOnline.setText(mzOnlineUserListDto.size() + "");
                for (int i = 0; i < mzOnlineUserListDto.size(); i++) {
                    personAvatars.add(mzOnlineUserListDto.get(i).getAvatar());
                }
                initOnlineAvatar();
                Log.d("gm", "dataResult: API_TYPE_ONLINE_USER_LIST");

            }

            @Override
            public void errorResult(String apiType, int code, String msg) {
                Log.d("gm", "errorResult: API_TYPE_ONLINE_USER_LIST");
            }
        });
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
        //主播信息回调
        mzApiRequestAnchorInfo.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String s, Object o) {
                AnchorInfoDto anchorInfoDto = (AnchorInfoDto) o;
                mTvNickName.setText(anchorInfoDto.getNickname());
                ImageLoader.getInstance().displayImage(anchorInfoDto.getAvatar() + String_Utils.getPictureSizeAvatar(), mIvAvatar, avatarOptions);
            }

            @Override
            public void errorResult(String s, int i, String s1) {

            }
        });

        //聊天用户头像点击的回调
        mChatFragment.setOnChatAvatarClickListener(new PlayerChatListFragment.OnChatAvatarClickListener() {
            @Override
            public void onChatAvatarClick(ChatTextDto dto) {
                if (null != mListener) {
                    mListener.onChatAvatar(dto);
                }
            }
        });
    }

    private void loadData() {

        mzApiRequest.createRequest(mActivity, MZApiRequest.API_TYPE_PLAY_INFO);
        mzApiRequestOnline.createRequest(mActivity, MZApiRequest.API_TYPE_ONLINE_USER_LIST);
        mzApiRequestGoods.createRequest(mActivity, MZApiRequest.API_TYPE_GOODS_LIST);
        mzApiRequestAnchorInfo.createRequest(mActivity, MZApiRequest.API_TYPE_ANCHOR_INFO);

        //请求商店列表api
        mzApiRequestGoods.startData(MZApiRequest.API_TYPE_GOODS_LIST, true, ticketId);
        //请求在线人数api
        mzApiRequestOnline.startData(MZApiRequest.API_TYPE_ONLINE_USER_LIST, true, ticketId);
        //请求主播信息api
        mzApiRequestAnchorInfo.startData(MZApiRequest.API_TYPE_ANCHOR_INFO, ticketId);
    }

    //需要加载该fragment的activity实现点击回调接口
    private IPlayerClickListener mListener;

    public void setIPlayerClickListener(IPlayerClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.civ_playerfragment_avatar) { //点击主播头像
            if (mListener != null) {
                mListener.onAvatarClick(mPlayInfoDto);
            }

        }
        if (view.getId() == R.id.tv_playerfragment_attention) { //点击关注
            if (mListener != null) {
                mListener.onAttentionClick(mPlayInfoDto);
            }

        }
        if (view.getId() == R.id.tv_playerfragment_person) { //点击在线人数
            if (mListener != null) {
                mListener.onOnlineClick();
            }
        }
        if (view.getId() == R.id.iv_playerfragment_close) { //点击退出
            if (mListener != null) {
                mListener.onCloseClick(mPlayInfoDto);
            }
        }
        if (view.getId() == R.id.iv_playerfragment_config) { //点击底部三个点显示举报
            isConfig = !isConfig;
            mIvConfig.setImageResource(isConfig ? R.mipmap.gm_icon_config_true : R.mipmap.gm_icon_config);
            mIvReport.setVisibility(isConfig ? View.VISIBLE : View.GONE);
        }
        if (view.getId() == R.id.iv_playerfragment_report) { //点击举报
            if (mListener != null) {
                mListener.onReportClick(mPlayInfoDto);
            }
        }
        if (view.getId() == R.id.iv_playerfragment_share) { //点击分享
            if (mListener != null) {
                mListener.onShareClick(mPlayInfoDto);
            }
        }
        if (view.getId() == R.id.iv_playerfragment_zan) { //点击点赞
            if (mListener != null) {
                mListener.onLikeClick(mPlayInfoDto);
            }
            //飘心
            mLoveLayout.addLoveView();
        }
        if (view.getId() == R.id.rl_playerfragment_send_chat) { //点击发送消息
            if (mPlayInfoDto.getUser_status() == 1) {
                ActivityUtils.startLandscapeTransActivity(mActivity);
            } else if (mPlayInfoDto.getUser_status() == 3) {
                Toast.makeText(mActivity, "您已被禁言", Toast.LENGTH_SHORT).show();

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

    /**
     * 获取观看信息的回调
     */
    @Override
    public void dataResult(String s, Object o) {
        mPlayInfoDto = (PlayInfoDto) o;
        //获取播放地址
        mVideoUrl = mPlayInfoDto.getVideo().getHttp_url();
        if (!TextUtils.isEmpty(mVideoUrl)) {
            //设置播放地址到播放器
            mzVideoView.setVideoPath(mVideoUrl);
            //开始观看
            mzVideoView.start();
        }
        //加载人气
        mTvPopular.setText("人气" + String_Utils.convert2W0_0(mPlayInfoDto.getPopular()) + "人");
        //是否禁言
        initBanChat(mPlayInfoDto.getUser_status() == 3);

        //添加聊天fragment
        Bundle bundle = new Bundle();
        bundle.putBoolean(PlayerChatListFragment.PLAY_TYPE_KEY, true);
        bundle.putSerializable(PlayerChatListFragment.PLAY_INFO_KEY, mPlayInfoDto);
        mChatFragment.setArguments(bundle);
        getChildFragmentManager().beginTransaction().replace(R.id.layout_activity_live_broadcast_chat, mChatFragment).commitAllowingStateLoss();
        //请求历史消息api
//        MZChatManager.getInstance(mActivity).startHistory((PlayInfoDto) o);

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
            ImageLoader.getInstance().displayImage(personAvatars.get(personAvatars.size() - 1), mOnlinePersonIv1, avatarOptions);
            ImageLoader.getInstance().displayImage(personAvatars.get(personAvatars.size() - 2), mOnlinePersonIv2, avatarOptions);
            ImageLoader.getInstance().displayImage(personAvatars.get(personAvatars.size() - 3), mOnlinePersonIv3, avatarOptions);
        } else if (personAvatars.size() == 2) {
            mOnlinePersonIv1.setVisibility(View.VISIBLE);
            mOnlinePersonIv2.setVisibility(View.VISIBLE);
            mOnlinePersonIv3.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage(personAvatars.get(personAvatars.size() - 1), mOnlinePersonIv1, avatarOptions);
            ImageLoader.getInstance().displayImage(personAvatars.get(personAvatars.size() - 2), mOnlinePersonIv2, avatarOptions);

        } else if (personAvatars.size() == 1) {
            mOnlinePersonIv1.setVisibility(View.VISIBLE);
            mOnlinePersonIv2.setVisibility(View.GONE);
            mOnlinePersonIv3.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage(personAvatars.get(personAvatars.size() - 1), mOnlinePersonIv1, avatarOptions);
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
                    mPlayerGoodsLayout.startPlayerGoods();
                    mPlayerGoodsLayout.setGoodsData(mGoodsListDtos.get(mPosition));
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
            public void onGoodsLoad(ArrayList<MZGoodsListDto> mzGoodsListDtos,int totalNum) {
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
        goodsListPopupWindow.showAtLocation(mzVideoView, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 获取观看者昵称
     *
     * @return
     */
    public String getUserNickName() {
        return MyUserInfoPresenter.getInstance().getUserInfo().getNickname();
    }

    /**
     * 获取观看者Uid
     *
     * @return
     */
    public String getUserUid() {
        return MyUserInfoPresenter.getInstance().getUserInfo().getUid();
    }

    /**
     * 获取观看者头像地址
     *
     * @return
     */
    public String getUserAvatar() {
        return MyUserInfoPresenter.getInstance().getUserInfo().getAvatar();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MZChatManager.getInstance(mActivity).destroyChat();
        //释放飘心资源
        if (null != mLoveLayout) {
            mLoveLayout.removeView();
        }
        //关闭播放器
        if (null != mzVideoView) {
            mzVideoView.destroy();
        }
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
}
