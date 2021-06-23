package com.mengzhu.sdk.demo.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mengzhu.sdk.demo.R;

/**
 * 新增F码弹窗
 */
public class AddFCodePop extends PopupWindow {
    private Context mContext;

    private TextView tv_cancel;
    private TextView tv_sure;
    private EditText etv_num;
    private ImageView iv_add;
    private ImageView iv_subtract;

    private int maxNum;

    public interface AddClickListener {
        void addClick(String phone_string);
    }

    private AddClickListener addClickListener;

    public void setAddClickListener(AddClickListener addClickListener) {
        this.addClickListener = addClickListener;
    }

    public AddFCodePop(Context context , int maxNum) {
        super(context);
        this.maxNum = maxNum;
        this.mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_add_f_code, null, false);
        tv_cancel = view.findViewById(R.id.tv_cancel);
        tv_sure = view.findViewById(R.id.tv_sure);
        etv_num = view.findViewById(R.id.pop_num_edit);
        iv_add = view.findViewById(R.id.iv_add);
        iv_subtract = view.findViewById(R.id.iv_subtract);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etv_num.getText().toString())) {
                    return;
                }
                if (addClickListener != null)
                    addClickListener.addClick(etv_num.getText().toString());
            }
        });
        etv_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changeNum();
            }
        });
        iv_subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etv_num.setText("" + (Integer.parseInt(etv_num.getText().toString()) - 1));
            }
        });
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etv_num.setText("" + (Integer.parseInt(etv_num.getText().toString()) + 1));
            }
        });
        setContentView(view);
        changeNum();
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.transparent)));
    }

    public void changeNum(){
        String num_str = etv_num.getText().toString();
        if (TextUtils.isEmpty(num_str)){
            iv_add.setImageResource(R.mipmap.icon_add1_not);
            iv_add.setClickable(false);
            iv_subtract.setImageResource(R.mipmap.icon_subtract_not);
            iv_subtract.setClickable(false);
            return;
        }
        int num = Integer.parseInt(num_str);
        if (num == 0){
            etv_num.setText("1");
            return;
        }
        if (1 < num && num < maxNum){
            iv_add.setImageResource(R.mipmap.icon_add1);
            iv_add.setClickable(true);
            iv_subtract.setImageResource(R.mipmap.icon_subtract);
            iv_subtract.setClickable(true);
        }else if (num < 2){
            iv_add.setImageResource(R.mipmap.icon_add1);
            iv_add.setClickable(true);
            iv_subtract.setImageResource(R.mipmap.icon_subtract_not);
            iv_subtract.setClickable(false);
        }else if (num == maxNum){
            iv_add.setImageResource(R.mipmap.icon_add1_not);
            iv_add.setClickable(false);
            iv_subtract.setImageResource(R.mipmap.icon_subtract);
            iv_subtract.setClickable(true);
        }else if (num > maxNum){
            etv_num.setText("" + maxNum);
            return;
        }
    }
}
