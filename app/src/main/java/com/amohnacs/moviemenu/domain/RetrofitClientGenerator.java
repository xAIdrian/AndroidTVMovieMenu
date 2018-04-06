package com.amohnacs.moviemenu.domain;

/**
 * Created by adrianmohnacs on 4/5/18.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * If we would like to expand on our application and include a number of other APIs or endpoints then
 * we need a long term way to do that.  By passing in the class of the Client we reduce boilerplate code
 * and can easily build variations of our Retrofit Clients.
 */
public class RetrofitClientGenerator {
    private static final String TAG = RetrofitClientGenerator.class.getSimpleName();

    public static final String TMDB_ENDPOINT = "https://api.themoviedb.org/3/";

    static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(TMDB_ENDPOINT)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(
                    buildGsonConverter()
            );

    public static <C> C generateClient(Class<C> clientsClass) {
        Retrofit retrofitClient = retrofitBuilder.client(
                buildLoggingHttpBuilder().build()
        ).build();

        return retrofitClient.create(clientsClass);
    }

    private static OkHttpClient.Builder buildLoggingHttpBuilder() {
        //logger
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(interceptor);
    }

    private static GsonConverterFactory buildGsonConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        //gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)

        Gson myGson = gsonBuilder.create();

        return GsonConverterFactory.create(myGson);
    }
}
