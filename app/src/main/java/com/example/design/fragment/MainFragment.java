package com.example.design.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.design.R;
import com.example.design.activity.InfoContentActivity;
import com.example.design.adapter.InfoItemAdapter;
import com.example.design.control.Constant;
import com.example.design.dao.InfosItemDao;
import com.example.design.model.InfoItem;
import com.example.design.tool.LogTool;
import com.example.design.tool.NetworkTool;
import com.example.design.util.InfoItemHandle;
import com.example.design.util.TimeUtil;
import com.example.design.xlistview.IXListViewLoadMore;
import com.example.design.xlistview.IXListViewRefreshListener;
import com.example.design.xlistview.XListView;
import com.gc.materialdesign.widgets.SnackBar;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment implements IXListViewRefreshListener, IXListViewLoadMore {

	private View view;
	private static final String tag = "MainFragment";
	private boolean isFirstIn = true;// 是否是第一次进入
	private boolean isLoadingDataFromNetWork;// 当前数据是否是从网络中获取的
	private int infoType = Constant.TYPE_GALLERY;// 默认的Type
	private int currentPage = 1;// 当前页面
	private InfoItemHandle infoItemHandle;// 处理新闻的业务类
	private InfosItemDao infosItemDao;// 与数据库交互
	private XListView xListView;// 扩展的ListView
	private InfoItemAdapter infoItemAdapter;// 数据适配器
	private List<InfoItem> infoItemList = new ArrayList<InfoItem>();// 数据

	public static MainFragment getInstance(int infoType) {
//		this.infoType = infoType;
//		infoItemHandle = new InfoItemHandle();
		MainFragment newFragment = new MainFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("infoType", infoType);
		newFragment.setArguments(bundle);
		return newFragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.tab_item_fragment_main, container, false);
		infosItemDao = new InfosItemDao(getActivity());
		infoItemAdapter = new InfoItemAdapter(getActivity(), infoItemList);
		xListView = (XListView) view.findViewById(R.id.xListView);
		xListView.setAdapter(infoItemAdapter);
		xListView.setPullRefreshEnable(this);
		xListView.setPullLoadEnable(this);
		xListView.setRefreshTime(TimeUtil.getRefreashTime(getActivity(), infoType));
		xListView.setOnItemClickListener(new OnItemClickListener() {
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

		if (isFirstIn) {
			xListView.startRefresh();
			isFirstIn = false;
		} else {
			xListView.NotRefreshAtBegin();
		}
		return view;
	}

	@Override
	public void onRefresh() {
		new LoadDatasTask().execute(Constant.LOAD_REFREASH);
	}

	@Override
	public void onLoadMore() {
		new LoadDatasTask().execute(Constant.LOAD_MORE);
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
		}
	}

	/**
	 * 下拉刷新数据
	 */
	public Integer refreashData() {

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
}
