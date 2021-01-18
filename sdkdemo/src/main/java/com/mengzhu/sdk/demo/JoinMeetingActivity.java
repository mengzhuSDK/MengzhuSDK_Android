package com.mengzhu.sdk.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mzmedia.utils.MUIImmerseUtils;

import androidx.fragment.app.FragmentActivity;
import tv.mengzhu.core.frame.coreutils.JurisdictionUtils;
import tv.mengzhu.core.frame.coreutils.URLParamsUtils;
import tv.mengzhu.core.wrap.library.utils.CommonUtil;
import tv.mengzhu.core.wrap.user.modle.UserDto;
import tv.mengzhu.netmeeting.MZNetMeetingEventCallBack;
import tv.mengzhu.netmeeting.MZNetMeetingEventEnum;
import tv.mengzhu.netmeeting.MZNetMeetingManage;

import static com.mengzhu.sdk.demo.TestPlayActivity.avatar;
import static com.mengzhu.sdk.demo.TestPlayActivity.nickName;

public class JoinMeetingActivity extends FragmentActivity implements View.OnClickListener {
    private ImageView mIvBack;
    private EditText mEtNo,mEtPassword;
    private ImageView mIs_Voice,mIs_Camera;
    private TextView mTvJoin;
    private boolean isVoice = true,isCamera = false;
    private String meeting_id, password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MUIImmerseUtils.setStatusTranslucent(getWindow() , this);
        setContentView(R.layout.activity_mz_join_meeting);
        initView();
    }

    private void initView() {
        mIvBack = findViewById(R.id.mz_join_back);
        mEtNo = (EditText) findViewById(R.id.et_video_conferencing_number);
        mEtPassword = (EditText) findViewById(R.id.et_video_conferencing_password);
        mIs_Voice = (ImageView) findViewById(R.id.iv_video_conferencing_is_voice);
        mIs_Camera = (ImageView) findViewById(R.id.iv_video_conferencing_is_camera);
        mTvJoin = (TextView) findViewById(R.id.tv_video_conferencing_join);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mIs_Voice.setOnClickListener(this);
        mIs_Camera.setOnClickListener(this);
        mTvJoin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.iv_video_conferencing_is_voice){
            isVoice = !isVoice;
            mIs_Voice.setSelected(!isVoice);

        }
        if(v.getId() == R.id.iv_video_conferencing_is_camera){
            isCamera = !isCamera;
            mIs_Camera.setSelected(isCamera);
        }
        if(v.getId() == R.id.tv_video_conferencing_join){
            if(CommonUtil.isFastDoubleClick()){
                return;
            }
            if(JurisdictionUtils.essentialRoot(this)){
                URLParamsUtils.setSecretKey(DemoApplication.secretKey);
                UserDto userDto =new UserDto();
                userDto.setUniqueID("asdfghjkl");
                userDto.setAppid(DemoApplication.app_id);
                userDto.setAvatar(avatar);
                userDto.setNickname(nickName);
                MZNetMeetingManage.registerNetMeetingEventCallBack(new MZNetMeetingEventCallBack() {
                    @Override
                    public void mzNetMeetingEvent(MZNetMeetingEventEnum mzNetMeetingEventEnum) {
                        Log.e("TAG", "mzNetMeetingEvent: " + mzNetMeetingEventEnum);
                    }
                });
                MZNetMeetingManage.joinNetMeeting(this , userDto , mEtNo.getText().toString(),
                        mEtPassword.getText().toString(),
                        "用户昵称",isVoice,isCamera);
            }
        }
    }
}
