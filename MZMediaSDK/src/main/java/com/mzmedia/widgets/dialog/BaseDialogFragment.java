package com.mzmedia.widgets.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public abstract class BaseDialogFragment extends DialogFragment {

    public int DEFAULT_WIDTH = WindowManager.LayoutParams.MATCH_PARENT;//宽
    public int DEFAULT_HEIGHT = WindowManager.LayoutParams.WRAP_CONTENT;//高
    public int DEFAULT_GRAVITY = Gravity.CENTER;//位置

    private boolean mCancelable = true;//默认可取消
    private boolean mCanceledOnTouchOutside = true;//默认点击外部可取消

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(getLayoutId(), container, false);
        initViews(mView);
        return mView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog mDialog = super.onCreateDialog(savedInstanceState);
        if (null != mDialog) {//初始化
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside);
            mDialog.setCancelable(mCancelable);
            Window window = mDialog.getWindow();
            if (null != window) {
                window.getDecorView().setPadding(0, 0, 0, 0);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.width = DEFAULT_WIDTH;
                lp.height = DEFAULT_HEIGHT;
                lp.gravity = DEFAULT_GRAVITY;
                lp.windowAnimations = android.R.style.Animation_InputMethod;
                window.setAttributes(lp);
            }
            mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return !mCancelable;
                }
            });
        }
        return mDialog;
    }

    /**
     * 设置位置
     *
     * @param gravity
     */
    public void setGravity(int gravity) {
        DEFAULT_GRAVITY = gravity;
    }

    /**
     * 设置宽
     *
     * @param width
     */
    public void setWidth(int width) {
        DEFAULT_WIDTH = width;
    }

    /**
     * 设置高
     *
     * @param height
     */
    public void setHeight(int height) {
        DEFAULT_HEIGHT = height;
    }

    /**
     * 设置点击返回按钮是否可取消
     *
     * @param cancelable
     */
    public void setCancelable(boolean cancelable) {
        mCancelable = cancelable;
    }

    /**
     * 设置点击外部是否可取消
     *
     * @param canceledOnTouchOutside
     */
    public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        mCanceledOnTouchOutside = canceledOnTouchOutside;
    }

    /**
     * 设置布局
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化Views
     *
     * @param v
     */
    protected abstract void initViews(View v);
}
