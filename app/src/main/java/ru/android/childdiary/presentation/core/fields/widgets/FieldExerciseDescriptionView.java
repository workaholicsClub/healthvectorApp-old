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

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import lombok.Setter;
import ru.android.childdiary.R;
import ru.android.childdiary.utils.HtmlUtils;

public class FieldExerciseDescriptionView extends FieldTextViewWithImageView {
    private static final String BASE_URL = "file:///android_asset/";
    private static final String APP_FONT_FAMILY = "CevFontFamily";
    private final static int PRIMARY_TEXT_SIZE = 16;

    @BindView(R.id.webView)
    WebView webView;

    @BindDimen(R.dimen.base_margin)
    int INDENT;

    @BindColor(R.color.primary_text)
    int PRIMARY_TEXT_COLOR;

    @BindString(R.string.font_path_regular)
    String PRIMARY_TEXT_FONT;

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
        String rgbColorString = String.format("#%08X", PRIMARY_TEXT_COLOR);
        text = "<p class=\"tab\" style=\"text-align:justify\">\n" +
                "Для каждого ребенка, проходящего реабилитацию по методу Домана, разрабатывается комплексная индивидуальная программа, в основе которой лежит копирование: взрослые выполняют движения руками, ногами, головой ребенка так, как он мог бы делать сам, будучи здоров. Остальное время ребенок проводит на полу лицом вниз под присмотром взрослых, учась ползать и двигаться: любая двигательная активность крайне положительно влияет на развитие мозга. Метод Гленна Домана также использует специальные карточки для обучения здоровых детей чтению и математике в раннем возрасте. Детям с ДЦП будет полезен для знакомства с окружающим миром. Система обучения основана на интересе ребенка и исключает скуку и принуждение. Более подробно с методикой можно ознакомиться по " +
                "<a href=\"https://healthvector.ru/helpful_info/programms/metod-glenna-domana/\">ссылке</a>\n" +
                "</p>";
        text = String.format("<html>\n" +
                "<style type=\"text/css\">\n" +
                "@font-face {\n" +
                "    font-family: " + APP_FONT_FAMILY + ";\n" +
                "    src: url(\"" + BASE_URL + PRIMARY_TEXT_FONT + "\")\n" +
                "}\n" +
                "body {\n" +
                "    font-family: " + APP_FONT_FAMILY + ";\n" +
                "    font-color: " + rgbColorString + ";\n" +
                "    font-size: " + PRIMARY_TEXT_SIZE + "px;\n" +
                "}\n" +
                ".tab { text-indent: " + INDENT + "px; }\n" +
                "</style>\n" +
                "<body>\n" +
                "%s\n" +
                "</body>\n" +
                "</html>\n", text);
        webView.loadDataWithBaseURL(BASE_URL,
                text,
                "text/html", "utf-8", "about:blank");
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
