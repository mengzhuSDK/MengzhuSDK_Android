package com.mzmedia.presentation.biz;


import tv.mengzhu.core.wrap.netwock.Page;

public interface IBaseBizListener {
	public void dataResult(Object obj, Page page, int status);
	public void errerResult(int code, String msg);

}
