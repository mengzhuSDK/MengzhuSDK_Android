package com.mengzhu.sdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.mengzhu.sdk.download.util.SharePreUtil;
import com.mengzhu.upload.callback.MZCreateTaskCallback;
import com.mengzhu.upload.callback.MZUploadTaskCallback;
import com.mengzhu.upload.manage.MZUploadManager;
import com.mengzhu.upload.manage.MZUploadTask;
import com.mzmedia.utils.MUIImmerseUtils;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class TestUploadActivity extends Activity {

    private ListView mListView;
    private TestUploadAdapter uploadAdapter;
    private UploadData uploadData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MUIImmerseUtils.setStatusTextColor(false , this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_test_layout);
        MZUploadManager.init(this);
        MZUploadManager.setMzCreateTaskCallback(mzCreateTaskCallback);
        MZUploadManager.setMzUploadTaskCallback(mzUploadTaskCallback);
        mListView = findViewById(R.id.upload_list);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.new_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceVideo();
            }
        });
        findViewById(R.id.delete_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MZUploadManager.delAllTask();
                uploadAdapter.clearData();
                uploadData.setUploadList(MZUploadManager.getUploadList());
            }
        });
        uploadAdapter = new TestUploadAdapter(this, mListView);
        mListView.setAdapter(uploadAdapter);

        uploadData = new UploadData();

        //先从内存中获取缓存，如果没有从本地获取
        if (!MZUploadManager.getUploadList().isEmpty()) {
            uploadData.setUploadList(MZUploadManager.getUploadList());
        } else {
            uploadData = SharePreUtil.getObject("UploadData", this, "uploadData", UploadData.class);
            if (uploadData == null) {
                uploadData = new UploadData();
            }
        }
        if (uploadData.getUploadList() != null && !uploadData.getUploadList().isEmpty()) {
            for (int i = 0; i < uploadData.getUploadList().size(); i++) {
                MZUploadManager.createTask(uploadData.getUploadList().get(i));
            }
        }
    }

    /**
     * 从相册中选择视频
     */
    private void choiceVideo() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 66);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 66 && resultCode == RESULT_OK && null != data) {
            Uri selectedVideo = data.getData();
            String[] filePathColumn = {MediaStore.Video.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedVideo,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String VIDEOPATH = cursor.getString(columnIndex);
            Log.e("Tag", "onActivityResult: " + VIDEOPATH);
            create(VIDEOPATH);
            cursor.close();
        }
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
    }

    public void create(String filePath) {
        String recordDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mz_record/";

        File recordDir = new File(recordDirectory);

        // 确保断点记录的保存文件夹已存在，如果不存在则新建断点记录的保存文件夹。
        if (!recordDir.exists()) {
            recordDir.mkdirs();
        }
        MZUploadTask uploadTask = new MZUploadTask();
        uploadTask.setFilePath(filePath);
        uploadTask.setRecordPath(recordDirectory);
        uploadTask.setTaskName(filePath);
        MZUploadManager.createTask(uploadTask);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uploadData.setUploadList(MZUploadManager.getUploadList());
        //缓存上传任务到share里面
        SharePreUtil.putObject("UploadData", this, "uploadData", UploadData.class, uploadData);
    }

    //创建任务回调
    MZCreateTaskCallback mzCreateTaskCallback = new MZCreateTaskCallback() {
        @Override
        public void Success(MZUploadTask task) {
            if (!uploadAdapter.getData().contains(task)) {
                uploadAdapter.addData(task);
            }
        }

        @Override
        public void onFailure(MZUploadTask task, int errorCode, String errorMsg) {
            Log.e("Mengzhu", "onFailure: " + errorMsg);
        }
    };

    //上传任务回调
    MZUploadTaskCallback mzUploadTaskCallback = new MZUploadTaskCallback() {
        @Override
        public void Success(MZUploadTask task) {
            int position = uploadAdapter.getData().indexOf(task);
            uploadAdapter.updateData(position, mListView, task);
        }

        @Override
        public void onFailure(MZUploadTask task, int errorCode, String errorMsg) {
            int position = uploadAdapter.getData().indexOf(task);
            uploadAdapter.updateData(position, mListView, task);
        }

        @Override
        public void onProgress(MZUploadTask task, long currentSize, long totalSize) {
            int position = uploadAdapter.getData().indexOf(task);
            uploadAdapter.updateData(position, mListView, task);
        }
    };

    //本地记录上传任务的实体类
    class UploadData implements Serializable {
        private List<MZUploadTask> uploadList;

        public List<MZUploadTask> getUploadList() {
            return uploadList;
        }

        public void setUploadList(List<MZUploadTask> uploadList) {
            this.uploadList = uploadList;
        }
    }
}