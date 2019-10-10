package com.mengzhu.live.sdk.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.mengzhu.live.sdk.business.dto.UserDto;
import com.mengzhu.live.sdk.core.utils.XxteaUtils;
import com.mengzhu.live.sdk.ui.player.PlayerEventListener;
import com.mzmedia.IPlayerClickListener;
import com.mzmedia.fragment.PlayerFragment;
import com.mzmedia.utils.MUIImmerseUtils;

/**
 * Created by DELL on 2018/10/12.
 */
public class PlayerActivity extends AppCompatActivity implements PlayerEventListener, IPlayerClickListener {
//    private MZPlayerManager mManager;
//    private MZPlayerView mzPlayerView;
    private FrameLayout mContainer;
    private FragmentManager mFragmentManager;
    private PlayerFragment mPlayerFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        MUIImmerseUtils.setStatusTranslucent(getWindow(),this);
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppCompatTheme);
        setContentView(R.layout.test_play_layout);
        MUIImmerseUtils.setStatusTextColor(false,this);
        mContainer=findViewById(R.id.container_activity_watch_broadcast);
        String videoUrl = "z7ZfqHvzWKRZYv9LXHqshqGKZttR2j+QCuHLsPIvHXEM0J24sqSf7kzNbWyzurtF4BN9U4MPinzaNTL1WXAVRzfGRdwl20YICrdqv86FVGs=";
        UserDto dto=new UserDto();
        dto.setUid("2095938");
        dto.setAppid("2019091711154563239");
        dto.setAvatar("https://avatars3.githubusercontent.com/u/13464940?s=60&v=4");
        dto.setNickname("test");
        String ticketID = getIntent().getStringExtra("live_URL");
        mPlayerFragment = PlayerFragment.newInstance(XxteaUtils.decryptToString(videoUrl),dto,ticketID);
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().replace(R.id.container_activity_watch_broadcast, mPlayerFragment).commitAllowingStateLoss();
        mPlayerFragment.setIPlayerClickListener(this);
//        mManager=new MZPlayerManager();
//        Intent intent= getIntent();
//        int type = 0;
//        String url = null;
//        if(intent!=null){
//            type=intent.getIntExtra("TYPE",1);
//            Bundle bundle=intent.getExtras();
//                url = bundle.getString("live_URL");
//                if(TextUtils.isEmpty(url)){
//                    url="http://vod.dev.zmengzhu.com/record/base/hls-sd/042dca9d63ae07d300006491.m3u8";
//                }
//        }
//        mManager.init(mzPlayerView).setBroadcastType(type,false).setMediaQuality(IMZPlayerManager.MZ_MEDIA_QUALITY_HIGH);
//        mManager.setEventListener(this);
//        mManager.setVideoPath(url);
//        mManager.showPreviewImage("https://inews.gtimg.com/newsapp_ls/0/9563866905_294195/0");
//        mManager.start();

    }

//    public void onConfigurationChanged(Configuration newConfig) {
//
//        super.onConfigurationChanged(newConfig);
//
//        //切换为竖屏
//
//        if (newConfig.orientation == this.getResources().getConfiguration().ORIENTATION_PORTRAIT) {
//            mzPlayerView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(this,200)));
//        }
//
//        //切换为横屏
//
//        else if (newConfig.orientation == this.getResources().getConfiguration().ORIENTATION_LANDSCAPE) {
//            mzPlayerView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
//        mManager.onResume();
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
//        mManager.onPause();
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
//        mManager.onDestroy();
    }

    @Override
    public void onAvatarClick() {
        Log.d("gm","点击主播头像");
    }

    @Override
    public void onAttentionClick() {
        Log.d("gm","关注");
    }

    @Override
    public void onOnlineClick() {
        Log.d("gm","在线人数");
    }

    @Override
    public void onCloseClick() {
        Log.d("gm","退出");
    }

    @Override
    public void onReportClick() {
        Log.d("gm","举报");
    }

    @Override
    public void onShareClick() {
        Log.d("gm","分享");
    }

    @Override
    public void onLikeClick() {
        Log.d("gm","点赞");
    }
}
