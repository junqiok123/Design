package com.example.design.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.design.R;
import com.example.design.control.Constant;
import com.example.design.util.JsonParse;
import com.example.design.view.DotsTextView;
import com.example.design.view.SecretTextView;

import org.json.JSONException;
import org.json.JSONObject;

public class Welcome extends Activity {

    boolean isFirstIn = false;
    private static final int GO_HOME = 1000;
    private static final int GO_GUIDE = 1001;
    private static final long SPLASH_DELAY_MILLIS = 4000;
    private static final String SHAREDPREFERENCES_NAME = "first_pref";

    private DotsTextView dotsTextView;
    private SecretTextView hello_words,loading_text;
    private LinearLayout linearLayout;
    private ImageView img;
    private String strResult,words;

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
                    hello_words.setDuration(2000);
                    hello_words.setIsVisible(true);
                    hello_words.show();

                    mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
//                    dotsTextView.hideAndStop();

                     /*高德定位*/
//                    mLocationManagerProxy = LocationManagerProxy.getInstance(Welcome.this);
//                    //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
//                    //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
//                    //在定位结束后，在合适的生命周期调用destroy()方法
//                    //其中如果间隔时间为-1，则定位只定一次
//                    mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, 60 * 1000, 15, Welcome.this);
//                    mLocationManagerProxy.setGpsEnable(false);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        init();
    }

    private void init() {

        dotsTextView = (DotsTextView) findViewById(R.id.dots);
        dotsTextView.showAndPlay();
        hello_words = (SecretTextView) findViewById(R.id.hello_words);
        loading_text = (SecretTextView) findViewById(R.id.loading_text);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        img = (ImageView) findViewById(R.id.img);

        loading_text.setDuration(2000);
        loading_text.setIsVisible(true);
        loading_text.show();
        dotsTextView.setVisibility(View.VISIBLE);
        mThread();

    }

    private void mThread() {
        new Thread() {
            public void run() {
                try {
                    strResult = new JsonParse().connServerForResult(Constant.HELLO_WORDS,mHandler);
                    words = new JSONObject(strResult).getString("words");
                    Thread.sleep(200);
                } catch (JSONException e) {
                    mHandler.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(0);
            };
        }.start();
    }

//    @Override
//    public void onLocationChanged(AMapLocation aMapLocation) {
//        if(aMapLocation != null && aMapLocation.getAMapException().getErrorCode() == 0){
//            //获取位置信息
//            SimpleLocation.setTime(aMapLocation.getTime()+"");
//            SimpleLocation.setLatitude(aMapLocation.getLatitude());
//            SimpleLocation.setLongitude(aMapLocation.getLongitude());
//            SimpleLocation.setDistrict(aMapLocation.getDistrict());
//            SimpleLocation.setAddr(aMapLocation.getAddress());
//            SimpleLocation.setCity(aMapLocation.getCity());
//
//            SharedPreferences preferences = getSharedPreferences(SHAREDPREFERENCES_NAME, MODE_PRIVATE);
//            // isFirstIn = preferences.getBoolean("isFirstIn", true);
//            // if (!isFirstIn) {
//            mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
//            // } else {
//            // mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
//            // }
//        }
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//
//    }

    private void goHome() {
        Intent intent = new Intent(Welcome.this, SampleActivity.class);
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
    protected void onDestroy() {
//        if (mLocationManagerProxy != null) {
//            mLocationManagerProxy.removeUpdates(this);
//            mLocationManagerProxy.destroy();
//        }
//        mLocationManagerProxy = null;
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}