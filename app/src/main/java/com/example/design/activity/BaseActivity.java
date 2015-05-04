package com.example.design.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.design.R;
import com.example.design.tool.ActivityCollectorTool;


public class BaseActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Activity初始化时，加入AcitivityCollector的List中
		ActivityCollectorTool.addActivity(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Activity被销毁时，在AcitivityCollector的List中移除
		ActivityCollectorTool.removeActivity(this);
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.activity_translate_right_in, R.anim.activity_translate_right_out);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
		overridePendingTransition(R.anim.activity_translate_right_in, R.anim.activity_translate_right_out);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.activity_translate_right_close_in, R.anim.activity_translate_right_close_out);
	}
}
