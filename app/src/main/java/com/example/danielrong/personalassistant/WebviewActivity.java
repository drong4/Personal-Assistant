package com.example.danielrong.personalassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/* Activity to be instantiated when user uses the websearch command.
 * This activity holds a webview which will open up a google search with the
  * search query the user provided. */
public class WebviewActivity extends AppCompatActivity {
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        query = getIntent().getStringExtra("QUERY");
        String url = getString(R.string.google_search_root) + query;
        WebView webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }
}
