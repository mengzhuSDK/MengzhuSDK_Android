package com.mengzhu.sdk.demo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import com.mengzhu.live.sdk.business.dto.push.StartBroadcastInfoDto;
import com.mengzhu.live.sdk.business.dto.push.StartCreateDto;
import com.mengzhu.live.sdk.core.utils.DateUtils;
import com.mengzhu.live.sdk.ui.api.MZApiDataListener;
import com.mengzhu.live.sdk.ui.api.MZApiRequest;
import java.net.URLEncoder;
import tv.mengzhu.core.frame.coreutils.JurisdictionUtils;
import tv.mengzhu.core.frame.coreutils.URLParamsUtils;
import tv.mengzhu.core.wrap.user.modle.UserDto;
import tv.mengzhu.core.wrap.user.presenter.MyUserInfoPresenter;
import tv.mengzhu.core.wrap.netwock.Page;

public class TestPushActivity extends Activity {

    private EditText unique_id;
    private EditText appId;
    private EditText fps;
    private EditText livetk;
    private EditText tv_ticket_id;
    private EditText time;
    private AppCompatCheckBox cbbeauty, cblater, cbAudio, cbAllBanChat;
    private RadioGroup radioGroup;
    private int screen;
    private int bitrate = 500 * 1024;
    private MZApiRequest mzLiveCreateApiRequest;
    private MZApiRequest mzLiveStreamApiRequest;

    private boolean isAudioPush = false;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_push);
        progressDialog = new ProgressDialog(this);
        mzLiveCreateApiRequest = new MZApiRequest();
        mzLiveStreamApiRequest = new MZApiRequest();
        mzLiveCreateApiRequest.createRequest(TestPushActivity.this, MZApiRequest.API_TYPE_LIVE_CREATE);
        mzLiveStreamApiRequest.createRequest(TestPushActivity.this, MZApiRequest.API_TYPE_LIVE_STREAM);

        unique_id = findViewById(R.id.account_no);
        findViewById(R.id.tv_push_p).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (JurisdictionUtils.essentialRoot(TestPushActivity.this)) {
                    isAudioPush = false;
                    screen = 1;
                    startPush();
                }
            }
        });
        findViewById(R.id.tv_push_l).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (JurisdictionUtils.essentialRoot(TestPushActivity.this)) {
                    isAudioPush = false;
                    screen = 2;
                    startPush();
                }
            }
        });
        findViewById(R.id.tv_audio_push).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (JurisdictionUtils.essentialRoot(TestPushActivity.this)) {
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
        tv_ticket_id = findViewById(R.id.tv_ticket_id);
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
        livetk.setText(TestActivity.live_tk);
        unique_id.setText(TestActivity.unique_id_test);
        appId.setText(TestActivity.app_id);
        URLParamsUtils.setSecretKey(TestActivity.secretKey);
    }

    private void startPush() {
        UserDto dto = new UserDto();
        dto.setUniqueID(unique_id.getText().toString());
        dto.setAppid(TestActivity.app_id);
        dto.setAvatar("http://s1.t.zmengzhu.com/upload/img/50/6d/506da693ecb2cf6f2fd0e3e92656dde4.png");
        dto.setNickname("T丶????");
        MyUserInfoPresenter.getInstance().saveUserinfo(dto);
        URLParamsUtils.setSecretKey(TestActivity.secretKey);

        int live_type = 0;
        if (isAudioPush) {
            live_type = 1;
        }
        //demo示例
        if (!TextUtils.isEmpty(livetk.getText().toString())) {
            mzLiveStreamApiRequest.setResultListener(new TestPushActivity.StartStream());
            progressDialog.show();
            mzLiveStreamApiRequest.startData(MZApiRequest.API_TYPE_LIVE_STREAM,
                    URLEncoder.encode(livetk.getText().toString()),
                    unique_id.getText().toString(),
                    URLEncoder.encode("T丶????"),
                    URLEncoder.encode("http://s1.t.zmengzhu.com/upload/img/50/6d/506da693ecb2cf6f2fd0e3e92656dde4.png"), tv_ticket_id.getText().toString());
        } else {
            final int finalLive_type = live_type;
            mzLiveCreateApiRequest.setResultListener(new MZApiDataListener() {
                @Override
                public void dataResult(String s, Object o, Page page, int status) {
                    StartCreateDto dto = (StartCreateDto) o;
                    livetk.setText(dto.getLive_tk());
                    tv_ticket_id.setText(dto.getTicket_id());
                    mzLiveStreamApiRequest.setResultListener(new TestPushActivity.StartStream());
                    mzLiveStreamApiRequest.startData(MZApiRequest.API_TYPE_LIVE_STREAM,
                            URLEncoder.encode(dto.getLive_tk()),
                            unique_id.getText().toString(),
                            URLEncoder.encode("T丶????"),
                            URLEncoder.encode("http://s1.t.zmengzhu.com/upload/img/50/6d/506da693ecb2cf6f2fd0e3e92656dde4.png"), dto.getTicket_id());
                }

                @Override
                public void errorResult(String s, int i, String s1) {

                }
            });
            //debug模式下提供此api用于测试
            mzLiveCreateApiRequest.startData(MZApiRequest.API_TYPE_LIVE_CREATE,
                    "直播活动描述" + DateUtils.stringToDateNoymds(System.currentTimeMillis() + ""), TestActivity.channel_id, live_type, screen == 2 ? "0" : "1", "http://s1.t.zmengzhu.com/upload/img/50/6d/506da693ecb2cf6f2fd0e3e92656dde4.png", "test" + DateUtils.stringToDateNoymds(System.currentTimeMillis() + ""));
        }
    }

    private class StartStream implements MZApiDataListener {

        @Override
        public void dataResult(String s, Object o, Page page, int status) {
            progressDialog.dismiss();
            StartBroadcastInfoDto dto = (StartBroadcastInfoDto) o;
            Intent intent = new Intent(TestPushActivity.this, PusherActivity.class);
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
            TestPushActivity.this.startActivity(intent);
        }

        @Override
        public void errorResult(String s, int i, String s1) {
            progressDialog.dismiss();
        }
    }
}