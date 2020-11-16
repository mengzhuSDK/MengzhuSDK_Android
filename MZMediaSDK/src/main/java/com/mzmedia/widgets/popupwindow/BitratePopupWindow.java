package com.mzmedia.widgets.popupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.mengzhu.sdk.R;
import com.mzmedia.fragment.push.MZPlugFlowFragement;

public class BitratePopupWindow extends PopupWindow implements View.OnClickListener {

    private Context mContext;
    private int bitrateValue;
    private LinearLayout ll_bitrate_layout;
    private ImageView iv_item_chaoqing;
    private ImageView iv_item_gaoqing;
    private ImageView iv_item_biaoqing;

    private View root;

    public interface OnBitrateSelectListener {
        void onSelectBitrate(int bitrate);
    }

    private OnBitrateSelectListener onBitrateSelectListener;

    public void setOnBitrateSelectListener(OnBitrateSelectListener onBitrateSelectListener) {
        this.onBitrateSelectListener = onBitrateSelectListener;
    }

    public BitratePopupWindow(Context context ,int bitrateValue) {
        super(context);
        mContext = context;
        this.bitrateValue = bitrateValue;
        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(true);
        root = View.inflate(mContext, R.layout.pop_bitrate_layout, null);
        setContentView(root);
        initView();
        setListener();
    }

    public void initView() {
        ll_bitrate_layout = root.findViewById(R.id.ll_bitrate_layout);
        iv_item_biaoqing = root.findViewById(R.id.iv_item_biaoqing);
        iv_item_gaoqing = root.findViewById(R.id.iv_item_gaoqing);
        iv_item_chaoqing = root.findViewById(R.id.iv_item_chaoqing);

        setSelectItem();
    }

    public void setListener() {
        iv_item_biaoqing.setOnClickListener(this);
        iv_item_gaoqing.setOnClickListener(this);
        iv_item_chaoqing.setOnClickListener(this);
    }

    public void setBitrateValue(int bitrateValue){
        this.bitrateValue = bitrateValue;
        setSelectItem();
    }

    private void setSelectItem(){
        if (bitrateValue <= MZPlugFlowFragement.RB360){
            iv_item_biaoqing.setImageResource(R.mipmap.mz_item_biaoqing_select);
            iv_item_gaoqing.setImageResource(R.mipmap.mz_item_gaoqing);
            iv_item_chaoqing.setImageResource(R.mipmap.mz_item_chaoqing);
        }else if (bitrateValue > MZPlugFlowFragement.RB360 && bitrateValue <= MZPlugFlowFragement.RB480){
            iv_item_biaoqing.setImageResource(R.mipmap.mz_item_biaoqing);
            iv_item_gaoqing.setImageResource(R.mipmap.mz_item_gaoqing_select);
            iv_item_chaoqing.setImageResource(R.mipmap.mz_item_chaoqing);
        }else if (bitrateValue > MZPlugFlowFragement.RB480 && bitrateValue <= MZPlugFlowFragement.RB720){
            iv_item_biaoqing.setImageResource(R.mipmap.mz_item_biaoqing);
            iv_item_gaoqing.setImageResource(R.mipmap.mz_item_gaoqing);
            iv_item_chaoqing.setImageResource(R.mipmap.mz_item_chaoqing_select);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (onBitrateSelectListener == null) {
            return;
        }
        if (id == R.id.iv_item_biaoqing) {
            onBitrateSelectListener.onSelectBitrate(MZPlugFlowFragement.RB360);
        } else if (id == R.id.iv_item_gaoqing) {
            onBitrateSelectListener.onSelectBitrate(MZPlugFlowFragement.RB480);
        } else if (id == R.id.iv_item_chaoqing) {
            onBitrateSelectListener.onSelectBitrate(MZPlugFlowFragement.RB720);
        }
        dismiss();
    }

    public void showAtCustomerLocation(View thePointView, int gravity, int direction, int Xpixl, int Ypixl) {
        //指定控件上面显示：
        if (gravity == Gravity.NO_GRAVITY) {
            int[] location = new int[2];//指定控件的坐标，界面左上角是（0，0），记住这一点，具体什么方向显示就easy了
            thePointView.getLocationOnScreen(location);

            ll_bitrate_layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int popWidth = ll_bitrate_layout.getMeasuredWidth();
            int popHeight = ll_bitrate_layout.getMeasuredHeight();
            switch (direction) {
                case 0://指定控件左面显示：
                    this.showAtLocation(thePointView, Gravity.NO_GRAVITY, location[0] - popWidth + Xpixl, location[1] + Ypixl);
                    break;
                case 1://指定控件右面显示：
                    this.showAtLocation(thePointView, Gravity.NO_GRAVITY, location[0] + popWidth + Xpixl, location[1] + Ypixl);
                    return;
                case 2://指定控件上面显示：
                    this.showAtLocation(thePointView, Gravity.NO_GRAVITY, location[0] - popWidth + thePointView.getWidth()/2 + Xpixl, location[1] - popHeight + Ypixl);
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
