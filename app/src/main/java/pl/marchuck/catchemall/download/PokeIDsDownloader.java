package pl.marchuck.catchemall.download;

import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.util.LinkedList;
import java.util.List;

import io.realm.Realm;
import pl.marchuck.catchemall.JsonArium.PokeIdDeserializer;
import pl.marchuck.catchemall.configuration.Config;
import pl.marchuck.catchemall.connection.PokeApi;
import pl.marchuck.catchemall.connection.SimpleRestAdapter;
import pl.marchuck.catchemall.data.PokeID;
import pl.marchuck.catchemall.data.realm.DBManager;
import pl.marchuck.catchemall.data.realm.RealmID;
import pl.marchuck.catchemall.fragments.Progressable;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Lukasz Marczak on 2015-09-19.
 */
public abstract class PokeIDsDownloader {
    public static final String TAG = PokeIDsDownloader.class.getSimpleName();
    /**
     * services for download
     */
    PokeApi serviceID;
    Progressable context;
    int fromThisPosition;

    public abstract void onDataReceived(List<PokeID> newPokes);

    public PokeIDsDownloader setup(final Progressable _context, int _fromThisPosition) {
        Log.d(TAG, "start ");
        context = _context;
        fromThisPosition = _fromThisPosition;
        serviceID = new SimpleRestAdapter(
                PokeApi.POKEMON_API_ENDPOINT, new TypeToken<PokeID>() {
        }.getType(), PokeIdDeserializer.getInstance(_context.getActivity())).getPokedexService();
        return this;
    }

    public void download() {
        Log.d(TAG, "downloadData ");
        List<Integer> ids = new LinkedList<>();
        int limit = (fromThisPosition + Config.MORE_POKES) > PokeApi.POKEMONS_COUNT ? PokeApi.POKEMONS_COUNT : fromThisPosition + Config.MORE_POKES;

        for (int j = fromThisPosition; j < limit; j++) {
            ids.add(j);
        }

        Observable.from(ids).flatMap(new Func1<Integer, Observable<PokeID>>() {
            @Override
            public Observable<PokeID> call(Integer id) {
                Log.d(TAG, "call observable<" + id + ">");
                return serviceID.getPokemonByID(id);
            }
        }).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                Log.d(TAG, "doOnSubscribe");
                context.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        context.showProgressBar(true);
                    }
                });
            }
        }).doOnCompleted(new Action0() {
            @Override
            public void call() {
                Log.d(TAG, "doOnCompleted");
                context.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        context.showProgressBar(false);
                    }
                });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends PokeID>>() {
                    @Override
                    public Observable<? extends PokeID> call(Throwable throwable) {
                        SimpleRestAdapter.onError(TAG, throwable);
                        return Observable.empty();
                    }
                }).subscribe(new Subscriber<PokeID>() {
            @Override
            public void onCompleted() {
                Realm r = Realm.getInstance(context.getActivity());
                r.beginTransaction();
                List<RealmID> list = r.where(RealmID.class).findAllSorted("id");
                List<PokeID> outputList = new LinkedList<>();
                for (RealmID id : list) {
                    outputList.add(DBManager.asPokeID(id));
                }
                r.commitTransaction();
                r.close();
                onDataReceived(outputList);
                SimpleRestAdapter.onCompleted(TAG, this);
            }

            @Override
            public void onError(Throwable e) {
                SimpleRestAdapter.onErrorCompleted(TAG, this, e);
            }

            @Override
            public void onNext(PokeID pokeID) {
                Log.d(TAG, "onNext " + pokeID.getId() + ", " + pokeID.getName());
                DBManager.savePokeID(pokeID);
            }
        });
    }
}
