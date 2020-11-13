package com.mengzhu.sdk.demo;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mengzhu.live.sdk.business.dto.Config.MZCategoryDto;
import com.mengzhu.live.sdk.business.dto.Config.MZFCodeDto;
import com.mengzhu.live.sdk.business.dto.Config.MZWhiteListDto;
import com.mengzhu.live.sdk.business.dto.push.EndBroadcastInfoDto;
import com.mengzhu.live.sdk.business.dto.push.MZCheckPushDto;
import com.mengzhu.live.sdk.business.dto.push.StartBroadcastInfoDto;
import com.mengzhu.live.sdk.business.dto.push.StartCreateDto;
import com.mengzhu.live.sdk.core.utils.DateUtils;
import com.mengzhu.live.sdk.core.utils.ToastUtils;
import com.mengzhu.live.sdk.ui.api.MZApiDataListener;
import com.mengzhu.live.sdk.ui.api.MZApiRequest;
import com.mzmedia.utils.MUIImmerseUtils;
import com.mzmedia.widgets.MZCategoryListPopWindow;
import com.mzmedia.widgets.MZFCodeListPopWindow;
import com.mzmedia.widgets.MZWhiteListPopWindow;
import com.mzmedia.widgets.dialog.MessageDialog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import tv.mengzhu.core.frame.coreutils.JurisdictionUtils;
import tv.mengzhu.core.frame.coreutils.URLParamsUtils;
import tv.mengzhu.core.wrap.netwock.Page;
import tv.mengzhu.core.wrap.user.modle.UserDto;
import tv.mengzhu.core.wrap.user.presenter.MyUserInfoPresenter;

public class TestPushActivity extends Activity {

    private EditText unique_id;
    private EditText livetk;
    private EditText tv_ticket_id;
    private EditText time;
    private TextView category;
    private AppCompatCheckBox cbbeauty, cblater, cbAudio, cbAllBanChat, auto_record_check;

    private RelativeLayout rb360_layout;
    private RelativeLayout rb480_layout;
    private RelativeLayout rb720_layout;

    private ImageView rb360_iv;
    private ImageView rb480_iv;
    private ImageView rb720_iv;

    private TextView rb360_tv;
    private TextView rb480_tv;
    private TextView rb720_tv;

    private TextView tv_free;
    private TextView tv_f_code;
    private TextView tv_white_list;

    private int screen;
    private int bitrate = 500 * 1024;
    private MZApiRequest mzLiveCreateApiRequest;
    private MZApiRequest mzLiveStreamApiRequest;
    private MZApiRequest mzCheckPushApiRequest;
    private MZApiRequest mzStopLiveApiRequest;

    private boolean isAudioPush = false;

    private ProgressDialog progressDialog;

    int live_type = 0;

    private int view_mode = 1; //1，免费。5白名单，6，F码
    private int f_code_id = 1;
    private int white_list_id = 1; //默认值

    private int category_id = 1; //默认值1
    private int auto_record = 1; //默认生成回放
    private String notice = "默认提示语提示语";

    MZFCodeListPopWindow mzfCodeListPopWindow;
    MZWhiteListPopWindow mzWhiteListPopWindow;
    MZCategoryListPopWindow mzCategoryListPopWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MUIImmerseUtils.setStatusTextColor(false , this);
        setContentView(R.layout.activity_test_push);
        progressDialog = new ProgressDialog(this);

        mzLiveCreateApiRequest = new MZApiRequest();
        mzLiveStreamApiRequest = new MZApiRequest();
        mzStopLiveApiRequest = new MZApiRequest();
        mzCheckPushApiRequest = new MZApiRequest();

        mzLiveCreateApiRequest.createRequest(TestPushActivity.this, MZApiRequest.API_TYPE_LIVE_CREATE);
        mzLiveStreamApiRequest.createRequest(TestPushActivity.this, MZApiRequest.API_TYPE_LIVE_STREAM);
        mzStopLiveApiRequest.createRequest(TestPushActivity.this, MZApiRequest.API_TYPE_LIVE_STOP);
        mzCheckPushApiRequest.createRequest(TestPushActivity.this, MZApiRequest.API_CHECK_PUSH);

        mzStopLiveApiRequest.setResultListener(stopLiveApiListener);
        mzCheckPushApiRequest.setResultListener(checkPushApiListener);

        mzfCodeListPopWindow = new MZFCodeListPopWindow(TestPushActivity.this);
        mzWhiteListPopWindow = new MZWhiteListPopWindow(TestPushActivity.this);
        mzCategoryListPopWindow = new MZCategoryListPopWindow(TestPushActivity.this);

        unique_id = findViewById(R.id.account_no);
        findViewById(R.id.tv_push_p).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (JurisdictionUtils.essentialRoot(TestPushActivity.this)) {
                    isAudioPush = false;
                    screen = 1;
                    requestCheckPush();
                }
            }
        });
        findViewById(R.id.tv_push_l).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (JurisdictionUtils.essentialRoot(TestPushActivity.this)) {
                    isAudioPush = false;
                    screen = 2;
                    requestCheckPush();
                }
            }
        });
        findViewById(R.id.tv_audio_push).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (JurisdictionUtils.essentialRoot(TestPushActivity.this)) {
                    isAudioPush = true;
                    screen = 1;
                    requestCheckPush();
                }
            }
        });
        cbAllBanChat = findViewById(R.id.cb_all_banchat);
        cbbeauty = findViewById(R.id.cb_beauty);
        cblater = findViewById(R.id.cb_later);
        cbAudio = findViewById(R.id.cb_audio);
        livetk = findViewById(R.id.live_tk);
        auto_record_check = findViewById(R.id.auto_record);
        auto_record_check.setChecked(true);
        tv_ticket_id = findViewById(R.id.tv_ticket_id);
        time = findViewById(R.id.time);
        rb360_layout = findViewById(R.id.rb360);
        rb480_layout = findViewById(R.id.rb480);
        rb720_layout = findViewById(R.id.rb720);

        rb360_iv = findViewById(R.id.rb_img1);
        rb480_iv = findViewById(R.id.rb_img2);
        rb720_iv = findViewById(R.id.rb_img3);

        rb360_tv = findViewById(R.id.rb_text1);
        rb480_tv = findViewById(R.id.rb_text2);
        rb720_tv = findViewById(R.id.rb_text3);

        tv_free = findViewById(R.id.tv_free);
        tv_f_code = findViewById(R.id.tv_f_code);
        tv_white_list = findViewById(R.id.tv_white_list);
        tv_free.setBackgroundResource(R.drawable.mz_shape_rb_bg_select);
        tv_free.setTextColor(getResources().getColor(R.color.color_ff1f60));

        category = findViewById(R.id.category);
        mzCategoryListPopWindow.setOnDisMissListener(new MZCategoryListPopWindow.OnPopDisMissListener() {
            @Override
            public void onDismiss(MZCategoryDto mzCategoryDto) {
                if (mzCategoryDto != null) {
                    category.setText(mzCategoryDto.getName());
                    category_id = mzCategoryDto.getId();
                } else {
                    category.setText("");
                    category_id = 1;
                }
            }
        });
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mzCategoryListPopWindow.isShowing()) {
                    mzCategoryListPopWindow.initData();
                    mzCategoryListPopWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                }
            }
        });

        mzfCodeListPopWindow.setOnDisMissListener(new MZFCodeListPopWindow.OnPopDisMissListener() {
            @Override
            public void onDismiss(MZFCodeDto mzfCodeDto) {
                if (mzfCodeDto != null) {
                    view_mode = 6;
                    white_list_id = 1;
                    f_code_id = mzfCodeDto.getId();
                    setViewModeDefault();
                    tv_f_code.setBackgroundResource(R.drawable.mz_shape_rb_bg_select);
                    tv_f_code.setTextColor(getResources().getColor(R.color.color_ff1f60));
                } else {
                    if (view_mode == 6) {
                        view_mode = 1;
                        white_list_id = 1;
                        f_code_id = 1;
                        setViewModeDefault();
                        tv_free.setBackgroundResource(R.drawable.mz_shape_rb_bg_select);
                        tv_free.setTextColor(getResources().getColor(R.color.color_ff1f60));
                    }
                }
            }
        });
        tv_f_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mzfCodeListPopWindow.isShowing()) {
                    mzfCodeListPopWindow.initData();
                    mzfCodeListPopWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                }
            }
        });

        mzWhiteListPopWindow.setOnDisMissListener(new MZWhiteListPopWindow.OnPopDisMissListener() {

            @Override
            public void onDismiss(MZWhiteListDto.WhiteListItem whiteListItem) {
                if (whiteListItem != null) {
                    view_mode = 5;
                    f_code_id = 1;
                    white_list_id = whiteListItem.getId();
                    setViewModeDefault();
                    tv_white_list.setBackgroundResource(R.drawable.mz_shape_rb_bg_select);
                    tv_white_list.setTextColor(getResources().getColor(R.color.color_ff1f60));
                } else {
                    if (view_mode == 5) {
                        if (view_mode == 6) {
                            view_mode = 1;
                            white_list_id = 1;
                            f_code_id = 1;
                            setViewModeDefault();
                            tv_free.setBackgroundResource(R.drawable.mz_shape_rb_bg_select);
                            tv_free.setTextColor(getResources().getColor(R.color.color_ff1f60));
                        }
                    }
                }
            }
        });
        tv_white_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mzWhiteListPopWindow.isShowing()) {
                    mzWhiteListPopWindow.initData();
                    mzWhiteListPopWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                }
            }
        });

        tv_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_mode = 1;
                white_list_id = 1;
                f_code_id = 1;
                setViewModeDefault();
                tv_free.setBackgroundResource(R.drawable.mz_shape_rb_bg_select);
                tv_free.setTextColor(getResources().getColor(R.color.color_ff1f60));
            }
        });

        rb360_layout.setBackgroundResource(R.drawable.mz_shape_rb_bg_select);
        rb360_iv.setImageResource(R.mipmap.rb2);
        rb360_tv.setTextColor(getResources().getColor(R.color.color_ff1f60));

        rb360_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitrate = 500 * 1024;
                setRBDefault();
                rb360_layout.setBackgroundResource(R.drawable.mz_shape_rb_bg_select);
                rb360_iv.setImageResource(R.mipmap.rb2);
                rb360_tv.setTextColor(getResources().getColor(R.color.color_ff1f60));
            }
        });
        rb480_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitrate = 800 * 1024;
                setRBDefault();
                rb480_layout.setBackgroundResource(R.drawable.mz_shape_rb_bg_select);
                rb480_iv.setImageResource(R.mipmap.rb4);
                rb480_tv.setTextColor(getResources().getColor(R.color.color_ff1f60));
            }
        });
        rb720_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitrate = 1000 * 1024;
                setRBDefault();
                rb720_layout.setBackgroundResource(R.drawable.mz_shape_rb_bg_select);
                rb720_iv.setImageResource(R.mipmap.rb6);
                rb720_tv.setTextColor(getResources().getColor(R.color.color_ff1f60));
            }
        });

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        livetk.setText(DemoApplication.live_tk);
        unique_id.setText(DemoApplication.unique_id_test);
        URLParamsUtils.setSecretKey(DemoApplication.secretKey);
    }

    public void setRBDefault() {
        rb360_layout.setBackgroundResource(R.drawable.mz_shape_rb_bg_default);
        rb480_layout.setBackgroundResource(R.drawable.mz_shape_rb_bg_default);
        rb720_layout.setBackgroundResource(R.drawable.mz_shape_rb_bg_default);

        rb360_iv.setImageResource(R.mipmap.rb1);
        rb480_iv.setImageResource(R.mipmap.rb3);
        rb720_iv.setImageResource(R.mipmap.rb5);

        rb360_tv.setTextColor(getResources().getColor(R.color.white));
        rb480_tv.setTextColor(getResources().getColor(R.color.white));
        rb720_tv.setTextColor(getResources().getColor(R.color.white));
    }

    public void setViewModeDefault() {
        tv_free.setBackgroundResource(R.drawable.mz_shape_rb_bg_default);
        tv_f_code.setBackgroundResource(R.drawable.mz_shape_rb_bg_default);
        tv_white_list.setBackgroundResource(R.drawable.mz_shape_rb_bg_default);

        tv_free.setTextColor(getResources().getColor(R.color.white));
        tv_f_code.setTextColor(getResources().getColor(R.color.white));
        tv_white_list.setTextColor(getResources().getColor(R.color.white));
    }

    private void requestCheckPush() {
        progressDialog.show();
        // 传入频道id
        mzCheckPushApiRequest.startData(MZApiRequest.API_CHECK_PUSH, DemoApplication.channel_id);
    }

    private void requestStopLive(String ticketId) {
        progressDialog.show();
        // 传入活动id
        mzStopLiveApiRequest.startData(MZApiRequest.API_TYPE_LIVE_STOP, ticketId);
    }

    private void startPush() {
        UserDto dto = new UserDto();
        dto.setUniqueID(unique_id.getText().toString());
        dto.setAppid(DemoApplication.app_id);
        dto.setAvatar("http://s1.t.zmengzhu.com/upload/img/50/6d/506da693ecb2cf6f2fd0e3e92656dde4.png");
        dto.setNickname("T丶????");
        MyUserInfoPresenter.getInstance().saveUserinfo(dto);
        URLParamsUtils.setSecretKey(DemoApplication.secretKey);

        if (isAudioPush) {
            live_type = 1;
        }
        //demo示例
        if (!TextUtils.isEmpty(livetk.getText().toString())) {
            mzLiveStreamApiRequest.setResultListener(new TestPushActivity.StartStream());
            progressDialog.show();
            mzLiveStreamApiRequest.startData(MZApiRequest.API_TYPE_LIVE_STREAM,
                    livetk.getText().toString(),
                    unique_id.getText().toString(),
                    URLEncoder.encode("T丶????"),
                    URLEncoder.encode("http://s1.t.zmengzhu.com/upload/img/50/6d/506da693ecb2cf6f2fd0e3e92656dde4.png"), tv_ticket_id.getText().toString());
        } else {
            final int finalLive_type = live_type;
            mzLiveCreateApiRequest.setResultListener(new MZApiDataListener() {
                @Override
                public void dataResult(String s, Object o, Page page, int status) {
                    StartCreateDto dto = (StartCreateDto) o;
                    livetk.setText(dto.getLive_tk());
                    tv_ticket_id.setText(dto.getTicket_id());
                    mzLiveStreamApiRequest.setResultListener(new TestPushActivity.StartStream());
                    mzLiveStreamApiRequest.startData(MZApiRequest.API_TYPE_LIVE_STREAM,
                            dto.getLive_tk(),
                            unique_id.getText().toString(),
                            URLEncoder.encode("T丶????"),
                            URLEncoder.encode("http://s1.t.zmengzhu.com/upload/img/50/6d/506da693ecb2cf6f2fd0e3e92656dde4.png"), dto.getTicket_id());
                }

                @Override
                public void errorResult(String s, int i, String s1) {

                }
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(TestPushActivity.this);
            builder.setTitle("提示");
            builder.setMessage("此创建活动功能API接口权限所属于服务端，提供此功能为了便于demo演示。如接入时考虑数据安全等因素建议通过服务访问API并将live_tk、ticket_id传入SDK进行推流。");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //debug模式下提供此api用于测试
                    mzLiveCreateApiRequest.startData(MZApiRequest.API_TYPE_LIVE_CREATE,
                            "直播活动描述" + DateUtils.stringToDateNoymds(System.currentTimeMillis() + ""), //活动介绍
                            DemoApplication.channel_id, //渠道号
                            live_type, //语音还是视频直播
                            screen == 2 ? "0" : "1", //横屏还是竖屏
                            "http://s1.t.zmengzhu.com/upload/img/50/6d/506da693ecb2cf6f2fd0e3e92656dde4.png", //活动介绍直播封面
                            "test" + DateUtils.stringToDateNoymds(System.currentTimeMillis() + ""), //活动标题
                            auto_record, //是否生成回放
                            view_mode, //观看方式id
                            white_list_id, //白名单id
                            f_code_id, //F码id
                            notice, //光看方式提示语提示语
                            category_id); //分类id
                }
            });
            builder.show();
        }
    }

    /**
     * 推流检测请求回调
     */
    MZApiDataListener checkPushApiListener = new MZApiDataListener() {
        @Override
        public void dataResult(String apiType, Object dto, Page page, int status) {
            progressDialog.dismiss();
            if (dto instanceof MZCheckPushDto) {
                final MZCheckPushDto mzCheckPushDto = (MZCheckPushDto) dto;
                //支持同时发起多路推流或者未开播直接发起创建活动并推流
                if (mzCheckPushDto.getIs_multipath() == 1 || mzCheckPushDto.getIs_live() == 0) {
                    startPush();
                } else {
                    if (mzCheckPushDto.getLive_info() != null && !TextUtils.isEmpty(mzCheckPushDto.getLive_info().getTicket_id())
                            && !TextUtils.isEmpty(mzCheckPushDto.getLive_info().getLive_tk())) {
                        MessageDialog messageDialog = new MessageDialog(TestPushActivity.this);
                        messageDialog.setTitle("提示");
                        messageDialog.setContentMessage("发现当前频道有正在直播的活动，如果开始新的直播，旧的直播将自动关闭");
                        messageDialog.setCancelText("继续直播");
                        messageDialog.setConfirmText("新的直播");
                        messageDialog.setMessageDialogCallBack(new MessageDialog.OnMessageDialogCallBack() {
                            @Override
                            public void onClick(boolean isConfirm) {
                                if (isConfirm) {
                                    requestStopLive(mzCheckPushDto.getLive_info().getTicket_id());
                                }else {
                                    livetk.setText(mzCheckPushDto.getLive_info().getLive_tk());
                                    tv_ticket_id.setText(mzCheckPushDto.getLive_info().getTicket_id());
                                    startPush();
                                }
                            }
                        });
                        messageDialog.show();
                    } else {
                        ToastUtils.popUpToast("活动信息错误");
                    }
                }
            }
        }

        @Override
        public void errorResult(String apiType, int code, String msg) {
            progressDialog.dismiss();
        }
    };

    /**
     * 结束直播请求回调
     */
    MZApiDataListener stopLiveApiListener = new MZApiDataListener() {
        @Override
        public void dataResult(String apiType, Object dto, Page page, int status) {
            progressDialog.dismiss();
            if (dto instanceof EndBroadcastInfoDto){
                startPush();
            }
        }

        @Override
        public void errorResult(String apiType, int code, String msg) {
            progressDialog.dismiss();
        }
    };

    private class StartStream implements MZApiDataListener {

        @Override
        public void dataResult(String s, Object o, Page page, int status) {
            progressDialog.dismiss();
            StartBroadcastInfoDto dto = (StartBroadcastInfoDto) o;
            Intent intent = new Intent(TestPushActivity.this, PusherActivity.class);
            intent.putExtra("pushDto", dto);
            intent.putExtra("screen", screen);
            intent.putExtra("bitrate", bitrate);
            intent.putExtra("cbbeauty", cbbeauty.isChecked());
            intent.putExtra("cblater", cblater.isChecked());
            intent.putExtra("cbAudio", cbAudio.isChecked());
            intent.putExtra("cbAllBanChat", cbAllBanChat.isChecked());
            intent.putExtra("livetk", livetk.getText().toString());
            intent.putExtra("fps", "");
            intent.putExtra("time", time.getText().toString());
            intent.putExtra("isAudioPush", isAudioPush);
            TestPushActivity.this.startActivity(intent);
        }

        @Override
        public void errorResult(String s, int i, String s1) {
            progressDialog.dismiss();
        }
    }
}