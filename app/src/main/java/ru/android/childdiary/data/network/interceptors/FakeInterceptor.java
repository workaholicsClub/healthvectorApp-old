package ru.android.childdiary.data.network.interceptors;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class FakeInterceptor implements Interceptor {
    private static final String CONTENT_TYPE = "text/xml;charset=UTF-8";
    private static final String CONTENT = "<Программы>\n" +
            "<Программа lang=\"ru\">\n" +
            "<ИД>1</ИД>\n" +
            "<Код>code1</Код>\n" +
            "<Наименование>name 1</Наименование>\n" +
            "<Краткое_описание>\n" +
            "<![CDATA[\n" +
            "<p class=\"tab\" style=\"text-align:justify;\">\n" +
            "description 1" +
            "</p>\n" +
            "]]>\n" +
            "</Краткое_описание>\n" +
            "</Программа>\n" +

            "<Программа lang=\"ru\">\n" +
            "<ИД>1</ИД>\n" +
            "<Код>code2</Код>\n" +
            "<Наименование>n 1</Наименование>\n" +
            "<Краткое_описание>\n" +
            "<![CDATA[\n" +
            "<p class=\"tab\" style=\"text-align:justify;\">\n" +
            "d 1" +
            "</p>\n" +
            "]]>\n" +
            "</Краткое_описание>\n" +
            "</Программа>\n" +

            "<Программа lang=\"en\">\n" +
            "<ИД>1</ИД>\n" +
            "<Наименование>name 1 english</Наименование>\n" +
            "<Краткое_описание>\n" +
            "<![CDATA[\n" +
            "<p class=\"tab\" style=\"text-align:justify;\">\n" +
            "description 1 english" +
            "</p>\n" +
            "]]>\n" +
            "</Краткое_описание>\n" +
            "</Программа>\n" +

            "<Программа lang=\"en\">\n" +
            "<ИД>2</ИД>\n" +
            "<Наименование>name 2 english</Наименование>\n" +
            "<Краткое_описание>\n" +
            "<![CDATA[\n" +
            "<p class=\"tab\" style=\"text-align:justify;\">\n" +
            "description 2 english" +
            "</p>\n" +
            "]]>\n" +
            "</Краткое_описание>\n" +
            "</Программа>\n" +

            "<Программа lang=\"en\">\n" +
            "<ИД>2</ИД>\n" +
            "<Наименование>n 2 english</Наименование>\n" +
            "<Краткое_описание>\n" +
            "<![CDATA[\n" +
            "<p class=\"tab\" style=\"text-align:justify;\">\n" +
            "d 2 english" +
            "</p>\n" +
            "]]>\n" +
            "</Краткое_описание>\n" +
            "</Программа>\n" +

            "<Программа lang=\"ru\">\n" +
            "<ИД>3</ИД>\n" +
            "<Код>code3</Код>\n" +
            "<Наименование>name 3</Наименование>\n" +
            "<Краткое_описание>\n" +
            "<![CDATA[\n" +
            "<p class=\"tab\" style=\"text-align:justify;\">\n" +
            "description 3" +
            "</p>\n" +
            "]]>\n" +
            "</Краткое_описание>\n" +
            "</Программа>\n" +

            "<Программа lang=\"ru\">\n" +
            "<ИД>4</ИД>\n" +
            "<Код>code4</Код>\n" +
            "<Наименование>name 4</Наименование>\n" +
            "<Краткое_описание>\n" +
            "<![CDATA[\n" +
            "<p class=\"tab\" style=\"text-align:justify;\">\n" +
            "description 4" +
            "</p>\n" +
            "]]>\n" +
            "</Краткое_описание>\n" +
            "</Программа>\n" +

            "<Программа lang=\"en\">\n" +
            "<ИД>4</ИД>\n" +
            "<Код>code5</Код>\n" +
            "<Наименование>name 4 english</Наименование>\n" +
            "<Краткое_описание>\n" +
            "<![CDATA[\n" +
            "<p class=\"tab\" style=\"text-align:justify;\">\n" +
            "description 4 english" +
            "</p>\n" +
            "]]>\n" +
            "</Краткое_описание>\n" +
            "</Программа>\n" +

            "<Программа lang=\"en\">\n" +
            "<ИД>10</ИД>\n" +
            "<Код>code10</Код>\n" +
            "<Наименование>name 10 english</Наименование>\n" +
            "<Краткое_описание>\n" +
            "<![CDATA[<p class=\"tab\" style=\"text-align:justify;\">Термином «Биологическая обратная связь» (БОС), или «Функциональное биоуправление» (ФБУ), называют метод обучения самоконтролю и саморегуляции функциональных систем организма. Это достигается путем подачи пациенту информации о текущем состоянии функции, которую контролируют по каналам внешней обратной связи (акустической, визуальной и/или тактильной). Более подробно с методикой можно ознакомиться по <a href=\"https://healthvector.ru/treatment/programms/biologicheskaya-obratnaya-svyaz/\">ссылке</a>.</p>]]>\n" +
            "</Краткое_описание>\n" +
            "</Программа>\n" +

            "</Программы>";

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        if (response.code() == 200) {
            MediaType contentType = MediaType.parse(CONTENT_TYPE);
            ResponseBody body = ResponseBody.create(contentType, CONTENT);
            return response.newBuilder().body(body).build();
        }
        return response;
    }
}
