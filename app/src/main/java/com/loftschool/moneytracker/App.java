package com.loftschool.moneytracker;

import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private static final String TAG = "App";

    private Api api;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG
                ? HttpLoggingInterceptor.Level.BODY
                : HttpLoggingInterceptor.Level.NONE);


        OkHttpClient client = new OkHttpClient.Builder() // Отвечает за соединение с сервером
                .addInterceptor(interceptor) // Перехватчик, перед тем как запрос отправится он его перехватит, обработает и дальше передаст на сервер и наоборот
                .build();

        Gson gson = new GsonBuilder() // Отвечает за то как парсить ответ от сервера и все соотв. настройки
                .setDateFormat("dd.MM.yyyy HH:mm:ss") // В каком формате у нас будет приходить дата
                .create();

        Retrofit retrofit = new Retrofit.Builder() // Настройка ретрофит чтобы он использовал Gson конвертер для обработки наших ответов
                .baseUrl("http://loftschoolandroid.getsandbox.com/") // Ссылка на сервер
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();


        api = retrofit.create(Api.class);
    }

    public Api getApi() {
        return api;
    }

}
