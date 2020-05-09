package com.mengzhu.live.sdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.mengzhu.core.coreutils.URLParamsUtils;
import com.mengzhu.live.sdk.business.dto.UserDto;
import com.mengzhu.live.sdk.business.dto.push.StartBroadcastInfoDto;
import com.mengzhu.live.sdk.business.dto.push.StartCreateDto;
import com.mengzhu.live.sdk.business.presenter.MyUserInfoPresenter;
import com.mengzhu.live.sdk.core.utils.JurisdictionUtils;
import com.mengzhu.live.sdk.ui.api.MZApiDataListener;
import com.mengzhu.live.sdk.ui.api.MZApiRequest;
import com.mzmedia.fragment.PlayerFragment;
import java.net.URLEncoder;


/**
 * Created by DELL on 2018/10/12.
 */
public class TestActivity extends Activity {
    private EditText ticketId;
    private EditText accountNo;
    private EditText appId;
    private EditText fps;
    private EditText livetk;
    private EditText time;
    private AppCompatCheckBox cbbeauty,cblater,cbAudio,cbAllBanChat;
    private RadioGroup radioGroup;
    private int screen;
    private int bitrate = 500 * 1000;
    private MZApiRequest mzLiveCreateApiRequest;
    private MZApiRequest mzLiveStreamApiRequest;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        //初始化请求
        mzLiveCreateApiRequest = new MZApiRequest();
        mzLiveStreamApiRequest = new MZApiRequest();
        mzLiveCreateApiRequest.createRequest(TestActivity.this,MZApiRequest.API_TYPE_LIVE_CREATE);
        mzLiveStreamApiRequest.createRequest(TestActivity.this,MZApiRequest.API_TYPE_LIVE_STREAM);

        ticketId = findViewById(R.id.ticket_id);
        accountNo = findViewById(R.id.account_no);
        findViewById(R.id.download_test_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestActivity.this.startActivity(new Intent(TestActivity.this,TestDownloadActivity.class));
            }
        });
        findViewById(R.id.tv_push_p).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(JurisdictionUtils.essentialRoot(TestActivity.this)){
                    screen = 1;
                    startPush();
                }
            }
        });
        findViewById(R.id.tv_push_l).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(JurisdictionUtils.essentialRoot(TestActivity.this)){
                    screen = 2;
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
                switch (checkedId){
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
    }

    public void onPlayClick(View view) {
        /**
         * 必填
         * 初始化签名Secret
         */
        URLParamsUtils.setSecretKey("自己的Secret_Key");
        //传入观看用户的信息和活动id到直播间
        Intent intent = new Intent(this, PlayerActivity.class);
        if (!TextUtils.isEmpty(ticketId.getText().toString())) {
            intent.putExtra(PlayerFragment.TICKET_ID, ticketId.getText().toString());
        }
//        if (!TextUtils.isEmpty(accountNo.getText().toString())) {
            intent.putExtra(PlayerFragment.ACCOUNTNO, accountNo.getText().toString());
//        }
        if (!TextUtils.isEmpty(appId.getText().toString())) {
            intent.putExtra(PlayerFragment.APP_ID, appId.getText().toString());
        }
        boolean debug=URLParamsUtils.isDebug();
        if (!TextUtils.isEmpty(ticketId.getText().toString())  && !TextUtils.isEmpty(appId.getText().toString())) {
            intent.putExtra(PlayerFragment.NICKNAME, "1111");
            intent.putExtra(PlayerFragment.AVATAR, "https://upload.jianshu.io/users/upload_avatars/11711317/38d64087-b8c9-489a-b203-9f297e35e1e7?imageMogr2/auto-orient/strip|imageView2/1/w/96/h/96/format/webp");
            startActivity(intent);
        } else {
            Toast.makeText(this, "所有id不能为空", Toast.LENGTH_SHORT).show();
        }

    }

    private void startPush(){
        UserDto dto=new UserDto();
        dto.setAccountNo(accountNo.getText().toString());
        dto.setAppid(appId.getText().toString()); //appId
        dto.setAvatar("https://avatars3.githubusercontent.com/u/13464940?s=60&v=4");
        dto.setNickname("test");
        URLParamsUtils.setDebug(true);
        URLParamsUtils.setSecretKey("自己的Secret_Key");
        MyUserInfoPresenter.getInstance().saveUserinfo(dto);
        //demo示例
        if(!TextUtils.isEmpty(livetk.getText().toString())){
            mzLiveStreamApiRequest.setResultListener(new StartStream());
            mzLiveStreamApiRequest.startData(MZApiRequest.API_TYPE_LIVE_STREAM,
                    livetk.getText().toString(),
                    appId.getText().toString(),
                    URLEncoder.encode("test"),
                    URLEncoder.encode("https://avatars3.githubusercontent.com/u/13464940?s=60&v=4"));
        }else {
            mzLiveCreateApiRequest.setResultListener(new MZApiDataListener() {
                @Override
                public void dataResult(String s, Object o) {
                    StartCreateDto dto = (StartCreateDto) o;
                    mzLiveStreamApiRequest.setResultListener(new StartStream());
                    mzLiveStreamApiRequest.startData(MZApiRequest.API_TYPE_LIVE_STREAM,
                            dto.getLive_tk(),
                            appId.getText().toString(),
                            URLEncoder.encode("test"),
                            URLEncoder.encode("https://avatars3.githubusercontent.com/u/13464940?s=60&v=4"));
                }

                @Override
                public void errorResult(String s, int i, String s1) {

                }
            });
            //debug模式下提供此api用于测试
            mzLiveCreateApiRequest.startData(MZApiRequest.API_TYPE_LIVE_CREATE,
                    "0","https://avatars3.githubusercontent.com/u/13464940?s=60&v=4","test");
        }
        mzLiveCreateApiRequest.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String s, Object o) {
                StartCreateDto dto = (StartCreateDto) o;
                mzLiveStreamApiRequest.setResultListener(new StartStream());
                mzLiveStreamApiRequest.startData(MZApiRequest.API_TYPE_LIVE_STREAM,
                        dto.getLive_tk(),
                        appId.getText().toString(),
                        URLEncoder.encode("test"),
                        URLEncoder.encode("https://avatars3.githubusercontent.com/u/13464940?s=60&v=4"));
            }

            @Override
            public void errorResult(String s, int i, String s1) {

            }
        });

    }
    private class StartStream implements MZApiDataListener {

        @Override
        public void dataResult(String s, Object o) {
            StartBroadcastInfoDto dto = (StartBroadcastInfoDto) o;
            Intent intent = new Intent(TestActivity.this,PusherActivity.class);
            intent.putExtra("pushDto",dto);
            intent.putExtra("screen",screen);
            intent.putExtra("bitrate",bitrate);
            intent.putExtra("cbbeauty",cbbeauty.isChecked());
            intent.putExtra("cblater",cblater.isChecked());
            intent.putExtra("cbAudio",cbAudio.isChecked());
            intent.putExtra("cbAllBanChat",cbAllBanChat.isChecked());
            intent.putExtra("fps",fps.getText().toString());
            intent.putExtra("time",time.getText().toString());
            TestActivity.this.startActivity(intent);
        }

        @Override
        public void errorResult(String s, int i, String s1) {

        }
    }
}
