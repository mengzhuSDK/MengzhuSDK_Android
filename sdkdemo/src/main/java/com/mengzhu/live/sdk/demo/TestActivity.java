package com.mengzhu.live.sdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
        //传入观看用户的信息和活动id到直播间
        Intent intent = new Intent(this, PlayerActivity.class);
        if (!TextUtils.isEmpty(ticketId.getText().toString())) {
            intent.putExtra(PlayerFragment.TICKET_ID, ticketId.getText().toString());
        }
        if (!TextUtils.isEmpty(accountNo.getText().toString())) {
            intent.putExtra(PlayerFragment.ACCOUNTNO, accountNo.getText().toString());
        }
        if (!TextUtils.isEmpty(appId.getText().toString())) {
            intent.putExtra(PlayerFragment.APP_ID, appId.getText().toString());
        }

        if (!TextUtils.isEmpty(ticketId.getText().toString()) && !TextUtils.isEmpty(accountNo.getText().toString()) && !TextUtils.isEmpty(appId.getText().toString())) {
            intent.putExtra(PlayerFragment.NICKNAME, "我是测试用户");
            intent.putExtra(PlayerFragment.AVATAR, "http://img3.duitang.com/uploads/item/201507/23/20150723115018_ma428.thumb.700_0.jpeg");
            startActivity(intent);
        } else {
            Toast.makeText(this, "所有id不能为空", Toast.LENGTH_SHORT).show();
        }
    }
}
