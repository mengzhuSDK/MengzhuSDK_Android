package com.mengzhu.sdk.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mengzhu.live.sdk.business.dto.AnchorInfoDto;
import com.mengzhu.live.sdk.business.dto.MZGoodsListDto;
import com.mengzhu.live.sdk.business.dto.MZOnlineUserListDto;
import com.mengzhu.live.sdk.business.dto.chat.ChatTextDto;
import com.mengzhu.live.sdk.business.dto.play.PlayInfoDto;
import com.mzmedia.IPlayerClickListener;
import com.mzmedia.fragment.HalfPlayerFragment;
import com.mzmedia.utils.MUIImmerseUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tv.mengzhu.sdk.module.PlayerEventListener;

/**
 * Created by DELL on 2018/10/12.
 */
public class HalfPlayerActivity extends AppCompatActivity implements IPlayerClickListener, PlayerEventListener {
    private FragmentManager mFragmentManager;
    private HalfPlayerFragment mPlayerFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        MUIImmerseUtils.setStatusTranslucent(getWindow(), this);
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppCompatTheme);
        setContentView(R.layout.test_play_layout);
        MUIImmerseUtils.setStatusTextColor(false, this);
//        String ticketID = getIntent().getStringExtra("ticketid");
        //传递用户信息
        mPlayerFragment = HalfPlayerFragment.newInstance(
                getIntent().getStringExtra(HalfPlayerFragment.APP_ID),
                getIntent().getStringExtra(HalfPlayerFragment.AVATAR),
                getIntent().getStringExtra(HalfPlayerFragment.NICKNAME),
                getIntent().getStringExtra(HalfPlayerFragment.UNIQUE_ID),
                getIntent().getStringExtra(HalfPlayerFragment.TICKET_ID));
        List<HashMap<String , Fragment>> tabs = new ArrayList<>();
        HashMap<String , Fragment> tab = new HashMap<>();
        tab.put("测试Tab" ,new Fragment());
        tabs.add(tab);
//        mPlayerFragment.setBottomTabs(tabs); // 添加底部fragment replace之前调用
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().replace(R.id.container_activity_watch_broadcast, mPlayerFragment).commitAllowingStateLoss();
        mPlayerFragment.setIPlayerClickListener(this);
        mPlayerFragment.setPlayerEventListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(mPlayerFragment!=null){
//            UserDto user= MyUserInfoPresenter.getInstance().getUserInfo();
//            user.setAccountNo("GM20181202092745000830");
//            user.setNickname("11111");
//            user.setAvatar("https://upload.jianshu.io/users/upload_avatars/11711317/38d64087-b8c9-489a-b203-9f297e35e1e7?imageMogr2/auto-orient/strip|imageView2/1/w/96/h/96/format/webp");
//            mPlayerFragment.loginCallback(user);
//        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 播放器上的控件点击回调
     */
    @Override
    public void onAvatarClick(AnchorInfoDto dto) {
        Toast.makeText(this, "国民实现点击主播头像", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAttentionClick(PlayInfoDto dto, TextView Attention) {
        Toast.makeText(this, "国民实现点击关注", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onOnlineClick(MZOnlineUserListDto onlineUserDto) {
        Toast.makeText(this, "国民实现点击在线人数", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCloseClick(PlayInfoDto dto) {
        Toast.makeText(this, "国民实现点击退出", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onReportClick(PlayInfoDto dto) {
        Toast.makeText(this, "国民实现点击举报", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onShareClick(PlayInfoDto dto) {
        Toast.makeText(this, "国民实现点击分享", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLikeClick(PlayInfoDto dto, ImageView Like) {

        Toast.makeText(this, "国民实现点击点赞", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRecommendGoods(MZGoodsListDto dto) {
        Toast.makeText(this, "国民实现点击推荐商品", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGoodsListItem(MZGoodsListDto dto) {
        Toast.makeText(this, "国民实现点击商品列表", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onChatAvatar(ChatTextDto dto) {
        Toast.makeText(this, "国民实现点击聊天用户头像", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNotLogin(PlayInfoDto dto) {
        Toast.makeText(this, "国民实现点击聊天未登录状态", Toast.LENGTH_LONG).show();
    }

    @Override
    public void resultAnchorInfo(AnchorInfoDto anchorInfoDto) {
        Toast.makeText(this, "回调主播信息数据" + anchorInfoDto.getNickname(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void hideAllEvent() {
        mPlayerFragment.showAllEvent();
    }

    @Override
    public void showAllEvent() {
        mPlayerFragment.hideAllEvent();
    }

    @Override
    public void onBackClick(boolean b) {

    }

    @Override
    public void onPausePlayer() {

    }

    @Override
    public void onStartPlayer() {

    }

    @Override
    public void onForbid(boolean b) {

    }
}
