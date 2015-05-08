package com.example.design.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.design.R;
import com.example.design.control.ImageLoaderControl;
import com.example.design.model.InfoItem;
import com.example.design.tool.StringTool;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

public class InfoItemAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<InfoItem> infoDatas;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private Context mContext;
	public InfoItemAdapter(Context context, List<InfoItem> datas) {
		this.infoDatas = datas;
		this.mContext = context;
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		mInflater = LayoutInflater.from(context);
	}

	public void addAll(List<InfoItem> infoDatas) {
		this.infoDatas.addAll(infoDatas);
	}

	public void setDatas(List<InfoItem> infoDatas) {
		this.infoDatas.clear();// 清空数据
		this.infoDatas.addAll(infoDatas);
	}

	@Override
	public int getCount() {
		return infoDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return infoDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.info_item_cell, parent, false);
			holder = new ViewHolder();
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
			LinearLayout.LayoutParams sp_params = new LinearLayout.LayoutParams(displayMetrics.widthPixels-100,  (displayMetrics.widthPixels-100) * 34/45);
//			sp_params.setMargins(10, 10, 20, 10);
			sp_params.gravity = Gravity.CENTER;
//			holder.image.setScaleType(ImageView.ScaleType.CENTER);
			holder.image.setAdjustViewBounds(true);
			holder.image.setLayoutParams(sp_params);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		InfoItem infoItem = infoDatas.get(position);
		holder.title.setText(StringTool.CN2EN(infoItem.getTitle()));
		holder.content.setText(StringTool.CN2EN(infoItem.getContent()));
		if (infoItem.getImgLink() != null) {
			holder.image.setVisibility(View.VISIBLE);
			imageLoader.displayImage(infoItem.getImgLink(), holder.image, ImageLoaderControl.options);
		} else {
			holder.image.setVisibility(View.GONE);
		}

		return convertView;
	}

	private final class ViewHolder {
		private TextView title;
		private TextView content;
		private ImageView image;
	}

}
