package com.mzmedia.fragment;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mengzhu.live.sdk.business.dto.play.PlayInfoDto;
import com.mengzhu.live.sdk.core.utils.DensityUtil;
import com.mengzhu.sdk.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import tv.mengzhu.dlna.DLNAController;
import tv.mengzhu.dlna.entity.ClingDevice;
import tv.mengzhu.dlna.event.DeviceEvent;
import tv.mengzhu.dlna.manager.ClingManager;
import tv.mengzhu.dlna.manager.DeviceManager;


/**
 * 搜索投屏列表弹窗
 */
public class ChannelDlanDialogFragment extends DialogFragment implements View.OnClickListener {
    private View root;
    private Dialog mDialog = null;
    private PlayInfoDto mPlayInfoDto;
    private TextView mCloseIv;
    private TextView mDlnaListHint;
    private ProgressBar mDlnaSearchLoading;
    private ImageButton mDlnaSearchLoadingBtn;
    private Button mBtnHelp;
    private LinearLayout mDlanOutsideLayout;
    private RelativeLayout mRlDlanLayout;
    public static final String PLAYINFO = "paly_info";
    private ListView mSearchList;
    private List<ClingDevice> mDevices;
    private DeviceAdapter mDeviceAdapter;
    private ObjectAnimator objectAnimator;

    private DLNAController controller; //投屏控制

    public static ChannelDlanDialogFragment newInstance(PlayInfoDto mPlayInfo , DLNAController controller) {
        ChannelDlanDialogFragment bottomFragment = new ChannelDlanDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(PLAYINFO, mPlayInfo);
        bottomFragment.setArguments(args);
        bottomFragment.controller = controller;
        return bottomFragment;
    }

    private Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.PushDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.dialog_fragment_channel_dlan, container);
        initDialog();
        initView();
        initData();
        initLinister();
        return root;
    }

    private void initDialog() {
        mDialog = getDialog();
        mDialog.getWindow().getAttributes().windowAnimations = R.style.dialogNoAnim;
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.height = WindowManager.LayoutParams.MATCH_PARENT; // 高度持平
        int screenW = DensityUtil.getScreenWidth(mActivity);
        int screenH = DensityUtil.getScreenHeight(mActivity);
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//		lp.height = screenH - screenW * 10 / 16 - 10;
//        } else {
//            lp.height = screenH - screenW * 10 / 16 - 10 + Device.getStatusBarHeight(mActivity);
//        }
//        if(mActivity instanceof WatchBroadcastActivity) {
//            lp.height= (int) ((float) ((WatchBroadcastActivity) mActivity).getBottomFragmentHeight() / 1.5);
//        }else if(mActivity instanceof PlaybackBroadcastActivity){
//            lp.height= (int) ((float) ((PlaybackBroadcastActivity) mActivity).getBottomFragmentHeight() / 1.5);
//        }
        window.setAttributes(lp);
        mDialog.setCanceledOnTouchOutside(true); // 外部点击取消
        window.requestFeature(Window.FEATURE_NO_TITLE);
        Bundle args = getArguments();
        if (null != args && args.containsKey(PLAYINFO)) {
            mPlayInfoDto = (PlayInfoDto) args.getSerializable(PLAYINFO);
        }
        Window windows = mActivity.getWindow();
        WindowManager.LayoutParams windowParams = windows.getAttributes();
        windowParams.alpha = 0.5f;
        windows.setAttributes(windowParams);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        Window windows = mActivity.getWindow();
        WindowManager.LayoutParams windowParams = windows.getAttributes();
        windowParams.alpha = 1f;//1.０全透明．０不透明．
        windows.setAttributes(windowParams);
    }

    private void initView() {
//        mSearchBtn = (Button) root.findViewById(R.id.btn_activity_dlan_tv_list);
        mSearchList = (ListView) root.findViewById(R.id.lv_activity_dlan_tv_list);
        mDlanOutsideLayout = (LinearLayout) root.findViewById(R.id.layout_dlan_outside);
        mRlDlanLayout = (RelativeLayout) root.findViewById(R.id.rl_channel_dlan_dialog_fragment);
        mCloseIv = (TextView) root.findViewById(R.id.iv_dialog_goods_close);
        mDlnaListHint = (TextView) root.findViewById(R.id.lv_activity_dlan_tv_list_hint);
        mDlnaSearchLoading = (ProgressBar) root.findViewById(R.id.dlna_search_loading);
        mDlnaSearchLoadingBtn = (ImageButton) root.findViewById(R.id.dlna_search_loading_btn);
        mBtnHelp = (Button) root.findViewById(R.id.dlan_bt_help);
        mDlnaSearchLoadingBtn.setOnClickListener(this);
        mBtnHelp.setOnClickListener(this);
//        mSearchBtn.setOnClickListener(this);
        mDeviceAdapter = new DeviceAdapter();

        mDevices = DeviceManager.getInstance().getClingDeviceList();
        if (mDevices.size() == 0) {
            mDlnaListHint.setVisibility(View.VISIBLE);
            mDlnaListHint.setText(mActivity.getString(R.string.dlna_loading_search));
        }
        mSearchList.setAdapter(mDeviceAdapter);
        mSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ClingDevice device = (ClingDevice) mDevices.get(position);
                DeviceManager.getInstance().setCurrClingDevice(device);
//                开启匹配
                dismiss();

                startDlan();
            }
        });


        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 1200);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mRlDlanLayout.setLayoutParams(params);

        objectAnimator = ObjectAnimator.ofFloat(mRlDlanLayout, "translationY", 1200f, 0f);
        objectAnimator.setDuration(250);
        objectAnimator.start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(DeviceEvent event) {
        refresh();
    }

    private void initData() {
        loadData();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Window windows = mActivity.getWindow();
        WindowManager.LayoutParams windowParams = windows.getAttributes();
        windowParams.alpha = 1f;//1.０全透明．０不透明．
        windows.setAttributes(windowParams);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void loadData() {
    }

    private void initLinister() {
        mCloseIv.setOnClickListener(this);
        mDlanOutsideLayout.setOnClickListener(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_dialog_goods_close) {
            exitDialog();
        } else if (id == R.id.layout_dlan_outside) {
            exitDialog();

            loadingStart();
            startDLNAService();
            ClingManager.getInstance().searchDevices();
            refresh();
        } else if (id == R.id.dlna_search_loading_btn) {//loading
            loadingStart();
            startDLNAService();
            ClingManager.getInstance().searchDevices();
            refresh();
        } else if (id == R.id.dlan_bt_help) {
        }
    }

    //开始投屏
    public void startDlan() {
        if (controller != null)
            controller.onExecute(DLNAController.PLAYER_DLNA);
    }

    private void exitDialog() {
        objectAnimator = ObjectAnimator.ofFloat(mRlDlanLayout, "translationY", 0f, 1200f);
        objectAnimator.setDuration(300);
        objectAnimator.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 250);
    }

    private void loadingHint() {
        if (mDevices.size() == 0) {
            mDlnaListHint.setVisibility(View.VISIBLE);
            mDlnaListHint.setText(mActivity.getString(R.string.dlna_not_find_device));
        } else {
            mDlnaListHint.setText(mActivity.getString(R.string.dlna_loading_search));
            mDlnaListHint.setVisibility(View.GONE);
        }
    }

    public void loadingStart() {
        // pBar.setIndeterminate(true);
        mDlnaSearchLoading.setVisibility(View.VISIBLE);
        mDlnaSearchLoadingBtn.setVisibility(View.GONE);
        mDlnaListHint.setVisibility(View.VISIBLE);
        mDlnaListHint.setText(mActivity.getString(R.string.dlna_loading_search));
    }

    public void loadingStop() {
        mDlnaSearchLoading.setVisibility(View.GONE);
        mDlnaSearchLoadingBtn.setVisibility(View.VISIBLE);
        loadingHint();
    }

    public void onClose() {
        if (this.isVisible()) {
            dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

//        stopDLNAService();
    }

    private void refresh() {
        if (mDeviceAdapter != null) {
            mDeviceAdapter.notifyDataSetChanged();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadingStop();
                }
            }, 2000);
//            loadingStop();
        }
    }

    private void startDLNAService() {
    }

    private void stopDLNAService() {
//        Intent intent = new Intent(mActivity, DLNAService.class);
//        mActivity.stopService(intent);
    }


    private void startControlActivity() {
//        Intent intent = new Intent(getApplicationContext(),
//                ControlActivity.class);
//        startActivity(intent);
//控制页面
    }

    private class DeviceAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mDevices == null) {
                return 0;
            } else {
                return mDevices.size();
            }
        }

        @Override
        public Object getItem(int position) {
            if (mDevices != null) {
                return mDevices.get(position);
            }

            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(mActivity,
                        R.layout.item_dlan_tv, null);
                holder = new ViewHolder();
                holder.tv_name_item = (TextView) convertView
                        .findViewById(R.id.tv_name_item);
                holder.iv_device_item = (ImageView) convertView.findViewById(R.id.iv_device_item);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv_name_item.setText(mDevices.get(position).getDevice().getDetails().getFriendlyName());
            if (DeviceManager.getInstance().getCurrClingDevice() != null && DeviceManager.getInstance()
                    .getCurrClingDevice().getDevice().getDetails().getFriendlyName().equals(mDevices.get(position)
                            .getDevice().getDetails().getFriendlyName())) {
                holder.iv_device_item.setVisibility(View.VISIBLE);
            } else {
                holder.iv_device_item.setVisibility(View.INVISIBLE);
            }
            return convertView;
        }
    }

    static class ViewHolder {
        private TextView tv_name_item;
        private ImageView iv_device_item;
    }

}
