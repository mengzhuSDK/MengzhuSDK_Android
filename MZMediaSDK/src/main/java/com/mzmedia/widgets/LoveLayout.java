package com.mzmedia.widgets;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.mengzhu.live.sdk.R;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 点赞飘出动画
 * */
public class LoveLayout extends RelativeLayout {

	private Context context;
	private LayoutParams params;
	private Drawable icons;
	private Interpolator[] interpolators = new Interpolator[4];
	private int mWidth;
	private int mHeight;
	private Handler handler;
	private Runnable runnable;
	public LoveLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	public void initView() {
		handler = new Handler();
		// 图片资源
		icons = getResources().getDrawable(R.mipmap.gm_icon_redheart);


		// 插值器
		interpolators[0] = new AccelerateDecelerateInterpolator(); // 在动画开始与结束的地方速率改变比较慢，在中间的时候加速
		interpolators[1] = new AccelerateInterpolator();  // 在动画开始的地方速率改变比较慢，然后开始加速
		interpolators[2] = new DecelerateInterpolator(); // 在动画开始的地方快然后慢
		interpolators[3] = new LinearInterpolator();  // 以常量速率改变
		
		int width = (int) (icons.getIntrinsicWidth() * 1);
		int height = (int) (icons.getIntrinsicWidth() * 1);
		params = new LayoutParams(width, height);
		params.addRule(CENTER_HORIZONTAL, TRUE);
		params.addRule(ALIGN_PARENT_BOTTOM, TRUE);
	}

	public void removeView(){
		handler.removeCallbacks(runnable);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mWidth = getMeasuredWidth();
		mHeight = getMeasuredHeight();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				LoveLayout.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				mWidth = LoveLayout.this.getMeasuredWidth();
				mHeight = LoveLayout.this.getMeasuredHeight();
				Log.e("wzh","h="+mHeight+"w="+mWidth);
				runnable = new Runnable() {
					@Override
					public void run() {
						addLoveView();
						handler.postDelayed(runnable,1000);
					}
				};
				handler.post(runnable);
			}
		});
	}

	public void addLoveView() {
		// TODO Auto-generated method stub
		final ImageView iv = new ImageView(context);
		iv.setLayoutParams(params);
		iv.setImageDrawable(icons);
		addView(iv);

		// 开启动画，并且用完销毁
		AnimatorSet set = getAnimatorSet(iv);
		set.setDuration(3000);
		set.start();
		set.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				super.onAnimationEnd(animation);
				removeView(iv);
			}
		});
	}


	/**
	 * 获取动画集合
	 * @param iv 
	 * */
	private AnimatorSet getAnimatorSet(ImageView iv) {

		// 1.alpha动画
		ObjectAnimator alpha = ObjectAnimator.ofFloat(iv, "alpha", 0.3f, 1f);
		// 2.缩放动画
		ObjectAnimator scaleX = ObjectAnimator.ofFloat(iv, "scaleX", 0.4f,1f);
		ObjectAnimator scaleY = ObjectAnimator.ofFloat(iv, "scaleY", 0.4f,1f);
		// 动画集合
		AnimatorSet set = new AnimatorSet();
		set.playTogether(getBzierAnimator(iv),scaleX,scaleY,alpha);
		set.setTarget(iv);
		return set;
	}

	/**
	 * 贝塞尔动画
	 * */
	private ValueAnimator getBzierAnimator(final ImageView iv) {
		// TODO Auto-generated method stub
		PointF[] PointFs = getPointFs(); // 4个点的坐标
		BasEvaluator evaluator = new BasEvaluator(PointFs[1], PointFs[2]);
		ValueAnimator valueAnim = ValueAnimator.ofObject(evaluator,PointFs[0],PointFs[3]);
		valueAnim.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// 刷新view移动
				PointF p = (PointF) animation.getAnimatedValue();
				iv.setX(p.x);
				iv.setY(p.y);
//				iv.setAlpha(1- animation.getAnimatedFraction()); // 透明度
			}
		});
		valueAnim.setTarget(iv);
		valueAnim.setInterpolator(interpolators[3]);
		return valueAnim;
	}


	private PointF[] getPointFs() {
		// TODO Auto-generated method stub
		PointF[] PointFs = new PointF[4];
		PointFs[0] = new PointF(); // p0
		PointFs[0].x = (mWidth- params.width)/ 2;
		PointFs[0].y = mHeight;

		PointFs[1] = new PointF();
		PointFs[1].x = (float) (-params.width/2+Math.random()*(mWidth+params.width/2));
		PointFs[1].y = (float) ((3*mHeight)/4);

		PointFs[2] = new PointF();
		PointFs[2].x = (float) (-params.width/2+Math.random()*(mWidth+params.width/2));
		PointFs[2].y = (float) ((mHeight)/3);

		PointFs[3] = new PointF();
		PointFs[3].x = (mWidth- params.width)/ 2;
		PointFs[3].y = 0;
		return PointFs;
	}
}