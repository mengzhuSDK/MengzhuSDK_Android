package com.mzmedia.adapter.push;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.mengzhu.live.sdk.business.dto.Config.MZFCodeDto;
import com.mengzhu.sdk.R;

import java.util.List;

/**
 * F码列表adapter
 */
public class MZFCodeListAdapter extends BaseAdapter {

    private Context mContext;
    private List<MZFCodeDto> dataList;

    public MZFCodeListAdapter(Context mContext, List<MZFCodeDto> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
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
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_f_code_layout , null , false);

            viewHolder.item_check = convertView.findViewById(R.id.item_check);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MZFCodeDto mzfCodeDto = dataList.get(position);
        viewHolder.item_check.setText(mzfCodeDto.getName());
        viewHolder.item_check.setChecked(mzfCodeDto.isCheck());
        return convertView;
    }

    class ViewHolder{
        private CheckBox item_check;
    }
}
