package ru.android.childdiary.utils;

import android.support.annotation.Nullable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

public class HtmlUtils {
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            //noinspection deprecation
            result = Html.fromHtml(html);
        }
        return result;
    }

    public static void setupClickableLinks(TextView textView,
                                           String html,
                                           @Nullable OnLinkClickListener onLinkClickListener) {
        CharSequence text = fromHtml(html);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        URLSpan[] urlSpans = spannableStringBuilder.getSpans(0, text.length(), URLSpan.class);
        for (URLSpan urlSpan : urlSpans) {
            makeLinkClickable(spannableStringBuilder, urlSpan, onLinkClickListener);
        }
        textView.setText(spannableStringBuilder);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private static void makeLinkClickable(SpannableStringBuilder spannableStringBuilder,
                                          URLSpan urlSpan,
                                          @Nullable OnLinkClickListener onLinkClickListener) {
        int start = spannableStringBuilder.getSpanStart(urlSpan);
        int end = spannableStringBuilder.getSpanEnd(urlSpan);
        int flags = spannableStringBuilder.getSpanFlags(urlSpan);
        ClickableSpan clickable = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                if (onLinkClickListener != null) {
                    onLinkClickListener.onLinkClick(urlSpan.getURL());
                }
            }
        };
        spannableStringBuilder.setSpan(clickable, start, end, flags);
        spannableStringBuilder.removeSpan(urlSpan);
    }

    public interface OnLinkClickListener {
        void onLinkClick(String url);
    }
}
