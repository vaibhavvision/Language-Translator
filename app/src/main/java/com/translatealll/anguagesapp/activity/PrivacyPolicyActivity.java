package com.translatealll.anguagesapp.activity;

import android.os.Build;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.translatealll.anguagesapp.R;

public class PrivacyPolicyActivity extends AppCompatActivity {


    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        findViewById(R.id.ivBackArrow).setOnClickListener(view -> {
            finish();
        });

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyBrowser());
        webView.loadUrl("https://ghanshyamprivacypolicy.blogspot.com/2021/10/privacy-policy-effective-year-2020.html");
    }

    class MyBrowser extends WebViewClient {

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}