package com.mzmedia.widgets;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mengzhu.live.sdk.business.dto.Config.MZFCodeDto;
import com.mengzhu.live.sdk.core.utils.DensityUtil;
import com.mengzhu.live.sdk.ui.api.MZApiDataListener;
import com.mengzhu.live.sdk.ui.api.MZApiRequest;
import com.mengzhu.live.sdk.ui.widgets.MzStateView;
import com.mengzhu.sdk.R;
import com.mzmedia.adapter.push.MZFCodeListAdapter;
import com.mzmedia.widgets.dialog.AbstractPopupWindow;

import java.util.ArrayList;
import java.util.List;

import tv.mengzhu.core.wrap.netwock.Page;

/**
 * F码选择列表
 */
public class MZFCodeListPopWindow extends AbstractPopupWindow {

    private Context mContext;

    private View root;
    private LinearLayout listLayout;
    private TextView tv_select;
    private ListView listView;
    private TextView createBtn;
    private MzStateView mzStateView;
    private Animation enterAnim, exitAnim;

    private MZFCodeListAdapter listAdapter;
    private List<MZFCodeDto> dataList = new ArrayList<>();
    MZApiRequest mzApiRequest;

    public interface OnPopDisMissListener {
        void onDismiss(MZFCodeDto mzfCodeDto);
    }

    private OnPopDisMissListener onDisMissListener;

    public void setOnDisMissListener(OnPopDisMissListener onDisMissListener) {
        this.onDisMissListener = onDisMissListener;
    }

    public interface OnCreateBtnClickListener {
        void onCreateBtnClick();
    }

    private OnCreateBtnClickListener onCreateBtnClickListener;

    public void setOnCreateBtnClickListener(OnCreateBtnClickListener onCreateBtnClickListener) {
        this.onCreateBtnClickListener = onCreateBtnClickListener;
    }

    public interface OnItemClickListener {
        void itemClick(MZFCodeDto mzfCodeDto);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MZFCodeListPopWindow(Context context) {
        super(context);
        this.mContext = context;
        root = View.inflate(context, R.layout.pop_f_code_list_layout, null);
        listLayout = root.findViewById(R.id.ll_list_layout);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) listLayout.getLayoutParams();
        params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        params.height = DensityUtil.dip2px(context, 413);
        listLayout.setLayoutParams(params);

        exitAnim = AnimationUtils.loadAnimation(context, R.anim.side_bottom_exit);
        enterAnim = AnimationUtils.loadAnimation(context, R.anim.side_bottom_enter);
        setContentView(root);
        initView(root);
    }

    public void initView(View root) {
        root.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_select = root.findViewById(R.id.tv_select);
        tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        listView = root.findViewById(R.id.list_listview);
        mzStateView = root.findViewById(R.id.mz_state_view);
        createBtn = root.findViewById(R.id.mz_create_btn);
        mzStateView.setContentView(listView, createBtn);
        mzStateView.show(MzStateView.NetState.LOADING);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCreateBtnClickListener != null) {
                    onCreateBtnClickListener.onCreateBtnClick();
                }
            }
        });
    }

    public void initData() {
        if (!dataList.isEmpty()) {
            return;
        }
        refreshData();
    }

    public void refreshData() {
        mzApiRequest = new MZApiRequest();
        mzApiRequest.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String apiType, Object dto, Page page, int status) {
                if (dto != null) {
                    dataList = (List<MZFCodeDto>) dto;
                    listAdapter = new MZFCodeListAdapter(mContext, dataList);
                    listView.setAdapter(listAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            boolean isCheck = !dataList.get(position).isCheck();
                            for (int i = 0; i < dataList.size(); i++) {
                                dataList.get(i).setCheck(false);
                            }
                            dataList.get(position).setCheck(isCheck);
                            listAdapter.notifyDataSetChanged();
                            if (onItemClickListener != null) {
                                onItemClickListener.itemClick(dataList.get(position));
                            }
                        }
                    });
                    mzStateView.show(MzStateView.NetState.CONTENT);
                } else {
                    mzStateView.show(MzStateView.NetState.LOADERROR);
                }
            }

            @Override
            public void errorResult(String apiType, int code, String msg) {
                mzStateView.show(MzStateView.NetState.LOADERROR);
            }
        });
        mzApiRequest.createRequest(mContext, MZApiRequest.API_GET_F_CODE_LIST);
        mzApiRequest.startData(MZApiRequest.API_GET_F_CODE_LIST);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (onDisMissListener != null) {
            if (dataList == null) {
                onDisMissListener.onDismiss(null);
            } else {
                MZFCodeDto mzfCodeDto = null;
                for (int i = 0; i < dataList.size(); i++) {
                    if (dataList.get(i).isCheck()) {
                        mzfCodeDto = dataList.get(i);
                    }
                }
                onDisMissListener.onDismiss(mzfCodeDto);
            }
        }
    }
}
