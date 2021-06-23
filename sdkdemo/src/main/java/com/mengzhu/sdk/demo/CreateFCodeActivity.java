package com.mengzhu.sdk.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mengzhu.live.sdk.business.dto.Config.MZCreateFCodeDto;
import com.mengzhu.live.sdk.ui.api.MZApiDataListener;
import com.mengzhu.live.sdk.ui.api.MZApiRequest;
import com.mzmedia.utils.MUIImmerseUtils;

import tv.mengzhu.core.wrap.netwock.Page;

public class CreateFCodeActivity extends AppCompatActivity {

    private EditText edit_name;
    private EditText edit_desc;
    private EditText edit_num;
    private TextView btn_create;

    private ImageView iv_back;

    MZApiRequest mzApiRequest;
    MZApiRequest mzADDRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MUIImmerseUtils.setStatusTextColor(false, this);
        setContentView(R.layout.activity_create);
        initView();
        initListener();
    }

    public void initView() {
        edit_name = findViewById(R.id.edit_name);
        edit_desc = findViewById(R.id.edit_desc);
        edit_num = findViewById(R.id.edit_num);
        btn_create = findViewById(R.id.btn_create);

        iv_back = findViewById(R.id.iv_back);
    }

    public void initListener() {
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFCode();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mzApiRequest = new MZApiRequest();
        mzApiRequest.createRequest(this, MZApiRequest.API_F_CODE_CREATE);
        mzApiRequest.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String apiType, Object dto, Page page, int status) {
                if (status == 200){
                    MZCreateFCodeDto fCodeDto = (MZCreateFCodeDto) dto;
                    mzADDRequest.startData(MZApiRequest.API_F_CODE_ADD , fCodeDto.getFcode_id() , edit_num.getEditableText().toString());
                }
            }

            @Override
            public void errorResult(String apiType, int code, String msg) {
                Toast.makeText(CreateFCodeActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
            }
        });

        mzADDRequest = new MZApiRequest();
        mzADDRequest.createRequest(this, MZApiRequest.API_F_CODE_ADD);
        mzADDRequest.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String apiType, Object dto, Page page, int status) {
                if (status == 200){
                    finish();
                }
            }

            @Override
            public void errorResult(String apiType, int code, String msg) {
                Toast.makeText(CreateFCodeActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createFCode() {
        String name = edit_name.getEditableText().toString();
        String desc = edit_desc.getEditableText().toString();
        String num = edit_num.getEditableText().toString();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(desc) && !TextUtils.isEmpty(num)) {
            //参数顺序，名称，描述，数量
            mzApiRequest.startData(MZApiRequest.API_F_CODE_CREATE, name, desc, num);
        }
    }
}