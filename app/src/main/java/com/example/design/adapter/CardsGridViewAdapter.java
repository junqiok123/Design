package com.example.design.adapter;

import android.content.Context;
import android.support.v7.internal.widget.TintCheckBox;
import android.support.v7.internal.widget.TintCheckedTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.design.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/5/15.
 */
public class CardsGridViewAdapter extends BaseAdapter {

    public static HashMap<Integer, Boolean> isSelected;

    private Context mContext;
    private List<Map<String, Object>> mList;
    private LayoutInflater mInflater;

    public CardsGridViewAdapter(Context context, List<Map<String, Object>> list) {
        this.mContext = context;
        this.mList = list;
        isSelected = new HashMap<>();
        mInflater = LayoutInflater.from(context);
        initDate();
    }

    private void initDate() {
        for (int i = 0; i < mList.size(); i++) {
            getIsSelected().put(i, (Boolean) mList.get(i).get("checked"));
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
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
            convertView = mInflater.inflate(R.layout.card_grid_item, null);
            holder.card_item_text = (TextView) convertView.findViewById(R.id.card_item_text);
            holder.card_item_tinicheckbox = (TintCheckBox) convertView.findViewById(R.id.card_item_tinicheckbox);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.card_item_text.setText(mList.get(position).get("title").toString());
        holder.card_item_tinicheckbox.setChecked(getIsSelected().get(position));

        return convertView;
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        CardsGridViewAdapter.isSelected = isSelected;
    }

    public static class ViewHolder {
        public TextView card_item_text;
        public TintCheckBox card_item_tinicheckbox;
    }
}
