package com.mzmedia.presentation.biz;

import com.mengzhu.live.sdk.core.netwock.Page;


public interface IBaseBizListener {
	public void dataResult(Object obj, Page page, int status);
	public void errerResult(int code, String msg);

}
