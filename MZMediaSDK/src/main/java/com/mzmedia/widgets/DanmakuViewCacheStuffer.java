package com.mzmedia.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mengzhu.sdk.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.imageaware.NonViewAware;

import java.lang.ref.WeakReference;

import tv.mengzhu.core.frame.coreutils.DensityUtil;
import tv.mengzhu.sdk.danmaku.controller.IDanmakuView;
import tv.mengzhu.sdk.danmaku.danmaku.model.BaseDanmaku;
import tv.mengzhu.sdk.danmaku.danmaku.model.android.AndroidDisplayer;
import tv.mengzhu.sdk.danmaku.danmaku.model.android.ViewCacheStuffer;
import tv.mengzhu.sdk.danmaku.danmaku.util.SystemClock;


public class DanmakuViewCacheStuffer extends ViewCacheStuffer<DanmakuViewCacheStuffer.MyViewHolder> {
    private Context mContext;
    private IDanmakuView mDanmakuView;

    public DanmakuViewCacheStuffer(Context context, IDanmakuView danmakuView) {
        this.mContext = context;
        this.mDanmakuView = danmakuView;
    }

    @Override
    public MyViewHolder onCreateViewHolder(int viewType) {
        return new MyViewHolder(View.inflate(mContext, R.layout.layout_view_cache, null));
    }

    @Override
    public void onBindViewHolder(int viewType, MyViewHolder viewHolder, BaseDanmaku danmaku, AndroidDisplayer.DisplayerConfig displayerConfig, TextPaint paint) {
        if (paint != null) {
            viewHolder.mText.getPaint().set(paint);
        }

        viewHolder.mText.setText( danmaku.text.toString());
        viewHolder.mText.setTextColor(danmaku.textColor);
        viewHolder.mText.setTextSize(danmaku.textSize);
        Bitmap bitmap = null;
        MyImageWare imageWare = (MyImageWare) danmaku.tag;
        if (imageWare != null) {
            bitmap = imageWare.bitmap;
        }
        if (bitmap != null) {
            viewHolder.mIcon.setImageBitmap(bitmap);
        } else {
            viewHolder.mIcon.setImageResource(R.mipmap.ic_launcher);
        }
        danmaku.paintHeight=viewHolder.mIcon.getLayoutParams().height+10;
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        viewHolder.mText.measure(spec,spec);
        danmaku.paintWidth= danmaku.paintHeight + viewHolder.mText.getMeasuredWidth() + 30;
    }

    @Override
    public void releaseResource(BaseDanmaku danmaku) {
        MyImageWare imageWare = (MyImageWare) danmaku.tag;
        if (imageWare != null) {
            ImageLoader.getInstance().cancelDisplayTask(imageWare);
        }
        danmaku.setTag(null);
    }

    int mIconWidth;

    @Override
    public void prepare(BaseDanmaku danmaku, boolean fromWorkerThread) {
        if (danmaku.isTimeOut()) {
            return;
        }
        MyImageWare imageWare = (MyImageWare) danmaku.tag;
        if (imageWare == null) {
            imageWare = new MyImageWare(danmaku.avatar, danmaku, mIconWidth, mIconWidth, mDanmakuView);
            danmaku.setTag(imageWare);
        }
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mContext));
        ImageLoader.getInstance().displayImage(imageWare.getImageUri(), imageWare);
    }


    public class MyViewHolder extends ViewCacheStuffer.ViewHolder {

        private final CircleImageView mIcon;
        private final TextView mText;
        private View mItemView;

        MyViewHolder(View itemView) {
            super(itemView);
            mIcon = (CircleImageView) itemView.findViewById(R.id.icon);
            mText = (TextView) itemView.findViewById(R.id.text);
            mItemView=itemView;
        }

        @Override
        public void measure(int widthMeasureSpec, int heightMeasureSpec) {
            try {
                super.measure(widthMeasureSpec, heightMeasureSpec);
            } catch (Exception ignored) {
            }
        }
    }


    public static class MyImageWare extends NonViewAware {

        private long start;
        private int id;
        private WeakReference<IDanmakuView> danmakuViewRef;
        private BaseDanmaku danmaku;
        private Bitmap bitmap;

        MyImageWare(String imageUri, BaseDanmaku danmaku, int width, int height, IDanmakuView danmakuView) {
            this(imageUri, new ImageSize(width, height), ViewScaleType.FIT_INSIDE);
            if (danmaku == null) {
                throw new IllegalArgumentException("danmaku may not be null");
            }
            this.danmaku = danmaku;
            this.id = danmaku.hashCode();
            this.danmakuViewRef = new WeakReference<>(danmakuView);
            this.start = SystemClock.uptimeMillis();
        }


        @Override
        public int getId() {
            return this.id;
        }

        String getImageUri() {
            return this.imageUri;
        }

        private MyImageWare(ImageSize imageSize, ViewScaleType scaleType) {
            super(imageSize, scaleType);
        }

        private MyImageWare(String imageUri, ImageSize imageSize, ViewScaleType scaleType) {
            super(imageUri, imageSize, scaleType);
        }

        @Override
        public boolean setImageDrawable(Drawable drawable) {
            return super.setImageDrawable(drawable);
        }

        @Override
        public boolean setImageBitmap(Bitmap bitmap) {
            if (this.danmaku.isTimeOut() || this.danmaku.isFiltered()) {
                return true;
            }
            this.bitmap = bitmap;
            IDanmakuView danmakuView = danmakuViewRef.get();
            if (danmakuView != null) {
                danmakuView.invalidateDanmaku(danmaku, true);
            }
            return true;
        }
    }
}
