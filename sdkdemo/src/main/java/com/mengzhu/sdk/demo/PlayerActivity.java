package com.mengzhu.sdk.demo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mengzhu.live.sdk.business.dto.AnchorInfoDto;
import com.mengzhu.live.sdk.business.dto.MZGoodsListDto;
import com.mengzhu.live.sdk.business.dto.MZOnlineUserListDto;
import com.mengzhu.live.sdk.business.dto.ad.MZAdDto;
import com.mengzhu.live.sdk.business.dto.chat.ChatTextDto;
import com.mengzhu.live.sdk.business.dto.play.PlayInfoDto;
import com.mengzhu.live.sdk.core.utils.ToastUtils;
import com.mengzhu.live.sdk.ui.widgets.popupwindow.MZFullScreenPopupWindow;
import com.mzmedia.IPlayerClickListener;
import com.mzmedia.fragment.PlayerFragment;
import com.mzmedia.utils.MUIImmerseUtils;
import com.mzmedia.widgets.popupwindow.MZWatchAuthPopWindow;

/**
 * Created by DELL on 2018/10/12.
 */
public class PlayerActivity extends AppCompatActivity implements IPlayerClickListener{
    private FragmentManager mFragmentManager;
    private PlayerFragment mPlayerFragment;
    private MZWatchAuthPopWindow authPopWindow;
    private String mTicketId;
    private String phone;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    showFullScreenDialog();
                    break;
                case 2:
                    mFragmentManager.beginTransaction().replace(R.id.container_activity_watch_broadcast, mPlayerFragment).commitAllowingStateLoss();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        MUIImmerseUtils.setStatusTranslucent(getWindow(), this);
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppCompatTheme);
        setContentView(R.layout.test_play_layout);
        MUIImmerseUtils.setStatusTextColor(false, this);
        mTicketId = getIntent().getStringExtra(PlayerFragment.TICKET_ID);
        phone = getIntent().getStringExtra("phone");
        //传递用户信息
        mPlayerFragment = PlayerFragment.newInstance(
                getIntent().getStringExtra(PlayerFragment.APP_ID),
                getIntent().getStringExtra(PlayerFragment.AVATAR),
                getIntent().getStringExtra(PlayerFragment.NICKNAME),
                getIntent().getStringExtra(PlayerFragment.UNIQUE_ID),
                getIntent().getStringExtra(PlayerFragment.TICKET_ID));
        mFragmentManager = getSupportFragmentManager();
        authPopWindow = new MZWatchAuthPopWindow(this, mTicketId, phone);
        authPopWindow.setOnCheckResultListener(new MZWatchAuthPopWindow.OnCheckResultListener() {
            @Override
            public void onSuccess() {
                isFullScreen();
            }

            @Override
            public void onFailed() {
                finish();
            }
        });
        mPlayerFragment.setIPlayerClickListener(this);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        authPopWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    private MZFullScreenPopupWindow popUp;
    private boolean isLink = false;
    private void isFullScreen(){
        popUp = new MZFullScreenPopupWindow(this,mTicketId);
        popUp.setClippingEnabled(false);
        popUp.setBackgroundDrawable(null);
        popUp.setOnGetMZAdResult(new MZFullScreenPopupWindow.OnGetMZAdResultListener() {
            @Override
            public void onMzAdResult(MZAdDto mzAdDto) {
                /**
                 * MZAdFullScreenDto 开屏广告
                 */
                if(mzAdDto.full_screen.getContent()!=null){
                    if(mzAdDto.full_screen.getContent().size()>0){
                        handler.sendEmptyMessage(1);
                    }
                }else {
                    handler.sendEmptyMessage(2);
                }
            }
        });
        popUp.setOnImagerClickListener(new MZFullScreenPopupWindow.OnImagerClickListener() {
            @Override
            public void onImagerClick(String link) {
                //点击开屏广告监听并返回对应网络地址
                ToastUtils.popUpToast("点击获取外链="+link);
//                isLink = true;
                if (!TextUtils.isEmpty(link))
                popUp.dismiss();
            }
        });
        popUp.setOnFullScreenDismissListener(new MZFullScreenPopupWindow.OnFullScreenDismiss() {
            @Override
            public void onDismiss() {
                //弹窗关闭回调
                if(!isLink){
                    handler.sendEmptyMessage(2);
                }
            }
        });
    }
    private void showFullScreenDialog(){
        popUp.showAtLocation(findViewById(R.id.container_activity_watch_broadcast), Gravity.CENTER, 0, 0);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //点击开屏广告跳转后来后加载fragment 不跳转可以不写
//        if(isLink){
//            isLink = false;
//            handler.sendEmptyMessage(2);
//        }
        mPlayerFragment.setControllerNoAutoGone();
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
