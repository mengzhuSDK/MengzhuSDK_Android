package com.mengzhu.live.sdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mengzhu.core.coreutils.URLParamsUtils;
import com.mengzhu.live.sdk.business.dto.UserDto;
import com.mengzhu.live.sdk.business.presenter.MyUserInfoPresenter;
import com.mzmedia.fragment.PlayerFragment;

/**
 * Created by DELL on 2018/10/12.
 */
public class TestActivity extends Activity {
    private EditText ticketId;
    private EditText accountNo;
    private EditText appId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        ticketId = findViewById(R.id.ticket_id);
        accountNo = findViewById(R.id.account_no);
        appId = findViewById(R.id.app_id);
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
        URLParamsUtils.setSecretKey("xEyRRg4QYWbk09hfRJHYHeKPv8nWZITlBiklc44MZCxbdk4E6cGVzrXve6iVaNBn");

        boolean debug=URLParamsUtils.isDebug();
        if (!TextUtils.isEmpty(ticketId.getText().toString())  && !TextUtils.isEmpty(appId.getText().toString())) {
            intent.putExtra(PlayerFragment.NICKNAME, "1111");
            intent.putExtra(PlayerFragment.AVATAR, "https://upload.jianshu.io/users/upload_avatars/11711317/38d64087-b8c9-489a-b203-9f297e35e1e7?imageMogr2/auto-orient/strip|imageView2/1/w/96/h/96/format/webp");
            startActivity(intent);
        } else {
            Toast.makeText(this, "所有id不能为空", Toast.LENGTH_SHORT).show();
        }

    }
}
