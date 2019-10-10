package com.mengzhu.live.sdk.demo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mengzhu.live.sdk.core.restreaming.ws.StreamAVOption;
import com.mengzhu.live.sdk.ui.push.MZPushManager;
import com.mengzhu.live.sdk.ui.push.PushStreamingListener;
import com.mengzhu.live.sdk.ui.push.StreamLiveCameraView;

public class LiveActivity extends AppCompatActivity implements View.OnClickListener,PushStreamingListener {
    private static final String TAG = LiveActivity.class.getSimpleName();
    private StreamLiveCameraView mLiveCameraView;
    private String rtmpUrl = "rtmp://push.live.t.zmengzhu.com/mz/4583ecae5088975e00008421?auth_key=1540833154-0-0-959469f500ade6c721e9eb83d2564af9" ;
    private Button btnStartStreaming;
    private Button btnStopStreaming;
    private Button btnStartRecord;
    private Button btnStopRecord;
    private Button btnFliter;
    private Button btnSwapCamera;
    private Button btnMirror;
    private ImageView imageView;
    private MZPushManager manager;
    private EditText edit_text;
    boolean isMirror = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        StatusBarUtils.setTranslucentStatus(this);
        mLiveCameraView=(StreamLiveCameraView)findViewById(R.id.stream_previewView);
        btnStartStreaming = (Button) findViewById(R.id.btn_startStreaming);
        btnStartStreaming.setOnClickListener(this);

        btnStopStreaming = (Button) findViewById(R.id.btn_stopStreaming);
        btnStopStreaming.setOnClickListener(this);

        btnStartRecord = (Button) findViewById(R.id.btn_startRecord);
        btnStartRecord.setOnClickListener(this);

        btnStopRecord = (Button) findViewById(R.id.btn_stopRecord);
        btnStopRecord.setOnClickListener(this);

        btnFliter = (Button) findViewById(R.id.btn_filter);
        btnFliter.setOnClickListener(this);

        btnSwapCamera = (Button) findViewById(R.id.btn_swapCamera);
        btnSwapCamera.setOnClickListener(this);


        btnMirror = (Button) findViewById(R.id.btn_mirror);
        btnMirror.setOnClickListener(this);

        imageView = (ImageView) findViewById(R.id.iv_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setVisibility(View.GONE);
            }
        });
        edit_text=findViewById(R.id.edit_text);

        StreamAVOption config=new StreamAVOption();
        config.streamUrl=rtmpUrl;
        manager=new MZPushManager(this,config);
        manager.initPushLive(mLiveCameraView);
        manager.setStreamingListener(new PushStreamingListener() {
            @Override
            public void onScreenShotResult(Bitmap bitmap) {
                //截帧回调
            }

            @Override
            public void onOpenConnectionResult(int i) {
                //开始连接回调
            }

            @Override
            public void onWriteError(int i) {
//                写入错误回调
            }

            @Override
            public void onCloseConnectionResult(int i) {
//                关闭连接回调
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_startStreaming://开始推流
                if(!manager.isStreaming()){
                    if(!TextUtils.isEmpty(edit_text.getText().toString())){
                        rtmpUrl=edit_text.getText().toString();
                        manager.setPushUrl(rtmpUrl);
                    }
                    manager.startStreaming();
                }
                break;
            case R.id.btn_stopStreaming://停止推流
                if(manager.isStreaming()){
                    manager.stopStreaming();
                }
                break;
            case R.id.btn_startRecord://开始录制
                if(!manager.isRecord()){
                    Toast.makeText(this,"开始录制视频", Toast.LENGTH_SHORT).show();
                    manager.startRecord();
                }
                break;
            case R.id.btn_stopRecord://停止录制
                if(manager.isRecord()){
                    manager.stopRecord();
                    Toast.makeText(this,"视频已成功保存至系统根目录的 Movies/WSLive文件夹中", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_filter://切换滤镜
                manager.cutFilter();
                break;
            case R.id.btn_swapCamera://切换摄像头
                manager.swapCamera();
                break;
            case R.id.btn_mirror://镜像
                if(isMirror){
                    manager.setMirror(true,false,false);
                }else {
                    manager.setMirror(true,true,true);
                }
                isMirror = !isMirror;
                break;
            default:
                break;
        }
    }

    @Override
    public void onScreenShotResult(Bitmap bitmap) {

    }

    @Override
    public void onOpenConnectionResult(int result) {

    }

    @Override
    public void onWriteError(int errno) {
        Toast.makeText(this,""+errno,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCloseConnectionResult(int result) {

    }
}
