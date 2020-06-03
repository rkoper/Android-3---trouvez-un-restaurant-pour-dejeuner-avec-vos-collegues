package com.m.sofiane.go4lunch.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.m.sofiane.go4lunch.R;

import butterknife.BindView;

/**
 * created by Sofiane M. 2020-04-06
 */
public class WebViewActivity extends AppCompatActivity {

    String mUrlSite;
    @BindView(R.id.webView)
    WebView simpleWebView;

    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        mUrlSite = getIntent().getStringExtra("Web");
        simpleWebView.getSettings().setJavaScriptEnabled(true);
        simpleWebView.loadUrl(mUrlSite); // load a web page in a web view

    }
}