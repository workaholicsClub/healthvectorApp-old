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
            "<ИД>257</ИД>\n" +
            "<Код>metod-glenna-domana</Код>\n" +
            "<Наименование>Метод Гленна Домана</Наименование>\n" +
            "<Краткое_описание>\n" +
            "<![CDATA[\n" +
            "<p class=\"tab\" style=\"text-align:justify;\">\n" +
            "Этот метод называют: «слуховое обучение», «слуховое возбуждение», или «слуховая терапия». Неоднократные аудиотренировки с использованием специальных устройств, стимулирующих функцию головного мозга, развивают его способность к обработке сенсорной информации. Более подробно с методикой можно ознакомиться по " +
            "<a href=\"https://healthvector.ru/helpful_info/programms/tomatis-slukhovoe-obuchenie/\">ссылке</a>\n" +
            "</p>\n" +
            "]]>\n" +
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
