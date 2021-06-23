package com.mengzhu.sdk.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mzmedia.fragment.redpacket.MZCreateRedPacketFragment;

/**
 * 发送随机红包界面
 */
public class SendRedPacketActivity extends AppCompatActivity {

    private String channel_id;
    private String ticket_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_red_packet);

        channel_id = getIntent().getExtras().getString("channel_id");
        ticket_id = getIntent().getExtras().getString("ticket_id");

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.view_fragment , MZCreateRedPacketFragment.newInstance(channel_id , ticket_id) , "TAG")
                .commit();
    }
}