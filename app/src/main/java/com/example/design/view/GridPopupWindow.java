package com.example.design.view;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.example.design.R;

public class GridPopupWindow extends PopupWindow {

	private Activity context;
	private GridView grid;
	private EditText editText;
	private OnPopupItemClickListener onPopupItemClickListener;
	private OnTextChangedListener onTextChangedListener;
	private int displayWidth;
	private int displayHeight;
	
	public GridPopupWindow(Activity context) {
		this.context = context;
		initView();
	}

	@SuppressWarnings("deprecation")
	private void initView() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.view_grid_popup, null);
		setContentView(view);
		int h = context.getWindowManager().getDefaultDisplay().getHeight();
		int w = context.getWindowManager().getDefaultDisplay().getWidth();
		// 设置宽度,若没有设置宽度为LayoutParams.WRAP_CONTENT
		
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		displayWidth = dm.widthPixels;
		displayHeight = GridLayout.LayoutParams.WRAP_CONTENT;
		
		setWidth(displayWidth);
		setHeight(displayHeight);
		
//		setWidth(w / 3);
//		setWidth(LayoutParams.MATCH_PARENT);
//		setHeight(LayoutParams.MATCH_PARENT);
		// 这里很重要，不设置这个ListView得不到相应
		this.setFocusable(true);
		this.setBackgroundDrawable(new BitmapDrawable());
		this.setOutsideTouchable(true);
		this.grid = (GridView) view.findViewById(R.id.task_grid);
		this.editText = (EditText) view.findViewById(R.id.edit_input);
		this.grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				GridPopupWindow.this.dismiss();
				if (onPopupItemClickListener != null) {
					onPopupItemClickListener.onPopupItemClick(parent, view,
							position, id);
				}
			}
		});
		this.editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (onTextChangedListener != null) {
					onTextChangedListener.OnTextChanged(s);
				}
			}
		});
		this.editText.setVisibility(View.GONE);
	}

	public void setAdapter(BaseAdapter adapter) {
		if (adapter == null) {
			return;
		}
		grid.setAdapter(adapter);
	}

	public OnPopupItemClickListener getOnPopupItemClickListener() {
		return onPopupItemClickListener;
	}

	public void setOnPopupItemClickListener(
			OnPopupItemClickListener onPopupItemClickListener) {
		this.onPopupItemClickListener = onPopupItemClickListener;
	}

	public OnTextChangedListener getOnTextChangedListener() {
		return onTextChangedListener;
	}

	public void setOnTextChangedListener(
			OnTextChangedListener onTextChangedListener) {
		this.onTextChangedListener = onTextChangedListener;
	}

	/**
	 * 回调接口.供外部调用
	 * 
	 */
	public interface OnPopupItemClickListener {
		/**
		 * 当点击PopupWindow的ListView 的item的时候调用此方法，用回调方法的好处就是降低耦合性
		 * 
		 * @param position
		 *            位置
		 */
		void onPopupItemClick(AdapterView<?> parent, View view, int position,
							  long id);
	}

	public interface OnTextChangedListener {
		void OnTextChanged(Editable s);
	}

}
