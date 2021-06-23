package com.mzmedia.fragment.user;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mengzhu.live.sdk.business.dto.MZOnlineUserListDto;
import com.mengzhu.live.sdk.business.dto.MZSilenceOrKickUserDto;
import com.mengzhu.live.sdk.ui.api.MZApiDataListener;
import com.mengzhu.live.sdk.ui.api.MZApiRequest;
import com.mengzhu.sdk.R;
import com.mzmedia.adapter.base.MZDialogUserAdapter;
import com.mzmedia.fragment.BaseFragement;
import com.mzmedia.pullrefresh.PullToRefreshBase;
import com.mzmedia.pullrefresh.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import tv.mengzhu.core.wrap.netwock.Page;

/**
 * 推流端 在线用户列表
 */
public class MZPushOnlineUserFragment extends BaseFragement {

    private Context mContext;
    private String channel_id;
    private String ticket_id;
    private int type = 0; //0在线用户 1踢出列表 2禁言列表

    private PullToRefreshRecyclerView plv;

    private MZApiRequest onlineApiRequest;

    private List<MZOnlineUserListDto> dataList = new ArrayList<>();
    private MZDialogUserAdapter mzDialogUserAdapter;

    private boolean isRefresh = true;
    private boolean isShowNoMoreLabel = false;

    private MZApiRequest kickRequest;
    private MZApiRequest relieveKickRequest;
    private MZApiRequest silenceRequest;
    private int silence_type = 0;


    public static MZPushOnlineUserFragment newInstance(String channel_id, String ticket_id , int type) {
        MZPushOnlineUserFragment mzPushOnlineUserFragment = new MZPushOnlineUserFragment();
        Bundle bundle = new Bundle();
        bundle.putString("channel_id", channel_id);
        bundle.putString("ticket_id", ticket_id);
        bundle.putInt("type" , type);
        mzPushOnlineUserFragment.setArguments(bundle);
        return mzPushOnlineUserFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void initView() {
        this.channel_id = getArguments().getString("channel_id");
        this.ticket_id = getArguments().getString("ticket_id");
        this.type = getArguments().getInt("type");
        plv = (PullToRefreshRecyclerView) findViewById(R.id.plv_dialog_user);
    }

    @Override
    public void initData() {
        mzDialogUserAdapter = new MZDialogUserAdapter(mContext, dataList, type);
        plv.getRefreshableView().setLayoutManager(new LinearLayoutManager(mContext));
        plv.getRefreshableView().setAdapter(mzDialogUserAdapter);

        onlineApiRequest = new MZApiRequest();
        switch (type){
            case 0:
                onlineApiRequest.createRequest(mContext, MZApiRequest.API_TYPE_ONLINE_USER_LIST);
                break;
            case 1:
                onlineApiRequest.createRequest(mContext, MZApiRequest.API_GET_KICKED_USERS);
                break;
            case 2:
                onlineApiRequest.createRequest(mContext, MZApiRequest.API_GET_SILENCED_USERS);
                break;
        }

        kickRequest = new MZApiRequest();
        kickRequest.createRequest(mContext , MZApiRequest.API_KICK_USER);

        relieveKickRequest = new MZApiRequest();
        relieveKickRequest.createRequest(mContext , MZApiRequest.API_RESTORE_KICK_USER);

        silenceRequest = new MZApiRequest();
        silenceRequest.createRequest(mContext , MZApiRequest.API_TYPE_ROOM_FORBIDDEN);
    }

    @Override
    public void setListener() {
        onlineApiRequest.setResultListener(new MZApiResultListener());

        plv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                isRefresh = true;
                isShowNoMoreLabel = false;
                requestData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                if (isShowNoMoreLabel){
                    plv.getLoadingLayoutProxy(false, true).setNoMoreLabel(true);
                    plv.onRefreshComplete();
                }else {
                    isRefresh = false;
                    requestData();
                }
            }
        });

        mzDialogUserAdapter.setOnHandleUserListener(new MZDialogUserAdapter.OnHandleUserListener() {
            @Override
            public void kickUser(MZOnlineUserListDto dto) {
                //踢出用户 参数顺序，渠道id，用户id，房间id
                kickRequest.startData(MZApiRequest.API_KICK_USER , channel_id , dto.getUid() , ticket_id);
            }

            @Override
            public void silenceUser(MZOnlineUserListDto dto) {
                //禁言用户 参数顺序 用户id，房间id，操作类型 1:禁言 2:解除禁言
                silence_type = 1;
                silenceRequest.startData(MZApiRequest.API_TYPE_ROOM_FORBIDDEN, dto.getUid(), ticket_id, 1);
            }

            @Override
            public void relieveKick(MZOnlineUserListDto dto) {
                //解除踢出
                relieveKickRequest.startData(MZApiRequest.API_KICK_USER , channel_id , dto.getUid() , ticket_id);
            }

            @Override
            public void relieveSilence(MZOnlineUserListDto dto) {
                //解除禁言 参数顺序 用户id，房间id，操作类型 1:禁言 2:解除禁言
                silence_type = 2;
                silenceRequest.startData(MZApiRequest.API_TYPE_ROOM_FORBIDDEN, dto.getUid(), ticket_id, 2);
            }
        });

        kickRequest.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String apiType, Object dto, Page page, int status) {
                if (status == 200){
                    isRefresh = true;
                    isShowNoMoreLabel = false;
                    requestData();
                }
            }

            @Override
            public void errorResult(String apiType, int code, String msg) {

            }
        });

        relieveKickRequest.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String apiType, Object dto, Page page, int status) {
                if (status == 200){
                    isRefresh = true;
                    isShowNoMoreLabel = false;
                    requestData();
                }
            }

            @Override
            public void errorResult(String apiType, int code, String msg) {

            }
        });

        silenceRequest.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String apiType, Object dto, Page page, int status) {
                if (status == 200){
                    isRefresh = true;
                    isShowNoMoreLabel = false;
                    requestData();
                }
            }

            @Override
            public void errorResult(String apiType, int code, String msg) {

            }
        });
    }

    @Override
    public void loadData() {
        isRefresh = true;
        requestData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_dialog_user;
    }

    public void requestData(){
        switch (type){
            case 0:
                onlineApiRequest.startData(MZApiRequest.API_TYPE_ONLINE_USER_LIST, isRefresh, ticket_id);
                break;
            case 1:
                onlineApiRequest.startData(MZApiRequest.API_GET_KICKED_USERS, channel_id, ticket_id , 50 , isRefresh);
                break;
            case 2:
                onlineApiRequest.startData(MZApiRequest.API_GET_SILENCED_USERS, channel_id, ticket_id , 50 , isRefresh);
                break;
        }
    }

    class MZApiResultListener implements MZApiDataListener {

        @Override
        public void dataResult(String apiType, Object dto, Page page, int status) {
            List<MZOnlineUserListDto> list;
            switch (type){
                case 0:
                    list = (List<MZOnlineUserListDto>) dto;
                    break;
                case 1:
                case 2:
                    list = ((MZSilenceOrKickUserDto) dto).getList();
                    break;
                default:
                    list = new ArrayList<>();
            }
            if (isRefresh) {
                dataList.clear();
            }
            dataList.addAll(list);
            mzDialogUserAdapter.notifyDataSetChanged();
            if (list.size() < 50){
                isShowNoMoreLabel = true;
            }
            plv.onRefreshComplete();
        }

        @Override
        public void errorResult(String apiType, int code, String msg) {
            plv.onRefreshComplete();
        }
    }
}
