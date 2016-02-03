package com.example.design.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.design.R;
import com.example.design.adapter.InfoContentAdapter;
import com.example.design.circularprogressbar.CircularProgressBar;
import com.example.design.control.Constant;
import com.example.design.control.ThemeControl;
import com.example.design.model.Infos;
import com.example.design.model.InfosDto;
import com.example.design.tool.LogTool;
import com.example.design.tool.NetworkTool;
import com.example.design.util.InfoItemHandle;
import com.example.design.util.SaveImagesUtil;
import com.example.design.util.ThemeUtil;
import com.example.design.util.ToastUtil;
import com.example.design.util.URLUtil;
import com.example.design.xlistview.IXListViewLoadMore;
import com.example.design.xlistview.XListView;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class InfoContentActivity extends BaseActivity implements IXListViewLoadMore, OnItemClickListener, AdapterView.OnItemLongClickListener, OnClickListener {
    private static final String tag = "InfoContentActivity";
    private XListView xListView;
    private InfoItemHandle infoItemBiz;
    private List<Infos> dataList = new ArrayList<Infos>();
    private List<Infos> infoDataList = new ArrayList<Infos>();
    private CircularProgressBar progressBar;
    private InfoContentAdapter infoContentAdapter;
    private String url;
    private String[] urls;
    private int currentPage = 1;
    private boolean hasData = true;
    private RelativeLayout action_bar;
    private ImageView back;
    private TextView title;
    private Button sea_web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_content_activity);
        getActionBar().hide();
        initView();
        initData();
    }

    private void initData() {
        infoItemBiz = new InfoItemHandle();
        infoContentAdapter = new InfoContentAdapter(this);
        action_bar = (RelativeLayout) findViewById(R.id.action_bar);
        themeChoose();
        sea_web = (Button) findViewById(R.id.sea_web);
        title = (TextView) findViewById(R.id.title);
        title.setText(getIntent().getExtras().get("title").toString());
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        sea_web.setOnClickListener(this);
        xListView.setAdapter(infoContentAdapter);// 设置内容数据
        xListView.disablePullRefreash();// 不显示下拉更新
        xListView.setPullLoadEnable(this);
        xListView.setOnItemClickListener(this);
        xListView.setOnItemLongClickListener(this);
        registerForContextMenu(xListView);

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
        progressBar = (CircularProgressBar) findViewById(R.id.progressBar);
        findViewById(R.id.back).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadRefresh() {
        new LoadDataTask().execute(Constant.LOAD_REFREASH);
    }

    @Override
    public void onLoadMore() {
        new LoadDataTask().execute(Constant.LOAD_MORE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        imageBrower(position - 2, urls);
    }

    private void imageBrower(int position, String[] urls) {
        Intent intent = new Intent(this, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
        overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
    }

    private void themeChoose() {
        View[] views = new View[]{action_bar};
        ThemeControl.setTheme(InfoContentActivity.this, views, ThemeUtil.getThemeChoose(InfoContentActivity.this));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//        ImageLoader imageLoader = ImageLoader.getInstance();
//        SaveImagesUtil.saveImages(InfoContentActivity.this, imageLoader.loadImageSync(urls[i - 2]), handler);
        ActionSheetDialogNoTitle(i);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sea_web:
                Intent intent = new Intent(InfoContentActivity.this, InfoContentWebActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    class LoadDataTask extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
            switch (params[0]) {
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
                    progressBar.setVisibility(View.GONE);// 隐藏进度
                    ToastUtil.show(InfoContentActivity.this, "没有网络连接！");
                    break;
                case Constant.ERROR_SERVER:
                    progressBar.setVisibility(View.GONE);// 隐藏进度
                    ToastUtil.show(InfoContentActivity.this, "服务器错误！");
                    break;
                default:
                    break;
            }
            if (dataList == null) {
                xListView.disablePullLoad();// 不显示加载更多
                return;// 下载的数据为空时
            } else {
                LogTool.e("HasData", hasData + "");
                if (hasData) {
                    infoContentAdapter.addList(dataList);// 将下载到的数据加载到适配器中
                    infoContentAdapter.notifyDataSetChanged();// 通知刷新数据
                    progressBar.setVisibility(View.GONE);// 隐藏进度
                    infoDataList.addAll(dataList);
                    urls = new String[infoDataList.size() - 1];
                    for (int i = 0; i < infoDataList.size() - 1; i++) {
                        urls[i] = infoDataList.get(i + 1).getImageLink();
                    }
                } else if (currentPage != 1) {
                    xListView.disablePullLoad();// 不显示加载更多
                    ToastUtil.show(InfoContentActivity.this, "看完了");
                } else {
                    progressBar.setVisibility(View.GONE);// 隐藏进度
                    ToastUtil.show(InfoContentActivity.this, "加载失败");
                    sea_web.setVisibility(View.VISIBLE);
                }
            }
            xListView.stopLoadMore();
        }
    }

    private void requstData() {
        try {
            InfosDto infosDto = infoItemBiz.getInfos(URLUtil.getDetailUrl(url, currentPage), currentPage);// 根据url加载内容
            dataList = infosDto.getInfoList();// 获取资讯信息列表
            hasData = true;
        } catch (Exception e) {
            e.printStackTrace();
            hasData = false;
        }
    }

    public Integer refreashData() {
        requstData();
        return -1;
    }

    public Integer loadMoreData() {
        currentPage++;
        requstData();
        return -1;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 5:
                    ToastUtil.show(InfoContentActivity.this, getString(R.string.save_img_failured));
                    break;
                case 6:
                    // 最后通知图库更新
                    InfoContentActivity.this
                            .sendBroadcast(new Intent(
                                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                    Uri.fromFile(new File(
                                            Environment
                                                    .getExternalStorageDirectory()
                                                    + "/DesignImages/"
                                                    + msg.obj))));
                    ToastUtil.show(InfoContentActivity.this, getString(R.string.save_img_succed));
                    break;
            }
        }
    };

    private void ActionSheetDialogNoTitle(final int i) {
        final String[] stringItems = {"保存"};
        final ActionSheetDialog dialog = new ActionSheetDialog(InfoContentActivity.this, stringItems, null);
        dialog.isTitleShow(false).show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ToastUtil.show(InfoContentActivity.this, stringItems[position]);
                ImageLoader imageLoader = ImageLoader.getInstance();
                SaveImagesUtil.saveImages(InfoContentActivity.this, imageLoader.loadImageSync(urls[i - 2]), handler);
                dialog.dismiss();
            }
        });
    }
}
