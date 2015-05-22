package com.example.design.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.design.R;

/**
 * Created by Administrator on 2015/5/20.
 */
public class NavdrawerAdapter extends BaseAdapter {

    private Context mContext;
    private String[] values;
    private LayoutInflater mInflater;

    public NavdrawerAdapter(Context context, String[] strings){
        this.mContext = context;
        this.values = strings;
        mInflater = LayoutInflater.from(context);

    }
    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public Object getItem(int position) {
        return values[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.navdrawer_item, null);
            holder.navdrawer_text = (TextView)convertView.findViewById(R.id.navdrawer_text);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.navdrawer_text.setText(values[position]);
        Drawable drawable = null;
        switch (position) {
            case 0:
                drawable = mContext.getResources().getDrawable(R.drawable.tabs);
                break;
            case 1:
                drawable = mContext.getResources().getDrawable(R.drawable.color_picker);
                break;
            case 2:
                drawable = mContext.getResources().getDrawable(R.drawable.tools);
                break;
        }
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        holder.navdrawer_text.setCompoundDrawablePadding(10);
        holder.navdrawer_text.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        holder.navdrawer_text.setGravity(Gravity.CENTER_VERTICAL);

        return convertView;
    }

    private final class ViewHolder {
        private TextView navdrawer_text;
    }
}
