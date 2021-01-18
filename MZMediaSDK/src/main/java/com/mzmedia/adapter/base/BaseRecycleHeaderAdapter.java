package com.mzmedia.adapter.base;

import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class BaseRecycleHeaderAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	protected View mHeaderView;

	public static final int TYPE_HEADER = 0;
	public static final int TYPE_NORMAL = 1;

	public abstract void clearData();

	public abstract void setData(List<T> list);

	public abstract List<T> getSelectedList();

	public void setHeaderView(View headerView) {
		mHeaderView = headerView;
		notifyItemInserted(0);
	}

	@Override
	public int getItemViewType(int position) {
		if (mHeaderView == null) return TYPE_NORMAL;
		if (position == 0) return TYPE_HEADER;
		return TYPE_NORMAL;
	}

	public int getRealPosition(RecyclerView.ViewHolder holder) {
		int position = holder.getLayoutPosition();
		return mHeaderView == null ? position : position - 1;
	}

	@Override
	public void onAttachedToRecyclerView(RecyclerView recyclerView) {
		super.onAttachedToRecyclerView(recyclerView);
		RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
		if (manager instanceof GridLayoutManager) {
			final GridLayoutManager gridManager = ((GridLayoutManager) manager);
			gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
				@Override
				public int getSpanSize(int position) {
					return getItemViewType(position) == TYPE_HEADER
							? gridManager.getSpanCount() : 1;
				}
			});
		}
	}
}
