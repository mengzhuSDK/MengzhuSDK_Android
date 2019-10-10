package com.mzmedia.adapter.base;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

/**
 * <p>最基本的itemView</p><br/>
 * <p>TODO (类的详细的功能描述)</p>
 *
 * @author sunxianhao
 * @since 1.0.0
 */
public abstract class BaseViewObtion<T> {
    private OnWrapItemClickListener mClickListener;
    protected Activity mActivity;

    public BaseViewObtion() {

    }

    public void setOnWrapItemClickListener(OnWrapItemClickListener clickListener) {
        mClickListener = clickListener;
    }

    protected void onItemElementClick(View v, Object... objects) {
        if (mClickListener != null) {
            mClickListener.onItemClick(v, objects);
        }
    }

    public void setOnActivity(Activity activity) {
        mActivity = activity;
    }

    public Activity getActivity() {
        return mActivity;
    }

//    public boolean isLock() {
//        if (null != mActivity && mActivity instanceof WatchBroadcastActivity) {
//            return ((WatchBroadcastActivity) mActivity).getIsLock();
//        }
//        return false;
//    }

    public View getView(int layoutid) {
        return getActivity().getLayoutInflater().inflate(layoutid, null);
    }

    /**
     * <p>创建view</p><br/>
     * <p>TODO(详细描述)</p>
     *
     * @param t
     * @param position
     * @param convertView
     * @param parent
     * @return
     * @author sunxianhao
     * @since 1.0.0
     */
    public abstract View createView(T t, int position, View convertView, ViewGroup parent);

    /**
     * <p>更新view中的数据</p><br/>
     * <p>TODO(详细描述)</p>
     *
     * @param t
     * @param position
     * @param convertView
     * @author sunxianhao
     * @since 1.0.0
     */
    public abstract void updateView(T t, int position, View convertView);


}
