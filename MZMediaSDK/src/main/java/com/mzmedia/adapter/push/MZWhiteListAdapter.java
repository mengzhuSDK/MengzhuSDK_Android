package com.mzmedia.adapter.push;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.mengzhu.live.sdk.business.dto.Config.MZFCodeDto;
import com.mengzhu.live.sdk.business.dto.Config.MZWhiteListDto;
import com.mengzhu.sdk.R;

import java.util.List;

/**
 * 白名单列表adapter
 */
public class MZWhiteListAdapter extends BaseAdapter {

    private Context mContext;
    private List<MZWhiteListDto.WhiteListItem> dataList;

    public interface EditClickListener{
        void editClick(View editView , int position , MZWhiteListDto.WhiteListItem item);
    }

    private EditClickListener editClickListener;

    public void setEditClickListener(EditClickListener editClickListener) {
        this.editClickListener = editClickListener;
    }

    public MZWhiteListAdapter(Context mContext, List<MZWhiteListDto.WhiteListItem> dataList) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_white_list_layout , null , false);

            viewHolder.item_check = convertView.findViewById(R.id.iv_check);
            viewHolder.item_name = convertView.findViewById(R.id.tv_name);
            viewHolder.item_ext = convertView.findViewById(R.id.tv_ext);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MZWhiteListDto.WhiteListItem whiteListItem = dataList.get(position);
        viewHolder.item_name.setText(whiteListItem.getName());
        if (whiteListItem.isCheck()){
            viewHolder.item_check.setImageResource(R.mipmap.icon_anonymous_select);
        }else {
            viewHolder.item_check.setImageResource(R.mipmap.icon_anonymous);
        }
        viewHolder.item_ext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editClickListener != null){
                    editClickListener.editClick(viewHolder.item_ext , position , dataList.get(position));
                }
            }
        });
        return convertView;
    }

    class ViewHolder{
        private ImageView item_check;
        private TextView item_name;
        private TextView item_ext;
    }
}
