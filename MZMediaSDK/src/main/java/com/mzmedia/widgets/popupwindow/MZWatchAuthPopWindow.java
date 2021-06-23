package com.mzmedia.widgets.popupwindow;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mengzhu.live.sdk.business.dto.watchcheck.WatchCheckDto;
import com.mengzhu.live.sdk.core.utils.ToastUtils;
import com.mengzhu.live.sdk.ui.api.MZApiDataListener;
import com.mengzhu.live.sdk.ui.api.MZApiRequest;
import com.mengzhu.live.sdk.ui.widgets.MzStateView;
import com.mengzhu.sdk.R;
import com.mzmedia.widgets.dialog.AbstractPopupWindow;

import tv.mengzhu.core.wrap.netwock.Page;

public class MZWatchAuthPopWindow extends AbstractPopupWindow implements View.OnClickListener {

    public static final int MODE_FREE = 1;
    public static final int MODE_WHITE_LIST = 5;
    public static final int MODE_F_CODE = 6;

    private Context mContext;
    private MzStateView mzStateView;
    private LinearLayout contentLayout;

    private LinearLayout whiteListLayout;
    private TextView whiteListBack;

    private LinearLayout fCodeLayout;
    private TextView fCodeSubmit;
    private EditText fCodeEdit;
    private TextView fCodeBack;

    private String ticket_id;
    private String phone; //用于验证白名单，其他可不传

    private MZApiRequest checkRequest;
    private MZApiRequest fCodeRequest;

    boolean isSuccess = false;

    ProgressDialog progressDialog;

    public interface OnCheckResultListener {
        void onSuccess();

        void onFailed();
    }

    private OnCheckResultListener onCheckResultListener;

    public void setOnCheckResultListener(OnCheckResultListener onCheckResultListener) {
        this.onCheckResultListener = onCheckResultListener;
    }

    public MZWatchAuthPopWindow(Context context, String ticket_id, String phone) {
        super(context);
        this.mContext = context;
        this.ticket_id = ticket_id;
        this.phone = phone;
        progressDialog = new ProgressDialog(mContext);
        initView();
        initListener();
        initRequest();
        loadData();
    }

    public void initView() {
        View view = View.inflate(mContext, R.layout.mz_pop_watchauth, null);
        setContentView(view);
        mzStateView = view.findViewById(R.id.mz_state_view);
        contentLayout = view.findViewById(R.id.content_layout);
        whiteListLayout = view.findViewById(R.id.ll_white_list_layout);
        whiteListBack = view.findViewById(R.id.tv_white_list_back);
        fCodeLayout = view.findViewById(R.id.ll_f_code_layout);
        fCodeSubmit = view.findViewById(R.id.tv_f_code_submit);
        fCodeEdit = view.findViewById(R.id.etv_f_code);
        fCodeBack = view.findViewById(R.id.tv_f_code_back);
        mzStateView.setContentView(contentLayout);
    }

    public void initListener() {
        whiteListBack.setOnClickListener(this);
        fCodeSubmit.setOnClickListener(this);
        fCodeBack.setOnClickListener(this);
    }

    public void initRequest() {
        checkRequest = new MZApiRequest();
        checkRequest.createRequest(mContext, MZApiRequest.API_WATCH_CHECK);
        checkRequest.setResultListener(checkListener);
        fCodeRequest = new MZApiRequest();
        fCodeRequest.createRequest(mContext, MZApiRequest.API_F_CODE_CHECK);
        fCodeRequest.setResultListener(fCodeCheckListener);
    }

    public void loadData() {
        mzStateView.show(MzStateView.NetState.LOADING);
        checkRequest.startData(MZApiRequest.API_WATCH_CHECK, ticket_id, phone);
    }

    public void fCodeCheck() {
        String code = fCodeEdit.getText().toString();
        if (TextUtils.isEmpty(code)) {
            ToastUtils.popUpToast("请填写F码");
        } else {
            progressDialog.show();
            fCodeRequest.startData(MZApiRequest.API_F_CODE_CHECK, ticket_id, code);
        }
    }

    MZApiDataListener checkListener = new MZApiDataListener() {
        @Override
        public void dataResult(String apiType, Object dto, Page page, int status) {
            if (dto instanceof WatchCheckDto) {
                WatchCheckDto checkDto = (WatchCheckDto) dto;
                int mode = checkDto.getView_mode();
                switch (mode) {
                    case MODE_FREE:
                        isSuccess = true;
                        dismiss();
                        break;
                    case MODE_WHITE_LIST:
                        if (checkDto.getAllow_play() == 1) {
                            isSuccess = true;
                            dismiss();
                        } else {
                            isSuccess = false;
                            fCodeLayout.setVisibility(View.GONE);
                            whiteListLayout.setVisibility(View.VISIBLE);
                            mzStateView.show(MzStateView.NetState.CONTENT);
                        }
                        break;
                    case MODE_F_CODE:
                        if (checkDto.getAllow_play() == 1) {
                            isSuccess = true;
                            dismiss();
                        } else {
                            isSuccess = false;
                            whiteListLayout.setVisibility(View.GONE);
                            fCodeLayout.setVisibility(View.VISIBLE);
                            mzStateView.show(MzStateView.NetState.CONTENT);
                        }
                        break;
                    default:
                        //接口请求失败默认不验证，可自行修改
                        isSuccess = true;
                        dismiss();
                        break;
                }
            } else {
                //接口请求失败默认不验证，可自行修改
                isSuccess = true;
                dismiss();
            }
        }

        @Override
        public void errorResult(String apiType, int code, String msg) {
            //接口请求失败默认不验证，可自行修改
            isSuccess = true;
            dismiss();
        }
    };

    MZApiDataListener fCodeCheckListener = new MZApiDataListener() {
        @Override
        public void dataResult(String apiType, Object dto, Page page, int status) {
            progressDialog.dismiss();
            if (status == 200) {
                isSuccess = true;
                dismiss();
            } else {
                ToastUtils.popUpToast("F码不正确!");
            }
        }

        @Override
        public void errorResult(String apiType, int code, String msg) {
            progressDialog.dismiss();
            ToastUtils.popUpToast("F码不正确!");
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_white_list_back || id == R.id.tv_f_code_back) {
            dismiss();
        } else if (id == R.id.tv_f_code_submit) {
            fCodeCheck();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (onCheckResultListener != null) {
            if (isSuccess) {
                onCheckResultListener.onSuccess();
            } else {
                onCheckResultListener.onFailed();
            }
        }
    }
}
