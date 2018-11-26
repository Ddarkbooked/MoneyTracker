package com.loftschool.moneytracker;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loftschool.moneytracker.api.Api;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private static final String PREFS_NAME = "shared_prefs";
    private static final String KEY_TOKEN = "auth_token";

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
                .addInterceptor(new AuthInterceptor())
                .build();

        Gson gson = new GsonBuilder() // Отвечает за то как парсить ответ от сервера и все соотв. настройки
                .setDateFormat("dd.MM.yyyy HH:mm:ss") // В каком формате у нас будет приходить дата
                .create();

        Retrofit retrofit = new Retrofit.Builder() // Настройка ретрофит чтобы он использовал Gson конвертер для обработки наших ответов
                .baseUrl(BuildConfig.BASE_URL) // Ссылка на сервер, обращается в gradle
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();


        api = retrofit.create(Api.class);
    }

    public Api getApi() {
        return api;
    }

    public void saveAuthToken(String token) { // Создаем метод, тут будем передавать токен (Сохраняет токен)
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE) // Между всеми экранами доступно хранилище настроек.
                .edit() // edit() - внести изменения в наши настройки
                .putString(KEY_TOKEN, token) // Вносим изменения
                .apply();

    }

    public String getAuthToken() { // Метод, которые возвращает String (Получает токен)
        return getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .getString(KEY_TOKEN, null); // Получаем токен; по умолчанию возвращаем null
    }

    public boolean isAuthorized() { // Авторизован или нет
        return !TextUtils.isEmpty(getAuthToken()); // Проверяет пустая или не пустая строка и также проверяет на null
    }


    private class AuthInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException { // Этот метод вызовется когда пойдет запрос на сервер
            Request request = chain.request();
            HttpUrl url = request.url();

            HttpUrl.Builder urlBuilder = url.newBuilder();
            HttpUrl newUrl = urlBuilder.addQueryParameter("auth-token", getAuthToken()).build();

            Request.Builder requestBuilder = request.newBuilder();
            Request newRequest = requestBuilder.url(newUrl).build();

            return chain.proceed(newRequest);
        }
    }
}
