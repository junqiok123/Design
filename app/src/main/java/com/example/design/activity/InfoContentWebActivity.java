package com.example.design.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;

import com.example.design.R;
import com.example.design.view.ProgressWebView;

public class InfoContentWebActivity extends BaseActivity {

    private ProgressWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_content_web);
        webView = (ProgressWebView) findViewById(R.id.web);
        String url = getIntent().getStringExtra("url");
        WebSettings ws = webView.getSettings();
        ws.setUseWideViewPort(true);
        ws.setLoadWithOverviewMode(true);
        ws.setJavaScriptEnabled(true);
        ws.setBuiltInZoomControls(true);
        ws.setSupportZoom(true);
        ws.setUseWideViewPort(true);
        ws.setDisplayZoomControls(false);
        webView.loadUrl(url);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack())
            webView.goBack();
        else
            super.onBackPressed();
    }
}
