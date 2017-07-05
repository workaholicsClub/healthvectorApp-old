package ru.android.childdiary.utils.ui;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.webkit.WebView;

import javax.inject.Inject;

import ru.android.childdiary.R;
import ru.android.childdiary.data.repositories.core.mappers.Mapper;
import ru.android.childdiary.domain.interactors.exercises.Exercise;

public class ExerciseDescriptionHelper implements Mapper<Exercise, String> {
    private static final String BASE_URL = "file:///android_asset/";
    private static final String APP_FONT_FAMILY = "CevFontFamily";

    /**
     * Размер текста описания занятия в пикселях. Подбирается опытным путем.
     * Должен совпадать с размером текста названия занятия.
     */
    private static final int PRIMARY_TEXT_SIZE = 16;

    private final String format;

    @Inject
    public ExerciseDescriptionHelper(Context context) {
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

    @Override
    public String map(@NonNull Exercise exercise) {
        String text;
        text = "<p class=\"tab\" style=\"text-align:justify\">\n" +
                "Для каждого ребенка, проходящего реабилитацию по методу Домана, разрабатывается комплексная индивидуальная программа, в основе которой лежит копирование: взрослые выполняют движения руками, ногами, головой ребенка так, как он мог бы делать сам, будучи здоров. Остальное время ребенок проводит на полу лицом вниз под присмотром взрослых, учась ползать и двигаться: любая двигательная активность крайне положительно влияет на развитие мозга. Метод Гленна Домана также использует специальные карточки для обучения здоровых детей чтению и математике в раннем возрасте. Детям с ДЦП будет полезен для знакомства с окружающим миром. Система обучения основана на интересе ребенка и исключает скуку и принуждение. Более подробно с методикой можно ознакомиться по " +
                "<a href=\"https://healthvector.ru/helpful_info/programms/metod-glenna-domana/\">ссылке</a>\n" +
                "</p>";
        // TODO: text = exercise.getDescription();
        text = String.format(format, text);
        return text;
    }

    public static void showInWebView(WebView webView, String text) {
        webView.loadDataWithBaseURL(BASE_URL, text, "text/html", "utf-8", "about:blank");
    }
}
