package com.mzmedia.fragment.redpacket;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mengzhu.live.sdk.business.dto.chat.ChatTextDto;
import com.mengzhu.live.sdk.business.dto.chat.impl.ChatChannelDto;
import com.mengzhu.live.sdk.business.dto.chat.impl.ChatCompleteDto;
import com.mengzhu.live.sdk.business.dto.redpacket.MZRedPacketStateDto;
import com.mengzhu.live.sdk.ui.api.MZApiDataListener;
import com.mengzhu.live.sdk.ui.api.MZApiRequest;
import com.mengzhu.sdk.R;
import com.mzmedia.adapter.chat.PlayChatRedPacketWrap;
import com.mzmedia.widgets.CircleImageView;
import com.mzmedia.widgets.dialog.BaseDialogFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import tv.mengzhu.core.wrap.netwock.Page;
import tv.mengzhu.core.wrap.user.presenter.MyUserInfoPresenter;

/**
 * 抢红包弹窗
 */
public class MZRedPacketDialogFragment extends BaseDialogFragment {

    private ImageView iv_close;
    private CircleImageView civ_user_avatar;
    private TextView tv_user_name;
    private TextView tv_title;
    //红包已抢完
    private TextView tv_finish;
    //红包已过期
    private TextView tv_overdue;
    //红包金额
    private TextView tv_amount;
    //红包记录
    private TextView tv_history;
    //打开按钮
    private ImageView iv_open;

    private ChatTextDto chatTextDto;
    private ChatCompleteDto chatCompleteDto;

    MZApiRequest stateRequest;
    MZApiRequest obtainRequest;

    public interface OnHistoryClickListener{
        void onHistoryClick(ChatTextDto dto);
    }

    private OnHistoryClickListener onHistoryClickListener;

    public void setOnHistoryClickListener(OnHistoryClickListener onHistoryClickListener) {
        this.onHistoryClickListener = onHistoryClickListener;
    }

    public static MZRedPacketDialogFragment newInstance(ChatTextDto dto) {
        MZRedPacketDialogFragment dialogFragment = new MZRedPacketDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("ChatTextDto", dto);
        dialogFragment.setArguments(bundle);
        dialogFragment.setGravity(Gravity.CENTER);
        return dialogFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_red_packet_info;
    }

    @Override
    protected void initViews(View v) {
        chatTextDto = (ChatTextDto) getArguments().getSerializable("ChatTextDto");
        if (chatTextDto != null) {
            chatCompleteDto = (ChatCompleteDto) chatTextDto.getBaseDto();
        }
        iv_close = v.findViewById(R.id.iv_close);
        civ_user_avatar = v.findViewById(R.id.civ_user_avatar);
        tv_user_name = v.findViewById(R.id.tv_user_name);
        tv_title = v.findViewById(R.id.tv_title);
        tv_finish = v.findViewById(R.id.tv_finish_text);
        tv_overdue = v.findViewById(R.id.tv_overdue_text);
        tv_amount = v.findViewById(R.id.tv_amount_text);
        tv_history = v.findViewById(R.id.tv_history_text);
        iv_open = v.findViewById(R.id.iv_open_red_packet);
        tv_history.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        stateRequest = new MZApiRequest();
        stateRequest.createRequest(getActivity(), MZApiRequest.API_CHECK_RED_PACKET_STATE);

        obtainRequest = new MZApiRequest();
        obtainRequest.createRequest(getActivity(), MZApiRequest.API_OBTAIN_RED_PACKET);
        if (chatTextDto == null || chatCompleteDto == null) {
            dismiss();
            return;
        }

        tv_user_name.setText(chatTextDto.getUser_name() + "");
        ImageLoader.getInstance().displayImage(chatTextDto.getAvatar(), civ_user_avatar, new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.icon_default_avatar)
                .showImageOnFail(R.mipmap.icon_default_avatar)
                .cacheInMemory(true)
                .build());
        tv_title.setText(chatCompleteDto.getSlogan() + "");

        initListener();
        loadData();
    }

    public void initListener() {
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        iv_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String unique_id = MyUserInfoPresenter.getInstance().getUserInfo().getUniqueID();
                //参数顺序 红包id unique_id
                obtainRequest.startData(MZApiRequest.API_OBTAIN_RED_PACKET, chatCompleteDto.getBonus_id(), unique_id);
            }
        });

        tv_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onHistoryClickListener != null){
                    onHistoryClickListener.onHistoryClick(chatTextDto);
                }
            }
        });

        stateRequest.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String apiType, Object dto, Page page, int status) {
                initData((MZRedPacketStateDto) dto);
            }

            @Override
            public void errorResult(String apiType, int code, String msg) {

            }
        });

        obtainRequest.setResultListener(new MZApiDataListener() {
            @Override
            public void dataResult(String apiType, Object dto, Page page, int status) {
                loadData();
            }

            @Override
            public void errorResult(String apiType, int code, String msg) {

            }
        });
    }

    public void loadData() {
        String unique_id = MyUserInfoPresenter.getInstance().getUserInfo().getUniqueID();
        //参数顺序 红包id unique_id
        stateRequest.startData(MZApiRequest.API_CHECK_RED_PACKET_STATE, chatCompleteDto.getBonus_id(), unique_id);
    }

    public void initData(MZRedPacketStateDto stateDto) {
        int status = stateDto.getStatus();
        switch (status) {
            case 10: //可以领取
                iv_open.setVisibility(View.VISIBLE);
                tv_finish.setVisibility(View.GONE);
                tv_overdue.setVisibility(View.GONE);
                tv_history.setVisibility(View.GONE);
                tv_amount.setVisibility(View.GONE);
                break;
            case 4: //已抢光
                iv_open.setVisibility(View.GONE);
                tv_finish.setVisibility(View.VISIBLE);
                tv_overdue.setVisibility(View.GONE);
                tv_history.setVisibility(View.GONE);
                tv_amount.setVisibility(View.GONE);
                break;
            case 1: //已领取
                iv_open.setVisibility(View.GONE);
                tv_finish.setVisibility(View.GONE);
                tv_overdue.setVisibility(View.GONE);
                tv_history.setVisibility(View.VISIBLE);
                tv_amount.setVisibility(View.VISIBLE);
                tv_amount.setText(stateDto.getMsg() + "");
                break;
            case 9: //已过期
                iv_open.setVisibility(View.GONE);
                tv_finish.setVisibility(View.GONE);
                tv_overdue.setVisibility(View.VISIBLE);
                tv_history.setVisibility(View.GONE);
                tv_amount.setVisibility(View.GONE);
                break;
        }
    }
}
