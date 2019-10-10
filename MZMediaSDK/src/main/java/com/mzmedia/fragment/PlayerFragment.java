package com.mzmedia.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mengzhu.live.sdk.R;
import com.mengzhu.live.sdk.business.dto.MZOnlineUserListDto;
import com.mengzhu.live.sdk.business.dto.UserDto;
import com.mengzhu.live.sdk.business.dto.play.PlayInfoDto;
import com.mengzhu.live.sdk.business.presenter.MyUserInfoPresenter;
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
import com.mzmedia.widgets.CircleImageView;
import com.mzmedia.widgets.LoveLayout;
import com.mzmedia.widgets.player.PlayerGoodsView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

public class PlayerFragment extends Fragment implements View.OnClickListener, SDKInitListener, MZApiDataListener {

    private static final String VIDEO_URL = "videoUrl";
    private static final String USER_INFO = "userDto";
    private static final String TICKET_ID = "ticket_id";
    private MZVideoView mzVideoView;
    private CircleImageView mIvAvatar; //头像
    private CircleImageView mOnlinePersonIv1,mOnlinePersonIv2,mOnlinePersonIv3; //在线人数头像
    private TextView mTvNickName; //昵称
    private TextView mTvPopular; //人气
    private TextView mTvAttention; //关注
    private TextView mTvOnline; //在线人数
    private ImageView mIvClose; //退出
    private ImageView mIvConfig; //配置
    private ImageView mIvShare; //分享
    private ImageView mIvLike; //点赞
    private ImageView mIvReport; //举报
    private RelativeLayout mRlSendChat; //发消息
    private TextView mTvHitChat; //发消息提示文字
    private LoveLayout mLoveLayout; //飘心
    private boolean isConfig;
    private PlayerGoodsView mPlayerGoodsLayout;
    private ImageView mGoodsIv; //店铺
    private String mVideoUrl; //观看地址
    private UserDto mUserDto; //主播信息
    private Activity mActivity;
    private DisplayImageOptions avatarOptions;
    private MZApiRequest mzApiRequest;
    private MZApiRequest mzApiRequestGoods;
    private MZApiRequest mzApiRequestOnline;
    private String ticketId;
    private List<String> personAvatars = new ArrayList<>();
    public PlayerFragment() {
        // Required empty public constructor
    }

    public static PlayerFragment newInstance(String videoUrl, UserDto userDto, String ticketId) {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putString(VIDEO_URL, videoUrl);
        args.putSerializable(USER_INFO, userDto);
        args.putString(TICKET_ID, ticketId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mVideoUrl = getArguments().getString(VIDEO_URL);
            mUserDto = (UserDto) getArguments().getSerializable(USER_INFO);
            ticketId = getArguments().getString(TICKET_ID);
        }

        MyUserInfoPresenter.getInstance().saveUserinfo(mUserDto);
        MZSDKInitManager.getInstance().initLive("cae0e5428b5d9f06c077c6784660c6d3_156136291484092_1566964162");
        MZSDKInitManager.getInstance().registerInitListener(this);
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
        mTvHitChat = rootView.findViewById(R.id.tv_playerfragment_chat);
        mPlayerGoodsLayout = rootView.findViewById(R.id.live_broadcast_goods_view);
        mGoodsIv = rootView.findViewById(R.id.iv_player_fragment_goods);

        PlayerChatListFragment mChatFragment = new PlayerChatListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(PlayerChatListFragment.PLAY_TYPE_KEY, true);
//        bundle.putSerializable(PlayerChatListFragment.PLAY_INFO_KEY, mStartBroadcastInfoDto);
        mChatFragment.setArguments(bundle);
        getChildFragmentManager().beginTransaction().replace(R.id.layout_activity_live_broadcast_chat, mChatFragment).commitAllowingStateLoss();

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

        mzVideoView.setAspectRatio(IRenderView.AR_ASPECT_FILL_PARENT);
        mzVideoView.setVideoPath(mVideoUrl);
        avatarOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.icon_default_avatar)
                .showImageForEmptyUri(R.mipmap.icon_default_avatar)
                .showImageOnFail(R.mipmap.icon_default_avatar)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        initView();
        initListener();
        loadData();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));
        ImageLoader.getInstance().displayImage(mUserDto.getAvatar() + String_Utils.getPictureSizeAvatar(), mIvAvatar, avatarOptions);
        mTvPopular.setText("人气" + String_Utils.convert2W0_0(100) + "人");
        mTvNickName.setText(mUserDto.getNickname());
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
    }

    private void loadData() {
        mzApiRequest = new MZApiRequest();
        mzApiRequestOnline = new MZApiRequest();
        mzApiRequestGoods = new MZApiRequest();
        mzApiRequest.createRequest(mActivity, MZApiRequest.API_TYPE_PLAY_INFO);
        mzApiRequestOnline.createRequest(mActivity, MZApiRequest.API_TYPE_ONLINE_USER_LIST);
        mzApiRequestGoods.createRequest(mActivity, MZApiRequest.API_TYPE_GOODS_LIST);
        mzApiRequest.setResultListener(this);
        //获取观看信息请求
        mzApiRequest.startData(MZApiRequest.API_TYPE_PLAY_INFO, ticketId);
        //商店
        mzApiRequestGoods.startData(MZApiRequest.API_TYPE_GOODS_LIST, true, ticketId);
        mzApiRequestGoods.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String s, Object o) {
                //循环商品
                GoodsCountDown goodsCountDown = new GoodsCountDown(10 * 5000, 5000);
                goodsCountDown.start();
//                MZGoodsListExternalDto mzGoodsListExternalDto = new MZGoodsListExternalDto();
//                if (mPlayerGoodsLayout!=null) {
//                    mPlayerGoodsLayout.setGoodsData(mzGoodsListExternalDto.getList().get(0));
//                }
            }

            @Override
            public void errorResult(String s, int i, String s1) {

            }
        });
        //在线人数列表回调
        mzApiRequestOnline.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String apiType, Object dto) {
                List<MZOnlineUserListDto> mzOnlineUserListDto = (List<MZOnlineUserListDto>) dto;
                mTvOnline.setText(mzOnlineUserListDto.size()+"");
                for(int i=0;i<mzOnlineUserListDto.size();i++){
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
        mzApiRequestOnline.startData(MZApiRequest.API_TYPE_ONLINE_USER_LIST,true,ticketId);
    }

    private IPlayerClickListener mListener;

    public void setIPlayerClickListener(IPlayerClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.civ_playerfragment_avatar) {
            if (mListener != null) {
                mListener.onAvatarClick();
            }

        }
        if (view.getId() == R.id.tv_playerfragment_attention) {
            if (mListener != null) {
                mListener.onAttentionClick();
            }

        }
        if(view.getId() == R.id.tv_playerfragment_person){
            if(mListener!=null){
                mListener.onOnlineClick();
            }
        }
        if (view.getId() == R.id.iv_playerfragment_close) {
            if (mListener != null) {
                mListener.onCloseClick();
            }
        }
        if (view.getId() == R.id.iv_playerfragment_config) {
            isConfig = !isConfig;
            mIvConfig.setImageResource(isConfig ? R.mipmap.gm_icon_config_true : R.mipmap.gm_icon_config);
            mIvReport.setVisibility(isConfig ? View.VISIBLE : View.GONE);
        }
        if (view.getId() == R.id.iv_playerfragment_report) {
            if (mListener != null) {
                mListener.onReportClick();
            }
        }
        if (view.getId() == R.id.iv_playerfragment_share) {
            if (mListener != null) {
                mListener.onShareClick();
            }
        }
        if (view.getId() == R.id.iv_playerfragment_zan) {
            if (mListener != null) {
                mListener.onLikeClick();
            }
            mLoveLayout.addLoveView();
        }
        if (view.getId() == R.id.rl_playerfragment_send_chat) {
//             if (StaticStateDto.getInstance().isBanned()) {
//                 mDialogManager = new DialogManager(LiveBroadcastActivity.this);
//                 Toast.makeText(this, R.string.mz_banned_to_post, Toast.LENGTH_SHORT).show();
//                 return;
//             }
//             isChat = true;
            ActivityUtils.startLandscapeTransActivity(mActivity);
        }
        if (view.getId() == R.id.iv_player_fragment_goods) {
            showGoodsDialog();
        }
    }

    @Override
    public void dataResult(int i) {
        mzVideoView.start();
        Toast.makeText(getActivity(), "初始化成功 ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void errorResult(int i, String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取观看信息的回调
     */
    @Override
    public void dataResult(String s, Object o) {
        PlayInfoDto mPlayInfoDto = (PlayInfoDto) o;
        mVideoUrl = mPlayInfoDto.getVideo().getHttp_url();
        mzVideoView.setVideoPath(mVideoUrl);
        MZChatManager.getInstance(mActivity).registerListener("max", new MZChatMessagerListener() {
            @Override
            public void dataResult(Object o, Page page, int i) {
                Log.e("max", "dataResult: " + o);
            }

            @Override
            public void errorResult(int i, String s) {
                Log.e("max", "dataResult: " + s);
            }

            @Override
            public void monitorInformResult(String s, Object o) {
                Log.e("max", "dataResult: " + o);

            }

            @Override
            public void monitorInformErrer(String s, int i, String s1) {
                Log.e("max", "dataResult: " + s);

            }
        });
        MZChatManager.getInstance(mActivity).startHistory((PlayInfoDto) o);
//        MZChatManager.getInstance(mActivity).setPlayinfo((PlayInfoDto) o);
    }

    @Override
    public void errorResult(String s, int i, String s1) {
        Log.e("max", "errorResult: "+s);
    }

    //初始化右侧观看用户的头像
    private void initOnlineAvatar() {
        if (personAvatars.size() >= 3) {
            mOnlinePersonIv1.setVisibility(View.VISIBLE);
            mOnlinePersonIv2.setVisibility(View.VISIBLE);
            mOnlinePersonIv3.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(personAvatars.get(personAvatars.size() - 1) + String_Utils.getPictureSizeAvatar(), mOnlinePersonIv1, avatarOptions);
            ImageLoader.getInstance().displayImage(personAvatars.get(personAvatars.size() - 2) + String_Utils.getPictureSizeAvatar(), mOnlinePersonIv2, avatarOptions);
            ImageLoader.getInstance().displayImage(personAvatars.get(personAvatars.size() - 3) + String_Utils.getPictureSizeAvatar(), mOnlinePersonIv3, avatarOptions);
        } else if (personAvatars.size() == 2) {
            mOnlinePersonIv1.setVisibility(View.VISIBLE);
            mOnlinePersonIv2.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(personAvatars.get(personAvatars.size() - 1) + String_Utils.getPictureSizeAvatar(), mOnlinePersonIv1, avatarOptions);
            ImageLoader.getInstance().displayImage(personAvatars.get(personAvatars.size() - 2) + String_Utils.getPictureSizeAvatar(), mOnlinePersonIv2, avatarOptions);

        } else if (personAvatars.size() == 1) {
            mOnlinePersonIv1.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(personAvatars.get(personAvatars.size() - 1) + String_Utils.getPictureSizeAvatar(), mOnlinePersonIv1, avatarOptions);
        }
    }
    private class GoodsCountDown extends CountDownTimer {

        public GoodsCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            Log.e("max", "onTick: " + l);
            if (mPlayerGoodsLayout != null) {
                mPlayerGoodsLayout.startPlayerGoods();
            }
        }

        @Override
        public void onFinish() {

        }
    }


    /**
     * 底面商店
     */
    public void showGoodsDialog() {
        try {
//         GoodsListFragment   goodsListFragment = (GoodsListFragment) mActivity.getSupportFragmentManager().findFragmentByTag("VOTEDIALOGFRAGMENT");
//            if (null == goodsListFragment) {
            GoodsListFragment goodsListFragment = GoodsListFragment.newInstance(1);
//            }
            goodsListFragment.show(getChildFragmentManager(), "GOODSFRAGMENT");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
