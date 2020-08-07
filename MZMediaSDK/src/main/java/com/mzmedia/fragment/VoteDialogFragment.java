package com.mzmedia.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mengzhu.live.sdk.business.dto.play.PlayInfoDto;
import com.mengzhu.live.sdk.business.dto.vote.VoteInfoDto;
import com.mengzhu.live.sdk.business.dto.vote.VoteOptionDto;
import com.mengzhu.live.sdk.core.utils.DateUtils;
import com.mengzhu.live.sdk.core.utils.ToastUtils;
import com.mengzhu.live.sdk.ui.api.MZApiDataListener;
import com.mengzhu.live.sdk.ui.api.MZApiRequest;
import com.mengzhu.sdk.R;
import com.mzmedia.adapter.base.BaseRecycleHeaderAdapter;
import com.mzmedia.adapter.vote.MultipleVoteAdapter;
import com.mzmedia.adapter.vote.VoteTextOptionAdapter;
import com.mzmedia.pullrefresh.PullToRefreshRecyclerView;

import java.util.List;

import tv.mengzhu.core.wrap.netwock.Page;

/**
 * 投屏对话框
 */
public class VoteDialogFragment extends DialogFragment implements View.OnClickListener , VoteTextOptionAdapter.OnVoteItemCheckChangeListener {
    public static final String PLAYINFO = "play_info";
    private Activity mActivity;

    private PlayInfoDto mPlayInfoDto;
    private VoteInfoDto mVoteDto;

    private MZApiRequest mInfoRequest;
    private MZApiRequest mOptionRequest;
    private MZApiRequest mVoteRequest;

    private View root;
    private Dialog mDialog = null;
    private ImageView mCloseIv;
    private TextView mTvContent;
    private TextView mTvDesc;
    private TextView mTvDate;
    private LinearLayout mLayoutContent;

    private BaseRecycleHeaderAdapter mAdapter;
    private PullToRefreshRecyclerView mPullRecyclerView;
    private RecyclerView mRecyclerView;
    private Button mBtnVote;
    private List<VoteOptionDto> mList;
    private View mHeaderView;

    public static VoteDialogFragment newInstance(PlayInfoDto mPlayInfo) {
        VoteDialogFragment bottomFragment = new VoteDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(PLAYINFO, mPlayInfo);
        bottomFragment.setArguments(args);
        return bottomFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.PushDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.dialog_fragment_vote, container);
        initDialog();
        initView();
        initData();
        initListener();
        return root;
    }

    private void initDialog() {
        mDialog = getDialog();
        mDialog.getWindow().getAttributes().windowAnimations = R.style.dialogNoAnim;
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.height = WindowManager.LayoutParams.MATCH_PARENT; // 高度持平
        window.setAttributes(lp);
        mDialog.setCanceledOnTouchOutside(true); // 外部点击取消
        window.requestFeature(Window.FEATURE_NO_TITLE);
        Bundle args = getArguments();
        if (null != args && args.containsKey(PLAYINFO)) {
            mPlayInfoDto = (PlayInfoDto) args.getSerializable(PLAYINFO);
        }
        Window windows = mActivity.getWindow();
        WindowManager.LayoutParams windowParams = windows.getAttributes();
        windowParams.alpha = 0.5f;
        windows.setAttributes(windowParams);
    }

    public void initView() {
        mCloseIv = (ImageView) root.findViewById(R.id.iv_dialog_vote_close);
        mLayoutContent = (LinearLayout) root.findViewById(R.id.layout_vote_content);
        mPullRecyclerView = (PullToRefreshRecyclerView) root.findViewById(R.id.rv_dialog_vote_option);
        mBtnVote = (Button) root.findViewById(R.id.btn_dialog_vote);
        mRecyclerView = mPullRecyclerView.getRefreshableView();
        mHeaderView = LayoutInflater.from(mActivity).inflate(R.layout.header_vote_option_list, mPullRecyclerView, false);
        mTvContent = mHeaderView.findViewById(R.id.tv_vote_content);
        mTvDesc = mHeaderView.findViewById(R.id.tv_vote_desc);
        mTvDate = mHeaderView.findViewById(R.id.tv_vote_date);
    }

    public void initListener() {
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mCloseIv.setOnClickListener(this);
        mBtnVote.setOnClickListener(this);

        mInfoRequest.setResultListener(apiDataListener);
        mOptionRequest.setResultListener(apiDataListener);
        mVoteRequest.setResultListener(apiDataListener);
    }

    public void initData() {
        mInfoRequest = new MZApiRequest();
        mOptionRequest = new MZApiRequest();
        mVoteRequest = new MZApiRequest();

        mInfoRequest.createRequest(mActivity, MZApiRequest.API_TYPE_VOTE_INFO);
        mOptionRequest.createRequest(mActivity, MZApiRequest.API_TYPE_VOTE_OPTION_LIST);
        mVoteRequest.createRequest(mActivity, MZApiRequest.API_TYPE_VOTE_COMMIT);
    }

    @Override
    public void onResume() {
        super.onResume();
        mInfoRequest.startData(MZApiRequest.API_TYPE_VOTE_INFO, mPlayInfoDto.getTicket_id());
    }

    private void setViewData() {

        mTvContent.setText(mVoteDto.getQuestion());
        mTvDate.setText("结束时间：" + DateUtils.stringToDateYY(mVoteDto.getEnd_time() + "000"));
        if (mVoteDto.getSelect_type() == VoteInfoDto.SINGLE_TYPE) {
            mTvDesc.setText("单选");
        } else {
            mTvDesc.setText(String.format("多选(最多%d项)", mVoteDto.getMax_select()));
        }
        if (mVoteDto.getIs_expired() == 1) {
            mBtnVote.setEnabled(false);
            mBtnVote.setVisibility(View.GONE);
        } else if (mVoteDto.getIs_vote() == 1) {
            mBtnVote.setEnabled(false);
            mBtnVote.setVisibility(View.GONE);
        }
        mRecyclerView.setHasFixedSize(false);
        if (mVoteDto.getType() == 0) {//文字
            mAdapter = new VoteTextOptionAdapter(mActivity, mVoteDto);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
            ((VoteTextOptionAdapter)mAdapter).setOnVoteItemCheckChangeListener(this);
        } else {
            mAdapter = new MultipleVoteAdapter(mActivity, mVoteDto);
            mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2));
            ((MultipleVoteAdapter)mAdapter).setOnVoteItemCheckChangeListener(this);
        }
        mAdapter.setHeaderView(mHeaderView);
        mRecyclerView.setAdapter(mAdapter);

        mOptionRequest.startData(MZApiRequest.API_TYPE_VOTE_OPTION_LIST, mVoteDto.getId());
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Window windows = mActivity.getWindow();
        WindowManager.LayoutParams windowParams = windows.getAttributes();
        windowParams.alpha = 1f;//1.０全透明．０不透明．
        windows.setAttributes(windowParams);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        Window windows = mActivity.getWindow();
        WindowManager.LayoutParams windowParams = windows.getAttributes();
        windowParams.alpha = 1f;//1.０全透明．０不透明．
        windows.setAttributes(windowParams);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_dialog_vote_close) {
            dismiss();
        } else if (id == R.id.btn_dialog_vote) {
            vote();
        }
    }

    private void vote() {
        List<VoteOptionDto> select = mAdapter.getSelectedList();
        if (select == null || select.size() == 0) {
            ToastUtils.popUpToast("请先选择内容再进行投票");
            return;
        }
        String optionIds = listToString(select);
        mVoteRequest.startData(MZApiRequest.API_TYPE_VOTE_COMMIT , mVoteDto.getId(), optionIds, mPlayInfoDto.getTicket_id());
    }

    public String listToString(List<VoteOptionDto> stringList) {
        if (stringList == null || stringList.size() == 0) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (VoteOptionDto string : stringList) {
            if (flag) {
                result.append(",");
            } else {
                flag = true;
            }
            result.append(string.getId());
        }
        return result.toString();
    }

    MZApiDataListener apiDataListener = new MZApiDataListener() {
        @Override
        public void dataResult(String apiType, Object dto, Page page, int status) {
            if (MZApiRequest.API_TYPE_VOTE_INFO.equals(apiType)) {

                VoteInfoDto voteInfoDto = (VoteInfoDto) dto;
                mVoteDto = voteInfoDto;
                setViewData();
            }
            if (MZApiRequest.API_TYPE_VOTE_OPTION_LIST.equals(apiType)) {
                mList = (List<VoteOptionDto>) dto;
                mAdapter.setData(mList);
                mAdapter.notifyDataSetChanged();
            }
            if (MZApiRequest.API_TYPE_VOTE_COMMIT.equals(apiType)){
                if (status == 200) {
                    ToastUtils.popUpToast("投票成功");
                    mInfoRequest.startData(MZApiRequest.API_TYPE_VOTE_INFO, mPlayInfoDto.getTicket_id());
                }
            }
        }

        @Override
        public void errorResult(String apiType, int code, String msg) {

        }
    };

    @Override
    public void checkChange() {
        if (mAdapter != null && !mAdapter.getSelectedList().isEmpty()){
            mBtnVote.setBackgroundResource(R.drawable.bg_vote_commit);
            mBtnVote.setClickable(true);
        }else {
            mBtnVote.setBackgroundResource(R.drawable.bg_vote_commit_normal);
            mBtnVote.setClickable(false);
        }
    }
}
