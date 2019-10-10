package com.mzmedia.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.mengzhu.live.sdk.R;
import com.mengzhu.live.sdk.business.dto.MZGoodsListDto;
import com.mengzhu.live.sdk.business.dto.MZGoodsListExternalDto;
import com.mengzhu.live.sdk.ui.api.MZApiDataListener;
import com.mengzhu.live.sdk.ui.api.MZApiRequest;

import java.util.ArrayList;

public class GoodsListFragment extends DialogFragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private Dialog mDialog = null;
    private Context mContext;
    private MZGoodsListExternalDto mzGoodsListExternalDto;
    private ArrayList<MZGoodsListDto> mGoodsList;
    private ArrayList<MZGoodsListDto> mTotalGoodsList = new ArrayList<>();
    private boolean isHeader = true;
    private MyGoodsListRecyclerViewAdapter myGoodsListRecyclerViewAdapter;
    private RecyclerView fragment_goodlist_rv;
    public GoodsListFragment() {
    }

    public static GoodsListFragment newInstance(int columnCount) {
        GoodsListFragment fragment = new GoodsListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initDialog();
        View view = inflater.inflate(R.layout.fragment_goodslist_list, container, false);
        fragment_goodlist_rv = view.findViewById(R.id.fragment_goodlist_rv);

        // Set the adapter
        if (fragment_goodlist_rv != null) {
            Context context = view.getContext();
            if (mColumnCount <= 1) {
                fragment_goodlist_rv.setLayoutManager(new LinearLayoutManager(context));
            } else {
                fragment_goodlist_rv.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            myGoodsListRecyclerViewAdapter = new MyGoodsListRecyclerViewAdapter(mTotalGoodsList, new MyGoodsListRecyclerViewAdapter.OnListFragmentInteractionListener() {
                @Override
                public void onListFragmentInteraction(MZGoodsListDto listBean) {

                }
            });
            fragment_goodlist_rv.setAdapter(myGoodsListRecyclerViewAdapter);
        }

        MZApiRequest apiRequest = new MZApiRequest();
        apiRequest.createRequest(mContext, MZApiRequest.API_TYPE_GOODS_LIST);
        apiRequest.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String apiType, Object dto) {
                mzGoodsListExternalDto = (MZGoodsListExternalDto) dto;
                mGoodsList = (ArrayList<MZGoodsListDto>) mzGoodsListExternalDto.getList();
                if (isHeader) {
                    mTotalGoodsList.clear();
                }

                if (mGoodsList.size() != 0) {
                    mTotalGoodsList.addAll(mGoodsList);
                }
                myGoodsListRecyclerViewAdapter.setData(mTotalGoodsList);
                myGoodsListRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void errorResult(String apiType, int code, String msg) {
            }
        });
        apiRequest.startData(MZApiRequest.API_TYPE_GOODS_LIST, true, "10008540");
        return view;
    }

    private void initDialog() {
        mDialog = getDialog();
        mDialog.getWindow().getAttributes().windowAnimations = R.style.dialogAnim;
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
//        lp.height = screenH - screenW * 10 / 16 - 10;
//        lp.height = mActivity.getBottomFragmentHeight();
        lp.height = 600;

        window.setAttributes(lp);
        mDialog.setCanceledOnTouchOutside(true); // 外部点击取消
        window.requestFeature(Window.FEATURE_NO_TITLE);
        Bundle args = getArguments();
//        if (null != args && args.containsKey(PLAYINFO)) {
//            mPlayInfoDto = (PlayInfoDto) args.getSerializable(PLAYINFO);
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
