package com.mzmedia.fragment.redpacket;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mengzhu.live.sdk.business.dto.redpacket.MZCreateRedPacketDto;
import com.mengzhu.live.sdk.ui.api.MZApiDataListener;
import com.mengzhu.live.sdk.ui.api.MZApiRequest;
import com.mengzhu.sdk.R;
import com.mzmedia.fragment.BaseFragement;

import tv.mengzhu.core.wrap.netwock.Page;
import tv.mengzhu.core.wrap.user.presenter.MyUserInfoPresenter;

/**
 * 发送随机红包
 */
public class MZCreateRedPacketFragment extends BaseFragement {

    private Context mContext;

    private EditText edit_amount;
    private EditText edit_num;
    private EditText edit_title;
    private TextView tv_total;
    private TextView tv_send;

    private String channel_id;
    private String ticket_id;

    private MZApiRequest createRequest;

    public static MZCreateRedPacketFragment newInstance(String channel_id , String ticket_id){
        MZCreateRedPacketFragment redPacketFragment = new MZCreateRedPacketFragment();
        Bundle bundle = new Bundle();
        bundle.putString("channel_id", channel_id);
        bundle.putString("ticket_id", ticket_id);
        redPacketFragment.setArguments(bundle);
        return redPacketFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void initView() {
        edit_amount = (EditText) findViewById(R.id.etv_amount);
        edit_num = (EditText) findViewById(R.id.etv_num);
        edit_title = (EditText) findViewById(R.id.etv_title);
        tv_total = (TextView) findViewById(R.id.tv_red_packet_total);
        tv_send = (TextView) findViewById(R.id.btn_send_red_packet);
    }

    @Override
    public void initData() {
        this.channel_id = getArguments().getString("channel_id");
        this.ticket_id = getArguments().getString("ticket_id");

        createRequest = new MZApiRequest();
        createRequest.createRequest(mContext , MZApiRequest.API_CREATE_RED_PACKET);
    }

    @Override
    public void setListener() {
        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRedPacket();
            }
        });

        edit_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String amount = edit_amount.getText().toString();
                if (TextUtils.isEmpty(amount)){
                    tv_total.setText("0.00");
                }else {
                    tv_total.setText(amount + "");
                }
            }
        });

        createRequest.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String apiType, Object dto, Page page, int status) {
                MZCreateRedPacketDto createRedPacketDto = (MZCreateRedPacketDto) dto;
                if (createRedPacketDto != null && !TextUtils.isEmpty(createRedPacketDto.getBonus_id())){
                    getActivity().finish();
                }
            }

            @Override
            public void errorResult(String apiType, int code, String msg) {
                Toast.makeText(mContext, "" + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void loadData() {

    }

    public void sendRedPacket(){
        String amount = edit_amount.getText().toString();
        String num = edit_num.getText().toString();

        if (TextUtils.isEmpty(amount)){
            Toast.makeText(mContext, "金额不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(num)){
            Toast.makeText(mContext, "个数不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String title = edit_title.getText().toString();
        if (TextUtils.isEmpty(title)){
            title = "恭喜发财，大吉大利！";
        }
        String unique_id = MyUserInfoPresenter.getInstance().getUserInfo().getUniqueID();
        // 参数顺序
//        发放方式; 1: 随机 2: 固定
//        支付方式 |
//        发放金额 |
//        发放数量 |
//        活动id |
//        频道id |
//        用户唯一id |
//        红包留言 |

        //此处可以调用自有支付，支付完成后请求接口
        createRequest.startData(MZApiRequest.API_CREATE_RED_PACKET , "1" , "weixin" , amount , num , ticket_id , channel_id , unique_id , title);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_create_red_packet;
    }
}
