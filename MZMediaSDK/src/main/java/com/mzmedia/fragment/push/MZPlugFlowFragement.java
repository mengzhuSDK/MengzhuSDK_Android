package com.mzmedia.fragment.push;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mengzhu.live.sdk.R;
import com.mengzhu.live.sdk.business.dto.AnchorInfoDto;
import com.mengzhu.live.sdk.business.dto.BaseDto;
import com.mengzhu.live.sdk.business.dto.MZOnlineUserListDto;
import com.mengzhu.live.sdk.business.dto.UserDto;
import com.mengzhu.live.sdk.business.dto.chat.ChatMessageDto;
import com.mengzhu.live.sdk.business.dto.chat.ChatTextDto;
import com.mengzhu.live.sdk.business.dto.chat.impl.ChatCmdDto;
import com.mengzhu.live.sdk.business.dto.chat.impl.ChatOnlineDto;
import com.mengzhu.live.sdk.business.dto.play.PlayInfoDto;
import com.mengzhu.live.sdk.business.presenter.IBasePresenterLinstener;
import com.mengzhu.live.sdk.business.presenter.chat.ChatMessageObserver;
import com.mengzhu.live.sdk.core.netwock.Page;
import com.mengzhu.live.sdk.core.restreaming.ws.StreamAVOption;
import com.mengzhu.live.sdk.ui.api.MZApiDataListener;
import com.mengzhu.live.sdk.ui.api.MZApiRequest;
import com.mengzhu.live.sdk.ui.chat.MZChatManager;
import com.mengzhu.live.sdk.ui.chat.MZChatMessagerListener;
import com.mengzhu.live.sdk.ui.push.MZPushManager;
import com.mengzhu.live.sdk.ui.push.PushStreamingListener;
import com.mengzhu.live.sdk.ui.push.StreamLiveCameraView;
import com.mengzhu.sdk.download.util.ScreenUtil;
import com.mzmedia.IPushClickListener;
import com.mzmedia.fragment.PlayerChatListFragment;
import com.mzmedia.presentation.dto.LiveConfigDto;
import com.mzmedia.presentation.presenter.stop.StopBroadcastPresenter;
import com.mzmedia.utils.ActivityUtils;
import com.mzmedia.utils.String_Utils;
import com.mzmedia.widgets.ChatOnlineView;
import com.mzmedia.widgets.CircleImageView;
import com.mzmedia.widgets.player.PlayerGoodsPushView;
import com.mzmedia.widgets.player.PlayerGoodsView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MZPlugFlowFragement extends Fragment implements View.OnClickListener {
    private StreamLiveCameraView streamLiveCameraView;
    private CircleImageView mPushIvAvatar; //头像
    private CircleImageView mPushOnlinePersonIv1, mPushOnlinePersonIv2, mPushOnlinePersonIv3; //在线人数头像
    private TextView mPushTvNickName; //昵称
    private TextView mPushTvPopular; //人气
    private TextView mPushTvOnline; //在线人数
    private ImageView mPushIvClose; //退出
    private ImageView mPushIvConfig; //配置
    private ImageView mPushIvShare; //分享
    public ImageView mPushIvCut; //切换镜头
    private RelativeLayout mPushRlLiveOver; //主播离开
    private RelativeLayout mPushRlSendChat; //发消息
    private TextView mPushLiveContent; //蒙层提示文字
    private TextView mPushTvHitChat; //发消息提示文字
    private TextView mCount; //倒计时文字
    private ImageView mIvStartClose; //倒计时可关闭
    private ChatOnlineView mPushChatOnlineView; //用户上线提示UI
    private ConstraintLayout mConstraintLayout; //倒计时布局
    private LinearLayout mConfigGroup; //配置按钮组
    private LinearLayout mConfigGroup_h; //横屏配置按钮组
    private ImageView mIvPushVoice; //静音
    private ImageView mIvPushFlash; //闪光灯
    private ImageView mIvPushMirror; //镜像
    private ImageView mIvPushBeauty; //美颜
    private ImageView mIvPushAllChat; //全体禁言
    private ImageView mIvPushVoice_h; //静音
    private ImageView mIvPushFlash_h; //闪光灯
    private ImageView mIvPushMirror_h; //镜像
    private ImageView mIvPushBeauty_h; //美颜
    private ImageView mIvPushAllChat_h; //全体禁言
    private LinearLayout mPushKborTimeLayout; //kb和直播时长布局
    private LinearLayout mPushOnlineCount; //在线人数布局
    private TextView mTvKb; //推流速度
    private TextView mTvLiveTime; // 直播时间
    private View mViewToolbar;
    private boolean isPushConfig;
    private boolean isBackStage; //是否切过后台
    private String mPushVideoUrl; //观看地址
    private UserDto mPushUserDto; //主播信息
    private MZPushManager mzPushManager;
    private StreamAVOption streamAVOption;
    private String ticketId,pushUrl;
    private PlayerChatListFragment mChatFragment;
    private IPushClickListener mIPushClickListener;
    private MZApiRequest mzApiRequestOnline; //在线人数信息
    private MZApiRequest mzApiRequestStopLive; //结束推流直播
    private MZApiRequest mzApiRequestAllChat; //全体禁言
    private MZApiRequest mzApiRequestBanChat; //单体禁言

    private PlayInfoDto mPlayInfoDto;
    private LiveConfigDto mLiveConfigDto;
    private List<String> personAvatars = new ArrayList<>(); //用户头像组
    private DisplayImageOptions avatarOptions;
    private Activity mActivity;
    private FragmentTransaction fragmentTransaction;
    private boolean isMirror,isFlash,isVoice,isBeauty = true,isAllChat;
    private int screen;
    public static MZPlugFlowFragement newInstance(String pushUrl, String ticket_id, int screen, PlayInfoDto dto, LiveConfigDto liveConfigDto) {

        Bundle args = new Bundle();
        args.putString("push_url",pushUrl);
        args.putString("ticketId",ticket_id);
        args.putInt("screen",screen);
        args.putSerializable("playinfoDto",dto);
        args.putSerializable("liveConfig",liveConfigDto);
        MZPlugFlowFragement fragment = new MZPlugFlowFragement();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pushUrl = getArguments().getString("push_url");
        ticketId = getArguments().getString("ticketId");
        screen = getArguments().getInt("screen");
        mPlayInfoDto = (PlayInfoDto) getArguments().getSerializable("playinfoDto");
        mLiveConfigDto = (LiveConfigDto) getArguments().getSerializable("liveConfig");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mz_fragment_plug_flow, container, false);
        streamLiveCameraView = rootView.findViewById(R.id.push_stream_previewView);
        mPushTvOnline = rootView.findViewById(R.id.push_tv_playerfragment_person);
        mPushIvClose = rootView.findViewById(R.id.push_iv_playerfragment_close);
        mPushIvAvatar = rootView.findViewById(R.id.push_civ_playerfragment_avatar);
        mPushOnlinePersonIv1 = rootView.findViewById(R.id.push_civ_activity_live_online_person1);
        mPushOnlinePersonIv2 = rootView.findViewById(R.id.push_civ_activity_live_online_person2);
        mPushOnlinePersonIv3 = rootView.findViewById(R.id.push_civ_activity_live_online_person3);
        mPushTvNickName = rootView.findViewById(R.id.push_tv_playerfragment_nickname);
        mPushTvPopular = rootView.findViewById(R.id.push_tv_playerfragment_popularity);
        mPushIvConfig = rootView.findViewById(R.id.push_iv_playerfragment_config);
        mPushIvShare = rootView.findViewById(R.id.push_iv_playerfragment_share);
        mPushIvCut = rootView.findViewById(R.id.push_iv_playerfragment_cut);
        mPushRlSendChat = rootView.findViewById(R.id.push_rl_playerfragment_send_chat);
        mPushRlLiveOver = rootView.findViewById(R.id.push_rl_activity_broadcast_live_over);
        mPushTvHitChat = rootView.findViewById(R.id.push_tv_playerfragment_chat);
        mPushLiveContent = rootView.findViewById(R.id.push_tv_activity_broadcast_live_over);
        mConstraintLayout = rootView.findViewById(R.id.layout_activity_broadcast_count);
        mCount = rootView.findViewById(R.id.tv_activity_broadcast_count);
        mIvStartClose = rootView.findViewById(R.id.iv_activity_live_broadcast_close_out);
        mPushChatOnlineView = rootView.findViewById(R.id.push_player_chat_list_online_view);
        mConfigGroup = rootView.findViewById(R.id.push_layout_live_config_group);
        mIvPushVoice = rootView.findViewById(R.id.push_iv_playerfragment_voice);
        mIvPushFlash = rootView.findViewById(R.id.push_iv_playerfragment_flash);
        mIvPushMirror = rootView.findViewById(R.id.push_iv_playerfragment_mirror);
        mIvPushBeauty = rootView.findViewById(R.id.push_iv_playerfragment_beauty);
        mIvPushAllChat = rootView.findViewById(R.id.push_iv_playerfragment_allchat);
        mConfigGroup_h = rootView.findViewById(R.id.push_layout_live_config_group_h);
        mIvPushVoice_h = rootView.findViewById(R.id.push_iv_playerfragment_voice_h);
        mIvPushFlash_h = rootView.findViewById(R.id.push_iv_playerfragment_flash_h);
        mIvPushMirror_h = rootView.findViewById(R.id.push_iv_playerfragment_mirror_h);
        mIvPushBeauty_h = rootView.findViewById(R.id.push_iv_playerfragment_beauty_h);
        mIvPushAllChat_h = rootView.findViewById(R.id.push_iv_playerfragment_allchat_h);
        mPushKborTimeLayout = rootView.findViewById(R.id.push_layout_live_kb_time_group);
        mPushOnlineCount = rootView.findViewById(R.id.push_ll_activity_live_broadcast_top_group);
        mViewToolbar = rootView.findViewById(R.id.push_v_live_toolbar);
        mTvKb = rootView.findViewById(R.id.tv_plug_fragment_kb);
        mTvLiveTime = rootView.findViewById(R.id.tv_plug_fragment_time);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        streamAVOption = new StreamAVOption();
        streamAVOption.streamUrl = pushUrl;
        mzPushManager = new MZPushManager(getActivity(),streamAVOption);
        mzPushManager.initPushLive(streamLiveCameraView);
        mzApiRequestOnline = new MZApiRequest();
        mzApiRequestStopLive = new MZApiRequest();
        mzApiRequestBanChat = new MZApiRequest();
        mzApiRequestAllChat = new MZApiRequest();
        if(screen==2){
            mPushIvConfig.setVisibility(View.GONE);
            mConfigGroup_h.setVisibility(View.VISIBLE);
            mViewToolbar.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.LEFT_OF,R.id.push_ll_activity_live_broadcast_top_group);
            params.topMargin = 12;
            mPushKborTimeLayout.setLayoutParams(params);
            mPushKborTimeLayout.invalidate();
        }

        initData();
        initListener();
        loadData();

        //头像加载失败默认处理
        avatarOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.icon_default_avatar)
                .showImageForEmptyUri(R.mipmap.icon_default_avatar)
                .showImageOnFail(R.mipmap.icon_default_avatar)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isBackStage){
            if(!mzPushManager.isStreaming()){
                mzPushManager.startStreaming();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        isBackStage = true;
        if(mzPushManager.isStreaming()){
            mzPushManager.stopStreaming();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mzPushManager.rePrepare();
    }

    private void initData(){
        //初始化图片加载
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));
        mChatFragment = new PlayerChatListFragment();
        fragmentTransaction = getChildFragmentManager().beginTransaction();
        int times;
        if(screen==2){
            times = mLiveConfigDto.getTime()+1;
        }else {
            times = mLiveConfigDto.getTime();
        }
        StartLiveCountDownTimer startLiveCountDownTimer = new StartLiveCountDownTimer(times * 1000,1000);
        startLiveCountDownTimer.start();
        //添加聊天fragment
        Bundle bundle = new Bundle();
        bundle.putBoolean(PlayerChatListFragment.PLAY_TYPE_KEY, true);
        bundle.putSerializable(PlayerChatListFragment.PLAY_INFO_KEY, mPlayInfoDto);
        mChatFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.push_layout_activity_live_broadcast_chat, mChatFragment).commitAllowingStateLoss();
        mPushTvNickName.setText("test");
        mPushTvPopular.setText("人气999");
        ImageLoader.getInstance().displayImage("https://avatars3.githubusercontent.com/u/13464940?s=60&v=4" + String_Utils.getPictureSizeAvatar(), mPushIvAvatar);

    }
    private void initListener(){
        mPushIvClose.setOnClickListener(this);
        mIvStartClose.setOnClickListener(this);
        mPushRlSendChat.setOnClickListener(this);
        mPushIvCut.setOnClickListener(this);
        mPushIvConfig.setOnClickListener(this);
        mIvPushVoice.setOnClickListener(this);
        mIvPushFlash.setOnClickListener(this);
        mIvPushMirror.setOnClickListener(this);
        mIvPushBeauty.setOnClickListener(this);
        mIvPushAllChat.setOnClickListener(this);
        mIvPushVoice_h.setOnClickListener(this);
        mIvPushFlash_h.setOnClickListener(this);
        mIvPushMirror_h.setOnClickListener(this);
        mIvPushBeauty_h.setOnClickListener(this);
        mIvPushAllChat_h.setOnClickListener(this);
        mPushIvShare.setOnClickListener(this);
        mPushTvOnline.setOnClickListener(this);
        mPushIvAvatar.setOnClickListener(this);
        //结束推流直播
        mzApiRequestStopLive.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String s, Object o) {
                if(mIPushClickListener!=null){
                    mIPushClickListener.onStopLive();
                }
                if(mzPushManager!=null){
                    mzPushManager.destroy();
                }
            }
            @Override
            public void errorResult(String s, int i, String s1) {}
        });
        //在线人数列表回调
        mzApiRequestOnline.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String apiType, Object dto) {
                List<MZOnlineUserListDto> mzOnlineUserListDto = (List<MZOnlineUserListDto>) dto;
                if(mIPushClickListener!=null){
                    mIPushClickListener.onOnlineNum(mzOnlineUserListDto);
                }
                if(personAvatars.size()>0){
                    personAvatars.clear();
                }
                mPushTvOnline.setText(mzOnlineUserListDto.size() + "");
                for (int i = 0; i < mzOnlineUserListDto.size(); i++) {
                    if (Long.parseLong(mzOnlineUserListDto.get(i).getUid()) < Long.parseLong("5000000000")) {
                        personAvatars.add(mzOnlineUserListDto.get(i).getAvatar());
                    }
                }
                initOnlineAvatar();

            }

            @Override
            public void errorResult(String apiType, int code, String msg) {
                Log.d("gm", "errorResult: API_TYPE_ONLINE_USER_LIST");
            }
        });
        //全体禁言回调
        mzApiRequestAllChat.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String s, Object o) {
                mIvPushAllChat.setImageResource(isAllChat?R.mipmap.mz_icon_allchat_on:R.mipmap.mz_icon_allchat_default);
                mIvPushAllChat_h.setImageResource(isAllChat?R.mipmap.mz_icon_allchat_on:R.mipmap.mz_icon_allchat_default);
            }

            @Override
            public void errorResult(String s, int i, String s1) {
                Log.e("wzh",s1+"ssss"+i);
            }
        });
        //单体禁言回调
        mzApiRequestBanChat.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String s, Object o) {
                if(mIPushClickListener!=null){
                    mIPushClickListener.onALLBanChat();
                }
            }

            @Override
            public void errorResult(String s, int i, String s1) {

            }
        });
        //各类型消息回调
        MZChatManager.getInstance(mActivity).registerListener(MZPlugFlowFragement.class.getSimpleName(), new MZChatMessager());
        //监听点击聊天用户头像
        mChatFragment.setOnChatAvatarClickListener(new ChatAvatarClick());
        mzPushManager.setStreamingListener(new MZPushStreamListener());
    }
    private void loadData(){

        mzApiRequestOnline.createRequest(getActivity(), MZApiRequest.API_TYPE_ONLINE_USER_LIST);
        mzApiRequestStopLive.createRequest(getActivity(),MZApiRequest.API_TYPE_LIVE_STOP);
        mzApiRequestBanChat.createRequest(getActivity(),MZApiRequest.API_TYPE_ROOM_FORBIDDEN);
        mzApiRequestAllChat.createRequest(getActivity(),MZApiRequest.API_TYPE_ROOM_ALLOWCHATALL);
        //请求在线人数api
        mzApiRequestOnline.startData(MZApiRequest.API_TYPE_ONLINE_USER_LIST, true, ticketId);
        isAllChat = mLiveConfigDto.getAllBanChat()==0;
        mzApiRequestAllChat.startData(MZApiRequest.API_TYPE_ROOM_ALLOWCHATALL,mPlayInfoDto.getTicket_id(),mPlayInfoDto.getChannel_id(),isAllChat?0:1);
    }

    private class MZPushStreamListener implements PushStreamingListener{
        //截屏
        @Override
        public void onScreenShotResult(Bitmap bitmap) {

        }
        //推流开始
        @Override
        public void onOpenConnectionResult(int result) {
            if(!isBackStage){ //防止切换前后台推流回调导致多次执行逻辑操作
                startTime();
                setConfig();
            }
            Toast.makeText(mActivity,"start="+result,Toast.LENGTH_SHORT).show();
        }
        //推流错误
        @Override
        public void onWriteError(int result) {
            Toast.makeText(mActivity,"error="+result,Toast.LENGTH_SHORT).show();
        }

        //推流停止
        @Override
        public void onCloseConnectionResult(int result) {
            if(!isBackStage){
                //请求结束推流直播
                mzApiRequestStopLive.startData(MZApiRequest.API_TYPE_LIVE_STOP,ticketId);
            }
            Toast.makeText(mActivity,"close="+result,Toast.LENGTH_SHORT).show();
        }
    }
    public void setPushClickListener(IPushClickListener listener){
        mIPushClickListener = listener;
    }

    /**
     * 开始直播倒计时
     */
    private class StartLiveCountDownTimer extends CountDownTimer {

        public StartLiveCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int mill = ((int) ((millisUntilFinished+100) / 1000));
            if (mill-1 >=0) {
                mCount.setText(mill + "");
            }
        }

        @Override
        public void onFinish() {
            if(!mzPushManager.isStreaming()){
                mzPushManager.startStreaming();
            }
            mConstraintLayout.setVisibility(View.GONE);
        }
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.push_iv_playerfragment_close) {
            if(mzPushManager.isStreaming()){
                isBackStage = false;
                mzPushManager.stopStreaming();
            }else {
                if(mIPushClickListener!=null){
                    mIPushClickListener.onStopLive();
                }
            }
        }
        if(v.getId() == R.id.iv_activity_live_broadcast_close_out){
            if(mIPushClickListener!=null){
                mIPushClickListener.onStopLive();
            }
        }
        if(v.getId() == R.id.push_rl_playerfragment_send_chat){
            ActivityUtils.startLandscapeTransActivity(getActivity());
        }
        if(v.getId() == R.id.push_iv_playerfragment_cut){
            mzPushManager.swapCamera();
            isFlash = false;
            mIvPushFlash.setImageResource(isFlash?R.mipmap.mz_icon_flash_on:R.mipmap.mz_icon_flash_default);
            mIvPushFlash_h.setImageResource(isFlash?R.mipmap.mz_icon_flash_on:R.mipmap.mz_icon_flash_default);
        }
        if(v.getId() == R.id.push_iv_playerfragment_config){
            isPushConfig = !isPushConfig;
            mPushIvConfig.setImageResource(isPushConfig?R.mipmap.mz_icon_config_off:R.mipmap.mz_icon_config);
            mConfigGroup.setVisibility(mConfigGroup.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
        }
        if(v.getId() == R.id.push_iv_playerfragment_allchat
                ||v.getId() == R.id.push_iv_playerfragment_allchat_h){
            isAllChat = !isAllChat;
            mzApiRequestAllChat.startData(MZApiRequest.API_TYPE_ROOM_ALLOWCHATALL,mPlayInfoDto.getTicket_id(),mPlayInfoDto.getChannel_id(),isAllChat?0:1);
        }
        if(v.getId() == R.id.push_iv_playerfragment_voice
        ||v.getId() == R.id.push_iv_playerfragment_voice_h){
            isVoice = !isVoice;
            mIvPushVoice.setImageResource(isVoice?R.mipmap.mz_icon_voice_on:R.mipmap.mz_icon_voice_default);
            mIvPushVoice_h.setImageResource(isVoice?R.mipmap.mz_icon_voice_on:R.mipmap.mz_icon_voice_default);
            if(mzPushManager!=null){
                mzPushManager.StartorStopAudio(isVoice);
            }
        }
        if(v.getId() == R.id.push_iv_playerfragment_flash
        ||v.getId() == R.id.push_iv_playerfragment_flash_h){
            isFlash = !isFlash;
            mIvPushFlash.setImageResource(isFlash?R.mipmap.mz_icon_flash_on:R.mipmap.mz_icon_flash_default);
            mIvPushFlash_h.setImageResource(isFlash?R.mipmap.mz_icon_flash_on:R.mipmap.mz_icon_flash_default);
            if(mzPushManager!=null){
                mzPushManager.toggleFlashLight();
            }
        }
        if(v.getId() == R.id.push_iv_playerfragment_mirror
        ||v.getId() == R.id.push_iv_playerfragment_mirror_h){
            isMirror = !isMirror;
            mIvPushMirror.setImageResource(isMirror?R.mipmap.mz_icon_mirror_on:R.mipmap.mz_icon_mirror_default);
            mIvPushMirror_h.setImageResource(isMirror?R.mipmap.mz_icon_mirror_on:R.mipmap.mz_icon_mirror_default);
            if(isMirror){
                if(mzPushManager!=null){
                    mzPushManager.setMirror(true,true,true);
                }
            }else {
                if(mzPushManager!=null){
                    mzPushManager.setMirror(true,false,false);
                }
            }
        }
        if(v.getId() == R.id.push_iv_playerfragment_beauty
        ||v.getId() == R.id.push_iv_playerfragment_beauty_h){
            isBeauty = !isBeauty;
            mIvPushBeauty.setImageResource(isBeauty?R.mipmap.mz_icon_beauty_on:R.mipmap.mz_icon_beauty_default);
            mIvPushBeauty_h.setImageResource(isBeauty?R.mipmap.mz_icon_beauty_on:R.mipmap.mz_icon_beauty_default);
            if(mzPushManager!=null){
                mzPushManager.cutFilter();
            }
        }
        if(v.getId() == R.id.push_iv_playerfragment_share){
            if(mIPushClickListener!=null){
                mIPushClickListener.onShare(mPlayInfoDto);
            }
        }
        if(v.getId() == R.id.push_tv_playerfragment_person){
            //请求在线人数api
            mzApiRequestOnline.startData(MZApiRequest.API_TYPE_ONLINE_USER_LIST, true, ticketId);
        }
        if(v.getId() == R.id.push_civ_playerfragment_avatar){
            if(mIPushClickListener!=null){
                mIPushClickListener.onLiveAvatar();
            }
        }
    }

    /**
     * 点击聊天用户头像回调
     */
    private class ChatAvatarClick implements PlayerChatListFragment.OnChatAvatarClickListener{
        @Override
        public void onChatAvatarClick(ChatTextDto dto) {
            if (null != mIPushClickListener) {
                mIPushClickListener.onChatAvatar(dto);
            }
            setBanChat(mPlayInfoDto.getId(),mPlayInfoDto.getTicket_id(),"1");
        }
    }
    private String mTotalPerson;
    private long mCountTime;
    private Timer timer;
    /**
     * 直播计时
     */
    private void startTime(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCountTime +=1000;
                        mTvKb.setText((mzPushManager.getAVSpeed()/1024)*8+"Kb/s");
                        mTvLiveTime.setText(String_Utils.converLongTimeToStr(mCountTime));
                    }
                });
            }
        },0,1000);
    }
    /**
     * 消息监听回调
     */
    int current = 0;
    private class  MZChatMessager implements MZChatMessagerListener{
        @Override
        public void dataResult(Object o, Page page, int i) {}
        @Override
        public void errorResult(int i, String s) {}
        @Override
        public void monitorInformResult(String s, Object o) {
            ChatMessageDto mChatMessage = (ChatMessageDto) o;
            ChatTextDto mChatText = mChatMessage.getText();
            BaseDto mBase = mChatText.getBaseDto();
            switch (s) {
                case ChatMessageObserver.ONLINE_TYPE: {//上下线消息
                    ChatOnlineDto mChatOnline = (ChatOnlineDto) mBase;
//                    mTotalPerson = mChatOnline.getConcurrent_user();
                    mPushChatOnlineView.startOnline(getActivity(), mChatMessage);
                    current++;
                    try {
                        mPushTvOnline.setText(String_Utils.convert2W0_0(current + ""));
                        if (Long.parseLong(mChatMessage.getText().getUser_id()) < Long.parseLong("5000000000")) {
                            personAvatars.add(mChatText.getAvatar());
                        }
                        initOnlineAvatar();
                    } catch (Exception e) {
                    }
                    break;
                }
                case ChatMessageObserver.OFFLINE_TYPE:
                    ChatOnlineDto mChatOnline = (ChatOnlineDto) mBase;
//                    mTotalPerson = mChatOnline.getConcurrent_user();
                    current--;
                    try {
                        mPushTvOnline.setText(String_Utils.convert2W0_0(current + ""));
                        personAvatars.remove(mChatText.getAvatar());
                        initOnlineAvatar();
                    } catch (Exception e) {
                    }
                    break;
                case ChatMessageObserver.CMD_TYPE:
                    ChatCmdDto mChatCmd = (ChatCmdDto) mBase;
                    switch (mChatCmd.getType()) {
                        case ChatCmdDto.DISABLE_CHAT:
//                            //禁言消息
//                            if (mPlayInfoDto.getChat_uid().equals(((ChatCmdDto) mBase).getUser_id())) {
//                                mPlayInfoDto.setUser_status(3);
//                                initBanChat(true);
//                            }
                            break;
                        case ChatCmdDto.PERMIT_CHAT:
                            //取消禁言消息
//                            if (mPlayInfoDto.getChat_uid().equals(((ChatCmdDto) mBase).getUser_id())) {
//                                mPlayInfoDto.setUser_status(1);
//                                initBanChat(false);
//                            }
                            break;
                        case ChatCmdDto.LIVE_OVER:
                            //主播离开
//                            mRlLiveOver.setVisibility(View.VISIBLE);
//                            mLiveContent.setText("主播暂时离开，\n稍等一下马上回来");
//                            mzVideoView.pause();
                            break;
                        case "*liveEnd":
                            //直播结束
//                            mRlLiveOver.setVisibility(View.VISIBLE);
//                            mLiveContent.setText("直播已结束");
//                            mzVideoView.stopPlayback();
                            break;
                    }
                    break;
            }
        }
        @Override
        public void monitorInformErrer(String s, int i, String s1) {}
    }
    /**
     *初始化右侧观看用户的头像
     */
    private void initOnlineAvatar() {
        if (personAvatars.size() >= 3) {
            mPushOnlinePersonIv1.setVisibility(View.VISIBLE);
            mPushOnlinePersonIv2.setVisibility(View.VISIBLE);
            mPushOnlinePersonIv3.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(personAvatars.get(personAvatars.size() - 1), mPushOnlinePersonIv1, avatarOptions);
            ImageLoader.getInstance().displayImage(personAvatars.get(personAvatars.size() - 2), mPushOnlinePersonIv2, avatarOptions);
            ImageLoader.getInstance().displayImage(personAvatars.get(personAvatars.size() - 3), mPushOnlinePersonIv3, avatarOptions);
        } else if (personAvatars.size() == 2) {
            mPushOnlinePersonIv1.setVisibility(View.VISIBLE);
            mPushOnlinePersonIv2.setVisibility(View.VISIBLE);
            mPushOnlinePersonIv3.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage(personAvatars.get(personAvatars.size() - 1), mPushOnlinePersonIv1, avatarOptions);
            ImageLoader.getInstance().displayImage(personAvatars.get(personAvatars.size() - 2), mPushOnlinePersonIv2, avatarOptions);

        } else if (personAvatars.size() == 1) {
            mPushOnlinePersonIv1.setVisibility(View.VISIBLE);
            mPushOnlinePersonIv2.setVisibility(View.GONE);
            mPushOnlinePersonIv3.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage(personAvatars.get(personAvatars.size() - 1), mPushOnlinePersonIv1, avatarOptions);
        }
    }

    /**
     * 单体禁言
     * @param uId 用户id
     * @param ticketId 直播活动id
     * @param type 0解除 1禁言
     */
    public void setBanChat(String uId,String ticketId,String type){
        if(mzApiRequestBanChat!=null){
            mzApiRequestBanChat.startData(MZApiRequest.API_TYPE_ROOM_FORBIDDEN,uId,ticketId,type);
        }
    }
    private void setConfig(){
        if(mLiveConfigDto.isCbbeauty()){
            isBeauty = false;
            mIvPushBeauty.setImageResource(isBeauty?R.mipmap.mz_icon_beauty_on:R.mipmap.mz_icon_beauty_default);
            mIvPushBeauty_h.setImageResource(isBeauty?R.mipmap.mz_icon_beauty_on:R.mipmap.mz_icon_beauty_default);
            mzPushManager.cutFilter();
        }
        if(mLiveConfigDto.isCbAudio()){
            isVoice = true;
            mIvPushVoice.setImageResource(isVoice?R.mipmap.mz_icon_voice_on:R.mipmap.mz_icon_voice_default);
            mIvPushVoice_h.setImageResource(isVoice?R.mipmap.mz_icon_voice_on:R.mipmap.mz_icon_voice_default);
            mzPushManager.StartorStopAudio(isVoice);
        }
        if(mLiveConfigDto.isCblater()){
            mzPushManager.swapCamera();
        }
        mzPushManager.reSetVideoBitrate(mLiveConfigDto.getBitrate());
        if(mLiveConfigDto.getFps()<15){
            mzPushManager.reSetVideoFPS(15);
        }else if(mLiveConfigDto.getFps()>30){
            mzPushManager.reSetVideoFPS(30);
        }else {
            mzPushManager.reSetVideoFPS(mLiveConfigDto.getFps());
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
        if(mActivity!=null){
            MZChatManager.getInstance(mActivity).destroyChat();
        }
    }
}
