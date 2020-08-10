package com.mengzhu.sdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mengzhu.live.sdk.business.dto.push.StartBroadcastInfoDto;
import com.mengzhu.live.sdk.business.dto.push.StartCreateDto;
import com.mengzhu.live.sdk.core.utils.DateUtils;
import com.mengzhu.live.sdk.ui.api.MZApiDataListener;
import com.mengzhu.live.sdk.ui.api.MZApiRequest;
import com.mzmedia.fragment.HalfPlayerFragment;
import com.mzmedia.fragment.PlayerFragment;

import java.net.URLEncoder;

import tv.mengzhu.core.frame.coreutils.JurisdictionUtils;
import tv.mengzhu.core.frame.coreutils.URLParamsUtils;
import tv.mengzhu.core.wrap.netwock.Page;
import tv.mengzhu.core.wrap.user.modle.UserDto;
import tv.mengzhu.core.wrap.user.presenter.MyUserInfoPresenter;


/**
 * Created by DELL on 2018/10/12.
 */
public class TestActivity extends Activity {

    public static String live_tk = "";
    public static String ticket_Id = "";
    public static String app_id = "";
    public static String unique_id_test = "";
    public static String channel_id = "";
    public static String secretKey = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
    }

    public void onPlayClick(View view) {
        Intent intent = new Intent(this , TestPlayActivity.class);
        startActivity(intent);
    }

    public void onPushClick(View view) {
        Intent intent = new Intent(this , TestPushActivity.class);
        startActivity(intent);
    }

    public void onDownLoadClick(View view) {
        Intent intent = new Intent(this , TestDownloadActivity.class);
        startActivity(intent);
    }
}
