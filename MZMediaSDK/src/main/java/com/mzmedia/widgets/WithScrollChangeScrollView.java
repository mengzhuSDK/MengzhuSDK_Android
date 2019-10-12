package com.mzmedia.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class WithScrollChangeScrollView extends ScrollView {
    public WithScrollChangeScrollView(Context context) {
        super(context);
    }

    public WithScrollChangeScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WithScrollChangeScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private OnScrollChangeListener listener;

    public void setOnScrollChangeListener(OnScrollChangeListener listener) {
        this.listener = listener;
    }

    public interface OnScrollChangeListener {
        void onScroll(int scrollY);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (listener != null) {
            listener.onScroll(t);
        }
    }
}
