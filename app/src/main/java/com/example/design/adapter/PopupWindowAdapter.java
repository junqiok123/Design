package com.example.design.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.design.R;

/**
 * Created by Administrator on 2015/5/14.
 */
public class PopupWindowAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private String[] titles;
    private int currentItem = -1;

    public PopupWindowAdapter(Context context, String[] strings) {
        this.mContext = context;
        this.titles = strings;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return titles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.popupwindow_item, parent, false);
            holder = new ViewHolder();
            holder.pop_grid_text = (TextView) convertView.findViewById(R.id.pop_grid_text);
            holder.pop_grid_text_line = (View) convertView.findViewById(R.id.pop_grid_text_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.pop_grid_text.setText(titles[position]);
        if (currentItem == position)
            holder.pop_grid_text_line.setVisibility(View.VISIBLE);
        else
            holder.pop_grid_text_line.setVisibility(View.INVISIBLE);
        return convertView;
    }

    public void setSelectItem(int selectItem) {
        this.currentItem = selectItem;
    }

    private final class ViewHolder {
        private TextView pop_grid_text;
        private View pop_grid_text_line;
    }
}
