package com.mengzhu.live.sdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

/**
 * Created by DELL on 2018/10/12.
 */
public class TestActivity extends Activity {
    private EditText ticketId;
    private EditText UId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        ticketId = findViewById(R.id.ticket_id);
        UId = findViewById(R.id.u_id);
    }

    public void onPlayClick(View view) {
        //传入观看用户的信息和活动id到直播间

        Intent intent = new Intent(this, PlayerActivity.class);
        if (ticketId.getText() != null && !ticketId.getText().equals("")) {
            intent.putExtra("ticketid", ticketId.getText().toString());
        }
        if (TextUtils.isEmpty(UId.getText().toString())) {
            intent.putExtra("uid", "10074142");
        } else {
            intent.putExtra("uid", UId.getText().toString());
        }
        intent.putExtra("accountNo", "GM20181202092729000830");
        intent.putExtra("nickname", "我是@#$%^&*%^&*(!@#$%~!@#$%^&用户");
        intent.putExtra("avatar", "http://img3.duitang.com/uploads/item/201507/23/20150723115018_ma428.thumb.700_0.jpeg");
        intent.putExtra("appid", "2019101019585068343");
        startActivity(intent);
    }
}
