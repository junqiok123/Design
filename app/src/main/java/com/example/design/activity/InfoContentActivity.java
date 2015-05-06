package com.example.design.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;

import com.example.design.R;
import com.example.design.adapter.InfoContentAdapter;
import com.example.design.control.Constant;
import com.example.design.control.ThemeControl;
import com.example.design.model.Infos;
import com.example.design.model.InfosDto;
import com.example.design.tool.LogTool;
import com.example.design.tool.NetworkTool;
import com.example.design.util.InfoItemHandle;
import com.example.design.util.ToastUtil;
import com.example.design.util.URLUtil;
import com.example.design.xlistview.IXListViewLoadMore;
import com.example.design.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;


public class InfoContentActivity extends BaseActivity implements IXListViewLoadMore, OnItemClickListener {
	private static final String tag = "InfoContentActivity";
	private XListView xListView;
	private InfoItemHandle infoItemBiz;
	private List<Infos> dataList = new ArrayList<Infos>();
	private List<Infos> infoDataList = new ArrayList<Infos>();
	private ProgressBar progressBar;
	private InfoContentAdapter infoContentAdapter;
	private String url;
	private String[] urls;
	private int currentPage = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LogTool.e(tag, "onCreate");
		ThemeControl.onActivityCreateSetTheme(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_content_activity);
		initView();
		initData();
	}

	private void initData() {
		infoItemBiz = new InfoItemHandle();
		infoContentAdapter = new InfoContentAdapter(this);

		xListView.setAdapter(infoContentAdapter);// 设置内容数据
		xListView.disablePullRefreash();// 不显示下拉更新
		xListView.setPullLoadEnable(this);
		xListView.setOnItemClickListener(this);

		progressBar.setVisibility(View.VISIBLE);// 准备加载数据显示进度圈
		if (NetworkTool.checkNetState(this)) {
			loadRefresh();// 加载内容
		} else {
			ToastUtil.show(this, getString(R.string.without_the_internet) + "!");
			finish();
		}
	}

	private void initView() {
		url = getIntent().getExtras().getString("url");// 获取点击的url
		LogTool.e(tag, "LoadDataTask,url = " + url);
		xListView = (XListView) findViewById(R.id.xlistView);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void loadRefresh(){
		LogTool.e(tag, "onRefresh()");
		new LoadDataTask().execute(Constant.LOAD_REFREASH);
	}

	@Override
	public void onLoadMore() {
		LogTool.e(tag, "onLoadMore()");
		new LoadDataTask().execute(Constant.LOAD_MORE);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		ImageShowActivity.startImageShowActivity(InfoContentActivity.this, infoDataList.get(position - 1).getImageLink());
		LogTool.e(tag, "onItemClick,infoDataList.size() = " + infoDataList.size());
		imageBrower(position-3, urls);
	}

	private void imageBrower(int position, String[] urls) {
		Intent intent = new Intent(this, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		startActivity(intent);
	}

	class LoadDataTask extends AsyncTask<Integer, Void, Integer> {

		@Override
		protected Integer doInBackground(Integer... params) {
			switch (params[0]){
				case Constant.LOAD_REFREASH:
					refreashData();
					break;
				case Constant.LOAD_MORE:
					return loadMoreData();
			}

			return -1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			switch (result) {
				case Constant.ERROR_NO_NETWORK:
					ToastUtil.show(InfoContentActivity.this, "没有网络连接！");
					break;
				case Constant.ERROR_SERVER:
					ToastUtil.show(InfoContentActivity.this, "服务器错误！");
					break;
				default:
					break;
			}
			LogTool.e(tag, "onPostExecute,dataList.size() = " + dataList.size());
			if (dataList == null) {
				xListView.disablePullLoad();// 不显示加载更多
				return;// 下载的数据为空时
			} else {
				infoContentAdapter.addList(dataList);// 将下载到的数据加载到适配器中
				infoContentAdapter.notifyDataSetChanged();// 通知刷新数据
				progressBar.setVisibility(View.GONE);// 隐藏进度
				infoDataList.addAll(dataList);
				urls = new String[infoDataList.size()-2];
				for (int i = 0; i < infoDataList.size() - 2; i++) {
					urls[i] = infoDataList.get(i+2).getImageLink();
				}
				LogTool.e(tag, "onPostExecute,dataList.size() = " + dataList.size() + "=====" + infoDataList.size());
				LogTool.e(tag, "onPostExecute,infoContentAdapter.notifyDataSetChanged()");
			}
			xListView.stopLoadMore();
		}
	}

	private void requstData(){
		try {
			InfosDto infosDto = infoItemBiz.getInfos(URLUtil.getDetailUrl(url,currentPage),currentPage);// 根据url加载内容
			dataList = infosDto.getInfoList();// 获取资讯信息列表
			LogTool.e(tag, "LoadDataTask,dataList.size() = " + infoDataList.size());
		} catch (Exception e) {
			Looper.prepare();
//			Toast.makeText(CSDN.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
			xListView.disablePullLoad();// 不显示加载更多
			xListView.stopLoadMore();
			Looper.loop();
		}
	}

	public Integer refreashData() {
		requstData();
		return -1;
	}

	public Integer loadMoreData() {
		currentPage ++;
		requstData();
		return -1;
	}
}
