package com.mengzhu.sdk.demo;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mengzhu.live.sdk.business.dto.push.StartBroadcastInfoDto;
import com.mengzhu.live.sdk.business.dto.push.StartCreateDto;
import com.mengzhu.live.sdk.core.utils.DateUtils;
import com.mengzhu.live.sdk.ui.api.MZApiDataListener;
import com.mengzhu.live.sdk.ui.api.MZApiRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import tv.mengzhu.core.frame.coreutils.JurisdictionUtils;
import tv.mengzhu.core.frame.coreutils.URLParamsUtils;
import tv.mengzhu.core.wrap.netwock.Page;
import tv.mengzhu.core.wrap.user.modle.UserDto;
import tv.mengzhu.core.wrap.user.presenter.MyUserInfoPresenter;

public class TestPushActivity extends Activity {

    private EditText unique_id;
    private EditText livetk;
    private EditText tv_ticket_id;
    private EditText time;
    private AppCompatCheckBox cbbeauty, cblater, cbAudio, cbAllBanChat;

    private RelativeLayout rb360_layout;
    private RelativeLayout rb480_layout;
    private RelativeLayout rb720_layout;

    private ImageView rb360_iv;
    private ImageView rb480_iv;
    private ImageView rb720_iv;

    private TextView rb360_tv;
    private TextView rb480_tv;
    private TextView rb720_tv;

    private int screen;
    private int bitrate = 500 * 1024;
    private MZApiRequest mzLiveCreateApiRequest;
    private MZApiRequest mzLiveStreamApiRequest;

    private boolean isAudioPush = false;

    private ProgressDialog progressDialog;

    int live_type = 0;

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
        cbAllBanChat = findViewById(R.id.cb_all_banchat);
        cbbeauty = findViewById(R.id.cb_beauty);
        cblater = findViewById(R.id.cb_later);
        cbAudio = findViewById(R.id.cb_audio);
        livetk = findViewById(R.id.live_tk);
        tv_ticket_id = findViewById(R.id.tv_ticket_id);
        time = findViewById(R.id.time);
        rb360_layout = findViewById(R.id.rb360);
        rb480_layout = findViewById(R.id.rb480);
        rb720_layout = findViewById(R.id.rb720);

        rb360_iv = findViewById(R.id.rb_img1);
        rb480_iv = findViewById(R.id.rb_img2);
        rb720_iv = findViewById(R.id.rb_img3);

        rb360_tv = findViewById(R.id.rb_text1);
        rb480_tv = findViewById(R.id.rb_text2);
        rb720_tv = findViewById(R.id.rb_text3);
        rb360_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitrate = 500 * 1024;
                setRBDefault();
                rb360_layout.setBackgroundResource(R.drawable.mz_shape_rb_bg_select);
                rb360_iv.setImageResource(R.mipmap.rb2);
                rb360_tv.setTextColor(getResources().getColor(R.color.color_ff1f60));
            }
        });
        rb480_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitrate = 800 * 1024;
                setRBDefault();
                rb480_layout.setBackgroundResource(R.drawable.mz_shape_rb_bg_select);
                rb480_iv.setImageResource(R.mipmap.rb4);
                rb480_tv.setTextColor(getResources().getColor(R.color.color_ff1f60));
            }
        });
        rb720_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitrate = 1000 * 1024;
                setRBDefault();
                rb720_layout.setBackgroundResource(R.drawable.mz_shape_rb_bg_select);
                rb720_iv.setImageResource(R.mipmap.rb6);
                rb720_tv.setTextColor(getResources().getColor(R.color.color_ff1f60));
            }
        });

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        livetk.setText(TestActivity.live_tk);
        unique_id.setText(TestActivity.unique_id_test);
        URLParamsUtils.setSecretKey(TestActivity.secretKey);
    }

    public void setRBDefault() {
        rb360_layout.setBackgroundResource(R.drawable.mz_shape_rb_bg_default);
        rb480_layout.setBackgroundResource(R.drawable.mz_shape_rb_bg_default);
        rb720_layout.setBackgroundResource(R.drawable.mz_shape_rb_bg_default);

        rb360_iv.setImageResource(R.mipmap.rb1);
        rb480_iv.setImageResource(R.mipmap.rb3);
        rb720_iv.setImageResource(R.mipmap.rb5);

        rb360_tv.setTextColor(getResources().getColor(R.color.white));
        rb480_tv.setTextColor(getResources().getColor(R.color.white));
        rb720_tv.setTextColor(getResources().getColor(R.color.white));
    }

    private void startPush() {
        UserDto dto = new UserDto();
        dto.setUniqueID(unique_id.getText().toString());
        dto.setAppid(TestActivity.app_id);
        dto.setAvatar("http://s1.t.zmengzhu.com/upload/img/50/6d/506da693ecb2cf6f2fd0e3e92656dde4.png");
        dto.setNickname("T丶????");
        MyUserInfoPresenter.getInstance().saveUserinfo(dto);
        URLParamsUtils.setSecretKey(TestActivity.secretKey);

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
                            dto.getLive_tk(),
                            unique_id.getText().toString(),
                            URLEncoder.encode("T丶????"),
                            URLEncoder.encode("http://s1.t.zmengzhu.com/upload/img/50/6d/506da693ecb2cf6f2fd0e3e92656dde4.png"), dto.getTicket_id());
                }

                @Override
                public void errorResult(String s, int i, String s1) {
                    Log.e("Lujie", "errorResult: " + s1);
                }
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(TestPushActivity.this);
            builder.setTitle("提示");
            builder.setMessage("此创建活动功能API接口权限所属于服务端，提供此功能为了便于demo演示。如接入时考虑数据安全等因素建议通过服务访问API并将live_tk、ticket_id传入SDK进行推流。");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //debug模式下提供此api用于测试
                    mzLiveCreateApiRequest.startData(MZApiRequest.API_TYPE_LIVE_CREATE,
                            "直播活动描述" + DateUtils.stringToDateNoymds(System.currentTimeMillis() + ""), TestActivity.channel_id, live_type, screen == 2 ? "0" : "1", "http://s1.t.zmengzhu.com/upload/img/50/6d/506da693ecb2cf6f2fd0e3e92656dde4.png", "test" + DateUtils.stringToDateNoymds(System.currentTimeMillis() + ""));
                }
            });
            builder.show();
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
            intent.putExtra("fps", "");
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