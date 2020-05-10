package com.m.sofiane.go4lunch.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.m.sofiane.go4lunch.R;

/**
 * created by Sofiane M. 2020-04-06
 */
public class webviewactivity extends AppCompatActivity {

    String mUrlSite;
    WebView simpleWebView;

    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        mUrlSite = getIntent().getStringExtra("Web");

        simpleWebView = findViewById(R.id.webView);
        simpleWebView.getSettings().setJavaScriptEnabled(true);
        simpleWebView.loadUrl(mUrlSite); // load a web page in a web view


    }
}