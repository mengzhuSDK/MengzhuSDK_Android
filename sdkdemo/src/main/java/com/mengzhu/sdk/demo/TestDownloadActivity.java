package com.mengzhu.sdk.demo;

import android.Manifest;
import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.mengzhu.sdk.download.MZDownloadManager;
import com.mengzhu.sdk.download.controller.scheduler.M3U8PeerTaskListener;
import com.mengzhu.sdk.download.controller.scheduler.NormalTaskListener;
import com.mengzhu.sdk.download.library.publics.core.download.DownloadEntity;
import com.mengzhu.sdk.download.library.publics.core.task.DownloadTask;
import com.mengzhu.sdk.download.library.publics.util.ALog;
import com.mengzhu.sdk.download.permission.OnPermissionCallback;
import com.mengzhu.sdk.download.permission.PermissionManager;
import com.mengzhu.sdk.download.util.UrlPathUtil;
import com.mzmedia.utils.MUIImmerseUtils;

import java.util.ArrayList;
import java.util.List;

public class TestDownloadActivity extends Activity implements NormalTaskListener<DownloadTask>, M3U8PeerTaskListener {
    private static final String TAG = TestDownloadActivity.class.getSimpleName();
    private ListView mListView;
    private TestDownloadAdapter mAdapter;
    private final String M3U8_PATH_KEY = "M3U8_PATH_KEY";
    private static int urlListCount=0;
    private int sort=1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MUIImmerseUtils.setStatusTextColor(false , this);
        setContentView(R.layout.download_test_layout);
        mListView=findViewById(R.id.download_list);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        MZDownloadManager.getInstance().init(this);
        mAdapter=new TestDownloadAdapter(this);
        mListView.setAdapter(mAdapter);

        findViewById(R.id.delete_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MZDownloadManager.getInstance().getDownloadContrller().removeAllTask(true);
                List<DownloadEntity> entityList= MZDownloadManager.getInstance().getDownloadTaskBiz().getTaskList();

                urlListCount=entityList!=null?entityList.size():0;
            }
        });
        final List<String> list=new ArrayList<>();
        list.add("http://vod-o.t.zmengzhu.com/record/base/6eab6cca45f2368b00086104.m3u8");
        list.add("http://vod01.zmengzhu.com/record/base/hls-sd/0e00257c1f0a454d00135606.m3u8");
        list.add("http://vod01-o.zmengzhu.com/record/base/7690f2a466ff762900132847.m3u8");
        list.add("http://vod01.zmengzhu.com/record/base/hls-sd/e06be438f4d0ce7d00130063.m3u8");
        list.add("http://vod01-o.zmengzhu.com/record/base/9541b5593394a4ca00169189.m3u8");
        list.add("http://vod01-o.zmengzhu.com/record/base/c3625a922608f2c400169177.m3u8");
        list.add("http://vod01-o.zmengzhu.com/record/base/8b32ae8158b8d1f100166876.m3u8");
        list.add("http://vod01-o.zmengzhu.com/record/base/2e491d39c62da5fb00165689.m3u8");
        list.add("http://vod01-o.zmengzhu.com/record/base/a6ee4b50e891e3c100157759.m3u8");
        list.add("http://vod01-o.zmengzhu.com/record/base/03c469a23567e8c900150369.m3u8");
        findViewById(R.id.new_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String downloadUrl="http://vod01.zmengzhu.com/record/base/hls-sd/c775c343b2c0eb4d00140907.m3u8";
                if(urlListCount<list.size()) {
                    String downloadUrl = list.get(urlListCount);
                    Uri uri = Uri.parse(downloadUrl);
                    //String parentUrl = "http://" + uri.getHost() + "/gear1/";
                    int index = downloadUrl.lastIndexOf("/");
                    int indexs = downloadUrl.lastIndexOf(".");
                    String parentUrl = downloadUrl.substring(index + 1, indexs + 1);
                    String path = UrlPathUtil.getConfigValue(TestDownloadActivity.this, M3U8_PATH_KEY, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + parentUrl + ".m3u8");

                    MZDownloadManager.getInstance().getDownloadContrller().startDownload(downloadUrl, path,parentUrl,"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1586499217528&di=6679e5aebd960a64b2a14b7c72313834&imgtype=0&src=http%3A%2F%2Fa0.att.hudong.com%2F78%2F52%2F01200000123847134434529793168.jpg","312312");
                    urlListCount++;
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        findViewById(R.id.sort_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort=sort==1?2:1;
                ((TextView)view).setText(sort==1?"降序":"升序");
                mAdapter.setSort(sort);
                mAdapter.notifyDataSetChanged();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean hasPermission = PermissionManager.getInstance()
                    .checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (!hasPermission) {
                PermissionManager.getInstance().requestPermission(this, new OnPermissionCallback() {
                    @Override public void onSuccess(String... permissions) {

                    }

                    @Override public void onFail(String... permissions) {
                        finish();
                    }
                }, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }


    @Override
    public void onPeerStart(String m3u8Url, String peerPath, int peerIndex) {
        ALog.d(TAG,
                "onPeerStart");
    }

    @Override
    public void onPeerComplete(String m3u8Url, String peerPath, int peerIndex) {
        ALog.d(TAG,
                "onPeerComplete");
    }

    @Override
    public void onPeerFail(String m3u8Url, String peerPath, int peerIndex) {
        ALog.d(TAG,
                "onPeerFail");
    }

    @Override
    public void onWait(DownloadTask task) {
        ALog.d(TAG,
                "onWait");
    }

    @Override
    public void onPre(DownloadTask task) {
        ALog.d(TAG,
                "onPre");
    }

    @Override
    public void onTaskPre(DownloadTask task) {
        ALog.d(TAG,
                "onTaskPre");
//        mAdapter.notifyDataSetChanged();
        mAdapter.updataData(mListView,task.getEntity());
    }

    @Override
    public void onTaskResume(DownloadTask task) {
        ALog.d(TAG,
                "onTaskResume");
//        mAdapter.notifyDataSetChanged();
        mAdapter.updataData(mListView,task.getEntity());
    }

    @Override
    public void onTaskStart(DownloadTask task) {
        ALog.d(TAG,
                "onTaskStart");
        mAdapter.notifyDataSetChanged();
//        mAdapter.updataData(mListView,task.getEntity());
    }

    @Override
    public void onTaskStop(DownloadTask task) {
//        mAdapter.notifyDataSetChanged();
        mAdapter.updataData(mListView,task.getEntity());
        ALog.d(TAG,
                "onTaskStop");
    }

    @Override
    public void onTaskCancel(DownloadTask task) {
        mAdapter.notifyDataSetChanged();
        ALog.d(TAG,
                "onTaskCancel");
    }

    @Override
    public void onTaskFail(DownloadTask task) {
        ALog.d(TAG,
                "onTaskFail");
    }

    @Override
    public void onTaskFail(DownloadTask task, Exception e) {
        ALog.d(TAG,
                "onTaskFail");
//        mAdapter.notifyDataSetChanged();
        mAdapter.updataData(mListView,task.getEntity());
    }


    @Override
    public void onTaskComplete(DownloadTask task) {
        ALog.d(TAG,
                "onTaskComplete");
//        mAdapter.notifyDataSetChanged();
        mAdapter.updataData(mListView,task.getEntity());
    }

    @Override
    public void onTaskRunning(DownloadTask task) {
//        mAdapter.notifyDataSetChanged();
        mAdapter.updataData(mListView,task.getEntity());
        Log.i(TAG,
                " task.getEntity().id"+ task.getEntity().getId()+"m3u8 void running, p = " + task.getPercent() + ", speed  = " + task.getConvertSpeed());
//        task.getEntity();
    }

    @Override
    public void onNoSupportBreakPoint(DownloadTask task) {
        ALog.d(TAG,
                "onNoSupportBreakPoint");
    }

    @Override
    public void setListener(Object obj) {
        ALog.d(TAG,
                "setListener");
    }
}
