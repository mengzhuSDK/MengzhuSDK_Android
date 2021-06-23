package com.mengzhu.sdk.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mengzhu.live.sdk.business.dto.Config.MZAddWhiteUserDto;
import com.mengzhu.live.sdk.business.dto.Config.MZWhiteUserDto;
import com.mengzhu.live.sdk.ui.api.MZApiDataListener;
import com.mengzhu.live.sdk.ui.api.MZApiRequest;
import com.mengzhu.sdk.demo.adapter.EditWhiteUserAdapter;
import com.mengzhu.sdk.demo.widget.AddWhiteUserPop;
import com.mzmedia.pullrefresh.PullToRefreshBase;
import com.mzmedia.pullrefresh.PullToRefreshRecyclerView;
import com.mzmedia.utils.MUIImmerseUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tv.mengzhu.core.wrap.netwock.Page;

/**
 * 编辑白名单用户
 */
public class EditWhiteUserActivity extends AppCompatActivity {

    private ImageView iv_back;
    private TextView tv_clear;
    private TextView tv_complete;

    private PullToRefreshRecyclerView prl_user;

    private EditWhiteUserAdapter editWhiteUserAdapter;
    private List<MZWhiteUserDto.WhiteUserItem> dataList = new ArrayList<>();

    private List<MZWhiteUserDto.WhiteUserItem> delList = new ArrayList<>();

    private boolean isShowNoMoreLabel = false;
    private int page_size = 100;

    private String white_id;

    MZApiRequest mzApiRequest;
    MZApiRequest mzADDRequest;
    MZApiRequest mzDelRequest;
    MZApiRequest mzClearRequest;

    AddWhiteUserPop pop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MUIImmerseUtils.setStatusTextColor(false, this);
        setContentView(R.layout.activity_edit_white_user);
        white_id = getIntent().getExtras().getString("white_id");
        initView();
        initListener();
        loadData();
    }

    public void initView() {
        iv_back = findViewById(R.id.iv_back);
        tv_clear = findViewById(R.id.tv_clear);
        tv_complete = findViewById(R.id.btn_complete);

        prl_user = findViewById(R.id.prl_user);

        prl_user.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        editWhiteUserAdapter = new EditWhiteUserAdapter(this, dataList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setAutoMeasureEnabled(true);
        prl_user.getRefreshableView().setLayoutManager(gridLayoutManager);
        prl_user.getRefreshableView().setAdapter(editWhiteUserAdapter);

        pop = new AddWhiteUserPop(EditWhiteUserActivity.this);
        pop.setAddClickListener(new AddWhiteUserPop.AddClickListener() {
            @Override
            public void addClick(String phone_string) {
                addItemData(phone_string);
            }
        });
    }

    public void initListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
            }
        });


        prl_user.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                if (isShowNoMoreLabel) {
                    prl_user.getLoadingLayoutProxy(false, true).setNoMoreLabel(true);
                    prl_user.onRefreshComplete();
                }else {
                    // 白名单id，分页条数，是否下一页
                    mzApiRequest.startData(MZApiRequest.API_GET_WHITE_USER, white_id, page_size, true);
                }
            }
        });

        editWhiteUserAdapter.setOnItemClickListener(new EditWhiteUserAdapter.OnItemClickListener() {
            @Override
            public void itemClick(MZWhiteUserDto.WhiteUserItem whiteUserItem) {

            }

            @Override
            public void delClick(MZWhiteUserDto.WhiteUserItem whiteUserItem) {
                delItemData(whiteUserItem);
            }

            @Override
            public void addClick() {
                pop.setFocusable(true);
                pop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
            }
        });
    }

    public void loadData() {
        mzApiRequest = new MZApiRequest();
        mzApiRequest.createRequest(this, MZApiRequest.API_GET_WHITE_USER);

        mzADDRequest = new MZApiRequest();
        mzADDRequest.createRequest(this, MZApiRequest.API_ADD_WHITE_USER);

        mzDelRequest = new MZApiRequest();
        mzDelRequest.createRequest(this, MZApiRequest.API_DEL_WHITE_USER);

        mzClearRequest = new MZApiRequest();
        mzClearRequest.createRequest(this, MZApiRequest.API_CLEAR_WHITE_USER);

        mzApiRequest.setResultListener(new DataResultListener());
        // 白名单id，分页条数，是否下一页
        mzApiRequest.startData(MZApiRequest.API_GET_WHITE_USER, white_id, page_size, false);
    }

    /**
     * 校验输入的手机号，校验方式可自行更改
     *
     * @param phone_string
     */
    public void addItemData(String phone_string) {
        String[] phones = phone_string.split(",");
        boolean isCanCommit = true;
        final List<MZWhiteUserDto.WhiteUserItem> addItemList = new ArrayList<>();
        for (int i = 0; i < phones.length; i++) {
            if (!isNumeric(phones[i]) || phones[i].length() != 11) {
                Toast.makeText(this, phones[i] + "格式不正确", Toast.LENGTH_LONG).show();
                isCanCommit = false;
                break;
            } else {
                MZWhiteUserDto.WhiteUserItem item = new MZWhiteUserDto.WhiteUserItem();
                item.setPhone(phones[i]);
                addItemList.add(item);
            }
        }
        mzADDRequest.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String apiType, Object dto, Page page, int status) {
                MZAddWhiteUserDto mzAddWhiteUserDto = (MZAddWhiteUserDto) dto;
                Toast.makeText(EditWhiteUserActivity.this, "成功添加" + mzAddWhiteUserDto.getSucc_create_num() + "条", Toast.LENGTH_SHORT).show();
                pop.dismiss();
                dataList.addAll(addItemList);
                editWhiteUserAdapter.notifyDataSetChanged();
            }

            @Override
            public void errorResult(String apiType, int code, String msg) {
                Toast.makeText(EditWhiteUserActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
            }
        });
        //白名单id，添加手机号字符串
        if (isCanCommit)
            mzADDRequest.startData(MZApiRequest.API_ADD_WHITE_USER, white_id, phone_string);
    }

    public void delItemData(final MZWhiteUserDto.WhiteUserItem whiteUserItem) {
        mzDelRequest.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String apiType, Object dto, Page page, int status) {
                dataList.remove(whiteUserItem);
                editWhiteUserAdapter.notifyDataSetChanged();
            }

            @Override
            public void errorResult(String apiType, int code, String msg) {

            }
        });
        //白名单id，删除手机号字符串
        mzDelRequest.startData(MZApiRequest.API_DEL_WHITE_USER, white_id, whiteUserItem.getPhone());
    }

    public void clearData() {
        mzClearRequest.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String apiType, Object dto, Page page, int status) {
                dataList.clear();
                editWhiteUserAdapter.notifyDataSetChanged();
            }

            @Override
            public void errorResult(String apiType, int code, String msg) {

            }
        });
        //白名单id
        mzClearRequest.startData(MZApiRequest.API_DEL_WHITE_USER, white_id);
    }

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    class DataResultListener implements MZApiDataListener {

        @Override
        public void dataResult(String apiType, Object dto, Page page, int status) {
            MZWhiteUserDto mzWhiteUserDto = (MZWhiteUserDto) dto;
            List<MZWhiteUserDto.WhiteUserItem> userItemList = mzWhiteUserDto.getList();
            if (userItemList.size() < page_size){
                isShowNoMoreLabel = true;
            }
            dataList.addAll(userItemList);
            editWhiteUserAdapter.notifyDataSetChanged();
            prl_user.onRefreshComplete();
        }

        @Override
        public void errorResult(String apiType, int code, String msg) {

        }
    }
}