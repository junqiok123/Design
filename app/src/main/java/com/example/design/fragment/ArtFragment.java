package com.example.design.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.design.R;
import com.example.design.activity.InfoContentActivity;
import com.example.design.adapter.InfoItemAdapter;
import com.example.design.adapter.PopGridAdapter;
import com.example.design.circularprogressbar.CircularProgressBar;
import com.example.design.control.Constant;
import com.example.design.dao.InfosItemDao;
import com.example.design.model.InfoItem;
import com.example.design.tool.NetworkTool;
import com.example.design.util.InfoItemHandle;
import com.example.design.util.TimeUtil;
import com.example.design.view.GridPopupWindow;
import com.example.design.xlistview.IXListViewLoadMore;
import com.example.design.xlistview.IXListViewRefreshListener;
import com.example.design.xlistview.XListView;
import com.gc.materialdesign.widgets.SnackBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Seven on 2016/2/16.
 *
 */
public class ArtFragment extends LazyFragment implements IXListViewRefreshListener, IXListViewLoadMore, View.OnClickListener {

    private View view;
    private static final String tag = "ArtFragment";
    private boolean isFirstIn = true;// 是否是第一次进入
    private boolean isLoadingDataFromNetWork;// 当前数据是否是从网络中获取的
    private int infoType = Constant.TYPE_PHOTOGRAPHY;// 默认的Type
    private int currentPage = 1;// 当前页面
    private InfoItemHandle infoItemHandle = new InfoItemHandle();// 处理新闻的业务类
    private InfosItemDao infosItemDao;// 与数据库交互
    private XListView xListView;// 扩展的ListView
    private InfoItemAdapter infoItemAdapter;// 数据适配器
    private List<InfoItem> infoItemList = new ArrayList<InfoItem>();// 数据

    private LinearLayout layoutTitle;
    private TextView title;
    private GridPopupWindow popupWindow;
    private List<String> titles;
    private PopGridAdapter popAdapter;
    private CircularProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_art, container, false);


//        if (isFirstIn) {
//            xListView.startRefresh();
//            isFirstIn = false;
//        } else {
//            xListView.NotRefreshAtBegin();
//        }
//        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutTitle = (LinearLayout) view.findViewById(R.id.title_layout);
        title = (TextView) view.findViewById(R.id.title);
        layoutTitle.setOnClickListener(this);
        setTitle();
        progressBar = (CircularProgressBar) view.findViewById(R.id.progressBar);

        infosItemDao = new InfosItemDao(getActivity());
        infoItemAdapter = new InfoItemAdapter(getActivity(), infoItemList);
        xListView = (XListView) view.findViewById(R.id.xListView);
        xListView.setAdapter(infoItemAdapter);
        xListView.setPullRefreshEnable(this);
        xListView.setPullLoadEnable(this);
        xListView.setRefreshTime(TimeUtil.getRefreashTime(getActivity(), infoType));
        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InfoItem infoItem = infoItemList.get(position - 1);
                Intent intent = new Intent(getActivity(), InfoContentActivity.class);
                intent.putExtra("url", infoItem.getLink());
                intent.putExtra("title", infoItem.getTitle());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
            }
        });

        isViewCreated = true;
        lazyLoad();
    }

    @Override
    public void onRefresh() {
        new LoadDatasTask().execute(Constant.LOAD_REFREASH);
    }

    @Override
    public void onLoadMore() {
        new LoadDatasTask().execute(Constant.LOAD_MORE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_layout:
                popupWindow.showAsDropDown(layoutTitle);
                break;
        }
    }

    private void setTitle() {
        titles = new ArrayList<>();
        String[] titleStr = getResources().getStringArray(R.array.tabTitles);
        for (int i = 21; i < titleStr.length - 1; i++) {
            titles.add(titleStr[i]);
        }
        popAdapter = new PopGridAdapter(getActivity(), titles);
        popupWindow = new GridPopupWindow(getActivity());
        popupWindow.setAdapter(popAdapter);
        popupWindow.setOnPopupItemClickListener(new PopupItemClickListener());
    }

    @Override
    public void lazyLoad() {
        if (isVisibleToUser && isViewCreated) {
            if (isFirstIn) {
                xListView.startRefresh();
                isFirstIn = false;
            } else {
                xListView.NotRefreshAtBegin();
            }
            isViewCreated = false;
        }
    }

    /**
     * 记载数据的异步任务
     */
    class LoadDatasTask extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
            switch (params[0]) {
                case Constant.LOAD_MORE:
                    loadMoreData();
                    break;
                case Constant.LOAD_REFREASH:
                    return refreashData();
            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            SnackBar snackBar;
            switch (result) {
                case Constant.ERROR_NO_NETWORK:
                    snackBar = new SnackBar(getActivity(), "没有网络连接！");
                    snackBar.setDismissTimer(1000);
                    snackBar.show();
                    infoItemAdapter.setDatas(infoItemList);
                    infoItemAdapter.notifyDataSetChanged();
                    break;
                case Constant.ERROR_SERVER:
                    snackBar = new SnackBar(getActivity(), "服务器错误！");
                    snackBar.setDismissTimer(1000);
                    snackBar.show();
                    break;
                default:
                    break;
            }
            xListView.setRefreshTime(TimeUtil.getRefreashTime(getActivity(), infoType));
            xListView.stopRefresh();
            xListView.stopLoadMore();
            handler.sendEmptyMessage(0);
        }
    }

    /**
     * 下拉刷新数据
     */
    public Integer refreashData() {
        currentPage = 1;
        if (NetworkTool.checkNetState(getActivity())) {
            // 获取最新数据
            try {
                List<InfoItem> inofItems = infoItemHandle.getInfosItems(infoType, 1);
                infoItemAdapter.setDatas(inofItems);

                isLoadingDataFromNetWork = true;
                // 设置刷新时间
                TimeUtil.setRefreashTime(getActivity(), infoType);
                // 清除数据库数据
                infosItemDao.deleteAll(infoType);
                // 存入数据库
                infosItemDao.add(inofItems);

            } catch (Exception e) {
                e.printStackTrace();
                isLoadingDataFromNetWork = false;
                return Constant.ERROR_SERVER;
            }
        } else {
            isLoadingDataFromNetWork = false;
            // TODO从数据库中加载
            List<InfoItem> infoItems = infosItemDao.list(infoType, 1);
            infoItemList = infoItems;
            return Constant.ERROR_NO_NETWORK;
        }
        handler.sendEmptyMessage(0);
        return -1;
    }

    public void loadMoreData() {
        // 当前数据是从网络获取的
        if (isLoadingDataFromNetWork) {
            currentPage += 1;
            try {
                List<InfoItem> infoItems = infoItemHandle.getInfosItems(infoType, currentPage);
                infosItemDao.add(infoItems);
                infoItemAdapter.addAll(infoItems);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // 从数据库加载的
            currentPage += 1;
            List<InfoItem> infoItems = infosItemDao.list(infoType, currentPage);
            infoItemAdapter.addAll(infoItems);
        }
    }

    private class PopupItemClickListener implements GridPopupWindow.OnPopupItemClickListener {
        @Override
        public void onPopupItemClick(AdapterView<?> parent, View view, int position, long id) {
            title.setText(titles.get(position));
            currentPage = 1;
            infoType = position + 22;
            xListView.startRefresh();
            progressBar.setVisibility(View.VISIBLE);
            popAdapter.setSelectPosition(position);
            popAdapter.notifyDataSetChanged();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressBar.setVisibility(View.GONE);
            infoItemAdapter.notifyDataSetChanged();
        }
    };
}
