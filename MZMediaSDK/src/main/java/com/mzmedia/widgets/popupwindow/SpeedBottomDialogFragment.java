package com.mzmedia.widgets.popupwindow;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mengzhu.sdk.R;
import com.mzmedia.widgets.dialog.AbstractPopupWindow;

import java.util.ArrayList;
import java.util.List;

public class SpeedBottomDialogFragment extends AbstractPopupWindow implements View.OnClickListener {

    private Context mContext;
    private View root;
    private Animation enterAnim;
    private Animation exitAnim;
    private LinearLayout mLayout075;
    private LinearLayout mLayout1;
    private LinearLayout mLayout125;
    private LinearLayout mLayout15;
    private LinearLayout mLayout2;
    private ImageView mIv075;
    private ImageView mIv1;
    private ImageView mIv125;
    private ImageView mIv15;
    private ImageView mIv2;
    private TextView mTv075;
    private TextView mTv1;
    private TextView mTv125;
    private TextView mTv15;
    private TextView mTv2;
    private ImageView mTvCancel;
    private boolean isAnim;
    private List<TextView> speedTv = new ArrayList<>();
    private List<ImageView> speedIv = new ArrayList<>();
    private int selectPosition = 1;
    public SpeedBottomDialogFragment(Context context, boolean isHorizontal) {
        super(context);
        mContext = context;
        initView(context,isHorizontal);
    }

    private void initView(Context context,boolean isHorizontal){
        if (isHorizontal){
            root = View.inflate(context, R.layout.popup_window_speed_horizontal,null);
        }else {
            root = View.inflate(context, R.layout.popup_window_speed_bottom,null);
        }
        enterAnim = AnimationUtils.loadAnimation(context, R.anim.side_bottom_enter);
        exitAnim = AnimationUtils.loadAnimation(context, R.anim.side_bottom_exit);
        setContentView(root);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);

        mLayout075 = root.findViewById(R.id.ll_watchbroadcast_speed_075);
        mLayout1 = root.findViewById(R.id.ll_watchbroadcast_speed_1);
        mLayout125 = root.findViewById(R.id.ll_watchbroadcast_speed_125);
        mLayout15 = root.findViewById(R.id.ll_watchbroadcast_speed_15);
        mLayout2 = root.findViewById(R.id.ll_watchbroadcast_speed_2);

        mIv075 = root.findViewById(R.id.iv_watchbroadcast_speed_075);
        mIv1 = root.findViewById(R.id.iv_watchbroadcast_speed_1);
        mIv125 = root.findViewById(R.id.iv_watchbroadcast_speed_125);
        mIv15 = root.findViewById(R.id.iv_watchbroadcast_speed_15);
        mIv2 = root.findViewById(R.id.iv_watchbroadcast_speed_2);

        mTvCancel = root.findViewById(R.id.tv_watchbroadcast_speed_cancel);
        mTv075 = root.findViewById(R.id.tv_watchbroadcast_speed_075);
        mTv1 = root.findViewById(R.id.tv_watchbroadcast_speed_1);
        mTv125 = root.findViewById(R.id.tv_watchbroadcast_speed_125);
        mTv15 = root.findViewById(R.id.tv_watchbroadcast_speed_15);
        mTv2 = root.findViewById(R.id.tv_watchbroadcast_speed_2);

        mLayout075.setOnClickListener(this);
        mLayout1.setOnClickListener(this);
        mLayout125.setOnClickListener(this);
        mLayout15.setOnClickListener(this);
        mLayout2.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
        speedTv.add(mTv075);
        speedTv.add(mTv1);
        speedTv.add(mTv125);
        speedTv.add(mTv15);
        speedTv.add(mTv2);
        speedIv.add(mIv075);
        speedIv.add(mIv1);
        speedIv.add(mIv125);
        speedIv.add(mIv15);
        speedIv.add(mIv2);
        setSelectStatus();
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ll_watchbroadcast_speed_075) {
            if (mListener != null) {
                mListener.OnSelectSpeed(0);
                selectPosition = 0;
                setSelectStatus();
            }
            dismiss();
        } else if (id == R.id.ll_watchbroadcast_speed_1) {
            if (mListener != null) {
                mListener.OnSelectSpeed(1);
                selectPosition = 1;
                setSelectStatus();
            }
            dismiss();
        } else if (id == R.id.ll_watchbroadcast_speed_125) {
            if (mListener != null) {
                mListener.OnSelectSpeed(2);
                selectPosition = 2;
                setSelectStatus();
            }
            dismiss();
        } else if (id == R.id.ll_watchbroadcast_speed_15) {
            if (mListener != null) {
                mListener.OnSelectSpeed(3);
                selectPosition = 3;
                setSelectStatus();
            }
            dismiss();
        } else if (id == R.id.ll_watchbroadcast_speed_2) {
            if (mListener != null) {
                mListener.OnSelectSpeed(4);
                selectPosition = 4;
                setSelectStatus();
            }
            dismiss();
        } else if (id == R.id.tv_watchbroadcast_speed_cancel) {
            dismiss();
        }
    }

    private void setSelectStatus(){
        for(int i = 0;i<5;i++){
            if(i == selectPosition){
                speedTv.get(i).setTextColor(ContextCompat.getColor(mContext,R.color.color_ff2145));
            }else {
                speedTv.get(i).setTextColor(ContextCompat.getColor(mContext,R.color.color_333333));
            }
        }
        for (int j = 0;j<5;j++){
            if(j == selectPosition){
                speedIv.get(j).setVisibility(View.VISIBLE);
            }else {
                speedIv.get(j).setVisibility(View.INVISIBLE);
            }
        }
    }
    private OnSelectSpeedListener mListener;
    public interface OnSelectSpeedListener{
        void OnSelectSpeed(int type);
    }
    public void setOnSelectSpeedListener(OnSelectSpeedListener onSelectSpeedListener){
        mListener = onSelectSpeedListener;
    }

    public void showAtLocation(View parent, int index) {
        super.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        selectPosition = index;
        setSelectStatus();
        ((ViewGroup) root).getChildAt(0).startAnimation(enterAnim);
    }
    @Override
    public void dismiss() {
        if (isAnim) {
            super.dismiss();
            return;
        }
        exitAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAnim = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        ((ViewGroup) root).getChildAt(0).startAnimation(exitAnim);
    }
}
