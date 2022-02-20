package com.example.radhegausala.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient instance = null;
    private WebServices myApi;

    private RetrofitClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(httpLoggingInterceptor);  // <-- this is the important line!
        httpClient.readTimeout(180, TimeUnit.SECONDS);
        httpClient.connectTimeout(180, TimeUnit.SECONDS);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(WebServices.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        myApi = mRetrofit.create(WebServices.class);

    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();

        }
        return instance;
    }

    public WebServices getApi() {
        return myApi;
    }
}
