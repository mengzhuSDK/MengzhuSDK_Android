package com.mengzhu.sdk.demo.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mengzhu.sdk.demo.R;


/**
 * 白名单编辑
 */
public class EditUserPop extends PopupWindow {

    private Context mContext;
    private TextView tv_del;
    private TextView tv_edit;

    public interface ItemClickListener{
        void editClick();
        void delClick();
    }

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public EditUserPop(Context context) {
        super(context);
        this.mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_edit_user , null , false);
        tv_del = view.findViewById(R.id.tv_del);
        tv_edit = view.findViewById(R.id.tv_edit);
        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null)
                    itemClickListener.editClick();
                dismiss();
            }
        });
        tv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null)
                    itemClickListener.delClick();
                dismiss();
            }
        });
        setContentView(view);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.transparent)));
    }

}
