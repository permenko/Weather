package com.permenko.weather.api;

import android.support.annotation.NonNull;

import com.permenko.weather.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public final class UnitsInterceptor implements Interceptor {

    @NonNull
    public static Interceptor create() {
        return new UnitsInterceptor();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url().newBuilder()
                .addQueryParameter("units", BuildConfig.API_UNITS)
                .build();
        request = request.newBuilder().url(url).build();
        return chain.proceed(request);
    }
}
