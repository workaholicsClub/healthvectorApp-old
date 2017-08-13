package ru.android.childdiary.utils.ui;

import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.webkit.WebView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import ru.android.childdiary.R;

public class FormatTextHelper {
    private static final String PARAGRAPH_FORMAT_JUSTIFY = "<p class=\"tab\" style=\"text-align:justify\">%s</p>";
    private static final String PARAGRAPH_FORMAT_CENTER = "<p style=\"text-align:center\">%s</p>";
    private static final String PARAGRAPH_FORMAT_LEFT = "<p class=\"tab\" style=\"text-align:left\">%s</p>";

    private static final String BASE_URL = "file:///android_asset/";
    private static final String APP_FONT_FAMILY = "CevFontFamily";

    /**
     * Размер текста описания занятия в пикселях. Подбирается опытным путем.
     * Должен совпадать с R.dimen.text_size_base.
     */
    private static final int PRIMARY_TEXT_SIZE = 16;

    private final String justifiedTextFormat;

    private FormatTextHelper(Context context) {
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
        FormatTextHelper helper = new FormatTextHelper(webView.getContext());
        text = helper.map(text);
        if (text == null) {
            return;
        }
        webView.loadDataWithBaseURL(BASE_URL, text, "text/html", "utf-8", "about:blank");
    }

    public static String getParagraphsWithJustifyAlignment(Context context, @ArrayRes int stringArrayId) {
        return getParagraphs(context, stringArrayId, FormatTextHelper::getParagraphWithJustifyAlignment);
    }

    public static String getParagraphsWithCenterAlignment(Context context, @ArrayRes int stringArrayId) {
        return getParagraphs(context, stringArrayId, FormatTextHelper::getParagraphWithCenterAlignment);
    }

    public static String getParagraphsWithLeftAlignment(Context context, @ArrayRes int stringArrayId) {
        return getParagraphs(context, stringArrayId, FormatTextHelper::getParagraphWithLeftAlignment);
    }

    private static String getParagraphs(Context context,
                                        @ArrayRes int stringArrayId,
                                        @NonNull Function<String, String> mapper) {
        List<String> strings = Observable.fromArray(context.getResources().getStringArray(stringArrayId))
                .map(mapper)
                .toList()
                .blockingGet();
        return TextUtils.join("", strings);
    }

    public static String getParagraphWithJustifyAlignment(Context context, @StringRes int stringId) {
        return getParagraphWithJustifyAlignment(context.getString(stringId));
    }

    public static String getParagraphWithJustifyAlignment(String text) {
        return String.format(PARAGRAPH_FORMAT_JUSTIFY, text);
    }

    public static String getParagraphWithCenterAlignment(Context context, @StringRes int stringId) {
        return getParagraphWithCenterAlignment(context.getString(stringId));
    }

    public static String getParagraphWithCenterAlignment(String text) {
        return String.format(PARAGRAPH_FORMAT_CENTER, text);
    }

    public static String getParagraphWithLeftAlignment(Context context, @StringRes int stringId) {
        return getParagraphWithLeftAlignment(context.getString(stringId));
    }

    public static String getParagraphWithLeftAlignment(String text) {
        return String.format(PARAGRAPH_FORMAT_LEFT, text);
    }

    @Nullable
    private String map(@Nullable String text) {
        if (text == null) {
            return null;
        }
        text = String.format(justifiedTextFormat, text);
        return text;
    }
}
