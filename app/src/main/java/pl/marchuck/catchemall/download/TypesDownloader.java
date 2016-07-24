package pl.marchuck.catchemall.download;

import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.realm.Realm;
import pl.marchuck.catchemall.JsonArium.PokeTypeDeserializer;
import pl.marchuck.catchemall.connection.PokeApi;
import pl.marchuck.catchemall.connection.SimpleRestAdapter;
import pl.marchuck.catchemall.data.PokeType;
import pl.marchuck.catchemall.data.realm.DBManager;
import pl.marchuck.catchemall.data.realm.RealmType;
import pl.marchuck.catchemall.fragments.main.PokeTypesFragment;
import retrofit.RetrofitError;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * Created by Lukasz Marczak on 2015-09-18.
 */
public abstract class TypesDownloader {
    public static final String TAG = TypesDownloader.class.getSimpleName();

    public abstract void onDataReceived(List<PokeType> list);

    public void start(final PokeTypesFragment context) {
        Log.d(TAG, "start ");
        List<Integer> types = new LinkedList<>();
        for (int j = 1; j < 19; j++) {
            types.add(j);
        }

        final PokeApi service = new SimpleRestAdapter(PokeApi.POKEMON_API_ENDPOINT, new TypeToken<PokeType>() {
        }.getType(), PokeTypeDeserializer.getInstance(context.getActivity())).getPokedexService();

        Observable.from(types).flatMap(new Func1<Integer, Observable<PokeType>>() {
            @Override
            public Observable<PokeType> call(Integer newType) {
                return service.getPokemonType(newType);
            }
        }).onErrorResumeNext(new Func1<Throwable, Observable<PokeType>>() {
            @Override
            public Observable<PokeType> call(Throwable throwable) {
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
        }).subscribe(new Subscriber<PokeType>() {
            @Override
            public void onCompleted() {
                Realm realm = Realm.getInstance(context.getActivity());
                realm.beginTransaction();
                List<RealmType> list = realm.where(RealmType.class).findAllSorted("id", true);

                for (RealmType t : list) {
                    Log.d(TAG, "type no. " + t.getId() + "," + t.getName());
                }
                List<PokeType> types =new ArrayList<PokeType>();
                for(RealmType t : list){
                    types.add(new PokeType(t.getId(),t.getName(),t.getWeakness(),t.getIneffective(),t.getSuperEffective()));
                }
                onDataReceived(types);
                realm.commitTransaction();

                SimpleRestAdapter.onCompleted(TAG, this);
            }

            @Override
            public void onError(Throwable e) {
                SimpleRestAdapter.onErrorCompleted(TAG, this, e);
            }

            @Override
            public void onNext(PokeType pokeType) {
                if (pokeType == null)
                    return;
                Log.d(TAG, "onNext " + pokeType.getId() + "," + pokeType.getName());
                DBManager.savePokeType(pokeType);
            }
        });
    }
}
