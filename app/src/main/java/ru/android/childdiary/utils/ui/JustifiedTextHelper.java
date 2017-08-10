package ru.android.childdiary.utils.ui;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.webkit.WebView;

import ru.android.childdiary.R;

public class JustifiedTextHelper {
    public static final String PARAGRAPH_FORMAT_JUSTIFY = "<p class=\"tab\" style=\"text-align:justify\">%s</p>";
    public static final String PARAGRAPH_FORMAT_CENTER = "<p class=\"tab\" style=\"text-align:center\">%s</p>";
    public static final String PARAGRAPH_FORMAT_LEFT = "<p class=\"tab\" style=\"text-align:left\">%s</p>";

    private static final String BASE_URL = "file:///android_asset/";
    private static final String APP_FONT_FAMILY = "CevFontFamily";

    /**
     * Размер текста описания занятия в пикселях. Подбирается опытным путем.
     * Должен совпадать с R.dimen.text_size_base.
     */
    private static final int PRIMARY_TEXT_SIZE = 16;

    private final String justifiedTextFormat;

    private JustifiedTextHelper(Context context) {
        int indent = context.getResources().getDimensionPixelSize(R.dimen.base_margin);
        @ColorInt int color = ContextCompat.getColor(context, R.color.primary_text);
        String primaryTextColor = String.format("#%08X", color);
        String primaryTextFont = context.getString(R.string.font_path_regular);
        justifiedTextFormat = "<html>\n" +
                "<style type=\"text/css\">\n" +
                "@font-face {\n" +
                "    font-family: " + APP_FONT_FAMILY + ";\n" +
                "    src: url(\"" + BASE_URL + primaryTextFont + "\")\n" +
                "}\n" +
                "body {\n" +
                "    font-family: " + APP_FONT_FAMILY + ";\n" +
                "    font-color: " + primaryTextColor + ";\n" +
                "    font-size: " + PRIMARY_TEXT_SIZE + "px;\n" +
                "}\n" +
                ".tab { text-indent: " + indent + "px; }\n" +
                "</style>\n" +
                "<body>\n" +
                "%s\n" +
                "</body>\n" +
                "</html>\n";
    }

    public static void showInWebView(WebView webView, @Nullable String text) {
        JustifiedTextHelper helper = new JustifiedTextHelper(webView.getContext());
        text = helper.map(text);
        if (text == null) {
            return;
        }
        webView.loadDataWithBaseURL(BASE_URL, text, "text/html", "utf-8", "about:blank");
    }

    @Nullable
    private String map(@Nullable String text) {
        if (text == null) {
            return null;
        }
        boolean containsHtml = text.contains("</p>");
        if (!containsHtml) {
            text = String.format(PARAGRAPH_FORMAT_JUSTIFY, text);
        }
        text = String.format(justifiedTextFormat, text);
        return text;
    }
}
