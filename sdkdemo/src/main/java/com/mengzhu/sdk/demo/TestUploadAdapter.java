package com.mengzhu.sdk.demo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mengzhu.upload.manage.IUploadState;
import com.mengzhu.upload.manage.MZUploadManager;
import com.mengzhu.upload.manage.MZUploadTask;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestUploadAdapter extends BaseAdapter {
    private Context mContext;
    private ListView listView;
    private List<MZUploadTask> mListEntity = new ArrayList<>();

    public TestUploadAdapter(Context context , ListView listView) {
        mContext = context;
        this.listView = listView;
    }

    public List<MZUploadTask> getData(){
        return mListEntity;
    }

    public void addData(final MZUploadTask uploadTask){
        mListEntity.add(uploadTask);
        notifyDataSetChanged();
    }

    public void clearData(){
        mListEntity.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mListEntity != null) {
            return mListEntity != null ? mListEntity.size() : 0;
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        return mListEntity.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        final MZUploadTask uploadTask = mListEntity.get(i);
        if (view != null) {
            viewHolder = (ViewHolder) view.getTag();
        } else {
            view = View.inflate(mContext, R.layout.download_test_item, null);
            viewHolder = new ViewHolder();
            viewHolder.icon = view.findViewById(R.id.icon);
            viewHolder.name = view.findViewById(R.id.name);
            viewHolder.real_time = view.findViewById(R.id.real_time);
            viewHolder.download_progress = view.findViewById(R.id.download_progress);
            viewHolder.ProgressBar = view.findViewById(R.id.ProgressBar);
            viewHolder.start_stop_btn = view.findViewById(R.id.start_stop_btn);
            viewHolder.cancel_btn = view.findViewById(R.id.cancel_btn);
            viewHolder.item_layout = view.findViewById(R.id.item_layout);
            viewHolder.download_progress.setVisibility(View.GONE);
            view.setTag(viewHolder);

        }
        viewHolder.item_layout.setClickable(false);
        viewHolder.name.setText(uploadTask.getTaskName());
        String str = formatFileSize(uploadTask.getCurrentSize()) + "/" + formatFileSize(uploadTask.getTotalSize());
        viewHolder.real_time.setText(str);
        viewHolder.download_progress.setText("");
        viewHolder.ProgressBar.setProgress(uploadTask.getUploadProgress());
        viewHolder.cancel_btn.setTag(uploadTask);
        viewHolder.cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MZUploadManager.cancelTask(uploadTask);
            }
        });
        switch (uploadTask.getState()) {
            case IUploadState.STATE_WAIT://等待状态
                viewHolder.start_stop_btn.setClickable(true);
                viewHolder.start_stop_btn.setText("等待");
                break;
            case IUploadState.STATE_CANCEL://停止状态
                viewHolder.start_stop_btn.setClickable(true);
                viewHolder.start_stop_btn.setText("继续上传");
                break;
            case IUploadState.STATE_RUNNING:// 正在执行
                viewHolder.start_stop_btn.setClickable(true);
                viewHolder.start_stop_btn.setText("上传中");
                break;
            case IUploadState.STATE_COMPLETE://完成状态
                viewHolder.start_stop_btn.setClickable(true);
                viewHolder.start_stop_btn.setText("完成");
                viewHolder.item_layout.setClickable(false);
                break;
            case IUploadState.STATE_FAIL://失败状态
                viewHolder.start_stop_btn.setClickable(true);
                viewHolder.start_stop_btn.setText("重新上传");
                break;
            case IUploadState.STATE_OTHER://其它状态
                break;

            default:
                break;
        }
        viewHolder.start_stop_btn.setOnClickListener(new StartStopClick(uploadTask , i));
        return view;
    }

    class StartStopClick implements View.OnClickListener {
        private MZUploadTask entity;
        private int position;

        public StartStopClick(MZUploadTask entity , int position) {
            this.entity = entity;
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            if (((Button) view).getText().equals("重新上传")) {
                entity.setVid("");
                MZUploadManager.cancelTask(entity);
            } else if (((Button) view).getText().equals("继续上传")) {
                MZUploadManager.cancelTask(entity);
            }
        }
    }

    public class ViewHolder {
        ImageView icon;
        TextView name;
        TextView real_time;
        TextView download_progress;
        android.widget.ProgressBar ProgressBar;
        Button start_stop_btn;
        Button cancel_btn;
        RelativeLayout item_layout;
    }

    public String formatFileSize(double size) {
        if (size < 0) {
            return "0k";
        }
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "b";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "k";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "m";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "g";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "t";
    }

    public void updateData(int position, ListView listView, final MZUploadTask uploadTask) {
        int firstvisible = listView.getFirstVisiblePosition();
        int lastvisibale = listView.getLastVisiblePosition();
        ViewHolder viewHolder = null;
            if (position >= firstvisible && position <= lastvisibale) {
                View view = listView.getChildAt(position - firstvisible);
                viewHolder = (ViewHolder) view.getTag();
                //然后使用viewholder去更新需要更新的view。
            }
            if (viewHolder != null) {
                viewHolder.name.setText(uploadTask.getTaskName());
                String str = formatFileSize(uploadTask.getCurrentSize()) + "/" + formatFileSize(uploadTask.getTotalSize());
                viewHolder.real_time.setText(str);
                viewHolder.download_progress.setText(str);
                viewHolder.ProgressBar.setProgress(uploadTask.getUploadProgress());
                viewHolder.cancel_btn.setTag(uploadTask);
                viewHolder.cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MZUploadManager.cancelTask(uploadTask);
                    }
                });
                switch (uploadTask.getState()) {
                    case IUploadState.STATE_WAIT://等待状态
                        viewHolder.start_stop_btn.setClickable(true);
                        viewHolder.start_stop_btn.setText("等待");
                        break;
                    case IUploadState.STATE_CANCEL://停止状态
                        viewHolder.start_stop_btn.setClickable(true);
                        viewHolder.start_stop_btn.setText("继续上传");
                        break;
                    case IUploadState.STATE_RUNNING:// 正在执行
                        viewHolder.start_stop_btn.setClickable(true);
                        viewHolder.start_stop_btn.setText("上传中");
                        break;
                    case IUploadState.STATE_COMPLETE://完成状态
                        viewHolder.start_stop_btn.setClickable(true);
                        viewHolder.start_stop_btn.setText("完成");
                        viewHolder.item_layout.setClickable(true);
                        break;
                    case IUploadState.STATE_FAIL://失败状态
                        viewHolder.start_stop_btn.setClickable(true);
                        viewHolder.start_stop_btn.setText("重新上传");
                        break;
                    case IUploadState.STATE_OTHER://其它状态
                        break;

                    default:
                        break;
                }
            }
    }

}
