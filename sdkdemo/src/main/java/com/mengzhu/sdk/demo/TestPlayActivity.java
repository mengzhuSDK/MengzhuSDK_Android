package com.mengzhu.sdk.demo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mengzhu.live.sdk.ui.api.MZApiDataListener;
import com.mengzhu.live.sdk.ui.api.MZApiRequest;
import com.mzmedia.fragment.HalfPlayerFragment;

import tv.mengzhu.core.frame.coreutils.URLParamsUtils;
import tv.mengzhu.core.wrap.user.modle.UserDto;
import tv.mengzhu.core.wrap.user.presenter.MyUserInfoPresenter;
import tv.mengzhu.core.wrap.netwock.Page;

public class TestPlayActivity extends Activity {

    private EditText ticketId;
    private EditText unique_id;
    private EditText appId;

    public static final String nickName = "Android用户" + TestActivity.unique_id_test;
    public static final String avatar = "http://pic1.zhimg.com/80/v2-efbee011404d77cf7ae75bac0f439755_720w.jpg";

    private ProgressDialog progressDialog;

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

        progressDialog = new ProgressDialog(this);
    }

    public void onPlayClick(View view) {
        URLParamsUtils.setSecretKey(TestActivity.secretKey);
        progressDialog.show();
        UserDto userDto =new UserDto();
        userDto.setUniqueID(unique_id.getText().toString());
        userDto.setAppid(TestActivity.app_id);
        userDto.setAvatar(avatar);
        userDto.setNickname(nickName);
        MyUserInfoPresenter.getInstance().saveUserinfo(userDto);
        MZApiRequest sdkLogin = new MZApiRequest();
        sdkLogin.createRequest(this , MZApiRequest.API_TYPE_LOGIN);
        sdkLogin.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String apiType, Object dto, Page page, int status) {
                MyUserInfoPresenter.getInstance().getUserInfo().setToken(((UserDto)dto).getToken());
                //传入观看用户的信息和活动id到直播间
                Intent intent = new Intent(TestPlayActivity.this, PlayerActivity.class);
                if (!TextUtils.isEmpty(ticketId.getText().toString())) {
                    intent.putExtra(HalfPlayerFragment.TICKET_ID, ticketId.getText().toString());
                }
                intent.putExtra(HalfPlayerFragment.UNIQUE_ID, unique_id.getText().toString());
                if (!TextUtils.isEmpty(appId.getText().toString())) {
                    intent.putExtra(HalfPlayerFragment.APP_ID, appId.getText().toString());
                }
                if (!TextUtils.isEmpty(ticketId.getText().toString()) && !TextUtils.isEmpty(appId.getText().toString())) {
                    intent.putExtra(HalfPlayerFragment.NICKNAME, nickName);
                    intent.putExtra(HalfPlayerFragment.AVATAR, avatar);
                    startActivity(intent);
                } else {
                    Toast.makeText(TestPlayActivity.this, "所有id不能为空", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void errorResult(String apiType, int code, String msg) {
                progressDialog.dismiss();
            }
        });
        sdkLogin.startData(MZApiRequest.API_TYPE_LOGIN , unique_id.getText().toString());

    }

    public void onHalfPlayClick(View view) {
        progressDialog.show();
        URLParamsUtils.setSecretKey(TestActivity.secretKey);
        UserDto userDto =new UserDto();
        userDto.setUniqueID(unique_id.getText().toString());
        userDto.setAppid(TestActivity.app_id);
        userDto.setAvatar(avatar);
        userDto.setNickname(nickName);
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
                intent.putExtra(HalfPlayerFragment.UNIQUE_ID, unique_id.getText().toString());
                if (!TextUtils.isEmpty(appId.getText().toString())) {
                    intent.putExtra(HalfPlayerFragment.APP_ID, appId.getText().toString());
                }
                if (!TextUtils.isEmpty(ticketId.getText().toString()) && !TextUtils.isEmpty(appId.getText().toString())) {
                    intent.putExtra(HalfPlayerFragment.NICKNAME, nickName);
                    intent.putExtra(HalfPlayerFragment.AVATAR, avatar);
                    startActivity(intent);
                } else {
                    Toast.makeText(TestPlayActivity.this, "所有id不能为空", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void errorResult(String apiType, int code, String msg) {
                progressDialog.dismiss();
            }
        });
        sdkLogin.startData(MZApiRequest.API_TYPE_LOGIN , unique_id.getText().toString());
    }

}