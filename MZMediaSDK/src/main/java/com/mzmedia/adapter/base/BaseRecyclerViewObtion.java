package com.mzmedia.adapter.base;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Created by DELL on 2017/8/30.
 */

public abstract class BaseRecyclerViewObtion<T> {
	private OnWrapItemClickListener mClickListener;
	protected Activity mActivity;
	private List<T> mDataList;

	public void updataDataList(List<T> list) {
		mDataList = list;
	}

	public List<T> getDataList() {
		return mDataList;
	}

	public BaseRecyclerViewObtion() {

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

	public View getView(int layoutid) {
		return getActivity().getLayoutInflater().inflate(layoutid, null);
	}

	/**
	 * <p>创建view</p><br/>
	 * <p>TODO(详细描述)</p>
	 *
	 * @param parent
	 * @return
	 * @author sunxianhao
	 * @since 1.0.0
	 */
	public abstract RecyclerView.ViewHolder createView(ViewGroup parent, int viewType);

	/**
	 * <p>更新view中的数据</p><br/>
	 * <p>TODO(详细描述)</p>
	 *
	 * @param t
	 * @param position
	 * @param holder
	 * @author sunxianhao
	 * @since 1.0.0
	 */
	public abstract void updateView(T t, int position, RecyclerView.ViewHolder holder);
}
