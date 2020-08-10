package com.mengzhu.sdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
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

public class TestPlayActivity extends Activity {

    private EditText ticketId;
    private EditText unique_id;
    private EditText appId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_play);

        ticketId = findViewById(R.id.ticket_id);
        unique_id = findViewById(R.id.account_no);
        appId = findViewById(R.id.app_id);


        ticketId.setText(TestActivity.ticket_Id);
        unique_id.setText(TestActivity.unique_id_test);
        appId.setText(TestActivity.app_id);
        URLParamsUtils.setSecretKey(TestActivity.secretKey);
    }

    public void onPlayClick(View view) {
        /**
         * 必填
         * 初始化签名Secret
         */
        URLParamsUtils.setSecretKey(TestActivity.secretKey);
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
        URLParamsUtils.setSecretKey(TestActivity.secretKey);
        UserDto userDto =new UserDto();
        userDto.setUniqueID(unique_id.getText().toString());
        userDto.setAppid(TestActivity.app_id);
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
                Intent intent = new Intent(TestPlayActivity.this, HalfPlayerActivity.class);
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
                    Toast.makeText(TestPlayActivity.this, "所有id不能为空", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void errorResult(String apiType, int code, String msg) {

            }
        });
        sdkLogin.startData(MZApiRequest.API_TYPE_LOGIN , TestActivity.unique_id_test);
    }

}