package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import butterknife.BindView;
import lombok.Setter;
import ru.android.childdiary.R;
import ru.android.childdiary.utils.HtmlUtils;
import ru.android.childdiary.utils.ui.JustifiedTextHelper;

public class FieldExerciseDescriptionView extends FieldTextViewWithImageView {
    @BindView(R.id.webView)
    WebView webView;

    private final Logger logger = LoggerFactory.getLogger(toString());

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

    @Nullable
    @Setter
    private HtmlUtils.OnLinkClickListener onLinkClickListener;

    public FieldExerciseDescriptionView(Context context) {
        super(context);
    }

    public FieldExerciseDescriptionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldExerciseDescriptionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        webView.getSettings().setJavaScriptEnabled(false);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        WebChromeClient client = new WebChromeClient();
        webView.setWebChromeClient(client);

        webView.setWebViewClient(webViewClient);
    }

    @Override
    public void setText(String text) {
        JustifiedTextHelper.showInWebView(webView, text);
    }

    @LayoutRes
    @Override
    protected int getLayoutResourceId() {
        return R.layout.field_exercise_description;
    }

    @Override
    protected int getIconResId() {
        return R.drawable.ic_exercise_description;
    }
}
