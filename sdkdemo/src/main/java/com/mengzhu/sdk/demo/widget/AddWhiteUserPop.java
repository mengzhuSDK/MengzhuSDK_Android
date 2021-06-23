package com.mengzhu.sdk.demo.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mengzhu.sdk.demo.R;

/**
 * 添加白名单用户弹窗
 */
public class AddWhiteUserPop extends PopupWindow {
    private Context mContext;

    private TextView tv_cancel;
    private TextView tv_sure;
    private EditText etv_phone;

    public interface AddClickListener {
        void addClick(String phone_string);
    }

    private AddClickListener addClickListener;

    public void setAddClickListener(AddClickListener addClickListener) {
        this.addClickListener = addClickListener;
    }

    public AddWhiteUserPop(Context context) {
        super(context);
        this.mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_add_white_user, null, false);
        tv_cancel = view.findViewById(R.id.tv_cancel);
        tv_sure = view.findViewById(R.id.tv_sure);
        etv_phone = view.findViewById(R.id.pop_phone_edit);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etv_phone.getText().toString())) {
                    return;
                }
                if (addClickListener != null)
                    addClickListener.addClick(etv_phone.getText().toString());
            }
        });
        setContentView(view);
        setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.transparent)));
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        etv_phone.setText("");
    }
}
