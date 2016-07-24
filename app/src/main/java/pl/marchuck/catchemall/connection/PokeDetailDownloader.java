package pl.marchuck.catchemall.connection;

import android.util.Log;

import com.google.gson.reflect.TypeToken;

import io.realm.Realm;
import pl.marchuck.catchemall.JsonArium.PokeDetailDeserializer;
import pl.marchuck.catchemall.data.PokeDetail;
import pl.marchuck.catchemall.data.realm.DBManager;
import pl.marchuck.catchemall.data.realm.RealmPokeDetail;
import pl.marchuck.catchemall.fragments.Progressable;
import retrofit.RetrofitError;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * Created by Lukasz Marczak on 2015-09-18.
 */
public abstract class PokeDetailDownloader {
    public static final String TAG = PokeDetailDownloader.class.getSimpleName();

    public abstract void onDataReceived(PokeDetail detail);

    public void start(final Progressable context, int pokeID) {
        Log.d(TAG, "start ");

        Realm realm = Realm.getInstance(context.getActivity());
        realm.beginTransaction();

        RealmPokeDetail detail = realm.where(RealmPokeDetail.class).equalTo("pkdxId", pokeID).findFirst();
        if (detail != null) {
            realm.commitTransaction();
            onDataReceived(DBManager.asPokeDetail(detail));
            realm.close();
            return;
        }
        realm.commitTransaction(); realm.close();
        final PokeApi service = new SimpleRestAdapter(PokeApi.POKEMON_API_ENDPOINT, new TypeToken<PokeDetail>() {
        }.getType(), PokeDetailDeserializer.getInstance(null)).getPokedexService();

        service.getPokemonDetail(pokeID).onErrorResumeNext(new Func1<Throwable, Observable<PokeDetail>>() {
            @Override
            public Observable<PokeDetail> call(Throwable throwable) {
                Log.e(TAG, "error with");
                if (throwable instanceof RetrofitError)
                    Log.e(TAG, "url = " + ((RetrofitError) throwable).getUrl());

                return Observable.empty();
            }
        }).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                context.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "show loader");
                        context.showProgressBar(true);
                    }
                });
            }
        }).doOnCompleted(new Action0() {
            @Override
            public void call() {
                context.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "hide loader");
                        context.showProgressBar(false);
                    }
                });
            }
        }).subscribe(new Subscriber<PokeDetail>() {
            @Override
            public void onCompleted() {
                SimpleRestAdapter.onCompleted(TAG, this);
            }

            @Override
            public void onError(Throwable e) {
                SimpleRestAdapter.onErrorCompleted(TAG, this, e);
            }

            @Override
            public void onNext(final PokeDetail pokeDetail) {
                if (pokeDetail == null) {
                    Log.e(TAG, "impossibru to got there");
                    return;
                }
                Log.d(TAG, "onNext : " + pokeDetail.getName());
                DBManager.savePokeDetail(pokeDetail);
               context.getActivity().runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       onDataReceived(pokeDetail);
                   }
               });
            }
        });
    }
}