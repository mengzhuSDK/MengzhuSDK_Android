package com.mengzhu.live.sdk.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.mengzhu.live.sdk.business.dto.MZGoodsListDto;
import com.mengzhu.live.sdk.business.dto.chat.ChatTextDto;
import com.mengzhu.live.sdk.business.dto.play.PlayInfoDto;
import com.mzmedia.IPlayerClickListener;
import com.mzmedia.fragment.PlayerFragment;
import com.mzmedia.utils.MUIImmerseUtils;

/**
 * Created by DELL on 2018/10/12.
 */
public class PlayerActivity extends AppCompatActivity implements  IPlayerClickListener {
    private FragmentManager mFragmentManager;
    private PlayerFragment mPlayerFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        MUIImmerseUtils.setStatusTranslucent(getWindow(),this);
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppCompatTheme);
        setContentView(R.layout.test_play_layout);
        MUIImmerseUtils.setStatusTextColor(false,this);
        String ticketID = getIntent().getStringExtra("ticketid");
        //传递用户信息
        mPlayerFragment = PlayerFragment.newInstance(
                getIntent().getStringExtra("uid"),
                getIntent().getStringExtra("appid"),
                getIntent().getStringExtra("avatar"),
                getIntent().getStringExtra("nickname"),
                getIntent().getStringExtra("accountNo"),
                ticketID);
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().replace(R.id.container_activity_watch_broadcast, mPlayerFragment).commitAllowingStateLoss();
        mPlayerFragment.setIPlayerClickListener(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
//        mManager.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
//        mManager.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mManager.onDestroy();
    }

    /**
     * 播放器上的控件点击回调
     */
    @Override
    public void onAvatarClick(PlayInfoDto dto) {
        Toast.makeText(this,"国民实现点击主播头像",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAttentionClick(PlayInfoDto dto) {
        Toast.makeText(this,"国民实现点击关注",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onOnlineClick() {
        Toast.makeText(this,"国民实现点击在线人数",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCloseClick(PlayInfoDto dto) {
        Toast.makeText(this,"国民实现点击退出",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onReportClick(PlayInfoDto dto) {
        Toast.makeText(this,"国民实现点击举报",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onShareClick(PlayInfoDto dto) {
        Toast.makeText(this,"国民实现点击分享",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLikeClick(PlayInfoDto dto) {
        Toast.makeText(this,"国民实现点击点赞",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRecommendGoods(PlayInfoDto dto) {
        Toast.makeText(this,"国民实现点击推荐商品",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGoodsListItem(MZGoodsListDto dto) {
        Toast.makeText(this,"国民实现点击商品列表",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onChatAvatar(ChatTextDto dto) {
        Toast.makeText(this,"国民实现点击聊天用户头像",Toast.LENGTH_LONG).show();
    }
}
