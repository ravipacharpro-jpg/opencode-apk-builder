package android.webkit;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class WebView extends View {
    public WebView(Context context) { super(context); }
    public WebView(Context context, AttributeSet attrs) { super(context); }
    public WebSettings getSettings() { return new WebSettings(); }
    public void setWebViewClient(WebViewClient client) {}
    public void setWebChromeClient(WebChromeClient client) {}
    public void addJavascriptInterface(Object object, String name) {}
    public void loadUrl(String url) {}
    public void post(Runnable r) { r.run(); }
}
