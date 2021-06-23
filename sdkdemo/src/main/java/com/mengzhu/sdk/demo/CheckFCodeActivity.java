package com.mengzhu.sdk.demo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mengzhu.live.sdk.business.dto.Config.MZUnUseFCodeDto;
import com.mengzhu.live.sdk.business.dto.Config.MZWhiteUserDto;
import com.mengzhu.live.sdk.ui.api.MZApiDataListener;
import com.mengzhu.live.sdk.ui.api.MZApiRequest;
import com.mengzhu.sdk.demo.adapter.CheckFCodeAdapter;
import com.mengzhu.sdk.demo.widget.AddFCodePop;
import com.mzmedia.pullrefresh.PullToRefreshBase;
import com.mzmedia.pullrefresh.PullToRefreshListView;
import com.mzmedia.utils.MUIImmerseUtils;

import java.util.ArrayList;
import java.util.List;

import tv.mengzhu.core.wrap.netwock.Page;

public class CheckFCodeActivity extends AppCompatActivity {

    private ImageView iv_back;

    private PullToRefreshListView prl_f_code;
    private ImageView iv_select_all;
    private TextView tv_select_all;
    private TextView btn_copy;
    private TextView tv_new_create;

    private CheckFCodeAdapter checkFCodeAdapter;
    private List<MZUnUseFCodeDto.UnUseListDto> dataList;

    private boolean isShowNoMoreLabel = false;

    private int page_size = 100;

    private String fcode_id;
    MZUnUseFCodeDto mzUnUseFCodeDto;

    MZApiRequest mzApiRequest;
    MZApiRequest mzADDRequest;

    boolean is_selectAll = true;
    boolean isNext = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MUIImmerseUtils.setStatusTextColor(false , this);
        setContentView(R.layout.activity_check_fcode);
        fcode_id = getIntent().getExtras().getString("fcode_id");
        initView();
        initListener();
        loadData();
    }

    public void initView(){
        iv_back = findViewById(R.id.iv_back);

        prl_f_code = findViewById(R.id.prl_f_code);
        iv_select_all = findViewById(R.id.iv_select_all);
        tv_select_all = findViewById(R.id.tv_select_all);
        btn_copy = findViewById(R.id.btn_copy);
        tv_new_create = findViewById(R.id.tv_new_create);

        prl_f_code.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        dataList = new ArrayList<>();
        checkFCodeAdapter = new CheckFCodeAdapter(this , dataList);
        prl_f_code.setAdapter(checkFCodeAdapter);
    }

    public void initListener(){
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        prl_f_code.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dataList.get(position).setCheck(!dataList.get(position).isCheck());
                checkFCodeAdapter.notifyDataSetChanged();
                is_selectAll = true;
                for (int i = 0; i < dataList.size(); i++) {
                    if (!dataList.get(i).isCheck()){
                        is_selectAll = false;
                        break;
                    }
                }
                if (is_selectAll){
                    iv_select_all.setImageResource(R.mipmap.icon_anonymous_select);
                }else {
                    iv_select_all.setImageResource(R.mipmap.icon_anonymous);
                }
            }
        });

        prl_f_code.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (isShowNoMoreLabel) {
                    prl_f_code.getLoadingLayoutProxy(false, true).setNoMoreLabel(true);
                    prl_f_code.onRefreshComplete();
                }else {
                    isNext = true;
                    // F码id，分页条数，是否下一页
                    mzApiRequest.startData(MZApiRequest.API_GET_UN_F_CODE_LIST, fcode_id, page_size, isNext);
                }
            }
        });

        tv_new_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mzUnUseFCodeDto == null){
                    return;
                }
                final AddFCodePop pop = new AddFCodePop(CheckFCodeActivity.this , mzUnUseFCodeDto.getCan_add_num());
                pop.setAddClickListener(new AddFCodePop.AddClickListener() {
                    @Override
                    public void addClick(String num) {
                        mzADDRequest.startData(MZApiRequest.API_F_CODE_ADD , fcode_id , num);
                        pop.dismiss();
                    }
                });
                pop.setFocusable(true);
                pop.showAtLocation(getWindow().getDecorView() , Gravity.CENTER , 0 , 0);
            }
        });

        tv_select_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < dataList.size(); i++) {
                    dataList.get(i).setCheck(!is_selectAll);
                }
                is_selectAll = !is_selectAll;
                if (is_selectAll){
                    iv_select_all.setImageResource(R.mipmap.icon_anonymous_select);
                }else {
                    iv_select_all.setImageResource(R.mipmap.icon_anonymous);
                }
                checkFCodeAdapter.notifyDataSetChanged();
            }
        });
        iv_select_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < dataList.size(); i++) {
                    dataList.get(i).setCheck(!is_selectAll);
                }
                is_selectAll = !is_selectAll;
                if (is_selectAll){
                    iv_select_all.setImageResource(R.mipmap.icon_anonymous_select);
                }else {
                    iv_select_all.setImageResource(R.mipmap.icon_anonymous);
                }
                checkFCodeAdapter.notifyDataSetChanged();
            }
        });

        btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < dataList.size(); i++) {
                    if (dataList.get(i).isCheck()){
                        stringBuffer.append( " " + dataList.get(i).getCode() + " ,");
                    }
                }
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", stringBuffer.toString());
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                Toast.makeText(CheckFCodeActivity.this, "已复制到粘贴板", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadData(){
        mzApiRequest = new MZApiRequest();
        mzApiRequest.createRequest(this, MZApiRequest.API_GET_UN_F_CODE_LIST);

        mzADDRequest = new MZApiRequest();
        mzADDRequest.createRequest(this, MZApiRequest.API_F_CODE_ADD);
        mzADDRequest.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String apiType, Object dto, Page page, int status) {
                if (status == 200){
                    //F码逐个添加可能会有延迟，延迟一秒请求
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isNext = false;
                            // F码id，分页条数，是否下一页
                            mzApiRequest.startData(MZApiRequest.API_GET_UN_F_CODE_LIST, fcode_id, page_size, false);
                        }
                    } , 1000);
                }
            }

            @Override
            public void errorResult(String apiType, int code, String msg) {

            }
        });

        mzApiRequest.setResultListener(new DataResultListener());
        isNext = false;
        // F码id，分页条数，是否下一页
        mzApiRequest.startData(MZApiRequest.API_GET_UN_F_CODE_LIST, fcode_id, page_size, false);
    }

    class DataResultListener implements MZApiDataListener {

        @Override
        public void dataResult(String apiType, Object dto, Page page, int status) {
            mzUnUseFCodeDto = (MZUnUseFCodeDto) dto;
            List<MZUnUseFCodeDto.UnUseListDto> userItemList = mzUnUseFCodeDto.getList();
            if (userItemList.size() < page_size){
                isShowNoMoreLabel = true;
            }else {
                isShowNoMoreLabel = false;
            }
            if (!isNext){
                dataList.clear();
            }
            dataList.addAll(userItemList);
            checkFCodeAdapter.notifyDataSetChanged();
            prl_f_code.onRefreshComplete();
        }

        @Override
        public void errorResult(String apiType, int code, String msg) {
            prl_f_code.onRefreshComplete();
        }
    }
}