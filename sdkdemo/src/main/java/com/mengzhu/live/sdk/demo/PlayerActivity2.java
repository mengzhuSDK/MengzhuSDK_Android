package com.mengzhu.live.sdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mengzhu.live.sdk.business.view.widgets.MZPlayerView;
import com.mengzhu.live.sdk.core.utils.DensityUtil;
import com.mengzhu.live.sdk.ui.player.IMZPlayerManager;
import com.mengzhu.live.sdk.ui.player.MZPlayerManager;
import com.mengzhu.live.sdk.ui.player.PlayerEventListener;

/**
 * Created by DELL on 2018/10/12.
 */
public class PlayerActivity2 extends Activity implements PlayerEventListener {
    private MZPlayerManager mManager;
    private MZPlayerView mzPlayerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_play_layout2);
        mzPlayerView=findViewById(R.id.vv_activity_watch_broadcast);
        mManager=new MZPlayerManager();
        Intent intent= getIntent();
        int type = 0;
        String url = null;
        if(intent!=null){
            type=intent.getIntExtra("TYPE",1);
            Bundle bundle=intent.getExtras();
                url = bundle.getString("live_URL");
                if(TextUtils.isEmpty(url)){
                    url="http://vod.t.zmengzhu.com/record/base/hls-sd/a002307efd50b06100084555.m3u8";
                }
        }
        mManager.init(mzPlayerView).setBroadcastType(type,false).setMediaQuality(IMZPlayerManager.MZ_MEDIA_QUALITY_HIGH);
        mManager.setEventListener(this);
        mManager.setVideoPath(url);
        mManager.showPreviewImage("https://inews.gtimg.com/newsapp_ls/0/9563866905_294195/0");
        mManager.start();

    }

    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
        mzPlayerView.configurationChanged(newConfig);
        //切换为竖屏

        if (newConfig.orientation == this.getResources().getConfiguration().ORIENTATION_PORTRAIT) {
            mzPlayerView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(this,200)));
        }

        //切换为横屏

        else if (newConfig.orientation == this.getResources().getConfiguration().ORIENTATION_LANDSCAPE) {
            mzPlayerView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mManager.onResume();
    }

    @Override
    public void hideAllEvent() {
        Toast.makeText(this,"隐藏上下栏",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAllEvent() {
        Toast.makeText(this,"显示上下栏",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackClick(boolean isBack) {

    }

    @Override
    public void onPausePlayer() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        mManager.onPause();
    }

    @Override
    public void onStartPlayer() {

    }

    @Override
    public void onForbid(boolean isForbid) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mManager.onDestroy();
    }
}
