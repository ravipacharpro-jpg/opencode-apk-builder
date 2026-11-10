package android.webkit;

import android.webkit.WebView;
import android.webkit.WebResourceResponse;
import android.net.http.SslError;

public class WebViewClient {
    public boolean shouldOverrideUrlLoading(WebView view, String url) { return false; }
    public void onPageFinished(WebView view, String url) {}
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {}
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {}
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) { return null; }
}
