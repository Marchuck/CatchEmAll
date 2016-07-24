package pl.marchuck.catchemall.connection;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.lang.reflect.Type;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;
import rx.Subscriber;

/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public class SimpleRestAdapter {


    public RestAdapter getAdapter() {
        return adapter;
    }

    private RestAdapter adapter;

    public PokeApi getPokedexService() {
        return adapter.create(PokeApi.class);
    }

    /**
     * bieda version
     */
    public SimpleRestAdapter(String endpoint) {

        adapter = new RestAdapter.Builder()
                .setEndpoint(endpoint)
                .build();
    }

    /**
     * full version
     */
    public SimpleRestAdapter(String endpoint, Type collectionType,
                             JsonDeserializer deserializer) {

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(collectionType, deserializer);
        Gson gson = builder.create();
        GsonConverter converter = new GsonConverter(gson);

        adapter = new RestAdapter.Builder()
                .setEndpoint(endpoint)
                .setConverter(converter)
                .build();
    }

    public static void onCompleted(String tag, Subscriber<?> subscriber) {
        Log.d(tag, "onCompleted ");
        if (subscriber != null && !subscriber.isUnsubscribed()) {
            subscriber.unsubscribe();
            subscriber = null;
        }
    }

    public static void onErrorCompleted(String tag, Subscriber<?> subscriber, Throwable e) {
        Log.e(tag, "onErrorCompleted ");
        Log.e(tag, "caused by " + e.getCause());
        if (e instanceof RetrofitError)
            Log.e(tag, "url = " + ((RetrofitError) e).getUrl());
        Log.e(tag, "message: " + e.getMessage());
        e.printStackTrace();
        onCompleted(tag, subscriber);
    }

    public static void onError(String tag, Throwable throwable) {
        Log.e(tag, "onError");
        Log.e(tag, "caused by " + throwable.getCause());
        Log.e(tag, "message: " + throwable.getMessage());
        if (throwable instanceof RetrofitError)
            Log.e(tag, "url = " + ((RetrofitError) throwable).getUrl());
        throwable.printStackTrace();
    }
}
