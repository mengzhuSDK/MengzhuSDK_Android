package com.mzmedia.widgets.popupwindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mengzhu.live.sdk.business.dto.MZOnlineUserListDto;
import com.mengzhu.live.sdk.core.utils.DensityUtil;
import com.mengzhu.live.sdk.ui.api.MZApiDataListener;
import com.mengzhu.live.sdk.ui.api.MZApiRequest;
import com.mengzhu.sdk.R;
import com.mzmedia.pullrefresh.PullToRefreshBase;
import com.mzmedia.pullrefresh.PullToRefreshListView;
import com.mzmedia.widgets.CircleImageView;
import com.mzmedia.widgets.dialog.AbstractPopupWindow;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;



/**
 * @author sunjiale
 * @description 观看人数列表
 */
public class PersonListPopupWindow extends AbstractPopupWindow implements OnClickListener, PopupWindow.OnDismissListener , MZApiDataListener {

    private ImageView iv_close;
    private TextView tv_num;
    private PullToRefreshListView mListView = null;
    private LinearLayout mLayout = null;
    private Context context;
    private MZApiRequest mzApiRequestOnline;
    private DisplayImageOptions avatarOptions;
    private PersonListAdapter adapter = null;
    private List<MZOnlineUserListDto> totalPersons = null;
    //offset
    private PersonListClickedCallBack listener;

    private String mTicketId;

    public PersonListPopupWindow(Context context,  String ticketId) {
        super(context);
        this.context = context;
        this.mTicketId = ticketId;
        View root = View.inflate(context, R.layout.popup_person_list, null);
        setContentView(root);
        mLayout = (LinearLayout) root.findViewById(R.id.ll_content);
        int minSize = DensityUtil.getScreenWidth(context) > DensityUtil.getScreenHeight(context) ? DensityUtil.getScreenHeight(context) : DensityUtil.getScreenWidth(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (minSize * 0.7),
                (int) (minSize * 0.8));
        mLayout.setLayoutParams(params);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        initView(root);
        setOnDismissListener(this);
    }

    /**
     * @param root
     * @author sunjiale
     * @description 初始化
     */
    private boolean isHeader;
    private boolean isShowNoMoreLabel;

    private void initView(View root) {
        //头像加载失败默认处理
        avatarOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.icon_default_avatar)
                .showImageForEmptyUri(R.mipmap.icon_default_avatar)
                .showImageOnFail(R.mipmap.icon_default_avatar)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        iv_close = (ImageView) root.findViewById(R.id.iv_popup_window_person_list_close);
        iv_close.setOnClickListener(this);
        tv_num = (TextView) root.findViewById(R.id.tv_popup_window_person_list_num);
        mListView = (PullToRefreshListView) root.findViewById(R.id.listveiw_popup_person_list);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (refreshView.isHeaderShown()) {
                    isHeader = true;
                    mzApiRequestOnline.startData(MZApiRequest.API_TYPE_ONLINE_USER_LIST, true, mTicketId);
                } else {
                    if (isShowNoMoreLabel) {
                        mListView.getLoadingLayoutProxy(false, true).setNoMoreLabel(true);
                        mListView.onRefreshComplete();
                    } else {
                        isHeader = false;
                        mzApiRequestOnline.startData(MZApiRequest.API_TYPE_ONLINE_USER_LIST, false, mTicketId);
                    }
                }
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (null == listener) {
                    return;
                }
                MZOnlineUserListDto userInfo = totalPersons.get(position);
                listener.onPersonItemClicked(userInfo);
            }
        });

        totalPersons = new ArrayList<MZOnlineUserListDto>();
        adapter = new PersonListAdapter(totalPersons , context);
        mListView.setAdapter(adapter);
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mzApiRequestOnline = new MZApiRequest();
        mzApiRequestOnline.createRequest(context, MZApiRequest.API_TYPE_ONLINE_USER_LIST);
        mzApiRequestOnline.setResultListener(this);
        mzApiRequestOnline.startData(MZApiRequest.API_TYPE_ONLINE_USER_LIST, true, mTicketId);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_popup_window_person_list_close) {
            this.dismiss();
        }
    }

    public void setOnPersonListClickCallBack(PersonListClickedCallBack listener) {
        this.listener = listener;
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        totalPersons.clear();
    }

    @Override
    public void onDismiss() {
        super.dismiss();
    }

    @Override
    public void dataResult(String apiType, Object dto) {
        List<MZOnlineUserListDto> mzOnlineUserListDto = (List<MZOnlineUserListDto>) dto;
        if (isHeader){
            totalPersons.clear();
        }
        isShowNoMoreLabel = mzOnlineUserListDto != null && mzOnlineUserListDto.size() < 20;
        totalPersons.addAll(mzOnlineUserListDto);
        adapter.notifyDataSetChanged();
        mListView.onRefreshComplete();
    }

    @Override
    public void errorResult(String apiType, int code, String msg) {

    }

    public interface PersonListClickedCallBack {
        void onPersonItemClicked(MZOnlineUserListDto onlineUserListDto);
    }

    class PersonListAdapter extends BaseAdapter{

        private List<MZOnlineUserListDto> personList;
        private Context context;

        private PersonListAdapter(List<MZOnlineUserListDto> personList , Context context){
            this.personList = personList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return personList == null?0:personList.size();
        }

        @Override
        public Object getItem(int i) {
            return personList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            PersonViewHolder viewHolder;
            if (view == null){
                viewHolder = new PersonViewHolder();
                view = LayoutInflater.from(context).inflate(R.layout.item_popup_window_person_list , viewGroup , false);
                viewHolder.avatar = view.findViewById(R.id.item_person_list_avatar);
                viewHolder.mTvName = view.findViewById(R.id.item_person_list_name);
                view.setTag(viewHolder);
            }else {
                viewHolder = (PersonViewHolder) view.getTag();
            }
            ImageLoader.getInstance().displayImage(personList.get(i).getAvatar(), viewHolder.avatar, avatarOptions);
            viewHolder.mTvName.setText(personList.get(i).getNickname());
            return view;
        }
        public class PersonViewHolder{
            CircleImageView avatar;
            TextView mTvName;
        }
    }
}
