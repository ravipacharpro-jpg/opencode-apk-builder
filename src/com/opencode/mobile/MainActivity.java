// ============================================================
// OpenCode Mobile — Apple Design (Fixed)
// ============================================================

package com.opencode.mobile;

import android.os.Bundle;
import android.webkit.*;
import android.content.*;
import android.content.pm.*;
import android.net.*;
import android.util.*;
import android.view.*;
import android.view.Gravity;
import android.widget.*;
import androidx.appcompat.app.*;
import java.io.*;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private ServerManager serverManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        setupWebView();
        setContentView(webView);

        serverManager = new ServerManager(this, webView);

        if (!Prefs.getBoolean(this, Prefs.KEY_ONBOARDED, false)) {
            showOnboarding();
        } else {
            serverManager.start();
        }
    }

    private void setupWebView() {
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setAllowFileAccessFromFileURLs(false);
        ws.setAllowUniversalAccessFromFileURLs(false);
        ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        ws.setUserAgentString("OpenCode Mobile/1.0 (Apple Design)");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("opencode://")) {
                    handleOpencodeUrl(url);
                    return true;
                }
                return false;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e("OpenCodeMobile", "WebView error: " + description);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });

        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new OpencodeBridge(this), "opencodeNative");
    }

    private void showOnboarding() {
        try {
            Intent intent = new Intent(this, OnboardingActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Log.e("OpenCodeMobile", "Onboarding failed", e);
        }
    }

    private void handleOpencodeUrl(String url) {
        try {
            android.net.Uri uri = android.net.Uri.parse(url);
            String path = uri.getPath();
            if ("/api-key".equals(path)) {
                String provider = uri.getQueryParameter("provider");
                String key = uri.getQueryParameter("key");
                Prefs.setString(this, "api_key_" + provider, key);
                Prefs.setBoolean(this, Prefs.KEY_ONBOARDED, true);
                serverManager.start();
                webView.loadUrl("file:///android_asset/www/index.html");
            }
        } catch (Exception e) {
            Log.e("OpenCodeMobile", "URL handler failed", e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (serverManager != null) serverManager.stop();
    }

    }

// ============================================================
// OpencodeBridge — JS <-> Native bridge
// ============================================================

class OpencodeBridge {
    private Context ctx;
    OpencodeBridge(Context c) { ctx = c; }

    @android.webkit.JavascriptInterface
    public String getApiKey(String provider) {
        return Prefs.getString(ctx, "api_key_" + provider, "");
    }

    @android.webkit.JavascriptInterface
    public String getServerUrl() {
        return Prefs.getString(ctx, Prefs.KEY_SERVER_URL, "");
    }

    @android.webkit.JavascriptInterface
    public void log(String msg) {
        android.util.Log.d("OpenCodeMobile", "JS: " + msg);
    }

    @android.webkit.JavascriptInterface
    public void openSettings() {
        try {
            Intent i = new Intent(ctx, SettingsActivity.class);
            ctx.startActivity(i);
        } catch (Exception e) {
            android.util.Log.e("OpenCodeMobile", "Settings open failed", e);
        }
    }

    @android.webkit.JavascriptInterface
    public void adbPair() {
        try {
            Intent i = new Intent(ctx, AdbPairActivity.class);
            ctx.startActivity(i);
        } catch (Exception e) {
            android.util.Log.e("OpenCodeMobile", "ADB pair open failed", e);
        }
    }
}

// ============================================================
// ServerManager — spawns opencode via musl loader (with error handling)
// ============================================================

class ServerManager {
    private Context ctx;
    private WebView webView;
    private Process proc;
    private int port = 49374;
    private String password;

    ServerManager(Context c, WebView wv) { ctx = c; webView = wv; }

    void start() {
        stop();
        password = Prefs.getString(ctx, Prefs.KEY_SERVER_PASSWORD, "");
        if (password.isEmpty()) {
            password = java.util.UUID.randomUUID().toString().substring(0, 12);
            Prefs.setString(ctx, Prefs.KEY_SERVER_PASSWORD, password);
        }

        new Thread(() -> {
            try {
                String appDir = ctx.getApplicationInfo().dataDir;
                String libDir = appDir + "/lib/arm64";
                String loader = libDir + "/libldmusl.so";
                String bin = libDir + "/libopencode.so";

                // Verify libs exist
                java.io.File loaderFile = new java.io.File(loader);
                java.io.File binFile = new java.io.File(bin);
                if (!loaderFile.exists() || !binFile.exists()) {
                    android.util.Log.e("OpenCodeMobile", "Native libs not found at " + libDir);
                    return;
                }

                String[] env = {
                    "LD_LIBRARY_PATH=" + libDir,
                    "HOME=" + appDir,
                    "OPENCODE_PASSWORD=" + password,
                    "OPENCODE_PORT=" + port,
                    "OPENCODE_HOSTNAME=127.0.0.1",
                    "PATH=" + libDir + ":/system/bin",
                    "TERM=xterm-256color"
                };

                ProcessBuilder pb = new ProcessBuilder(
                    loader, bin, "serve", "--hostname", "127.0.0.1", "--port", String.valueOf(port)
                );
                pb.environment().clear();
                for (String e : env) {
                    String[] kv = e.split("=", 2);
                    pb.environment().put(kv[0], kv[1]);
                }
                pb.directory(new java.io.File(appDir));
                pb.redirectErrorStream(true);

                proc = pb.start();

                boolean ready = waitForServer(30);
                if (ready && webView != null) {
                    webView.post(() -> {
                        String url = "http://127.0.0.1:" + port + "?password=" + password;
                        webView.loadUrl(url);
                    });
                } else {
                    android.util.Log.e("OpenCodeMobile", "Server not ready");
                }
            } catch (Exception e) {
                android.util.Log.e("OpenCodeMobile", "Server start failed", e);
            }
        }).start();
    }

    private boolean waitForServer(int seconds) {
        long end = System.currentTimeMillis() + seconds * 1000;
        while (System.currentTimeMillis() < end) {
            try {
                java.net.Socket s = new java.net.Socket("127.0.0.1", port);
                s.close();
                return true;
            } catch (Exception ignored) {
                try { Thread.sleep(200); } catch (InterruptedException ie) { return false; }
            }
        }
        return false;
    }

    void stop() {
        if (proc != null) {
            try { proc.destroyForcibly(); } catch (Exception ignored) {}
            proc = null;
        }
    }
}

// ============================================================
// Prefs — SimplePreferences wrapper
// ============================================================

class Prefs {
    static final String KEY_ONBOARDED = "onboarded";
    static final String KEY_SERVER_URL = "server_url";
    static final String KEY_SERVER_PASSWORD = "server_password";

    static boolean getBoolean(Context c, String k, boolean d) {
        return c.getSharedPreferences("opencode", Context.MODE_PRIVATE).getBoolean(k, d);
    }
    static void setBoolean(Context c, String k, boolean v) {
        c.getSharedPreferences("opencode", Context.MODE_PRIVATE).edit().putBoolean(k, v).apply();
    }
    static String getString(Context c, String k, String d) {
        return c.getSharedPreferences("opencode", Context.MODE_PRIVATE).getString(k, d);
    }
    static void setString(Context c, String k, String v) {
        c.getSharedPreferences("opencode", Context.MODE_PRIVATE).edit().putString(k, v).apply();
    }
}
