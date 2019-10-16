package com.mzmedia.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mengzhu.live.sdk.R;
import com.mengzhu.live.sdk.business.dto.MZGoodsListDto;
import com.mzmedia.fragment.dummy.DummyContent.DummyItem;
import com.mzmedia.utils.String_Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyGoodsListRecyclerViewAdapter extends RecyclerView.Adapter<MyGoodsListRecyclerViewAdapter.ViewHolder> {

    private ArrayList<MZGoodsListDto> mValues;
    private OnListFragmentInteractionListener mListener;
    private int labelPosition;
    private int mTotalNum;

    public MyGoodsListRecyclerViewAdapter(ArrayList<MZGoodsListDto> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public void setData(ArrayList<MZGoodsListDto> items, int totalNum) {
        mValues = items;
        labelPosition = mValues.size();
        mTotalNum = totalNum;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_channel_goods, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MZGoodsListDto listBean = mValues.get(position);
        holder.tv_item_goods_name.setText(listBean.getName());
        if (mTotalNum <= 1) {
            labelPosition = 1;
        } else {
            labelPosition = mTotalNum - position;
        }
        holder.tv_item_goods_icon_label.setText("" + labelPosition);
        holder.tv_item_goods_price.setText("¥" + listBean.getPrice());
        ImageLoader.getInstance().displayImage(listBean.getPic() + String_Utils.getPictureSizeAvatar(), holder.iv_item_goods_icon, new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.icon_goods_default)
                .showImageForEmptyUri(R.mipmap.icon_goods_default)
                .showImageOnFail(R.mipmap.icon_goods_default)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .displayer(new RoundedBitmapDisplayer(0))
                .build());
        holder.layout_item_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(listBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout layout_item_goods;
        private TextView tv_item_goods_name;
        private ImageView iv_item_goods_icon;
        private TextView tv_item_goods_price;
        private TextView tv_item_goods_icon_label;

        private ViewHolder(View view) {
            super(view);
            layout_item_goods = view.findViewById(R.id.layout_item_goods);
            tv_item_goods_icon_label = view.findViewById(R.id.tv_item_goods_icon_label);
            tv_item_goods_name = view.findViewById(R.id.tv_item_goods_name);
            iv_item_goods_icon = view.findViewById(R.id.iv_item_goods_icon);
            tv_item_goods_price = view.findViewById(R.id.tv_item_goods_price);
        }

    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(MZGoodsListDto listBean);
    }
}
