package pl.marchuck.catchemall.download;

import android.util.Log;


import com.google.gson.reflect.TypeToken;

import java.util.LinkedList;
import java.util.List;

import pl.marchuck.catchemall.JsonArium.PokeIdDeserializer;
import pl.marchuck.catchemall.activities.MainActivity;
import pl.marchuck.catchemall.connection.PokeApi;
import pl.marchuck.catchemall.connection.SimpleRestAdapter;
import pl.marchuck.catchemall.data.PokeID;
import pl.marchuck.catchemall.data.realm.DBManager;
import pl.marchuck.catchemall.fragments.Progressable;
import pl.marchuck.catchemall.fragments.main.Changeable;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author Lukasz Marczak
 * @since 2015-09-19.
 */
public class First151Downloader {
    public static final String TAG = First151Downloader.class.getSimpleName();

    public static void downloadData(final Changeable chageable) {
        Log.d(TAG, "downloadData ");

        Log.d(TAG, "start ");
        final PokeApi serviceID = new SimpleRestAdapter(PokeApi.POKEMON_API_ENDPOINT, new TypeToken<PokeID>() {
        }.getType(), new PokeIdDeserializer()).getPokedexService();
        List<Integer> details = new LinkedList<>();
        for (int j = 1; j <= 151; j++) details.add(j);

        Observable.from(details).flatMap(new Func1<Integer, Observable<PokeID>>() {
            @Override
            public Observable<PokeID> call(Integer id) {
                Log.d(TAG, "call observable<" + id + ">");
                return serviceID.getPokemonByID(id);
            }
        }).map(new Func1<PokeID, Boolean>() {
            @Override
            public Boolean call(PokeID poke) {
                DBManager.savePokeID(poke);
                return true;
            }
        }).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                chageable.onChange(true);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Throwable throwable) {
                        SimpleRestAdapter.onError(TAG, throwable);
                        return Observable.empty();
                    }
                }).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted ");
                chageable.onChange(false);
                SimpleRestAdapter.onCompleted(TAG, this);
            }

            @Override
            public void onError(Throwable e) {
                chageable.onChange(false);
                SimpleRestAdapter.onErrorCompleted(TAG, this, e);
            }

            @Override
            public void onNext(Boolean aBoolean) {
                Log.d(TAG, "onNext ");
            }
        });
    }
}
