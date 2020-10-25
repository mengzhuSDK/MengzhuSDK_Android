package com.mzmedia.widgets.popupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mengzhu.sdk.R;

public class BeautyPopWindow extends PopupWindow implements View.OnClickListener {

    private Context mContext;
    private float beautyValue;
    private LinearLayout ll_beauty_layout;
    private TextView tv_beauty_4x;
    private TextView tv_beauty_3x;
    private TextView tv_beauty_2x;
    private TextView tv_beauty_1x;
    private TextView tv_beauty_0x;

    private View root;

    public interface OnbeautySelectListener {
        void onSelectbeauty(float beauty);
    }

    private OnbeautySelectListener onbeautySelectListener;

    public void setOnbeautySelectListener(OnbeautySelectListener onbeautySelectListener) {
        this.onbeautySelectListener = onbeautySelectListener;
    }

    public BeautyPopWindow(Context context ,float beautyValue) {
        super(context);
        mContext = context;
        this.beautyValue = beautyValue;
        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(true);
        root = View.inflate(mContext, R.layout.pop_beauty_layout, null);
        setContentView(root);
        initView();
        setListener();
    }

    public void initView() {
        ll_beauty_layout = root.findViewById(R.id.ll_beauty_layout);
        tv_beauty_0x = root.findViewById(R.id.item_beauty_0x);
        tv_beauty_1x = root.findViewById(R.id.item_beauty_1x);
        tv_beauty_2x = root.findViewById(R.id.item_beauty_2x);
        tv_beauty_3x = root.findViewById(R.id.item_beauty_3x);
        tv_beauty_4x = root.findViewById(R.id.item_beauty_4x);

        setSelect();
    }

    private void setSelect(){
        if (beautyValue == 0){
            setNoSelect();
            tv_beauty_0x.setTextColor(mContext.getResources().getColor(R.color.color_ff2145));
        }else if (beautyValue == 0.1f){
            setNoSelect();
            tv_beauty_1x.setTextColor(mContext.getResources().getColor(R.color.color_ff2145));
        }else if (beautyValue == 0.2f){
            setNoSelect();
            tv_beauty_2x.setTextColor(mContext.getResources().getColor(R.color.color_ff2145));
        }else if (beautyValue == 0.3f){
            setNoSelect();
            tv_beauty_3x.setTextColor(mContext.getResources().getColor(R.color.color_ff2145));
        }else if (beautyValue == 0.4f){
            setNoSelect();
            tv_beauty_4x.setTextColor(mContext.getResources().getColor(R.color.color_ff2145));
        }
    }

    public void setBeautyValue(float beautyValue){
        this.beautyValue = beautyValue;
        setSelect();
    }

    public void setNoSelect(){
        tv_beauty_0x.setTextColor(mContext.getResources().getColor(R.color.white));
        tv_beauty_1x.setTextColor(mContext.getResources().getColor(R.color.white));
        tv_beauty_2x.setTextColor(mContext.getResources().getColor(R.color.white));
        tv_beauty_3x.setTextColor(mContext.getResources().getColor(R.color.white));
        tv_beauty_4x.setTextColor(mContext.getResources().getColor(R.color.white));
    }

    public void setListener() {
        tv_beauty_0x.setOnClickListener(this);
        tv_beauty_1x.setOnClickListener(this);
        tv_beauty_2x.setOnClickListener(this);
        tv_beauty_3x.setOnClickListener(this);
        tv_beauty_4x.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (onbeautySelectListener == null) {
            return;
        }
        if (id == R.id.item_beauty_0x) {
            onbeautySelectListener.onSelectbeauty(0);
        } else if (id == R.id.item_beauty_1x) {
            onbeautySelectListener.onSelectbeauty(0.1f);
        } else if (id == R.id.item_beauty_2x) {
            onbeautySelectListener.onSelectbeauty(0.2f);
        }else if (id == R.id.item_beauty_3x) {
            onbeautySelectListener.onSelectbeauty(0.3f);
        }else if (id == R.id.item_beauty_4x) {
            onbeautySelectListener.onSelectbeauty(0.4f);
        }
        dismiss();
    }

    public void showAtCustomerLocation(View thePointView, int gravity, int direction, int Xpixl, int Ypixl) {
        //指定控件上面显示：
        if (gravity == Gravity.NO_GRAVITY) {
            int[] location = new int[2];//指定控件的坐标，界面左上角是（0，0），记住这一点，具体什么方向显示就easy了
            thePointView.getLocationOnScreen(location);

            ll_beauty_layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int popWidth = ll_beauty_layout.getMeasuredWidth();
            int popHeight = ll_beauty_layout.getMeasuredHeight();
            switch (direction) {
                case 0://指定控件左面显示：
                    this.showAtLocation(thePointView, Gravity.NO_GRAVITY, location[0] - popWidth + Xpixl, location[1] + Ypixl);
                    break;
                case 1://指定控件右面显示：
                    this.showAtLocation(thePointView, Gravity.NO_GRAVITY, location[0] + popWidth + Xpixl, location[1] + Ypixl);
                    return;
                case 2://指定控件上面显示：
                    this.showAtLocation(thePointView, Gravity.NO_GRAVITY, location[0] - popWidth - popWidth/2 + thePointView.getWidth()/2 + Xpixl, location[1] - popHeight + Ypixl);
                    return;
                case 3: //指定控件下面显示：
                    this.showAtLocation(thePointView, Gravity.NO_GRAVITY, location[0] + Xpixl, location[1] + thePointView.getHeight() + Ypixl);
                    return;
            }
        }
        //中间显示
        else {
            this.showAtLocation(thePointView, gravity, 0, 0);
        }

    }
}
