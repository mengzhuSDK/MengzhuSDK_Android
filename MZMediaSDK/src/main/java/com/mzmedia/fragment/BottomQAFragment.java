package com.mzmedia.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mengzhu.live.sdk.business.dto.play.PlayInfoDto;
import com.mengzhu.live.sdk.business.dto.qa.QAListDto;
import com.mengzhu.live.sdk.business.dto.qa.QAModelDto;
import com.mengzhu.live.sdk.business.dto.qa.SubmitQADto;
import com.mengzhu.live.sdk.core.utils.DensityUtil;
import com.mengzhu.live.sdk.core.utils.ToastUtils;
import com.mengzhu.live.sdk.ui.api.MZApiDataListener;
import com.mengzhu.live.sdk.ui.api.MZApiRequest;
import com.mengzhu.live.sdk.ui.fragment.ViewDocumentFragment;
import com.mengzhu.live.sdk.ui.widgets.MzStateView;
import com.mengzhu.sdk.R;
import com.mzmedia.adapter.QAExpandableAdapter;
import com.mzmedia.pullrefresh.PullToRefreshBase;
import com.mzmedia.pullrefresh.PullToRefreshExpandableListView;
import com.mzmedia.pullrefresh.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import tv.mengzhu.core.wrap.netwock.Page;

/**
 * 问答fragment
 */
public class BottomQAFragment extends BaseFragement implements View.OnClickListener {

    private static final String PLAYINFO = "playinfo";

    private LinearLayout ll_qa_news;
    private TextView tv_qa_news;
    private TextView tv_qa_all;
    private PullToRefreshExpandableListView ex_qa_list;
    private LinearLayout ll_qa_list_layout;
    private MzStateView mzStateView;
    private ImageView iv_anonymous;
    private EditText etv_ask;
    private TextView tv_ask_submit;

    PlayInfoDto mPlayInfoDto;
    private String ticket_id;
    private List<QAModelDto> groupList = new ArrayList<>();

    private List<QAModelDto> resultList = new ArrayList<>();
    private QAExpandableAdapter qaAdapter;

    MZApiRequest submitApiRequest;
    MZApiRequest qaListApiRequest;

    private int offset = 0;
    private int is_new_reply = 0;
    private int pageSize = 20;
    private int is_anonymous = 0; //0不匿名 1匿名

    public static BottomQAFragment newInstance(PlayInfoDto playInfoDto) {
        BottomQAFragment bottomFragment = new BottomQAFragment();
        Bundle args = new Bundle();
        args.putSerializable(PLAYINFO, playInfoDto);
        bottomFragment.setArguments(args);
        return bottomFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (null == args || !args.containsKey(PLAYINFO)) {
            return;
        }
        mPlayInfoDto = (PlayInfoDto) args.getSerializable(PLAYINFO);
        ticket_id = mPlayInfoDto.getTicket_id();
    }

    @Override
    public void initView() {
        ll_qa_news = mView.findViewById(R.id.tv_qa_list_news_layout);
        tv_qa_news = mView.findViewById(R.id.tv_qa_list_news);
        tv_qa_all = mView.findViewById(R.id.tv_qa_list_all);
        ex_qa_list = mView.findViewById(R.id.ex_qa_list);
        mzStateView = mView.findViewById(R.id.fragment_qa_state);
        iv_anonymous = mView.findViewById(R.id.iv_anonymous_icon);
        etv_ask = mView.findViewById(R.id.etv_ask);
        tv_ask_submit = mView.findViewById(R.id.tv_ask_submit);
        ll_qa_list_layout = mView.findViewById(R.id.fragment_qa_list_layout);
        ex_qa_list.setMode(PullToRefreshBase.Mode.BOTH);
        mzStateView.setContentView(ll_qa_list_layout);
    }

    @Override
    public void initData() {
        submitApiRequest = new MZApiRequest();
        submitApiRequest.createRequest(getContext(), MZApiRequest.API_TYPE_SUBMIT_ASK);
        submitApiRequest.setResultListener(submitDataListener);
        qaListApiRequest = new MZApiRequest();
        qaListApiRequest.createRequest(getContext(), MZApiRequest.API_TYPE_QA_LIST);
        qaListApiRequest.setResultListener(qaListDataListener);

        qaAdapter = new QAExpandableAdapter(getContext(), groupList);
        ex_qa_list.getRefreshableView().setAdapter(qaAdapter);
    }

    @Override
    public void setListener() {
        ll_qa_news.setOnClickListener(this);
        tv_qa_all.setOnClickListener(this);
        iv_anonymous.setOnClickListener(this);
        tv_ask_submit.setOnClickListener(this);

        ex_qa_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ExpandableListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                offset = 0;
                requestQAList(is_new_reply, offset);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                if (resultList.size() < pageSize) {
                    ex_qa_list.getLoadingLayoutProxy(false, true).setNoMoreLabel(true);
                    ex_qa_list.onRefreshComplete();
                } else {
                    offset++;
                    requestQAList(is_new_reply, offset);
                }
            }
        });
    }

    @Override
    public void loadData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bottom_qa;
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.tv_qa_list_news_layout) {
            //请求我的问答回复
            loadMyQAList();
        } else if (viewId == R.id.tv_qa_list_all) {
            //请求所有文档列表
            loadAllQAList();
        } else if (viewId == R.id.iv_anonymous_icon) {
            if (is_anonymous == 0) {
                is_anonymous = 1;
                iv_anonymous.setImageResource(R.mipmap.icon_anonymous_select);
            } else {
                is_anonymous = 0;
                iv_anonymous.setImageResource(R.mipmap.icon_anonymous);
            }
        } else if (viewId == R.id.tv_ask_submit) {
            submitAsk();
        }
    }

    public void requestQAList(int is_new_reply, int offset) {
        qaListApiRequest.startData(MZApiRequest.API_TYPE_QA_LIST, ticket_id, is_new_reply, offset, pageSize);
    }

    public void loadAllQAList() {
        tv_qa_all.setVisibility(View.GONE);
        mzStateView.show(MzStateView.NetState.LOADING);
        offset = 0;
        is_new_reply = 0;
        requestQAList(is_new_reply, offset);
    }

    public void loadMyQAList() {
        mzStateView.show(MzStateView.NetState.LOADING);
        ll_qa_news.setVisibility(View.GONE);
        tv_qa_all.setVisibility(View.VISIBLE);
        is_new_reply = 1;
        offset = 0;
        requestQAList(is_new_reply, offset);
    }

    public void submitAsk() {
        String content = etv_ask.getText().toString();
        if (!TextUtils.isEmpty(content)) {
            submitApiRequest.startData(MZApiRequest.API_TYPE_SUBMIT_ASK, ticket_id, content, is_anonymous + "");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            loadAllQAList();
        }
    }

    public void setNewsInfo(int unread) {
        if (tv_qa_all.getVisibility() == View.GONE) {
            ll_qa_news.setVisibility(View.VISIBLE);
            tv_qa_news.setText("您有新的消息回复" + unread);
        }
    }

    MZApiDataListener submitDataListener = new MZApiDataListener() {
        @Override
        public void dataResult(String apiType, Object dto, Page page, int status) {
            if (dto instanceof SubmitQADto) {
                SubmitQADto qaDto = (SubmitQADto) dto;
                if (qaDto.getInfo() != null) {
                    etv_ask.setText("");
                    loadAllQAList();
                }
            }
        }

        @Override
        public void errorResult(String apiType, int code, String msg) {
            ToastUtils.popUpToast("提交失败");
        }
    };

    MZApiDataListener qaListDataListener = new MZApiDataListener() {
        @Override
        public void dataResult(String apiType, Object dto, Page page, int status) {
            if (dto instanceof QAListDto) {
                QAListDto qaListDto = (QAListDto) dto;
                if (qaListDto.getUnread() > 0) {
                    tv_qa_news.setText("您有新的消息回复" + qaListDto.getUnread());
                    ll_qa_news.setVisibility(View.VISIBLE);
                } else {
                    ll_qa_news.setVisibility(View.GONE);
                }
                resultList = qaListDto.getList();
                if (offset == 0) {
                    groupList.clear();
                }
                if (qaListDto.getList() == null || qaListDto.getList().isEmpty()) {
                    if (ex_qa_list.isRefreshing()) {
                        ex_qa_list.onRefreshComplete();
                    }
                    mzStateView.show(MzStateView.NetState.EMPTY);
                    return;
                }
                groupList.addAll(resultList);
                qaAdapter.setData(groupList);
                expand();
                mzStateView.show(MzStateView.NetState.CONTENT);
                if (ex_qa_list.isRefreshing()) {
                    ex_qa_list.onRefreshComplete();
                }
            }
        }

        @Override
        public void errorResult(String apiType, int code, String msg) {
            if (ex_qa_list.isRefreshing()) {
                ex_qa_list.onRefreshComplete();
            }
            mzStateView.show(MzStateView.NetState.LOADERROR);
        }
    };

    public void expand() {
        for (int i = 0; i < qaAdapter.getGroupCount(); i++) {
            ex_qa_list.getRefreshableView().expandGroup(i);
        }
    }
}
