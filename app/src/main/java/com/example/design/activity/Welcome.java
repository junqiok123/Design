package com.example.design.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.design.R;
import com.example.design.control.Constant;
import com.example.design.tool.LogTool;
import com.example.design.util.JsonParse;
import com.example.design.view.DotsTextView;
import com.example.design.view.SecretTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import kll.dod.rtk.AdManager;
import kll.dod.rtk.st.SpotManager;

public class Welcome extends Activity {

    boolean isFirstIn = false;
    private static final int GO_HOME = 1000;
    private static final int GO_GUIDE = 1001;
    private static final long SPLASH_DELAY_MILLIS = 5000;
    private static final String SHAREDPREFERENCES_NAME = "first_pref";

    private SecretTextView hello_words, hello_words_name;
    private LinearLayout linearLayout;
    private String strResult, words, words_name;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME:
                    goHome();
                    break;
                case GO_GUIDE:
                    // goGuide();
                    break;
                case 0:
                    hello_words.setText(words);
                    hello_words.setDuration(1000);
                    hello_words.setIsVisible(true);
                    hello_words.show();
                    hello_words_name.setText(words_name);
                    hello_words_name.setDuration(1000);
                    hello_words_name.setIsVisible(true);
                    hello_words_name.show();

                    mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
//                    dotsTextView.hideAndStop();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

        if (!isTaskRoot()) {
            finish();
            return;
        }

        init();
//        AdManager.getInstance(getApplicationContext()).init("dad7e9f379621a86", "6a465fa58538216c", false);
    }

    private void init() {

        DotsTextView dotsTextView = (DotsTextView) findViewById(R.id.dots);
        dotsTextView.showAndPlay();
        hello_words = (SecretTextView) findViewById(R.id.hello_words);
        hello_words_name = (SecretTextView) findViewById(R.id.hello_words_name);
        SecretTextView loading_text = (SecretTextView) findViewById(R.id.loading_text);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        loading_text.setDuration(2000);
        loading_text.setIsVisible(true);
        loading_text.show();
        dotsTextView.setVisibility(View.VISIBLE);
//        mThread();

        new HelloWordTask(new Random().nextInt(150) + 1).execute();

    }

    private void mThread() {
        new Thread() {
            public void run() {
                try {
                    strResult = new JsonParse().connServerForResult(Constant.HELLO_WORDS, mHandler);
                    words = new JSONObject(strResult).getString("words");
                    Thread.sleep(200);
                } catch (JSONException e) {
                    mHandler.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(0);
            }

            ;
        }.start();
    }

    class HelloWordTask extends AsyncTask<String, Void, String> {

        String httpUrl = "http://apis.baidu.com/avatardata/mingrenmingyan/lookup";
        String httpArg = "dtype=JSON&keyword=艺术&rows=1&page=";

        public HelloWordTask(int i) {
            httpArg = httpArg + i;
        }

        @Override
        protected String doInBackground(String... strings) {
            return request(httpUrl, httpArg);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("0")) {
                words = getResources().getString(R.string.helloword);
                words_name = getResources().getString(R.string.helloword_name);
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    int error = Integer.parseInt(jsonObject.get("error_code").toString());
                    if (error == 0) {
                        words = jsonArray.getJSONObject(0).get("famous_saying").toString();
                        words_name = jsonArray.getJSONObject(0).get("famous_name").toString();
                    } else {
                        words = getResources().getString(R.string.helloword);
                        words_name = getResources().getString(R.string.helloword_name);
                    }
                } catch (JSONException e) {
                    words = getResources().getString(R.string.helloword);
                    words_name = getResources().getString(R.string.helloword_name);
                    e.printStackTrace();
                }
            }
            mHandler.sendEmptyMessage(0);
        }
    }

    /**
     * @param httpUrl :请求接口
     * @param httpArg :参数
     * @return 返回结果
     */
    public static String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey", "5c0deae6d5ff542d7ab271ecf14c0a5a");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
        Log.e("result", result);
        return result;
    }

    private void goHome() {
        Intent intent = new Intent(Welcome.this, MainActivity.class);
        Welcome.this.startActivity(intent);
        Welcome.this.finish();
        Welcome.this.overridePendingTransition(R.anim.activity_translate_right_in, R.anim.activity_translate_right_out);
    }

    // private void goGuide() {
    // Intent intent = new Intent(Welcome.this, GuidanceActivity.class);
    // Welcome.this.startActivity(intent);
    // Welcome.this.finish();
    // Welcome.this.overridePendingTransition(R.anim.push_left_in,
    // R.anim.push_left_out);
    // }

    @Override
    protected void onStop() {
        // 如果不调用此方法，则按home键的时候会出现图标无法显示的情况
//        SpotManager.getInstance(getApplicationContext()).onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return !(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) && super.onKeyDown(keyCode, event);
    }
}