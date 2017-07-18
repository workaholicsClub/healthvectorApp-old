package ru.android.childdiary.presentation.exercises;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;

public class WebBrowserActivity extends BaseMvpActivity {
    @BindView(R.id.webView)
    WebView webView;

    private final WebViewClient webViewClient = new WebViewClient() {
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (URLUtil.isNetworkUrl(url)) {
                return false;
            }

            // otherwise allow the OS to handle it
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
            return true;
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView view,
                                    int errorCode, String description, String failingUrl) {
            logger.debug("onReceivedError: errorCode = " + errorCode
                    + "; description = " + description
                    + "; failingUrl = " + failingUrl);
            String keyName = "intent:";
            if (failingUrl.startsWith(keyName)) {
                String uri = failingUrl.substring(keyName.length());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                    webView.goBack();
                }
            }
        }
    };

    private String title, url;

    public static Intent getIntent(Context context, @NonNull String title, @NonNull String url,
                                   @Nullable Sex sex) {
        return new Intent(context, WebBrowserActivity.class)
                .putExtra(ExtraConstants.EXTRA_TITLE, title)
                .putExtra(ExtraConstants.EXTRA_URL, url)
                .putExtra(ExtraConstants.EXTRA_SEX, sex);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        title = getIntent().getStringExtra(ExtraConstants.EXTRA_TITLE);
        url = getIntent().getStringExtra(ExtraConstants.EXTRA_URL);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_browser);

        webView.clearFormData();
        webView.clearHistory();
        webView.clearCache(true);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        WebChromeClient client = new WebChromeClient();
        webView.setWebChromeClient(client);

        webView.setWebViewClient(webViewClient);

        if (savedInstanceState == null) {
            logger.debug("open url: " + url);
            webView.loadUrl(url);
        }
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarTitle(title);
        toolbar.setNavigationIcon(R.drawable.toolbar_action_back);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.toolbar_action_overflow));
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        webView.restoreState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onDestroy() {
        webView.clearView();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_open_in_web_browser:
                startBrowser(this, url);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startBrowser(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            logger.debug("starting web browser");
            startActivity(intent);
        } else {
            logger.error("not found app to open intent: " + intent);
            showToast(getString(R.string.browser_not_available));
        }
    }
}
