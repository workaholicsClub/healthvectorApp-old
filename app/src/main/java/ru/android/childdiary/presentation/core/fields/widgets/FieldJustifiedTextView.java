package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Setter;
import ru.android.childdiary.R;
import ru.android.childdiary.utils.HtmlUtils;
import ru.android.childdiary.utils.ui.JustifiedTextHelper;

public class FieldJustifiedTextView extends LinearLayout {
    private final Logger logger = LoggerFactory.getLogger(toString());

    @BindView(R.id.webView)
    WebView webView;

    @Nullable
    @Setter
    private HtmlUtils.OnLinkClickListener onLinkClickListener;

    private final WebViewClient webViewClient = new WebViewClient() {
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (onLinkClickListener != null) {
                onLinkClickListener.onLinkClick(url);
            }
            return true;
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView view,
                                    int errorCode, String description, String failingUrl) {
            logger.error("onReceivedError: errorCode = " + errorCode
                    + "; description = " + description
                    + "; failingUrl = " + failingUrl);
        }
    };

    public FieldJustifiedTextView(Context context) {
        super(context);
        init();
    }

    public FieldJustifiedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FieldJustifiedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.field_justified_text, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        webView.getSettings().setJavaScriptEnabled(false);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        WebChromeClient client = new WebChromeClient();
        webView.setWebChromeClient(client);

        webView.setWebViewClient(webViewClient);
    }

    public void setText(String text) {
        JustifiedTextHelper.showInWebView(webView, text);
    }
}
