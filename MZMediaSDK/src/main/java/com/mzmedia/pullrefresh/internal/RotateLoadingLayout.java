package com.mzmedia.pullrefresh.internal;

/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView.ScaleType;

import com.mengzhu.sdk.R;
import com.mzmedia.pullrefresh.PullToRefreshBase;

@SuppressLint("ViewConstructor")
public class RotateLoadingLayout extends LoadingLayout {

	static final int ROTATION_ANIMATION_DURATION = 1200;

	private final Animation mRotateAnimation;
	private final Matrix mHeaderImageMatrix;
	private float mRotationPivotX, mRotationPivotY;

	private final Animation mRotateAnimationWrapper;
	private final Matrix mHeaderImageMatrixWrapper;

	private final boolean mRotateDrawableWhilePulling;

	public RotateLoadingLayout(Context context, PullToRefreshBase.Mode mode, PullToRefreshBase.Orientation scrollDirection, TypedArray attrs) {
		super(context, mode, scrollDirection, attrs);

		//TODO 需求拉动过程中不转动
//		mRotateDrawableWhilePulling = attrs.getBoolean(R.styleable.PullToRefresh_ptrRotateDrawableWhilePulling, true);
		mRotateDrawableWhilePulling = false;

		iv_inner.setScaleType(ScaleType.MATRIX);
		mHeaderImageMatrix = new Matrix();
		iv_inner.setImageMatrix(mHeaderImageMatrix);

		iv_wrapper.setScaleType(ScaleType.MATRIX);
		mHeaderImageMatrixWrapper = new Matrix();
		iv_wrapper.setImageMatrix(mHeaderImageMatrixWrapper);

		mRotateAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
		mRotateAnimation.setDuration(ROTATION_ANIMATION_DURATION);
		mRotateAnimation.setRepeatCount(Animation.INFINITE);
		mRotateAnimation.setRepeatMode(Animation.RESTART);

		mRotateAnimationWrapper = new RotateAnimation(0, -720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateAnimationWrapper.setInterpolator(ANIMATION_INTERPOLATOR);
		mRotateAnimationWrapper.setDuration(ROTATION_ANIMATION_DURATION);
		mRotateAnimationWrapper.setRepeatCount(Animation.INFINITE);
		mRotateAnimationWrapper.setRepeatMode(Animation.RESTART);
	}

	public void onLoadingDrawableSet(Drawable imageDrawable) {
		if (null != imageDrawable) {
			mRotationPivotX = Math.round(imageDrawable.getIntrinsicWidth() / 2f);
			mRotationPivotY = Math.round(imageDrawable.getIntrinsicHeight() / 2f);
		}
	}

	protected void onPullImpl(float scaleOfLayout) {
		float angle;
		if (mRotateDrawableWhilePulling) {
			angle = scaleOfLayout * 90f;
		} else {
			angle = Math.max(0f, Math.min(180f, scaleOfLayout * 360f - 180f));
		}

		mHeaderImageMatrix.setRotate(angle, mRotationPivotX, mRotationPivotY);
		iv_inner.setImageMatrix(mHeaderImageMatrix);
	}

	@Override
	protected void refreshingImpl() {
		iv_inner.startAnimation(mRotateAnimation);
		iv_wrapper.startAnimation(mRotateAnimationWrapper);
	}

	@Override
	protected void resetImpl() {
		iv_inner.clearAnimation();
		iv_wrapper.clearAnimation();
		resetImageRotation();
	}

	private void resetImageRotation() {
		if (null != mHeaderImageMatrix) {
			mHeaderImageMatrix.reset();
			iv_inner.setImageMatrix(mHeaderImageMatrix);
		}
		if (null != mHeaderImageMatrixWrapper) {
			mHeaderImageMatrixWrapper.reset();
			iv_wrapper.setImageMatrix(mHeaderImageMatrixWrapper);
		}
	}

	@Override
	protected void pullToRefreshImpl() {
		// NO-OP
	}

	@Override
	protected void releaseToRefreshImpl() {
		// NO-OP
	}

	@Override
	protected int getDefaultDrawableResId() {
		return R.drawable.icon_pull_to_refresh;
	}

}
