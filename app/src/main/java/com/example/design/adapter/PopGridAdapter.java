package com.example.design.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.design.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopGridAdapter extends BaseAdapter {

	private Activity context;
	private List<String> data;
	private LayoutInflater inflater;
	private Map<Integer, Boolean> map;

	public PopGridAdapter(Activity context, List<String> data) {
		this.context = context;
		this.data = data;
		inflater = LayoutInflater.from(this.context);
		map = new HashMap<>();
		setSelect();
	}

	private void setSelect() {
		for (int i = 0; i < data.size(); i++) {
			map.put(i, false);
		}
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View v, ViewGroup parent) {
		ViewHolder holder;
		if (v == null) {
			holder = new ViewHolder();
			v = inflater.inflate(R.layout.item_pop_gridview, null);
			holder.textView = (TextView) v.findViewById(R.id.text);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		holder.textView.setText(data.get(position));
		if (getSelectPosition(position))
			holder.textView.setBackground(context.getResources().getDrawable(R.drawable.pop_grid_text_bg_selector));
		else
			holder.textView.setBackground(context.getResources().getDrawable(R.drawable.pop_grid_text_bg));
		return v;
	}

	static class ViewHolder {
		public TextView textView;
	}

	public void setSelectPosition(int position) {
		setSelect();
		map.put(position, true);
	}

	public boolean getSelectPosition(int position) {
		return map.get(position);
	}

}
