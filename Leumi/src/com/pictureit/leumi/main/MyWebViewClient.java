package com.pictureit.leumi.main;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

class MyWebViewClient extends WebViewClient {
    @Override

    // show the web page in webview but not in web browser
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
    }

}
