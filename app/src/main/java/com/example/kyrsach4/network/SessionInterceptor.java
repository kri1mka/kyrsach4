package com.example.kyrsach4.network;

import com.example.kyrsach4.network.SessionStorage;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.Request;

public class SessionInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request.Builder requestBuilder = chain.request().newBuilder();

        // добавляем Cookie если есть
        if (SessionStorage.sessionId != null) {
            requestBuilder.addHeader("Cookie", "JSESSIONID=" + SessionStorage.sessionId);
        }

        Response response = chain.proceed(requestBuilder.build());

        // читаем Set-Cookie и сохраняем JSESSIONID
        for (String header : response.headers("Set-Cookie")) {
            if (header.startsWith("JSESSIONID")) {
                String id = header.substring(header.indexOf("=") + 1, header.indexOf(";"));
                SessionStorage.sessionId = id;
            }
        }

        return response;
    }
}
