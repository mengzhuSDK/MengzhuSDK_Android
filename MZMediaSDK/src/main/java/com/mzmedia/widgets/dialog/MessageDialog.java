package com.mzmedia.widgets.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mengzhu.sdk.R;

import tv.mengzhu.core.frame.coreutils.DeviceUtil;

/**
 * @author max
 * @description 消息对话框
 */
@SuppressLint("WrongConstant")
public class MessageDialog extends BaseDialog implements android.view.View.OnClickListener {

    private OnMessageDialogCallBack messageDialogCallBack = null;
    private TextView message_dialog_title;
    private TextView message_dialog_msg_content;
    private Button message_dialog_btn_ok;
    private Button message_dialog_btn_cancle;

    private CardView layout_message_dialog;
    private LinearLayout view_dialog_content;
    private LinearLayout mz_dialog_btn_layout;
    private LinearLayout message_dialog_title_layout;
    private Context mContext;

    public MessageDialog(Context context) {
        this(context, -1, -1, true);
    }

    public MessageDialog(Context context, boolean isLimit) {
        this(context, -1, -1, isLimit);
    }

    public MessageDialog(Context context, int cancle, int ok) {
        this(context, cancle, ok, true);
    }

    public MessageDialog(Context context, int cancle, int ok, boolean isLimit) {
        super(context);
        mContext = context;
        View content = View.inflate(context, R.layout.dialog_message, null);
        message_dialog_title = (TextView) content.findViewById(R.id.message_dialog_title);
        message_dialog_msg_content = (TextView) content.findViewById(R.id.message_dialog_msg_content);
        message_dialog_btn_cancle = (Button) content.findViewById(R.id.message_dialog_btn_cancle);
        layout_message_dialog = content.findViewById(R.id.layout_message_dialog);
        view_dialog_content = (LinearLayout) content.findViewById(R.id.view_dialog_content);
        mz_dialog_btn_layout = (LinearLayout) content.findViewById(R.id.mz_dialog_btn_layout);
        message_dialog_title_layout = (LinearLayout) content.findViewById(R.id.message_dialog_title_layout);

        if (cancle > 0) {
            message_dialog_btn_cancle.setText(cancle);
        }

        message_dialog_btn_cancle.setOnClickListener(this);
        message_dialog_btn_ok = (Button) content.findViewById(R.id.message_dialog_btn_ok);
        if (ok > 0) {
            message_dialog_btn_ok.setText(ok);
        }
        message_dialog_btn_ok.setOnClickListener(this);
        message_dialog_title.setVisibility(View.GONE);
        message_dialog_title_layout.setVisibility(View.GONE);
        // 设置内容
        if (isLimit) {
            setDialogViewLimit(content);
        } else {
            setDialogView(content);
        }
    }

    public void setBtnLayoutVisibility(boolean isShow) {
        mz_dialog_btn_layout.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void setTitleLayoutVisibility(boolean isShow) {
        message_dialog_title_layout.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void setMessageTitle(int title) {
        message_dialog_title.setVisibility(View.VISIBLE);
        message_dialog_title.setText(title);
        message_dialog_title_layout.setVisibility(View.VISIBLE);
    }


    public void setMessageTitle(String title) {
        message_dialog_title.setVisibility(View.VISIBLE);
        message_dialog_title.setText(title);
        message_dialog_title_layout.setVisibility(View.VISIBLE);
    }

    /**
     * 设置dialog标题是否可见
     *
     * @param isShow
     */
    public void setTitleVisibility(boolean isShow) {
        message_dialog_title.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置dialog高度
     *
     * @param px 像素
     */
    public void setLayoutHeight(int px) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, px);
        layout_message_dialog.setLayoutParams(params);
    }

    /**
     * 设置dialog内容布局
     *
     * @param
     */
    public void setMesssageView(int id) {
        View view = View.inflate(mContext, id, null);
        message_dialog_msg_content.setVisibility(View.GONE);
        view_dialog_content.setVisibility(View.VISIBLE);
        view_dialog_content.addView(view);

    }

    /**
     * 设置dialog内容布局
     *
     * @param
     */
    private View mContentView;

    public void setMesssageView(View view) {
        mContentView = view;
        if (view != null) {
            message_dialog_msg_content.setVisibility(View.GONE);
            view_dialog_content.setVisibility(View.VISIBLE);
            view_dialog_content.addView(view);
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (null != mContentView) {
            if (!inRangeOfView(view_dialog_content, event)) {
                this.dismiss();
            }
        }
        return super.onTouchEvent(event);
    }

    private boolean inRangeOfView(View view, MotionEvent ev) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if (ev.getX() < x || ev.getX() > (x + view.getWidth()) || ev.getY() < y || ev.getY() > (y + view.getHeight())) {
            return false;
        }
        return true;
    }


    public void setContentMessage(int resid) {
        message_dialog_msg_content.setText(resid);
    }

    public void setContentMessage(String msgStr) {
        message_dialog_msg_content.setText(msgStr);
    }


    /**
     * @author sunjiale
     * @description 设置显示文本
     */
    public void setConfirmText(String context) {
        if (!TextUtils.isEmpty(context)) {
            message_dialog_btn_ok.setText(context);
        }
    }

    public void setConfirmText(int context) {
        message_dialog_btn_ok.setText(context);
    }

    public void setCancelText(String context) {
        if (!TextUtils.isEmpty(context)) {
            message_dialog_btn_cancle.setText(context);
        }
    }

    public void setCancelText(int context) {
        message_dialog_btn_cancle.setText(context);
    }

    /**
     * @author sunjiale
     * @description 只显示一个按钮
     */
    public void setSingleType(int... content) {
        message_dialog_btn_cancle.setVisibility(View.GONE);
        if (content != null && content.length > 0) {
            message_dialog_btn_ok.setText(content[0]);
        }
    }

    public void setSingleType(String... content) {
        message_dialog_btn_cancle.setVisibility(View.GONE);
        if (content != null && content.length > 0) {
            message_dialog_btn_ok.setText(content[0]);
        }
    }

    /**
     * @author sunjiale
     * @description 设置一个按钮背景
     */
    public void setSingleTypeBg(int bgRes) {
        message_dialog_btn_cancle.setVisibility(View.GONE);
        message_dialog_btn_ok.setBackgroundResource(bgRes);
    }

    /**
     * @author max
     * @description 设置cancel，confirm按钮背景
     */
    public void setTypeBtnBg(int cancelBgRes, int confirmBgRes) {
        message_dialog_btn_cancle.setBackgroundResource(cancelBgRes);
        message_dialog_btn_ok.setBackgroundResource(confirmBgRes);
    }

    public void setDialogNopadding() {
        view_dialog_content.setPadding(0, 0, 0, 0);
    }

    public void setDialogHeight() {
        view_dialog_content.setMinimumHeight(DeviceUtil.dip2px(mContext, 60));
    }

    public void setContentGravity(int gravity) {
        message_dialog_msg_content.setGravity(gravity | Gravity.CENTER_VERTICAL);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.message_dialog_btn_ok) {
            if (messageDialogCallBack != null) {
                messageDialogCallBack.onClick(true);
            }
        } else if (id == R.id.message_dialog_btn_cancle) {
            if (messageDialogCallBack != null) {
                messageDialogCallBack.onClick(false);
            }
        }
        dismiss();
    }

    private boolean isDismiss = true;

    public void setDismiss(boolean isDismiss) {
        this.isDismiss = isDismiss;
    }

    @Override
    public void dismiss() {
        if (!isDismiss) {
            return;
        }
        super.dismiss();
        if (onCallback != null) {
            onCallback.isDismiss(true);
        }
    }

    @Override
    public void show() {
        super.show();
        if (onCallback != null) {
            onCallback.isDismiss(false);
        }
    }

    public void setMessageDialogCallBack(OnMessageDialogCallBack messageDialogCallBack) {
        this.messageDialogCallBack = messageDialogCallBack;
    }

    /**
     * @author sunjiale
     * @description 回调接口
     */
    public interface OnMessageDialogCallBack {
        void onClick(boolean isConfirm);
    }

    private OnDismissCallback onCallback;

    public void setOnDismissCallback(OnDismissCallback onCallback) {
        this.onCallback = onCallback;
    }

    public interface OnDismissCallback {
        public void isDismiss(boolean isDismiss);
    }


}
