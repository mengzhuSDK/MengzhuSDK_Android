package com.mengzhu.sdk.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mengzhu.live.sdk.business.dto.Config.MZAddWhiteUserDto;
import com.mengzhu.live.sdk.ui.api.MZApiDataListener;
import com.mengzhu.live.sdk.ui.api.MZApiRequest;
import com.mzmedia.utils.MUIImmerseUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tv.mengzhu.core.wrap.netwock.Page;

public class CreateWhiteUserActivity extends AppCompatActivity {

    private EditText edit_phone;
    private TextView btn_create;

    private ImageView iv_back;

    private String white_id;

    MZApiRequest mzApiRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MUIImmerseUtils.setStatusTextColor(false , this);
        setContentView(R.layout.activity_create_white_user);
        white_id = getIntent().getExtras().getString("white_id");
        initView();
        initListener();

    }

    public void initView(){
        edit_phone = findViewById(R.id.edit_phone);
        btn_create = findViewById(R.id.btn_create);

        iv_back = findViewById(R.id.iv_back);
    }

    public void initListener(){
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mzApiRequest = new MZApiRequest();
        mzApiRequest.createRequest(this, MZApiRequest.API_ADD_WHITE_USER);
    }

    public void createUser(){
        String phone = edit_phone.getEditableText().toString();
        if (!TextUtils.isEmpty(phone)){
            addItemData(phone);
        }
    }

    /**
     * 校验输入的手机号，校验方式可自行更改
     *
     * @param phone_string
     */
    public void addItemData(String phone_string) {
        String[] phones = phone_string.split(",");
        boolean isCanCommit = true;
        for (int i = 0; i < phones.length; i++) {
            if (!isNumeric(phones[i]) || phones[i].length() != 11) {
                Toast.makeText(this, phones[i] + "格式不正确", Toast.LENGTH_LONG).show();
                isCanCommit = false;
                break;
            }
        }
        mzApiRequest.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String apiType, Object dto, Page page, int status) {
                MZAddWhiteUserDto mzAddWhiteUserDto = (MZAddWhiteUserDto) dto;
                Toast.makeText(CreateWhiteUserActivity.this, "成功添加" + mzAddWhiteUserDto.getSucc_create_num() + "条", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void errorResult(String apiType, int code, String msg) {
                Toast.makeText(CreateWhiteUserActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
            }
        });
        //白名单id，添加手机号字符串
        if (isCanCommit)
            mzApiRequest.startData(MZApiRequest.API_ADD_WHITE_USER, white_id, phone_string);
    }

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}