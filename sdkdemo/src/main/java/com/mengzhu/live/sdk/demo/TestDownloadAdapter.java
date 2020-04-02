package com.mengzhu.live.sdk.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mengzhu.sdk.download.MZDownloadManager;
import com.mengzhu.sdk.download.library.publics.core.download.DownloadEntity;
import com.mengzhu.sdk.download.library.publics.core.inf.IEntity;

import java.math.BigDecimal;
import java.util.List;

public class TestDownloadAdapter extends BaseAdapter {
    private Context mContext;
    private  int sort=1;
    private List<DownloadEntity> mListEntity;
    public TestDownloadAdapter(Context context){
        mContext=context;
        mListEntity=MZDownloadManager.getInstance().getDownloadTaskBiz().getTaskList(sort);
    }
    @Override
    public int getCount() {
        mListEntity= MZDownloadManager.getInstance().getDownloadTaskBiz().getTaskList(sort);
        if(mListEntity!=null) {
            return mListEntity!=null? mListEntity.size():0;
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        return MZDownloadManager.getInstance().getDownloadTaskBiz().getTaskList(sort).get(i);
    }

    @Override
    public long getItemId(int i) {
        return MZDownloadManager.getInstance().getDownloadTaskBiz().getTaskList(sort).get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        List<DownloadEntity> list=MZDownloadManager.getInstance().getDownloadTaskBiz().getTaskList(sort);
        if(list.size()>i) {

            DownloadEntity entity = list.get(i);
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
                viewHolder.item_layout=view.findViewById(R.id.item_layout);
                view.setTag(viewHolder);

            }
            viewHolder.item_layout.setClickable(false);
            viewHolder.name.setText(entity.getName());
            viewHolder.real_time.setText(entity.getConvertSpeed());
            String str =
                    formatFileSize(entity.getCurrentProgress()) + "/" + formatFileSize(entity.getFileSize());
            viewHolder.download_progress.setText(str);
            viewHolder.ProgressBar.setProgress(entity.getPercent());
            viewHolder.cancel_btn.setTag(entity);
            viewHolder.cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MZDownloadManager.getInstance().getDownloadContrller().cancelDownload(((DownloadEntity) view.getTag()).getId());
                }
            });
            switch (entity.getState()) {
                case IEntity.STATE_CANCEL://删除任务
                    viewHolder.start_stop_btn.setClickable(false);
                    break;
                case IEntity.STATE_POST_PRE://预处理完成
                case IEntity.STATE_WAIT://等待状态
                    viewHolder.start_stop_btn.setClickable(true);
                    viewHolder.start_stop_btn.setText("等待");
                    break;
                case IEntity.STATE_STOP://停止状态
                    viewHolder.start_stop_btn.setClickable(true);
                    viewHolder.start_stop_btn.setText("继续下载");
                    break;
                case IEntity.STATE_PRE://预处理
                    viewHolder.start_stop_btn.setClickable(false);
                    viewHolder.start_stop_btn.setText("等待");
                    break;
                case IEntity.STATE_RUNNING:// 正在执行
                    viewHolder.start_stop_btn.setClickable(true);
                    viewHolder.start_stop_btn.setText("停止");
                    break;
                case IEntity.STATE_COMPLETE://完成状态
                    viewHolder.start_stop_btn.setClickable(true);
                    viewHolder.start_stop_btn.setText("重新下载");
                    viewHolder.item_layout.setClickable(true);
                    viewHolder.item_layout.setOnClickListener(new ItemClick(entity));
                    break;
                case IEntity.STATE_FAIL://失败状态
                    viewHolder.start_stop_btn.setClickable(true);
                    viewHolder.start_stop_btn.setText("重新下载");
                    break;
                case IEntity.STATE_OTHER://其它状态
                    break;

                default:
                    break;
            }
            viewHolder.start_stop_btn.setOnClickListener(new StartStopClick(entity));
        }
        return view;
    }

    class ItemClick implements View.OnClickListener{
        DownloadEntity entity;
        public ItemClick(DownloadEntity entity){
            this.entity=entity;
        }
        @Override
        public void onClick(View view) {

             Intent in=new Intent(mContext,PlayerActivity.class);
             in.putExtra("live_URL",entity.getFilePath());
            ((Activity)mContext).startActivity(in);
        }
    }

    class StartStopClick implements View.OnClickListener{
        private DownloadEntity entity;
        public  StartStopClick(DownloadEntity entity){
            this.entity=entity;
        }

        @Override
        public void onClick(View view) {
            if(((Button)view).getText().equals("等待")){
//                MZDownloadManager.getInstance().getDownloadContrller().startDownload(entity.getUrl(),entity.);
                MZDownloadManager.getInstance().getDownloadContrller().resumeDownload(entity.getId());
            }else if(((Button)view).getText().equals("重新下载")){
                MZDownloadManager.getInstance().getDownloadContrller().reStart(entity.getId());
            }else if(((Button)view).getText().equals("继续下载")){
                MZDownloadManager.getInstance().getDownloadContrller().resumeDownload(entity.getId());
            } else {
                MZDownloadManager.getInstance().getDownloadContrller().stopDownload(entity.getId());
            }
            notifyDataSetChanged();
        }
    }

    class ViewHolder {
        ImageView icon;
        TextView name;
        TextView real_time;
        TextView download_progress;
        ProgressBar ProgressBar;
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

    public void setSort(int sort){
        this.sort=sort;
    }

    public void updataData(ListView listView, DownloadEntity entity){
        int firstvisible = listView.getFirstVisiblePosition();
        int lastvisibale = listView.getLastVisiblePosition();
        ViewHolder viewHolder=null;
        DownloadEntity positionEntity=null;
//                =MZDownloadManager.getInstance().getDownloadTaskBiz().getDownloadEntity(entity.getId());
//        int position=MZDownloadManager.getInstance().getDownloadTaskBiz().getTaskList().indexOf(positionEntity);
        List<DownloadEntity> list=MZDownloadManager.getInstance().getDownloadTaskBiz().getTaskList(sort);
        if(list!=null&&list.size()!=0) {
            int position = 0;
            while (list!=null&&list.size() > position) {
                positionEntity = list.get(position);
                if (positionEntity.getId() == entity.getId()) {
                    break;
                }
                position++;
            }
            if(list==null){
                return;
            }
            if (position >= firstvisible && position <= lastvisibale) {
                View view = listView.getChildAt(position - firstvisible);
                viewHolder = (ViewHolder) view.getTag();
                //然后使用viewholder去更新需要更新的view。
            }
            if (viewHolder != null) {
                viewHolder.item_layout.setClickable(false);
                viewHolder.name.setText(entity.getName());
                viewHolder.real_time.setText(entity.getConvertSpeed());
                String str =
                        formatFileSize(entity.getCurrentProgress()) + "/" + formatFileSize(entity.getFileSize());
                viewHolder.download_progress.setText(str);
                viewHolder.ProgressBar.setProgress(entity.getPercent());
                viewHolder.cancel_btn.setTag(entity);
                viewHolder.cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MZDownloadManager.getInstance().getDownloadContrller().cancelDownload(((DownloadEntity) view.getTag()).getId());
                    }
                });
                switch (entity.getState()) {
                    case IEntity.STATE_CANCEL://删除任务
                        viewHolder.start_stop_btn.setClickable(false);
                        break;
                    case IEntity.STATE_POST_PRE://预处理完成
                    case IEntity.STATE_WAIT://等待状态
                        viewHolder.start_stop_btn.setClickable(true);
                        viewHolder.start_stop_btn.setText("等待");
                        break;
                    case IEntity.STATE_STOP://停止状态
                        viewHolder.start_stop_btn.setClickable(true);
                        viewHolder.start_stop_btn.setText("继续下载");
                        break;
                    case IEntity.STATE_PRE://预处理
                        viewHolder.start_stop_btn.setClickable(false);
                        viewHolder.start_stop_btn.setText("等待");
                        break;
                    case IEntity.STATE_RUNNING:// 正在执行
                        viewHolder.start_stop_btn.setClickable(true);
                        viewHolder.start_stop_btn.setText("停止");
                        break;
                    case IEntity.STATE_COMPLETE://完成状态
                        viewHolder.start_stop_btn.setClickable(true);
                        viewHolder.start_stop_btn.setText("重新下载");
                        viewHolder.item_layout.setClickable(true);
                        viewHolder.item_layout.setOnClickListener(new ItemClick(entity));
                        break;
                    case IEntity.STATE_FAIL://失败状态
                        viewHolder.start_stop_btn.setClickable(true);
                        viewHolder.start_stop_btn.setText("重新下载");
                        break;
                    case IEntity.STATE_OTHER://其它状态
                        break;

                    default:
                        break;
                }
            }
        }
    }



}
