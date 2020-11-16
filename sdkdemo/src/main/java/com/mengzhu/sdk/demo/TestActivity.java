package com.mengzhu.sdk.demo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.mengzhu.live.sdk.business.model.Paths;
import com.mengzhu.live.sdk.ui.api.MZApiDataListener;
import com.mengzhu.live.sdk.ui.api.MZApiRequest;
import com.mzmedia.utils.MUIImmerseUtils;

import tv.mengzhu.core.frame.coreutils.JurisdictionUtils;
import tv.mengzhu.core.frame.coreutils.URLParamsUtils;
import tv.mengzhu.core.wrap.netwock.Page;
import tv.mengzhu.core.wrap.user.modle.UserDto;
import tv.mengzhu.core.wrap.user.presenter.MyUserInfoPresenter;

import static com.mengzhu.sdk.demo.TestPlayActivity.avatar;
import static com.mengzhu.sdk.demo.TestPlayActivity.nickName;


/**
 * Created by DELL on 2018/10/12.
 */
public class TestActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        MUIImmerseUtils.setStatusTextColor(false , this);
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

    public void onUploadClick(View view) {
        URLParamsUtils.setSecretKey(DemoApplication.secretKey);
        UserDto userDto =new UserDto();
        userDto.setUniqueID("B123456789");
        userDto.setAppid(DemoApplication.app_id);
        userDto.setAvatar(avatar);
        userDto.setNickname(nickName);
        MyUserInfoPresenter.getInstance().saveUserinfo(userDto);
        if (JurisdictionUtils.isStorageRoot(this)) {
            Intent intent = new Intent(TestActivity.this , TestUploadActivity.class);
            startActivity(intent);
        }
    }
}
