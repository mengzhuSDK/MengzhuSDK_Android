package com.mengzhu.sdk.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.mzmedia.fragment.PlayerFragment;
import com.mzmedia.utils.MUIImmerseUtils;

import tv.mengzhu.sdk.module.PlayerEventListener;

/**
 * Created by DELL on 2018/10/12.
 */
public class PlayerActivity extends AppCompatActivity implements IPlayerClickListener{
    private FragmentManager mFragmentManager;
    private PlayerFragment mPlayerFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        MUIImmerseUtils.setStatusTranslucent(getWindow(), this);
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppCompatTheme);
        setContentView(R.layout.test_play_layout);
        MUIImmerseUtils.setStatusTextColor(false, this);
//        String ticketID = getIntent().getStringExtra("ticketid");
        //传递用户信息
        mPlayerFragment = PlayerFragment.newInstance(
                getIntent().getStringExtra(PlayerFragment.APP_ID),
                getIntent().getStringExtra(PlayerFragment.AVATAR),
                getIntent().getStringExtra(PlayerFragment.NICKNAME),
                getIntent().getStringExtra(PlayerFragment.UNIQUE_ID),
                getIntent().getStringExtra(PlayerFragment.TICKET_ID));
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().replace(R.id.container_activity_watch_broadcast, mPlayerFragment).commitAllowingStateLoss();
        mPlayerFragment.setIPlayerClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayerFragment.setControllerNoAutoGone();
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
        Toast.makeText(this,"点击主播头像",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAttentionClick(PlayInfoDto dto, TextView Attention) {
        Toast.makeText(this,"点击关注",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onOnlineClick(MZOnlineUserListDto onlineUserDto) {
        Toast.makeText(this, "点击在线人数", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCloseClick(PlayInfoDto dto) {
        Toast.makeText(this, "点击退出", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onReportClick(PlayInfoDto dto) {
        Toast.makeText(this, "点击举报", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onShareClick(PlayInfoDto dto) {
        Toast.makeText(this, "点击分享", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLikeClick(PlayInfoDto dto, ImageView Like) {

        Toast.makeText(this,"点击点赞",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRecommendGoods(MZGoodsListDto dto) {
        Toast.makeText(this, "点击推荐商品", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGoodsListItem(MZGoodsListDto dto) {
        Toast.makeText(this, "点击商品列表", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onChatAvatar(ChatTextDto dto) {
        Toast.makeText(this, "点击聊天用户头像", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNotLogin(PlayInfoDto dto) {
        Toast.makeText(this, "点击聊天未登录状态", Toast.LENGTH_LONG).show();
    }

    @Override
    public void resultAnchorInfo(AnchorInfoDto anchorInfoDto) {
//        Toast.makeText(this, "回调主播信息数据" + anchorInfoDto.getNickname(), Toast.LENGTH_LONG).show();
    }
}
