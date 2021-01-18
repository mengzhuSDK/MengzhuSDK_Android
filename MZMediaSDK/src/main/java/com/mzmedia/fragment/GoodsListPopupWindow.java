package com.mzmedia.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mengzhu.live.sdk.business.dto.MZGoodsListDto;
import com.mengzhu.live.sdk.business.dto.MZGoodsListExternalDto;
import com.mengzhu.live.sdk.core.utils.DensityUtil;
import com.mengzhu.live.sdk.core.utils.UiUtils;
import com.mengzhu.live.sdk.ui.api.MZApiDataListener;
import com.mengzhu.live.sdk.ui.api.MZApiRequest;
import com.mengzhu.sdk.R;
import com.mzmedia.pullrefresh.PullToRefreshBase;
import com.mzmedia.pullrefresh.PullToRefreshRecyclerView;
import com.mzmedia.widgets.dialog.AbstractPopupWindow;

import java.util.ArrayList;

import tv.mengzhu.core.wrap.netwock.Page;

public class GoodsListPopupWindow extends AbstractPopupWindow {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String TICKET_ID = "ticket-id";
    private int mColumnCount = 1;
    private Dialog mDialog = null;
    private Context mContext;
    private MZGoodsListExternalDto mzGoodsListExternalDto;
    private ArrayList<MZGoodsListDto> mGoodsList;
    private ArrayList<MZGoodsListDto> mTotalGoodsList = new ArrayList<>();
    private boolean isHeader = true;
    private MyGoodsListRecyclerViewAdapter myGoodsListRecyclerViewAdapter;
    private RelativeLayout fragment_goodlist_layout;
    private PullToRefreshRecyclerView fragment_goodlist_rv;
    private RecyclerView mRecyclerView;
    private TextView tv_dialog_goods_title;
    private ImageView iv_dialog_goods_close;
    private LinearLayout goodsLayout;
    private boolean isShowNoMoreLabel;
    private MZApiRequest apiRequest;
    private static OnGoodsLoadListener mOnGoodsLoadListener;
    private String ticketId;
    private View root;
    private Animation enterAnim, exitAnim;

    public GoodsListPopupWindow(Context context, int columnCount, String ticket_id, OnGoodsLoadListener onGoodsLoadListener) {
        super(context);
        this.mContext = context;
        root = View.inflate(context, R.layout.fragment_goodslist_list, null);
        goodsLayout = root.findViewById(R.id.ll_goods_layout);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) goodsLayout.getLayoutParams();
        params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        if (UiUtils.getOrientation(context) == Configuration.ORIENTATION_LANDSCAPE){
            params.height = DensityUtil.dip2px(context ,240);
        }else {
            params.height = DensityUtil.dip2px(context ,413);
        }
        goodsLayout.setLayoutParams(params);
        exitAnim = AnimationUtils.loadAnimation(context, R.anim.side_bottom_exit);
        enterAnim = AnimationUtils.loadAnimation(context, R.anim.side_bottom_enter);
        setContentView(root);
        mOnGoodsLoadListener = onGoodsLoadListener;
//        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        mColumnCount = columnCount;
        ticketId = ticket_id;
        initView(root);
    }

    private void initView(View view) {
        fragment_goodlist_layout = view.findViewById(R.id.fragment_goodlist_layout);
        fragment_goodlist_rv = view.findViewById(R.id.fragment_goodlist_rv);
        tv_dialog_goods_title = view.findViewById(R.id.tv_dialog_goods_title);
        iv_dialog_goods_close = view.findViewById(R.id.iv_dialog_goods_close);
        iv_dialog_goods_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mRecyclerView = fragment_goodlist_rv.getRefreshableView();
        // Set the adapter
        if (mRecyclerView != null) {
            Context context = view.getContext();
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            myGoodsListRecyclerViewAdapter = new MyGoodsListRecyclerViewAdapter(mTotalGoodsList, new MyGoodsListRecyclerViewAdapter.OnListFragmentInteractionListener() {
                @Override
                public void onListFragmentInteraction(MZGoodsListDto listBean) {
                    if (mOnGoodsListItemClickListener != null) {
                        mOnGoodsListItemClickListener.onGoodsListItemClick(listBean);
                    }
                }
            });
            mRecyclerView.setAdapter(myGoodsListRecyclerViewAdapter);
        }

        fragment_goodlist_rv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                isHeader = true;
                apiRequest.startData(MZApiRequest.API_TYPE_GOODS_LIST, true, ticketId);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                if (isShowNoMoreLabel) {
                    fragment_goodlist_rv.getLoadingLayoutProxy(false, true).setNoMoreLabel(true);
                    fragment_goodlist_rv.onRefreshComplete();
                } else {
                    isHeader = false;
                    apiRequest.startData(MZApiRequest.API_TYPE_GOODS_LIST, false, ticketId);
                }
            }
        });
        //请求商店列表api
        apiRequest = new MZApiRequest();
        apiRequest.createRequest(mContext, MZApiRequest.API_TYPE_GOODS_LIST);
        apiRequest.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String apiType, Object dto , Page page, int status) {
                mzGoodsListExternalDto = (MZGoodsListExternalDto) dto;
                mGoodsList = (ArrayList<MZGoodsListDto>) mzGoodsListExternalDto.getList();
                if (isHeader) {
                    mTotalGoodsList.clear();
                }
                isShowNoMoreLabel = mGoodsList.size() < 50;

                if (mGoodsList.size() != 0) {
                    mTotalGoodsList.addAll(mGoodsList);
                }
                if (mOnGoodsLoadListener != null) {
                    mOnGoodsLoadListener.onGoodsLoad(mTotalGoodsList, mzGoodsListExternalDto.getTotal());
                }
                fragment_goodlist_rv.onRefreshComplete();
                tv_dialog_goods_title.setText("全部商品·" + mzGoodsListExternalDto.getTotal());
                myGoodsListRecyclerViewAdapter.setData(mTotalGoodsList, mzGoodsListExternalDto.getTotal());
                myGoodsListRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void errorResult(String apiType, int code, String msg) {
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }
        });
        apiRequest.startData(MZApiRequest.API_TYPE_GOODS_LIST, true, ticketId);
    }

    public interface OnGoodsLoadListener {
        void onGoodsLoad(ArrayList<MZGoodsListDto> mzGoodsListDtos, int totalNum);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        root.startAnimation(enterAnim);
    }

    private boolean isAnim;

    @Override
    public void dismiss() {
        if (isAnim) {
            super.dismiss();
            isAnim = false;
            ((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            return;
        }
        exitAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAnim = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        root.startAnimation(exitAnim);
    }

    private OnGoodsListItemClickListener mOnGoodsListItemClickListener;

    public interface OnGoodsListItemClickListener {
        void onGoodsListItemClick(MZGoodsListDto dto);
    }

    public void setOnGoodsListItemClickListener(OnGoodsListItemClickListener listener) {
        mOnGoodsListItemClickListener = listener;
    }
}
