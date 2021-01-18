package com.mzmedia.widgets;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.mengzhu.live.sdk.business.dto.chat.ChatMessageDto;
import com.mengzhu.sdk.R;

import java.util.Timer;
import java.util.TimerTask;

public class ChatOnlineView extends LinearLayout {
    private TextView play_chat_list_online_name;
    private LinearLayout plat_chat_list_online_layout;
    private Timer mTimer;
    private ChatMessageDto chatMessageDto;
    private ObjectAnimator mAnimator;
    private boolean isEndAnimation = false;
    private Activity mActivity;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            plat_chat_list_online_layout.measure(0, 0);
            int width = plat_chat_list_online_layout.getMeasuredWidth();
            mAnimator = ObjectAnimator.ofFloat(plat_chat_list_online_layout, "translationX", 0, -width).setDuration(800);
            mAnimator.removeAllUpdateListeners();
            mAnimator.addListener(new AlphaAnimatorListener());
            mAnimator.start();

        }
    };

    public ChatOnlineView(Context context) {
        super(context);
        initView(context);
    }

    public ChatOnlineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.chat_online_view_layout, this);
        play_chat_list_online_name = findViewById(R.id.play_chat_list_online_name);
        plat_chat_list_online_layout = findViewById(R.id.plat_chat_list_online_layout);
        plat_chat_list_online_layout.setVisibility(GONE);
        mTimer = new Timer();
    }

    class AnimatorTask extends TimerTask {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
        }
    }

    private void startTimeTask() {
        mTimer.cancel();
        mTimer = null;
        mTimer = new Timer();
        mTimer.schedule(new AnimatorTask(), 3000);
    }

    class OnlineAnimatorListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animator) {
            plat_chat_list_online_layout.setVisibility(View.VISIBLE);
            Log.i("OnlineAnimatorListener", "onAnimationStart+VISIBLE");
            isEndAnimation = true;
        }

        @Override
        public void onAnimationEnd(Animator animator) {

        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    }

    public void startOnline(Activity activity, ChatMessageDto chatMessageDto) {
        mActivity = activity;
        plat_chat_list_online_layout.measure(0, 0);
        int width = plat_chat_list_online_layout.getMeasuredWidth();
        if (!isEndAnimation) {
            mAnimator = ObjectAnimator.ofFloat(plat_chat_list_online_layout, "translationX", -width, 0).setDuration(800);
            mAnimator.removeAllUpdateListeners();
            mAnimator.addListener(new OnlineAnimatorListener());
            mAnimator.start();
            isEndAnimation = true;
        }
        startTimeTask();
        play_chat_list_online_name.setText(chatMessageDto.getText().getUser_name());
    }

    class AlphaAnimatorListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animator) {
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            plat_chat_list_online_layout.setVisibility(View.GONE);
            isEndAnimation = false;
        }

        @Override
        public void onAnimationCancel(Animator animator) {
        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    }
}
