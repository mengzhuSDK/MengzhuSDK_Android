package com.mengzhu.sdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mengzhu.live.sdk.business.dto.push.StartBroadcastInfoDto;
import com.mengzhu.live.sdk.business.dto.push.StartCreateDto;
import com.mengzhu.live.sdk.core.utils.DateUtils;
import com.mengzhu.live.sdk.ui.api.MZApiDataListener;
import com.mengzhu.live.sdk.ui.api.MZApiRequest;
import com.mzmedia.fragment.HalfPlayerFragment;
import com.mzmedia.fragment.PlayerFragment;

import java.net.URLEncoder;

import tv.mengzhu.core.frame.coreutils.JurisdictionUtils;
import tv.mengzhu.core.frame.coreutils.URLParamsUtils;
import tv.mengzhu.core.wrap.netwock.Page;
import tv.mengzhu.core.wrap.user.modle.UserDto;
import tv.mengzhu.core.wrap.user.presenter.MyUserInfoPresenter;


/**
 * Created by DELL on 2018/10/12.
 */
public class TestActivity extends Activity {
    private EditText ticketId;
    private EditText unique_id;
    private EditText appId;
    private EditText fps;
    private EditText livetk;
    private EditText time;
    private AppCompatCheckBox cbbeauty, cblater, cbAudio, cbAllBanChat;
    private RadioGroup radioGroup;
    private int screen;
    private int bitrate = 500 * 1000;
    private MZApiRequest mzLiveCreateApiRequest;
    private MZApiRequest mzLiveStreamApiRequest;

    private boolean isAudioPush = false;

    public String live_tk = "";
    public String ticket_Id = "";
    public String app_id = "";
    public String unique_id_test = "";
    public String channel_id = "";
    public String secretKey = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        mzLiveCreateApiRequest = new MZApiRequest();
        mzLiveStreamApiRequest = new MZApiRequest();
        mzLiveCreateApiRequest.createRequest(TestActivity.this, MZApiRequest.API_TYPE_LIVE_CREATE);
        mzLiveStreamApiRequest.createRequest(TestActivity.this, MZApiRequest.API_TYPE_LIVE_STREAM);

        ticketId = findViewById(R.id.ticket_id);
        unique_id = findViewById(R.id.account_no);
        findViewById(R.id.download_test_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestActivity.this.startActivity(new Intent(TestActivity.this, TestDownloadActivity.class));
            }
        });
        findViewById(R.id.tv_push_p).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (JurisdictionUtils.essentialRoot(TestActivity.this)) {
                    isAudioPush = false;
                    screen = 1;
                    startPush();
                }
            }
        });
        findViewById(R.id.tv_push_l).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (JurisdictionUtils.essentialRoot(TestActivity.this)) {
                    isAudioPush = false;
                    screen = 2;
                    startPush();
                }
            }
        });
        findViewById(R.id.tv_audio_push).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (JurisdictionUtils.essentialRoot(TestActivity.this)) {
                    isAudioPush = true;
                    screen = 1;
                    startPush();
                }
            }
        });
        appId = findViewById(R.id.app_id);
        cbAllBanChat = findViewById(R.id.cb_all_banchat);
        cbbeauty = findViewById(R.id.cb_beauty);
        cblater = findViewById(R.id.cb_later);
        cbAudio = findViewById(R.id.cb_audio);
        livetk = findViewById(R.id.live_tk);
        fps = findViewById(R.id.fps);
        time = findViewById(R.id.time);
        radioGroup = findViewById(R.id.test_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb360:
                        bitrate = 500 * 1024;
                        break;
                    case R.id.rb480:
                        bitrate = 800 * 1024;
                        break;
                    case R.id.rb720:
                        bitrate = 1000 * 1024;
                        break;
                }
            }
        });
        if (!URLParamsUtils.isDebug()){
            live_tk = "";
            ticket_Id = "";
            app_id = "";
            channel_id = "";
            secretKey = "";
        }
        livetk.setText(live_tk);
        ticketId.setText(ticket_Id);
        unique_id.setText(unique_id_test);
        appId.setText(app_id);
        URLParamsUtils.setSecretKey(secretKey);
    }

    public void onPlayClick(View view) {
        /**
         * 必填
         * 初始化签名Secret
         */
        URLParamsUtils.setSecretKey(secretKey);
        //传入观看用户的信息和活动id到直播间
        Intent intent = new Intent(this, PlayerActivity.class);
        if (!TextUtils.isEmpty(ticketId.getText().toString())) {
            intent.putExtra(PlayerFragment.TICKET_ID, ticketId.getText().toString());
        }
//        if (!TextUtils.isEmpty(accountNo.getText().toString())) {
        intent.putExtra(PlayerFragment.UNIQUE_ID, unique_id.getText().toString());
//        }
        if (!TextUtils.isEmpty(appId.getText().toString())) {
            intent.putExtra(PlayerFragment.APP_ID, appId.getText().toString());
        }
        boolean debug = URLParamsUtils.isDebug();
        if (!TextUtils.isEmpty(ticketId.getText().toString()) && !TextUtils.isEmpty(appId.getText().toString())) {
            intent.putExtra(PlayerFragment.NICKNAME, "1111");
            intent.putExtra(PlayerFragment.AVATAR, "https://cdn.duitang.com/uploads/item/201410/26/20141026191422_yEKyd.thumb.700_0.jpeg");
            startActivity(intent);
        } else {
            Toast.makeText(this, "所有id不能为空", Toast.LENGTH_SHORT).show();
        }

    }

    public void onHalfPlayClick(View view) {
        URLParamsUtils.setSecretKey(secretKey);
        UserDto userDto =new UserDto();
        userDto.setUniqueID(unique_id.getText().toString());
        userDto.setAppid(app_id);
        userDto.setAvatar("http://s1.t.zmengzhu.com/upload/img/50/6d/506da693ecb2cf6f2fd0e3e92656dde4.png");
        userDto.setNickname("T丶????");
        MyUserInfoPresenter.getInstance().saveUserinfo(userDto);
        MZApiRequest sdkLogin = new MZApiRequest();
        sdkLogin.createRequest(this , MZApiRequest.API_TYPE_LOGIN);
        sdkLogin.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String apiType, Object dto, Page page, int status) {
                MyUserInfoPresenter.getInstance().getUserInfo().setToken(((UserDto)dto).getToken());
                //传入观看用户的信息和活动id到直播间
                Intent intent = new Intent(TestActivity.this, HalfPlayerActivity.class);
                if (!TextUtils.isEmpty(ticketId.getText().toString())) {
                    intent.putExtra(HalfPlayerFragment.TICKET_ID, ticketId.getText().toString());
                }
//        if (!TextUtils.isEmpty(accountNo.getText().toString())) {
                intent.putExtra(HalfPlayerFragment.UNIQUE_ID, unique_id.getText().toString());
//        }
                if (!TextUtils.isEmpty(appId.getText().toString())) {
                    intent.putExtra(HalfPlayerFragment.APP_ID, appId.getText().toString());
                }
                if (!TextUtils.isEmpty(ticketId.getText().toString()) && !TextUtils.isEmpty(appId.getText().toString())) {
                    intent.putExtra(HalfPlayerFragment.NICKNAME, "1111");
                    intent.putExtra(HalfPlayerFragment.AVATAR, "http://s1.t.zmengzhu.com/upload/img/50/6d/506da693ecb2cf6f2fd0e3e92656dde4.png");
                    startActivity(intent);
                } else {
                    Toast.makeText(TestActivity.this, "所有id不能为空", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void errorResult(String apiType, int code, String msg) {

            }
        });
        sdkLogin.startData(MZApiRequest.API_TYPE_LOGIN , unique_id_test);
    }

    private void startPush() {
        UserDto dto = new UserDto();
        dto.setUniqueID(unique_id.getText().toString());
        dto.setAppid(app_id);
        dto.setAvatar("http://s1.t.zmengzhu.com/upload/img/50/6d/506da693ecb2cf6f2fd0e3e92656dde4.png");
        dto.setNickname("T丶????");
        MyUserInfoPresenter.getInstance().saveUserinfo(dto);
        URLParamsUtils.setSecretKey(secretKey);

        int live_type = 0;
        if (isAudioPush) {
            live_type = 1;
        }
        //demo示例
        if (!TextUtils.isEmpty(livetk.getText().toString())) {
            mzLiveStreamApiRequest.setResultListener(new StartStream());
            mzLiveStreamApiRequest.startData(MZApiRequest.API_TYPE_LIVE_STREAM,
                    URLEncoder.encode(livetk.getText().toString()),
                    appId.getText().toString(),
                    URLEncoder.encode("T丶????"),
                    URLEncoder.encode("http://s1.t.zmengzhu.com/upload/img/50/6d/506da693ecb2cf6f2fd0e3e92656dde4.png"), live_type);
        } else {
            final int finalLive_type = live_type;
            mzLiveCreateApiRequest.setResultListener(new MZApiDataListener() {
                @Override
                public void dataResult(String s, Object o, Page page, int status) {
                    StartCreateDto dto = (StartCreateDto) o;
                    livetk.setText(dto.getLive_tk());
                    ticketId.setText(dto.getTicket_id());
                    mzLiveStreamApiRequest.setResultListener(new StartStream());
                    mzLiveStreamApiRequest.startData(MZApiRequest.API_TYPE_LIVE_STREAM,
                            URLEncoder.encode(dto.getLive_tk()),
                            appId.getText().toString(),
                            URLEncoder.encode("T丶????"),
                            URLEncoder.encode("http://s1.t.zmengzhu.com/upload/img/50/6d/506da693ecb2cf6f2fd0e3e92656dde4.png"), finalLive_type);
                }

                @Override
                public void errorResult(String s, int i, String s1) {

                }
            });
            //debug模式下提供此api用于测试
            mzLiveCreateApiRequest.startData(MZApiRequest.API_TYPE_LIVE_CREATE,
                    "直播活动描述" + DateUtils.stringToDateNoymds(System.currentTimeMillis() + ""), channel_id, live_type, screen == 2 ? "0" : "1", "http://s1.t.zmengzhu.com/upload/img/50/6d/506da693ecb2cf6f2fd0e3e92656dde4.png", "test" + DateUtils.stringToDateNoymds(System.currentTimeMillis() + ""));
        }
    }

    private class StartStream implements MZApiDataListener {

        @Override
        public void dataResult(String s, Object o, Page page, int status) {
            StartBroadcastInfoDto dto = (StartBroadcastInfoDto) o;
            Intent intent = new Intent(TestActivity.this, PusherActivity.class);
            intent.putExtra("pushDto", dto);
            intent.putExtra("screen", screen);
            intent.putExtra("bitrate", bitrate);
            intent.putExtra("cbbeauty", cbbeauty.isChecked());
            intent.putExtra("cblater", cblater.isChecked());
            intent.putExtra("cbAudio", cbAudio.isChecked());
            intent.putExtra("cbAllBanChat", cbAllBanChat.isChecked());
            intent.putExtra("livetk", livetk.getText().toString());
            intent.putExtra("fps", fps.getText().toString());
            intent.putExtra("time", time.getText().toString());
            intent.putExtra("isAudioPush", isAudioPush);
            TestActivity.this.startActivity(intent);
        }

        @Override
        public void errorResult(String s, int i, String s1) {

        }
    }
}
