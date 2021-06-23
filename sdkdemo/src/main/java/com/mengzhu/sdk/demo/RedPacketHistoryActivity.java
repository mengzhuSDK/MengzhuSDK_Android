package com.mengzhu.sdk.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mengzhu.live.sdk.business.dto.chat.ChatTextDto;
import com.mzmedia.fragment.redpacket.MZRedPacketHistoryFragment;

public class RedPacketHistoryActivity extends AppCompatActivity {

    private ChatTextDto chatTextDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_packet_history);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        chatTextDto = (ChatTextDto) getIntent().getExtras().getSerializable("ChatTextDto");
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.view_fragment , MZRedPacketHistoryFragment.newInstance(chatTextDto) , "TAG")
                .commit();
    }
}