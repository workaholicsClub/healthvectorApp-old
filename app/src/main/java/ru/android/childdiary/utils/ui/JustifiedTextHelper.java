package ru.android.childdiary.utils.ui;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.webkit.WebView;

import javax.inject.Inject;

import ru.android.childdiary.R;

public class JustifiedTextHelper {
    private static final String BASE_URL = "file:///android_asset/";
    private static final String APP_FONT_FAMILY = "CevFontFamily";

    /**
     * Размер текста описания занятия в пикселях. Подбирается опытным путем.
     * Должен совпадать с R.dimen.text_size_base.
     */
    private static final int PRIMARY_TEXT_SIZE = 16;

    private final String format;

    @Inject
    public JustifiedTextHelper(Context context) {
        int indent = context.getResources().getDimensionPixelSize(R.dimen.base_margin);
        @ColorInt int color = ContextCompat.getColor(context, R.color.primary_text);
        String primaryTextColor = String.format("#%08X", color);
        String primaryTextFont = context.getString(R.string.font_path_regular);
        format = "<html>\n" +
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

    @Nullable
    public String map(@Nullable String text) {
        if (text == null) {
            return null;
        }
        // TODO: it's temporary... На сервере должны обновить описание занятий в соответствии с шаблоном
        boolean containsHtml = text.contains("</p>");
        if (!containsHtml) {
            text = "<p class=\"tab\" style=\"text-align:justify\">\n" + text + "</p>";
        }
        text = String.format(format, text);
        return text;
    }

    public static void showInWebView(WebView webView, String text) {
        webView.loadDataWithBaseURL(BASE_URL, text, "text/html", "utf-8", "about:blank");
    }
}
