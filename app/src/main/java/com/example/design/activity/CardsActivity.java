package com.example.design.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.design.R;
import com.example.design.adapter.CardsGridViewAdapter;
import com.example.design.control.Constant;
import com.example.design.control.ThemeControl;
import com.example.design.util.ThemeUtil;
import com.example.design.util.TitlesUtil;
import com.example.design.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardsActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageView back;
    private Button card_save;
    private GridView gridView;
    private RelativeLayout action_bar;
    private CardsGridViewAdapter adapter;
    private List<Map<String, Object>> list = new ArrayList<>();
    private List<String> listChecked = new ArrayList<>();
    private CardsGridViewAdapter.ViewHolder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        initView();
    }

    private void initView() {
        Constant.isCardsChange = true;
        back = (ImageView) findViewById(R.id.back);
        card_save = (Button) findViewById(R.id.card_save);
        gridView = (GridView) findViewById(R.id.card_grid);
        action_bar = (RelativeLayout) findViewById(R.id.action_bar);
        back.setOnClickListener(this);
        card_save.setOnClickListener(this);

        themeChoose();

        getData();

        adapter = new CardsGridViewAdapter(CardsActivity.this, list);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
    }

    private void getData(){
        String[] titles = getResources().getStringArray(R.array.tabTitles);
        String checkedTitles = TitlesUtil.getTitleChecked(CardsActivity.this);
        Map<String, Object> map;
        for (int i = 1; i < titles.length; i++) {
            map = new HashMap<>();
            map.put("title", titles[i]);
            if (checkedTitles != null) {
                if (checkedTitles.contains(titles[i].toString())) {
                    map.put("checked", true);
                } else
                    map.put("checked", false);
            } else
                map.put("checked", true);
            list.add(map);
        }
    }

    /*
	 * 判断字符串是否包含一些字符 contains
	 */
    public static boolean containsString(String src, String dest) {
        boolean flag = false;
        if (src.contains(dest)) {
            flag = true;
        }
        return flag;
    }

    private void themeChoose() {
        View[] views = new View[]{action_bar};
        ThemeControl.setTheme(CardsActivity.this, views, ThemeUtil.getThemeChoose(CardsActivity.this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                Constant.isCardsChange = false;
                break;
            case R.id.card_save:
                getChekcedData();
                finish();
                break;
        }
    }

    private void getChekcedData() {
        HashMap<Integer, Boolean> state = CardsGridViewAdapter.isSelected;
        for (int i = 0; i < list.size(); i++) {
            if (state.get(i)) {
                listChecked.add(list.get(i).get("title").toString());
            }
        }
        StringBuffer stringBuffer = new StringBuffer();
        if (listChecked.size() == 0) {
            TitlesUtil.setTitleChecked(CardsActivity.this, null);
        } else {
            for (int i = 0; i < listChecked.size(); i++) {
                stringBuffer.append(listChecked.get(i));
                if (i < listChecked.size()-1)
                    stringBuffer.append(",");
            }
            TitlesUtil.setTitleChecked(CardsActivity.this, stringBuffer.toString());
        }
        ToastUtil.show(CardsActivity.this, "选中了" + TitlesUtil.getTitleChecked(CardsActivity.this).toString());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        holder = (CardsGridViewAdapter.ViewHolder) view.getTag();
        holder.card_item_tinicheckbox.toggle();
        CardsGridViewAdapter.getIsSelected().put(position, holder.card_item_tinicheckbox.isChecked());
        Constant.isCardsChange = true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Constant.isCardsChange = false;
    }
}
