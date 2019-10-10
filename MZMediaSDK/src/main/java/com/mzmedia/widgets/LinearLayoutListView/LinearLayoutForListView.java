package com.mzmedia.widgets.LinearLayoutListView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.mengzhu.live.sdk.R;
import com.mengzhu.live.sdk.core.utils.DensityUtil;


/**
 * <p>模仿listView展开模式</p><br/>
 * <p>用linearLayout模仿listView展开模式,此控件存在原因：
 * 因为官方建议每个界面只可以存在一个listView,否则会导致栈溢出的异常,
 * 所有linearLayout来模仿listView</p>
 *
 * @author sunxh
 * @since 2.6
 */
public class LinearLayoutForListView extends android.widget.LinearLayout {
    private android.widget.BaseAdapter adapter;
    private OnClickListener onClickListener = null;
    private int counts;
    private ViewCache mCache;
    private ViewCache mLineCache;
    private Context mContext;
    private static final int LINE_ID = 10029;

    /**
     * <p>控件初始化</p>
     * <p>调用传进来的adapter获取view</p>
     *
     * @author sunxh
     * @since 2.6
     */
    public void fillLinearLayout() {
        int count = adapter.getCount();
        removeAllViews();
        counts = 0;
        View v = null;
        View line = null;
        for (int i = 0; i < count; i++) {
            View contentView = mCache.getCache();
            v = adapter.getView(i, contentView, null);
            if (contentView == null) {
                mCache.putCache(contentView);
            }
            line = new View(mContext);
            line.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
            line.setBackgroundResource(R.color.color_dddddd);
            v.setOnClickListener(this.onClickListener);
//			onClickListener.onClick(v);
            v.setId(i);
            line.setId(i + LINE_ID);
            if (DensityUtil.getDensity(mContext) >= 3) {
                addView(v, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120));
            } else {
                addView(v, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 80));
            }
            if (i + 1 != count) {
                addView(line, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
            }
            counts++;
        }
    }


    /**
     * <p>去除更多按钮</p>
     *
     * @author sunxh
     * @since 2.6
     */
    public void removeMore() {
        View view = getChildAt(counts - 1);
        if (getChildAt(counts) != null) {
            removeViewAt(counts);
        }

    }

    public LinearLayoutForListView(Context context) {
        super(context);
        mContext = context;
        mCache = new ViewCache();
    }

    public LinearLayoutForListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mCache = new ViewCache();
        mLineCache = new ViewCache();
    }

    /**
     * <p>获取adapter方法</p>
     *
     * @return
     * @author sunxh
     * @since 2.6
     */
    public android.widget.BaseAdapter getAdpater() {
        return adapter;
    }

    /**
     * <p>添加adapter</p>
     *
     * @param adapter
     * @author sunxh
     * @since 2.6
     */
    public void setAdapter(android.widget.BaseAdapter adapter) {
        this.adapter = adapter;
        fillLinearLayout();

    }

    /**
     * <p>获取监听方法</p>
     * <p>TODO</p>(详细描述)
     *
     * @return
     * @author sunxh
     * @since 2.6
     */
    public OnClickListener getOnclickListner() {
        return onClickListener;
    }

    /**
     * <p>获取监听方法</p>
     * <p>TODO</p>(详细描述)
     *
     * @param onClickListener
     * @author sunxh
     * @since 2.6
     */
    public void setOnclickLinstener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    /**
     * <p>控件刷新方法</p>
     * <p>TODO</p>(详细描述)
     *
     * @return
     * @author sunxh
     * @since 2.6
     */
    public boolean inform() {
        // TODO Auto-generated method stub
        fillLinearLayout();
        return true;
    }

}