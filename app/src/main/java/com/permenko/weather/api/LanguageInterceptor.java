package com.permenko.weather.api;

import android.support.annotation.NonNull;

import com.permenko.weather.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public final class LanguageInterceptor implements Interceptor {

    @NonNull
    public static Interceptor create() {
        return new LanguageInterceptor();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url().newBuilder()
                .addQueryParameter("lang", BuildConfig.API_LANGUAGE)
                .build();
        request = request.newBuilder().url(url).build();
        return chain.proceed(request);
    }
}
