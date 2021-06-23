package com.mengzhu.sdk.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mengzhu.live.sdk.business.dto.Config.MZWhiteCreateDto;
import com.mengzhu.live.sdk.ui.api.MZApiDataListener;
import com.mengzhu.live.sdk.ui.api.MZApiRequest;
import com.mzmedia.utils.MUIImmerseUtils;

import tv.mengzhu.core.wrap.netwock.Page;

public class CreateWhiteListActivity extends AppCompatActivity {

    private EditText edit_name;
    private EditText edit_desc;
    private TextView btn_create;

    private ImageView iv_back;

    MZApiRequest mzApiRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MUIImmerseUtils.setStatusTextColor(false , this);
        setContentView(R.layout.activity_create_white_list);
        initView();
        initListener();
    }

    public void initView(){
        edit_name = findViewById(R.id.edit_name);
        edit_desc = findViewById(R.id.edit_desc);
        btn_create = findViewById(R.id.btn_create);

        iv_back = findViewById(R.id.iv_back);
    }

    public void initListener(){
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
        mzApiRequest.createRequest(this , MZApiRequest.API_WHITE_CREATE);
        mzApiRequest.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String apiType, Object dto, Page page, int status) {
                MZWhiteCreateDto mzWhiteCreateDto = (MZWhiteCreateDto) dto;
                Intent intent = new Intent(CreateWhiteListActivity.this , CreateWhiteUserActivity.class);
                intent.putExtra("white_id" , mzWhiteCreateDto.getId());
                startActivity(intent);
                finish();
            }

            @Override
            public void errorResult(String apiType, int code, String msg) {
                Toast.makeText(CreateWhiteListActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createFCode(){
        String name = edit_name.getEditableText().toString();
        String desc = edit_desc.getEditableText().toString();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(desc)){
            //参数顺序 名称 描述
            mzApiRequest.startData(MZApiRequest.API_WHITE_CREATE , name , desc);
        }
    }
}