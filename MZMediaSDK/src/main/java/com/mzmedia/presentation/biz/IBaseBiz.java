package com.mzmedia.presentation.biz;


import android.content.Context;

public interface IBaseBiz<T> {
	public  void initBiz(Context context);
	public  void registerListener(T listener);
	public  void startData(Object... obj);
}
