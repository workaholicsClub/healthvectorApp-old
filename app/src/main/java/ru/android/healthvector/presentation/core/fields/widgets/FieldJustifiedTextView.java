package ru.android.healthvector.presentation.core.fields.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Setter;
import ru.android.healthvector.R;
import ru.android.healthvector.utils.HtmlUtils;
import ru.android.healthvector.utils.ui.FormatTextHelper;

public class FieldJustifiedTextView extends LinearLayout {
    private final Logger logger = LoggerFactory.getLogger(toString());

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.webView)
    WebView webView;

    @BindDimen(R.dimen.base_margin)
    int MARGIN;

    private boolean showIcon;
    private Drawable icon;

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
        init(null);
    }

    public FieldJustifiedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FieldJustifiedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        inflate(getContext(), R.layout.field_justified_text, this);
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.FieldJustifiedTextView, 0, 0);
            try {
                showIcon = ta.getBoolean(R.styleable.FieldJustifiedTextView_fieldShowIcon, false);
                icon = ta.getDrawable(R.styleable.FieldJustifiedTextView_fieldIcon);
            } finally {
                ta.recycle();
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        if (showIcon) {
            imageView.setImageDrawable(icon);
        } else {
            imageView.setVisibility(GONE);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) webView.getLayoutParams();
            layoutParams.leftMargin = MARGIN;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                layoutParams.setMarginStart(MARGIN);
            }
            webView.setLayoutParams(layoutParams);
        }

        webView.getSettings().setJavaScriptEnabled(false);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        WebChromeClient client = new WebChromeClient();
        webView.setWebChromeClient(client);

        webView.setWebViewClient(webViewClient);
    }

    public void setText(String text) {
        FormatTextHelper.showInWebView(webView, text);
    }
}
