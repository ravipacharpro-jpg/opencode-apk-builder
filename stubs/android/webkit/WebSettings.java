package android.webkit;

import android.content.Context;
import android.webkit.WebView;

public class WebSettings {
    public static final int MIXED_CONTENT_ALWAYS_ALLOW = 0;
    public void setJavaScriptEnabled(boolean flag) {}
    public void setDomStorageEnabled(boolean flag) {}
    public void setAllowFileAccessFromFileURLs(boolean allow) {}
    public void setAllowUniversalAccessFromFileURLs(boolean allow) {}
    public void setMixedContentMode(int mode) {}
    public void setUserAgentString(String ua) {}
}
