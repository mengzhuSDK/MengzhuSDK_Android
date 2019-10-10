package com.mzmedia.widgets.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

public abstract class AbstractPopupWindow extends PopupWindow {

    public AbstractPopupWindow(Context context) {
        super(context);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.MATCH_PARENT);
        setAnimationStyle(0);
        ColorDrawable dw = new ColorDrawable(0x80000000);
        setBackgroundDrawable(dw);
        setFocusable(true);
    }

    @Override
    public void setContentView(View contentView) {
        super.setContentView(contentView);
        final ViewGroup root = (ViewGroup) contentView;
        contentView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isOutSideDismiss) {
                    int top = root.getChildAt(0).getTop();
                    int bottom = root.getChildAt(0).getBottom();
                    int left = root.getChildAt(0).getLeft();
                    int right = root.getChildAt(0).getRight();
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (y < top || y > bottom || x < left || x > right) {
                            dismiss();
                        }
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
    }

    private boolean isOutSideDismiss = true;

    protected void setOutSideDismiss(boolean outSideDismiss) {
        isOutSideDismiss = outSideDismiss;
    }

//    @Override
//    public void dismiss() {
//        super.dismiss();
//        if (Build.VERSION.SDK_INT > 28) {
//        //TODO ANDROID 10
////            if (isShowing()) {
//////                new Thread(new Runnable() {
//////                    @Override
//////                    public void run() {
////                        CommonUtil.execByRuntime("input keyevent 4");
//////                    }
//////                }).start();
////            }
//        }
////        else {
////        }
//    }
}
