package com.mzmedia.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.mengzhu.live.sdk.business.dto.chat.ChatMessageDto;
import com.mengzhu.live.sdk.business.dto.chat.ChatTextDto;
import com.mengzhu.live.sdk.business.dto.chat.impl.ChatCompleteDto;
import com.mengzhu.live.sdk.business.dto.play.PlayInfoDto;
import com.mengzhu.live.sdk.business.presenter.chat.ChatMessageObserver;
import com.mengzhu.live.sdk.business.presenter.chat.ChatPresenter;
import com.mengzhu.live.sdk.ui.chat.MZChatManager;
import com.mengzhu.live.sdk.ui.chat.MZChatMessagerListener;
import com.mengzhu.sdk.R;
import com.mzmedia.adapter.base.CommonAdapterType;
import com.mzmedia.adapter.chat.PlayChatRedPacketWrap;
import com.mzmedia.adapter.chat.PlayerChatGiftLeftWrap;
import com.mzmedia.adapter.chat.PlayerChatGiftRightWrap;
import com.mzmedia.adapter.chat.PlayerChatLeltWrap;
import com.mzmedia.adapter.chat.PlayerChatNoticeWrap;
import com.mzmedia.adapter.chat.PlayerChatRightWrap;
import com.mzmedia.widgets.PlayerChatLayout;
import com.mzmedia.widgets.WithScrollChangeScrollView;

import java.util.ArrayList;
import java.util.List;

import tv.mengzhu.core.module.model.dto.BaseItemDto;
import tv.mengzhu.core.wrap.netwock.Page;

/**
 * Created by DELL on 2016/7/8.
 */
public class PlayerChatListFragment extends BaseFragement implements MZChatMessagerListener {
    private Activity mActivity;
    private PlayerChatLayout mListView;
    private CommonAdapterType mAdapter;
    private PlayerChatLeltWrap mLeltWrap;
    private PlayerChatRightWrap mRightWrap;
    private PlayerChatNoticeWrap mNoticeWrap;
    private PlayerChatGiftLeftWrap mGiftLeftWrap;
    private PlayerChatGiftRightWrap mGiftRightWrap;
    private PlayChatRedPacketWrap mRedPacketWarp;
    //    private ChatPresenter mChatPresenter;
    private WithScrollChangeScrollView mPayerScroll;
    public static final String PLAY_TYPE_KEY = "play_type_key";
    public static final String UI_TYPE_KEY = "UI_type_key";
    public static final String PLAY_INFO_KEY = "PLAY_INFO_KEY";
    private PlayInfoDto mPlayInfoDto;
    private boolean isPush = false;
    private boolean isHalfPlayer = false;
    private boolean isVoiceChat = false;
    public static final String AUDIO_VOICE_CHAT = "audio_voice_chat";
    private ImageView mVoiceChatTopBg;
    private RelativeLayout player_chat_list_reward_layout;
    private RelativeLayout play_chat_list_gift_layout;
    private LinearLayout mFillScreenGiftLayout;
    private boolean isNoMore = false;

    List<BaseItemDto> dataList = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    public LinearLayout getmFillScreenGiftLayout() {
        return mFillScreenGiftLayout;
    }

    @Override
    public void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            isPush = bundle.getBoolean(PLAY_TYPE_KEY, false);
            isHalfPlayer = bundle.getBoolean(UI_TYPE_KEY, false);
            mPlayInfoDto = (PlayInfoDto) getArguments().getSerializable(PLAY_INFO_KEY);
            isVoiceChat = getArguments().getBoolean(AUDIO_VOICE_CHAT, false);
            MZChatManager.getInstance(mActivity).setPlayinfo(mPlayInfoDto);
        }

        mListView = (PlayerChatLayout) findViewById(R.id.mz_player_chat_list);
        mVoiceChatTopBg = (ImageView) findViewById(R.id.voice_chat_top_bg);
        mPayerScroll = (WithScrollChangeScrollView) findViewById(R.id.mz_payer_scroll);
        mFillScreenGiftLayout = (LinearLayout) findViewById(R.id.fill_screen_gift_layout);
        if (isPush) {
            mFillScreenGiftLayout.setVisibility(View.GONE);
        } else {
            mFillScreenGiftLayout.setVisibility(View.VISIBLE);
        }
        mAdapter = new CommonAdapterType(getActivity());
        mLeltWrap = new PlayerChatLeltWrap(getActivity());
        mRightWrap = new PlayerChatRightWrap(getActivity());
        mNoticeWrap = new PlayerChatNoticeWrap(getActivity());
        mGiftLeftWrap = new PlayerChatGiftLeftWrap(getActivity());
        mGiftRightWrap = new PlayerChatGiftRightWrap(getActivity());
        mRedPacketWarp = new PlayChatRedPacketWrap(getActivity());
        mRightWrap.setIsLandscape(isPush);
        mRightWrap.setHalfPlayer(isHalfPlayer);
        mLeltWrap.setIsLandscape(isPush);
        mLeltWrap.setHalfPlayer(isHalfPlayer);
        mGiftLeftWrap.setIsLandscape(isPush);
        mGiftLeftWrap.setHalfPlayer(isHalfPlayer);
        mGiftRightWrap.setIsLandscape(isPush);
        mGiftRightWrap.setHalfPlayer(isHalfPlayer);
        mRedPacketWarp.setHalfPlayer(isHalfPlayer);
        mAdapter.addViewObtains(ChatMessageDto.CHAT_LELT_WRAP, mLeltWrap);
        mAdapter.addViewObtains(ChatMessageDto.CHAT_RRIGHT_WRAP, mRightWrap);
        mAdapter.addViewObtains(ChatMessageDto.CHAT_NOTICE_WRAP, mNoticeWrap);
        mAdapter.addViewObtains(ChatMessageDto.CHAT_GIFT_LEFT, mGiftLeftWrap);
        mAdapter.addViewObtains(ChatMessageDto.CHAT_GIFT_RIGHT, mGiftRightWrap);
        mAdapter.addViewObtains(ChatMessageDto.CHAT_CARD_RED_PACHET, mRedPacketWarp);
        mListView.setAdapter(mAdapter);
        mListView.setAddViewListener(new MyAddViewListener());
        if (isVoiceChat) {
            mVoiceChatTopBg.setVisibility(View.VISIBLE);
        }
        mListView.setConstant(true);
        mPayerScroll.setOnScrollChangeListener(new WithScrollChangeScrollView.OnScrollChangeListener() {
            @Override
            public void onScroll(int scrollY) {
                if (scrollY == 0) {
//                    if (!isNoMore) {
                    MZChatManager.getInstance(mActivity).nextHistory("20");
//                    }
                }
            }
        });
    }

    @Override
    public void dataResult(Object o, Page page, int i) {
        List<BaseItemDto> list = (List<BaseItemDto>) o;
        for (int j = 0; j < list.size(); j++) {
            ChatMessageDto dto = (ChatMessageDto) list.get(j);
            if (dto.isHistory()) {
                isNoMore = list.size() < 20;
                dataList.addAll(0, list);
                mAdapter.setList(dataList);
                mListView.inform();
                if (dataList.size() <= 21) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            mPayerScroll.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }
                break;
            } else {
                if (mAdapter.getList() == null || mAdapter.getList().isEmpty()) {
                    mAdapter.setList(dataList);
                }
                dataList.add(list.get(j));
                mListView.addItemView(list.get(j));
            }
        }
    }

    @Override
    public void errorResult(int i, String s) {

    }

    @Override
    public void monitorInformResult(String s, Object o) {

    }

    @Override
    public void monitorInformError(String s, int i, String s1) {

    }

    @Override
    public void monitorInformResult(String type, Object obj, Object extend) {

    }

    class MyAddViewListener implements PlayerChatLayout.AddViewListener {

        @Override
        public void onAddView() {
//            int offset = inner.getMeasuredHeight() - scroll.getHeight();
//            if (offset < 0) {
//                offset = 0;
//            }
//            scroll.scrollTo(0, offset);
//            mPayerScroll.scrollTo(0, 1000);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPayerScroll.fullScroll(ScrollView.FOCUS_DOWN);
                }
            }, 100);

        }
    }

    class PlaymMonitorCallback implements ChatMessageObserver.InformMonitorCallback {

        @Override
        public void monitorInformResult(String type, Object obj) {
            if (type.equals(ChatMessageObserver.COMPLETE)) {

                ChatMessageDto baseDto = (ChatMessageDto) obj;
                if (baseDto.getText().getBaseDto() instanceof ChatCompleteDto) {
                    ChatCompleteDto dto = (ChatCompleteDto) baseDto.getText().getBaseDto();
                }
            }
        }

        @Override
        public void monitorInformError(String type, int state, String msg) {

        }
    }

    @Override
    public void initData() {
//        mChatPresenter = ChatPresenter.getInstance(getActivity());
//        mChatPresenter.setIsLandscape(isPush);
//        mChatPresenter.setRoomid("12345");
//        mChatPresenter.registerCallback(PlayerChatListFragment.class.getSimpleName());
//        if (isPush) {
//            mChatPresenter.initPresenter(getActivity());
//        }
//        mChatPresenter.registerListener(this);
//        mChatPresenter.registerListener(PlayerChatListFragment.class.getSimpleName(), this);
    }

    @Override
    public void setListener() {
        // 注册只看主播切换监听事件
        MZChatManager.getInstance(mActivity).registerChangeIsOnlyAnchorListener(PlayerChatListFragment.class.getSimpleName(), changeISOnlyAnchorListener);

        ChatMessageObserver.getInstance().register(new PlaymMonitorCallback(), PlayerChatListFragment.class.getSimpleName());
        MZChatManager.getInstance(mActivity).registerListener(PlayerChatListFragment.class.getSimpleName(), this);
        mLeltWrap.setOnChatIconClickListener(new PlayerChatLeltWrap.OnChatIconClickListener() {
            @Override
            public void onChatIconClick(ChatTextDto dto) {
                if (mOnChatAvatarClickListener != null) {
                    mOnChatAvatarClickListener.onChatAvatarClick(dto);
                }
            }
        });
        mRightWrap.setOnChatIconClickListener(new PlayerChatLeltWrap.OnChatIconClickListener() {
            @Override
            public void onChatIconClick(ChatTextDto dto) {
                if (mOnChatAvatarClickListener != null) {
                    mOnChatAvatarClickListener.onChatAvatarClick(dto);
                }
            }
        });
        mGiftLeftWrap.setOnChatIconClickListener(new PlayerChatLeltWrap.OnChatIconClickListener() {
            @Override
            public void onChatIconClick(ChatTextDto dto) {
                if (mOnChatAvatarClickListener != null) {
                    mOnChatAvatarClickListener.onChatAvatarClick(dto);
                }
            }
        });
        mGiftRightWrap.setOnChatIconClickListener(new PlayerChatLeltWrap.OnChatIconClickListener() {
            @Override
            public void onChatIconClick(ChatTextDto dto) {
                if (mOnChatAvatarClickListener != null) {
                    mOnChatAvatarClickListener.onChatAvatarClick(dto);
                }
            }
        });
        mRedPacketWarp.setOnChatIconClickListener(new PlayerChatLeltWrap.OnChatIconClickListener() {
            @Override
            public void onChatIconClick(ChatTextDto dto) {
                if (mOnChatAvatarClickListener != null) {
                    mOnChatAvatarClickListener.onChatAvatarClick(dto);
                }
            }
        });

        mRedPacketWarp.setOnRedPacketClickListener(onRedPacketClickListener);

    }

    ChatPresenter.OnChangeISOnlyAnchorListener changeISOnlyAnchorListener = new ChatPresenter.OnChangeISOnlyAnchorListener() {
        @Override
        public void changeIsOnlyAnchor(boolean isOnlyAnchor, List<ChatMessageDto> messageList) {
//            isOnlyAnchor 是否是只看主播 messageList切换后的列表数据
            dataList.clear();
            dataList.addAll(messageList);
            mAdapter.setList(dataList);
            mListView.inform();
        }
    };

    private OnChatAvatarClickListener mOnChatAvatarClickListener;

    public interface OnChatAvatarClickListener {
        void onChatAvatarClick(ChatTextDto dto);
    }

    public void setOnChatAvatarClickListener(OnChatAvatarClickListener listener) {
        mOnChatAvatarClickListener = listener;
    }

    public interface OnRedPacketClickListener{
        void onRedPacketClick(ChatTextDto dto);
    }

    private OnRedPacketClickListener onRedPacketClickListener;

    public void setOnRedPacketClickListener(OnRedPacketClickListener onRedPacketClickListener) {
        this.onRedPacketClickListener = onRedPacketClickListener;
    }

    @Override
    public void loadData() {
        if (isPush) {
            if (mPlayInfoDto != null) {
//                mChatPresenter.setChatConfDto(mPlayInfoDto);
//                mChatPresenter.onExecutes();
//                mChatPresenter.destroySocket();
//                mChatPresenter.connectSocket(mPlayInfoDto.getMsg_config().getMsg_srv() + "?token=" + mPlayInfoDto.getMsg_config().getMsg_token() + "&app=smb");
//                mChatPresenter.initAffiche(true);
                MZChatManager.getInstance(mActivity).startHistory(mPlayInfoDto);
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.mz_player_chat_list_layout;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            mChatPresenter.registerListener(PlayerChatListFragment.class.getSimpleName(), this);
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            mChatPresenter.removeListener(PlayerChatListFragment.class.getSimpleName());
//        }
    }

    @Override
    public void onDestroy() {
//        mChatPresenter.closeChat();
//        mChatPresenter.closeChat();
//        mChatPresenter.destroySocket();
//        mChatPresenter.destroySocket();
        //重置是否只看主播的判断标识
        super.onDestroy();
    }
}
