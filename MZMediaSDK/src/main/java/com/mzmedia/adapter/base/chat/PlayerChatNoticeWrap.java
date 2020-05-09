package com.mzmedia.adapter.base.chat;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mengzhu.live.sdk.R;
import com.mengzhu.live.sdk.business.dto.chat.ChatMessageDto;
import com.mengzhu.live.sdk.business.dto.chat.impl.ChatNoticeDto;
import com.mengzhu.live.sdk.business.dto.play.PlayInfoDto;
import com.mzmedia.adapter.base.BaseViewObtion;


/**
 * Created by DELL on 2017/3/14.
 */

public class PlayerChatNoticeWrap extends BaseViewObtion {
    private Context mContext;

    public PlayerChatNoticeWrap(Context context){
        mContext=context;
    }

    @Override
    public View createView(Object o, int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView= View.inflate(mContext, R.layout.play_chat_notice_item,null);
            holder.play_chat_notice_txt= (TextView) convertView.findViewById(R.id.play_chat_notice_txt);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    @Override
    public void updateView(Object o, int position, View convertView) {
        ViewHolder holder= (ViewHolder) convertView.getTag();
        holder.play_chat_notice_txt.setVisibility(View.VISIBLE);
        ChatMessageDto dto= (ChatMessageDto) o;
        ChatNoticeDto megTxtDto= (ChatNoticeDto) dto.getText().getBaseDto();
        holder.play_chat_notice_txt.setText( megTxtDto.getContent());
    }

    public class ViewHolder{
        private TextView play_chat_notice_txt;
    }
}
