package com.mzmedia.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.mengzhu.live.sdk.business.dto.StaticStateDto;
import com.mengzhu.live.sdk.business.dto.chat.ChatMessageDto;
import com.mengzhu.live.sdk.business.dto.chat.ChatTextDto;
import com.mengzhu.live.sdk.business.dto.chat.impl.ChatMegTxtDto;
import com.mengzhu.live.sdk.business.dto.play.ChatConfDto;
import com.mengzhu.live.sdk.business.presenter.IBasePresenterLinstener;
import com.mengzhu.live.sdk.business.presenter.chat.ChatMessageObserver;
import com.mengzhu.live.sdk.business.presenter.chat.ChatPresenter;
import com.mengzhu.live.sdk.core.utils.KeyBoardUtils;
import com.mengzhu.live.sdk.ui.chat.MZChatManager;
import com.mengzhu.live.sdk.ui.widgets.emoji.EmojiPagerAdapter;
import com.mengzhu.live.sdk.ui.widgets.emoji.EmojiUtils;
import com.mengzhu.sdk.R;
import com.mengzhu.sdk.download.util.SharePreUtil;
import com.mzmedia.utils.String_Utils;

import java.util.ArrayList;
import java.util.List;

import tv.mengzhu.core.frame.coreutils.Device;
import tv.mengzhu.core.wrap.user.presenter.MyUserInfoPresenter;
import tv.mengzhu.core.wrap.netwock.Page;

/**
 * Created by DELL on 2016/7/13.
 */
public class LandscapeTransActivity extends Activity implements View.OnClickListener, IBasePresenterLinstener {
    private TextView tv_send;
    private LinearLayout ll_is_only_anchor;
    private ImageView iv_is_only_anchor_icon;
    private EditText et_input;
    private LinearLayout rl_trans;
    private int[] mWidthAndHeights;
    private LinearLayout include_bottom;
    int num = 0;
    public static final int CODE_REQUEST = 1010;
    public static final int CODE_ = 1011;
    private ChatPresenter mPresenter;
    public static final String TRANS_TOKEN = "trans_token";
    public static final String TRANS_AT = "at";
    private ChatConfDto mToken;
    private String mAt;
    public static final String MSG_COMPLETE = "msg_complete";
    private ViewPager mVpEmoji;
    private ImageView mActivityInputEmoji;
    private boolean isShowEmoji = false;
    private Intent mIntent;
    public static final String IS_CROSSWISE = "isCrosswise";
    private String mAtUid;
    private String mAtTicketId;
    //    private PlayInfoDto mPlayinfo;
//    private ImageView mInputDanmukuIv;
    private int isDanmuku;

    private boolean isOnlyAnchor = false;

    public interface SendMsgInterface {
        public void sendTransMsg(String msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mIntent = getIntent();
        boolean isCrosswise = mIntent.getBooleanExtra(IS_CROSSWISE, false);
//        if (isCrosswise) {
//            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//            }
//        }
        setContentView(R.layout.mz_activity_trans);
        super.onCreate(savedInstanceState);
        ChatPresenter.isRefreshList = false;
        initView();
        initListener();
        initLogic();
        loadData();
    }

    private int maxLen = 100 * 2;

    public void initView() {
        mWidthAndHeights = Device.getScreenWidthAndHeight(this);
        include_bottom = (LinearLayout) findViewById(R.id.include_bottom);
        mVpEmoji = (ViewPager) findViewById(R.id.mz_activity_input_emoji_pager);
        et_input = (EditText) findViewById(R.id.et_input);
        tv_send = (TextView) findViewById(R.id.tv_send);
        rl_trans = (LinearLayout) findViewById(R.id.rl_trans);
        ll_is_only_anchor = findViewById(R.id.ll_is_only_anchor);
        iv_is_only_anchor_icon = findViewById(R.id.iv_is_only_anchor_icon);
        mActivityInputEmoji = (ImageView) findViewById(R.id.mz_activity_input_emoji);
//        mInputDanmukuIv = (ImageView) findViewById(R.id.mz_activity_input_danmuku);
        mToken = (ChatConfDto) mIntent.getSerializableExtra(TRANS_TOKEN);
//        String content = (String) MemoryCache.get(mToken.getRoom(), "");
//        et_input.setFocusable(true);
//        et_input.setFocusableInTouchMode(true);
//        et_input.requestFocus();
//        et_input.setText(content);
//        et_input.setSelection(content.length());
//        et_input.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);

        mAt = mIntent.getStringExtra(TRANS_AT);
//        mAtUid = mIntent.getStringExtra(RequestParamConstants.UID);
//        mAtTicketId = mIntent.getStringExtra(RequestParamConstants.TICKET_ID);
//        mPlayinfo = (PlayInfoDto) mIntent.getSerializableExtra(TransActivity.PLAYINFO);
        initEmoji();
//        et_input.addTextChangedListener(new Testlistener(et_input, 100));

        //设置输入长度
//        InputFilter[] inputFilters = new InputFilter[1];
//        inputFilters[0] = InputFilterUtils.getAllFilter(maxLen);
//        et_input.setInputType(InputType.TYPE_CLASS_TEXT);
//        et_input.setFilters(inputFilters);
//        mInputDanmukuIv.setVisibility(mPlayinfo.isInputDanmuku() && PreferencesUtils.loadPrefBoolean(this, CommontConstant.INPUT_DANMUKU_PLAYER, false) ? View.VISIBLE : View.GONE);
//        isDanmuku = PreferencesUtils.loadPrefInt(this, CommontConstant.INPUT_DANMUKU, -1);
//        mInputDanmukuIv.setSelected(isDanmuku == 1);
    }

    public void initListener() {
//        ShowMsgMonitor.getInstance().register(new TransShowMsgMonitorCallback(), LandscapeTransActivity.class.getSimpleName());
        rl_trans.setOnClickListener(this);
//        mInputDanmukuIv.setOnClickListener(this);
        tv_send.setOnClickListener(this);
        mActivityInputEmoji.setOnClickListener(this);
        et_input.setOnClickListener(this);
        ll_is_only_anchor.setOnClickListener(this);
        et_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                send(v.getEditableText().toString());
                return true;
            }
        });

        rl_trans.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private int preHeight = 0;

            @Override
            public void onGlobalLayout() {
                int heightDiff = rl_trans.getRootView().getHeight() - rl_trans.getHeight();
                // 在数据相同时，减少发送重复消息。因为实际上在输入法出现时会多次调用这个onGlobalLayout方法。
                if (preHeight == heightDiff) {
                    return;
                }
                preHeight = heightDiff;
                System.out.println(heightDiff + "---" + mWidthAndHeights[1] + "---" + mWidthAndHeights[1] / 3);
                if (heightDiff > mWidthAndHeights[1] / 3) {

                } else {
                    if (num > 0 && !isShowEmoji) {
                        finish();
                        overridePendingTransition(R.anim.dialog_in_no_anim, R.anim.dialog_out_no_anim);
                    }
                    num++;
                }
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void initLogic() {
        mPresenter = ChatPresenter.getInstance(this);
        mPresenter.initSendMessagePresenter();
        mPresenter.registerSendMessageListener(this);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void loadData() {
        if (!TextUtils.isEmpty(mAt)) {
            et_input.setText("@" + mAt + " ");
            SpannableStringBuilder builder = new SpannableStringBuilder(et_input.getText().toString());
            String_Utils.setSpans(builder, 0, et_input.length());
            et_input.setText(builder);
            CharSequence text = et_input.getText();
            //Debug.asserts(text instanceof Spannable);
            if (text instanceof Spannable) {
                Spannable spanText = (Spannable) text;
                Selection.setSelection(spanText, text.length());
            }
        }
        isOnlyAnchor = MZChatManager.getInstance(this).isOnlyAnchor();
        if (isOnlyAnchor){
            iv_is_only_anchor_icon.setImageResource(R.mipmap.mz_is_only_anchor_open);
        }else {
            iv_is_only_anchor_icon.setImageResource(R.mipmap.mz_is_only_anchor_close);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.dialog_in_no_anim, R.anim.dialog_out_no_anim);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_send) {
            if (StaticStateDto.getInstance().isBanned()) {
                Toast.makeText(this, R.string.mz_banned_to_post, Toast.LENGTH_SHORT).show();
                return;
            }
//                if (!StaticStateDto.getInstance().isForbidPublicMsg() || mPlayinfo.getRole_name().equals(PlayInfoDto.ROLE_HOST) || mPlayinfo.getRole_name().equals(PlayInfoDto.ROLE_SUB_ACCOUNT)) {
            if (!StaticStateDto.getInstance().isForbidPublicMsg()) {
                String msg = et_input.getText().toString();
                send(msg);
            } else {
                Toast.makeText(this, R.string.forbid_public_hint, Toast.LENGTH_SHORT).show();
            }
//                String msg = et_input.getText().toString();
//                send(msg);
        } else if (id == R.id.rl_trans) {
            finish();
            overridePendingTransition(R.anim.dialog_in_no_anim, R.anim.dialog_out_no_anim);
            //            case R.id.mz_activity_input_emoji:
//                if (!isShowEmoji) {
//                    showEmoji();
//                    isShowEmoji = true;
//                } else {
//                    showKeyboard();
//                    isShowEmoji = false;
//                }
//                break;
        } else if (id == R.id.et_input) {//                mActivityInputEmoji.setImageResource(R.drawable.icon_input_emoji);
            mVpEmoji.setVisibility(View.GONE);
            KeyBoardUtils.openKeybord(et_input, LandscapeTransActivity.this);
            isShowEmoji = false;
            //            case R.id.mz_activity_input_danmuku:
//                danmukuControl(mInputDanmukuIv);
//                break;
        } else if (id == R.id.mz_activity_input_emoji) {
            num = 1;
            if (!isShowEmoji) {
                showEmoji();
                isShowEmoji = true;
            } else {
                showKeyboard();
                isShowEmoji = false;
            }
        }else if (id == R.id.ll_is_only_anchor){
            changeIsOnlyAnchor();
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void send(String msg) {

        if ("".equals(msg) || msg.trim().isEmpty()) {
            Toast.makeText(this, R.string.chat_is_empty, Toast.LENGTH_SHORT).show();
        } else if (StaticStateDto.getInstance().isBanned()) {
            ChatPresenter.isRefreshList = true;
            ChatMessageObserver.getInstance().sendMsgMonitor(ChatMessageObserver.CMD_TYPE, null);
            finish();
            overridePendingTransition(R.anim.dialog_in_no_anim, R.anim.dialog_out_no_anim);
        } else {

                ChatMegTxtDto dto = new ChatMegTxtDto();
                ChatTextDto textDto = new ChatTextDto();
                ChatMessageDto chatMessageDto = new ChatMessageDto();
                dto.setText(msg);
                String avatar = MyUserInfoPresenter.getInstance().getUserInfo().getAvatar();
                dto.setAvatar(avatar);
                dto.setUniqueID(MyUserInfoPresenter.getInstance().getUserInfo().getUniqueID());
                String name = MyUserInfoPresenter.getInstance().getUserInfo().getNickname();
                textDto.setBaseDto(dto);
                textDto.setAvatar(avatar);
                textDto.setUser_name(name);
                textDto.setEvent(ChatMessageObserver.MESSAGE_TYPE);
                chatMessageDto.setText(textDto);
                ChatMessageObserver.getInstance().sendMsgMonitor(ChatMessageObserver.MESSAGE_TYPE, chatMessageDto);
            MZChatManager.getInstance(this).registerAtPushListener(new IBasePresenterLinstener() {
                    @Override
                    public void dataResult(Object obj, Page page, int status) {
                    }

                    @Override
                    public void errorResult(int code, String msg) {

                    }
                });
            MZChatManager.getInstance(this).sendMessageExecute(ChatMessageObserver.MESSAGE_TYPE, dto);

//            ChatPresenter.isRefreshList = true;
//            ChatMegTxtDto dto = new ChatMegTxtDto();
//            dto.setText(msg);
//            if (mInputDanmukuIv.getVisibility() == View.VISIBLE) {
//                dto.setBarrage(isDanmuku);
//            }
//            String avatar = "";
////            if (LoginSystemManage.getInstance().isLogin() && LoginSystemManage.getInstance().getUser() != null) {
////                avatar = LoginSystemManage.getInstance().getUser().getAvatar();
////                dto.setAvatar(avatar);
////            }
//            mPresenter.sendMessageExecute(ChatMessageObserver.MESSAGE_TYPE, dto, mToken);
//            Intent data = new Intent();
//            data.putExtra(MSG_COMPLETE, msg);
//            setResult(ActivityUtils.TRANS_REQUEST_CODE, data);
//            ChatMessageDto chatDto = new ChatMessageDto();
//            ChatTextDto textDto = new ChatTextDto();
//            ChatMegTxtDto megDto = new ChatMegTxtDto();
//            megDto.setText(msg);
//            if (mInputDanmukuIv.getVisibility() == View.VISIBLE) {
//                megDto.setBarrage(isDanmuku);
//            }
////            if (!LoginSystemManage.getInstance().isLogin() || LoginSystemManage.getInstance().getUser() == null) {
////                Toast.makeText(this, R.string.error_send_msg, Toast.LENGTH_SHORT).show();
////                finish();
////                UiUtils.finishAllALiveAcitity();
////                ActivityUtils.startLoginMain(this);
////                return;
////            }
////            megDto.setAvatar(LoginSystemManage.getInstance().getUser().getAvatar());
////            textDto.setAvatar(LoginSystemManage.getInstance().getUser().getAvatar());
////            textDto.setUser_id(LoginSystemManage.getInstance().getUserID());
////            textDto.setUser_name(LoginSystemManage.getInstance().getUser().getNickname());
////            textDto.setTime(DateUtils.longToDate(System.currentTimeMillis()));
//            textDto.setBaseDto(megDto);
//            textDto.setEvent("msg");
//            chatDto.setText(textDto);
//            ChatMessageObserver.getInstance().sendMsgMonitor(ChatMessageObserver.MESSAGE_TYPE, chatDto);
//            if (!TextUtils.isEmpty(mAt) && msg.contains("@" + mAt + " ") && !TextUtils.isEmpty(mAtUid) && !TextUtils.isEmpty(mAtTicketId)) {
//                //发送推送
//                mPresenter.registerAtPushListener(new AtPushListener());
//                mPresenter.atPushExecute(mAtUid, mAt, mAtTicketId);
//            }
//            et_input.setText("");
            finish();
//            overridePendingTransition(R.anim.dialog_in_no_anim, R.anim.dialog_out_no_anim);
        }
    }

    @Override
    public void finish() {
//        MemoryCache.put(mToken.getRoom(), et_input.getText().toString());
        et_input.setText("");
//        setResult(TransActivity.TRANS_END_RESULT);
        KeyBoardUtils.closeKeybord(et_input, LandscapeTransActivity.this);
        ChatPresenter.isRefreshList = true;
        super.finish();
//        ShowMsgMonitor.getInstance().cancelRegister(LandscapeTransActivity.class.getSimpleName());
    }


    public void initEmoji() {
        List emoji = EmojiUtils.getExpressionRes(90);
        List<View> views = new ArrayList<View>();
        for (int i = 1; i <= 4; i++) {// 20*5
            View view = EmojiUtils.getGridChildView(this, i, emoji, et_input);
            views.add(view);
        }
        mVpEmoji.setAdapter(new EmojiPagerAdapter(views));
    }

    class AtPushListener implements IBasePresenterLinstener {

        @Override
        public void dataResult(Object obj, Page page, int status) {

        }

        @Override
        public void errorResult(int i, String s) {

        }
    }

    private void showEmoji() {
        KeyBoardUtils.closeKeybord(et_input, this);
        new Handler() {//延时0.2秒显示表情
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mActivityInputEmoji.setImageResource(R.mipmap.icon_keybord);
                mVpEmoji.setVisibility(View.VISIBLE);
            }
        }.sendEmptyMessageDelayed(1, 200);
    }

    private void showKeyboard() {
        mActivityInputEmoji.setImageResource(R.mipmap.icon_emoji);
        mVpEmoji.setVisibility(View.GONE);
        new Handler() {//延时0.2秒显示表情
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                KeyBoardUtils.openKeybord(et_input, LandscapeTransActivity.this);
            }
        }.sendEmptyMessageDelayed(1, 300);
    }

    @Override
    public void dataResult(Object obj, Page page, int status) {

    }

    @Override
    public void errorResult(int code, String msg) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        WatchBroadcastActivity.isChatResume = true;
//        WatchBroadcastActivity.isChatPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChatPresenter.isRefreshList = true;
    }

    public void changeIsOnlyAnchor(){
        if (isOnlyAnchor){
            isOnlyAnchor = false;
            MZChatManager.getInstance(this).setOnlyAnchor(isOnlyAnchor);
            iv_is_only_anchor_icon.setImageResource(R.mipmap.mz_is_only_anchor_close);
        }else {
            isOnlyAnchor = true;
            MZChatManager.getInstance(this).setOnlyAnchor(isOnlyAnchor);
            iv_is_only_anchor_icon.setImageResource(R.mipmap.mz_is_only_anchor_open);
        }
        Intent intent = new Intent("isOnlyAnchor");
        intent.putExtra("isOnlyAnchor", isOnlyAnchor);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        finish();
    }

//    class TransShowMsgMonitorCallback implements ShowMsgMonitor.ShowMsgMonitorCallback {
//
//        @Override
//        public void onCallback(Object obj) {
//            try {
//                if (!LandscapeTransActivity.this.isFinishing()) {
//                    finish();
//                }
//            } catch (Exception e) {
//
//            }
//        }
//    }

    //    发送弹幕开关
//    private void danmukuControl(View view) {
//        if (view.isSelected()) {
//            view.setSelected(false);
//            isDanmuku = 0;
//        } else {
//            view.setSelected(true);
//            isDanmuku = 1;
//        }
//        PreferencesUtils.savePrefInt(this, CommontConstant.INPUT_DANMUKU, isDanmuku);
//    }
}
