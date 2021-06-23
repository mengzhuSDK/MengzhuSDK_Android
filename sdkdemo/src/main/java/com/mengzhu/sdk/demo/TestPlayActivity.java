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

import com.alibaba.fastjson.JSONObject;
import com.mengzhu.live.sdk.ui.api.MZApiDataListener;
import com.mengzhu.live.sdk.ui.api.MZApiRequest;
import com.mzmedia.fragment.HalfPlayerFragment;
import com.mzmedia.utils.MUIImmerseUtils;

import tv.mengzhu.core.frame.coreutils.URLParamsUtils;
import tv.mengzhu.core.wrap.user.modle.UserDto;
import tv.mengzhu.core.wrap.user.presenter.MyUserInfoPresenter;
import tv.mengzhu.core.wrap.netwock.Page;

public class TestPlayActivity extends Activity {

    private EditText ticketId;
    private EditText unique_id;
    private EditText user_name;
    private EditText user_avatar;
    private EditText user_phone;

    public static final String nickName = "Android用户" + DemoApplication.unique_id_test;
    public static final String avatar = "http://pic1.zhimg.com/80/v2-efbee011404d77cf7ae75bac0f439755_720w.jpg";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MUIImmerseUtils.setStatusTextColor(false , this);
        setContentView(R.layout.activity_test_play);

        ticketId = findViewById(R.id.ticket_id);
        unique_id = findViewById(R.id.account_no);
        user_name = findViewById(R.id.user_name);
        user_avatar = findViewById(R.id.user_avatar);
        user_phone = findViewById(R.id.user_phone);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ticketId.setText(DemoApplication.ticket_Id);
        unique_id.setText(DemoApplication.unique_id_test);
        user_name.setText(nickName);
        user_avatar.setText(avatar);
        URLParamsUtils.setSecretKey(DemoApplication.secretKey);
    }

    public void onPlayClick(View view) {
        URLParamsUtils.setSecretKey(DemoApplication.secretKey);
        UserDto userDto = new UserDto();
        userDto.setUniqueID(unique_id.getText().toString());
        userDto.setAppid(DemoApplication.app_id);
        userDto.setAvatar(avatar);
        userDto.setNickname(nickName);
        MyUserInfoPresenter.getInstance().saveUserinfo(userDto);

        //传入观看用户的信息和活动id到直播间
        Intent intent = new Intent(TestPlayActivity.this, PlayerActivity.class);
        if (!TextUtils.isEmpty(ticketId.getText().toString())) {
            intent.putExtra(HalfPlayerFragment.TICKET_ID, ticketId.getText().toString());
        }
        intent.putExtra(HalfPlayerFragment.UNIQUE_ID, unique_id.getText().toString());
        intent.putExtra(HalfPlayerFragment.APP_ID, DemoApplication.app_id);
        if (!TextUtils.isEmpty(ticketId.getText().toString())) {
            intent.putExtra(HalfPlayerFragment.NICKNAME, nickName);
            intent.putExtra(HalfPlayerFragment.AVATAR, avatar);
            if (TextUtils.isEmpty(user_phone.getText().toString())) {
                //默认手机号
                intent.putExtra("phone", "18688888888");
            } else {
                intent.putExtra("phone", user_phone.getText().toString());
            }
            startActivity(intent);
        } else {
            Toast.makeText(TestPlayActivity.this, "所有id不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    public void onHalfPlayClick(View view) {
        URLParamsUtils.setSecretKey(DemoApplication.secretKey);
        UserDto userDto = new UserDto();
        userDto.setUniqueID(unique_id.getText().toString());
        userDto.setAppid(DemoApplication.app_id);
        userDto.setAvatar(avatar);
        userDto.setNickname(nickName);
        MyUserInfoPresenter.getInstance().saveUserinfo(userDto);
        //传入观看用户的信息和活动id到直播间
        Intent intent = new Intent(TestPlayActivity.this, HalfPlayerActivity.class);
        if (!TextUtils.isEmpty(ticketId.getText().toString())) {
            intent.putExtra(HalfPlayerFragment.TICKET_ID, ticketId.getText().toString());
        }
        intent.putExtra(HalfPlayerFragment.UNIQUE_ID, unique_id.getText().toString());
        intent.putExtra(HalfPlayerFragment.APP_ID, DemoApplication.app_id);
        if (!TextUtils.isEmpty(ticketId.getText().toString())) {
            intent.putExtra(HalfPlayerFragment.NICKNAME, nickName);
            intent.putExtra(HalfPlayerFragment.AVATAR, avatar);
            if (TextUtils.isEmpty(user_phone.getText().toString())) {
                //默认手机号
                intent.putExtra("phone", "18688888888");
            } else {
                intent.putExtra("phone", user_phone.getText().toString());
            }
            startActivity(intent);
        } else {
            Toast.makeText(TestPlayActivity.this, "所有id不能为空", Toast.LENGTH_SHORT).show();
        }
    }
}