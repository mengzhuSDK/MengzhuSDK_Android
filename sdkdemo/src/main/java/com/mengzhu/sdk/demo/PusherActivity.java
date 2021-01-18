package com.mengzhu.sdk.demo;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mengzhu.live.sdk.business.dto.MZOnlineUserListDto;
import com.mengzhu.live.sdk.business.dto.chat.ChatTextDto;
import com.mengzhu.live.sdk.business.dto.play.PlayInfoDto;
import com.mengzhu.live.sdk.business.dto.push.StartBroadcastInfoDto;
import com.mzmedia.IPushClickListener;
import com.mzmedia.fragment.push.MZPlugFlowFragement;
import com.mzmedia.presentation.dto.LiveConfigDto;
import com.mzmedia.utils.MUIImmerseUtils;

import tv.mengzhu.core.wrap.user.presenter.MyUserInfoPresenter;


public class PusherActivity  extends AppCompatActivity implements IPushClickListener {
    private FragmentManager mFragmentManager;
    private MZPlugFlowFragement mzPlugFlowFragement;
    private StartBroadcastInfoDto startBroadcastInfoDto;
    private PlayInfoDto mPlayInfoDto;
    private TextView mTvCount;
    private ImageView mImClose;
    private int screen,bitrate;
    private boolean cbbeauty,cblater,cbAudio,cbAllBanChat;
    private String liveTk,ticketId,fps,time;
    private LiveConfigDto liveConfigDto;

    private boolean isAudioPush = false;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        screen = getIntent().getIntExtra("screen",0);
        MUIImmerseUtils.setStatusTranslucent(getWindow(), this);
        switch (screen){
            case 1:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case 2:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
        }
        startBroadcastInfoDto = (StartBroadcastInfoDto) getIntent().getSerializableExtra("pushDto");
        mFragmentManager = getSupportFragmentManager();
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppCompatTheme);
        setContentView(R.layout.test_push_layout);

        isAudioPush = getIntent().getBooleanExtra("isAudioPush" , false);
        bitrate = getIntent().getIntExtra("bitrate",0);
        liveTk = getIntent().getStringExtra("live_tk");
        ticketId = getIntent().getStringExtra("ticketId");
        cbbeauty = getIntent().getBooleanExtra("cbbeauty",false);
        cblater = getIntent().getBooleanExtra("cblater",false);
        cbAudio = getIntent().getBooleanExtra("cbAudio",false);
        cbAllBanChat = getIntent().getBooleanExtra("cbAllBanChat",false);
        fps = getIntent().getStringExtra("fps");
        time = getIntent().getStringExtra("time");
        liveConfigDto = new LiveConfigDto();
        liveConfigDto.setBitrate(bitrate);
        liveConfigDto.setCbbeauty(cbbeauty);
        liveConfigDto.setCblater(cblater);
        liveConfigDto.setCbAudio(cbAudio);
        liveConfigDto.setLive_tk(liveTk);
        liveConfigDto.setTicketId(ticketId);
        liveConfigDto.setAllBanChat(cbAllBanChat?0:1);
        if(TextUtils.isEmpty(fps)){
            fps = "15";
        }
        liveConfigDto.setFps(Integer.parseInt(fps));
        if(TextUtils.isEmpty(time)){
            time = "0";
        }
        liveConfigDto.setTime(Integer.parseInt(time));
        MUIImmerseUtils.setStatusTextColor(false, this);
        initView();
    }
    private void initView(){
        mTvCount = findViewById(R.id.tv_activity_broadcast_count);
        mPlayInfoDto = new PlayInfoDto();
        mPlayInfoDto.setMsg_config(startBroadcastInfoDto.getMsg_conf());
        mPlayInfoDto.setChat_config(startBroadcastInfoDto.getChat_conf());
        mPlayInfoDto.setTicket_id(startBroadcastInfoDto.getTicket_id());
        mPlayInfoDto.setChannel_id(startBroadcastInfoDto.getChannel_id());
        mPlayInfoDto.setChat_uid(startBroadcastInfoDto.getChat_conf().getChat_uid());
        mzPlugFlowFragement = MZPlugFlowFragement.newInstance(startBroadcastInfoDto.getPush_url(),startBroadcastInfoDto.getTicket_id()
                ,screen,mPlayInfoDto,liveConfigDto , isAudioPush);
        mFragmentManager.beginTransaction().replace(R.id.layout_push, mzPlugFlowFragement).commitAllowingStateLoss();
        mzPlugFlowFragement.setPushClickListener(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onStopLive() {
        finish();
    }

    @Override
    public void onChatAvatar(ChatTextDto chatTextDto) {
        Toast.makeText(PusherActivity.this,"点击用户头像",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onALLBanChat() {
        Toast.makeText(PusherActivity.this,"全体禁言成功",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBanChat() {
        Toast.makeText(PusherActivity.this,"单体禁言成功",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onShare(PlayInfoDto dto) {
        Toast.makeText(PusherActivity.this,"点击分享",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLiveAvatar() {
        Toast.makeText(PusherActivity.this,"点击主播头像",Toast.LENGTH_LONG).show();
        MyUserInfoPresenter.getInstance().getUserInfo().getAvatar(); //获取主播头像
        MyUserInfoPresenter.getInstance().getUserInfo().getNickname();//获取主播昵称
    }

    @Override
    public void onOnlineNum(MZOnlineUserListDto onlineUserDto) {
        Toast.makeText(PusherActivity.this,"点击在线人数",Toast.LENGTH_LONG).show();
    }
}
