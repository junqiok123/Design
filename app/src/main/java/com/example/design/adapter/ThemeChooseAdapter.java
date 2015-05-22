package com.example.design.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.design.R;

/**
 * Created by Administrator on 2015/5/20.
 */
public class ThemeChooseAdapter extends BaseAdapter {

    private Context mContext;
    private int[] colors;
    private LayoutInflater mInflater;

    public ThemeChooseAdapter(Context context, int[] ints){
        this.mContext = context;
        this.colors = ints;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return colors.length;
    }

    @Override
    public Object getItem(int position) {
        return colors[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.theme_grid_item,null);
        View view = convertView.findViewById(R.id.color_view);
        view.setBackgroundColor(mContext.getResources().getColor(colors[position]));
        return convertView;
    }
}
