package com.mzmedia.widgets.popupwindow;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mengzhu.sdk.R;
import com.mzmedia.widgets.CircleImageView;
import com.mzmedia.widgets.dialog.AbstractPopupWindow;

public class ForbidUserPopupWindow extends AbstractPopupWindow {
    private CircleImageView cv_popup_forbid_user_avatar;
    private TextView tv_popup_forbid_user_name;
    private TextView tv_popup_forbid_user_confirm;
    private TextView tv_popup_forbid_user_cancel;
//    private OtherUserInfoPresenter mInfoPresenter;
//    private UserInfoDto mUserInfo;
    private String uid;
    private String channel_id;


    public ForbidUserPopupWindow(Context context) {
        super(context);
        View root = View.inflate(context, R.layout.popup_forbid_user_layout, null);
        setContentView(root);
        cv_popup_forbid_user_avatar = root.findViewById(R.id.cv_popup_forbid_user_avatar);
        tv_popup_forbid_user_name = root.findViewById(R.id.tv_popup_forbid_user_name);
        tv_popup_forbid_user_confirm = root.findViewById(R.id.tv_popup_forbid_user_confirm);
        tv_popup_forbid_user_cancel = root.findViewById(R.id.tv_popup_forbid_user_cancel);

        tv_popup_forbid_user_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        tv_popup_forbid_user_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        //init
//        mInfoPresenter = new OtherUserInfoPresenter();
//        mInfoPresenter.initPresenter(context);
//        mInfoPresenter.registerListener(new IBasePresenterLinstener() {
//            @Override
//            public void dataResult(Object obj, Page page, int status) {
//                if (null != obj) {
//                    mUserInfo = (UserInfoDto) obj;
//                }
//                ImageLoader.getInstance().displayImage(mUserInfo.getAvatar() + String_Utils.getPictureSizeAvatar(), cv_popup_forbid_user_avatar, ImageLoaderUtils.getCommonAvatarOption());
//                tv_popup_forbid_user_name.setText("确认要将" + mUserInfo.getNickname() + "禁言");
//            }
//
//            @Override
//            public void errerResult(int code, String msg) {
//            }
//        });

    }


    public void loadInfoData(String channel_id, String uid) {
        this.uid = uid;
        this.channel_id = channel_id;
//        mInfoPresenter.onExecute(uid, channel_id);
    }
}
