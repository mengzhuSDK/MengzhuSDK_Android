package com.mzmedia.widgets.LinearLayoutListView;

import android.view.View;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 *<p>对View对缓存</p><br/>
 *<p>主要是为viewpager多页缓存</p>
 * @since 1.0.0
 * @author sunxianhao
 */
public class ViewCache {
	List<SoftReference<View>> mCache ;

	public ViewCache() {
		mCache = new ArrayList<SoftReference<View>>();
	}
	
	public  void putCache(View view) {
		if (view == null) {
			return;
		}
		SoftReference<View> reference = new SoftReference<View>(view);
		mCache.add(reference);
	}
	
	public View getCache() {
		if (mCache.size() == 0) {
			return null;
		}
		SoftReference<View> reference = mCache.get(mCache.size() - 1);
		View convertView = null;
		if (reference != null) {
			convertView = reference.get();
			mCache.remove(mCache.size() - 1);
		}
		return convertView;
	}

	
	public void clearCache() {
		mCache.clear();
	}

}
