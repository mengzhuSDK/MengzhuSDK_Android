package com.mengzhu.sdk.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mengzhu.live.sdk.business.dto.Config.MZUnUseFCodeDto;
import com.mengzhu.sdk.demo.R;

import java.util.List;

/**
 * 未使用F码adapter
 */
public class CheckFCodeAdapter extends BaseAdapter {

    private Context mContext;
    private List<MZUnUseFCodeDto.UnUseListDto> dataList;

    public CheckFCodeAdapter(Context mContext, List<MZUnUseFCodeDto.UnUseListDto> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList != null ? dataList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_check_f_code, null, false);

            viewHolder.item_check = convertView.findViewById(R.id.iv_check);
            viewHolder.item_name = convertView.findViewById(R.id.tv_name);
            viewHolder.line = convertView.findViewById(R.id.view_line);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.item_name.setText(dataList.get(position).getCode());
        if (dataList.get(position).isCheck()) {
            viewHolder.item_check.setImageResource(R.mipmap.icon_anonymous_select);
        } else {
            viewHolder.item_check.setImageResource(R.mipmap.icon_anonymous);
        }
        if (position == dataList.size() - 1) {
            viewHolder.line.setVisibility(View.GONE);
        } else {
            viewHolder.line.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        private ImageView item_check;
        private TextView item_name;
        private View line;
    }
}
