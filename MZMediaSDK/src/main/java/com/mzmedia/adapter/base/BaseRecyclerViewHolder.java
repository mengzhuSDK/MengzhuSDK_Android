package com.mzmedia.adapter.base;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by DELL on 2017/8/30.
 */

public class BaseRecyclerViewHolder<T> extends RecyclerView.ViewHolder{
    private BaseRecyclerViewObtion<T> mObtion;
    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
    }
    public void setBaseRecyclerViewObtion(BaseRecyclerViewObtion<T> obtion){
        mObtion=obtion;
    }

    public BaseRecyclerViewObtion<T> getBaseRecyclerViewObtion() {
        return mObtion;
    }
}