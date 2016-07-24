package pl.marchuck.catchemall.download;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import pl.marchuck.catchemall.JsonArium.PokeMoveDeserializer;
import pl.marchuck.catchemall.connection.PokeApi;
import pl.marchuck.catchemall.connection.SimpleRestAdapter;
import pl.marchuck.catchemall.data.PokeMove;
import pl.marchuck.catchemall.data.realm.DBManager;
import pl.marchuck.catchemall.data.realm.RealmMove;
import pl.marchuck.catchemall.fragments.Progressable;
import retrofit.RetrofitError;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Lukasz Marczak on 2015-09-18.
 * fetches selected set of attacks
 */
public abstract class MovesDownloader {
    public static final String TAG = MovesDownloader.class.getSimpleName();

    public abstract void onDataReceived(List<PokeMove> moves);

    public void start(final Progressable context, List<Integer> moves) {
        if (moves == null || moves.size() == 0) {
            Log.e(TAG, "start ");
            Realm realm = Realm.getInstance(context.getActivity());
            try {
                realm.beginTransaction();

                List<RealmMove> list = realm.where(RealmMove.class).findAllSorted("id", true);
                List<PokeMove> moves1 = new ArrayList<>();

                for (RealmMove t : list) {
                    Log.d(TAG, "type no. " + t.getId() + "," + t.getName());
                    moves1.add(DBManager.asPokeMove(t));
                }
                realm.commitTransaction();
                onDataReceived(moves1);
                context.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        context.showProgressBar(false);
                    }
                });
            } catch (Exception x) {
                realm.cancelTransaction();
                Log.e(TAG, "start: ", x);
            }
            //realm.close();

            return;
        }
        Log.d(TAG, "start ");

        Realm realm = Realm.getInstance(context.getActivity());
        realm.beginTransaction();
        List<RealmMove> realmAvailableMoves = realm.where(RealmMove.class).findAll();
        List<Integer> queryList = getMissingMoves(moves, realmAvailableMoves);
        realm.commitTransaction();
        realm.close();
        final PokeApi service = new SimpleRestAdapter(PokeApi.POKEMON_API_ENDPOINT, new TypeToken<PokeMove>() {
        }.getType(), PokeMoveDeserializer.getInstance()).getPokedexService();

        Set<Integer> uniqueMoves = new HashSet<>(queryList);
        Log.d(TAG, "start with");
        for (Integer i : uniqueMoves) {
            Log.d(TAG, "move : " + i);
        }
        Observable.from(uniqueMoves).flatMap(new Func1<Integer, Observable<PokeMove>>() {
            @Override
            public Observable<PokeMove> call(Integer newMove) {
                return service.getPokemonMove(newMove);
            }
        }).onErrorResumeNext(new Func1<Throwable, Observable<PokeMove>>() {
            @Override
            public Observable<PokeMove> call(Throwable throwable) {
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
                Log.d(TAG, "hide loader");
                context.showProgressBar(false);

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PokeMove>() {
                    @Override
                    public void onCompleted() {
                        Realm realm = Realm.getInstance(context.getActivity());
                        realm.beginTransaction();
                        List<RealmMove> list = realm.where(RealmMove.class).findAllSorted("id", true);

                        final List<PokeMove> moves = new ArrayList<>();
                        for (RealmMove t : list) {
                            Log.d(TAG, "type no. " + t.getId() + "," + t.getName());
                            moves.add(DBManager.asPokeMove(t));
                        }
                        context.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onDataReceived(moves);
                                context.showProgressBar(false);
                            }
                        });
                        realm.commitTransaction();
                        realm.close();
                        SimpleRestAdapter.onCompleted(TAG, this);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Realm realm = Realm.getInstance(context.getActivity());
                        realm.beginTransaction();
                        List<RealmMove> list = realm.where(RealmMove.class).findAllSorted("id", true);

                        final List<PokeMove> moves = new ArrayList<>();
                        for (RealmMove t : list) {
                            Log.d(TAG, "type no. " + t.getId() + "," + t.getName());
                            moves.add(DBManager.asPokeMove(t));
                        }
                        context.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onDataReceived(moves);
                                context.showProgressBar(false);
                            }
                        });
                        realm.commitTransaction();
                        realm.close();
                        SimpleRestAdapter.onErrorCompleted(TAG, this, e);
                    }

                    @Override
                    public void onNext(PokeMove pokeMove) {
                        if (pokeMove == null) {
                            Log.e(TAG, "impossibru to got there");
                            return;
                        }
                        Log.d(TAG, "onNext " + pokeMove.getId() + "," + pokeMove.getName());
                        DBManager.savePokeMove(pokeMove);
                    }
                });
    }

    public List<Integer> getMissingMoves(@NonNull List<Integer> pokemonMoves, @Nullable List<RealmMove> realmAvailableMoves) {
        Log.d(TAG, "getMissingMoves ");
        List<Integer> list = new ArrayList<>();

        if (realmAvailableMoves == null) {
            Log.e(TAG, "list is null!!!");
            return pokemonMoves;
        }

        for (RealmMove m : realmAvailableMoves) {
            Log.d(TAG, "getMissingMoves : realmMove " + m.getId());
        }
        for (Integer m : pokemonMoves) {
            Log.d(TAG, "getMissingMoves : existing moves " + m);
        }

        for (Integer move : pokemonMoves) {
            if (!containsId(move, realmAvailableMoves) && move != 0)
                list.add(move);
        }
        for (Integer s : list) {
            Log.d(TAG, "item for query " + s);
        }
        return list;
    }

    private boolean containsId(Integer moveId, List<RealmMove> realmAvailableMoves) {
        for (RealmMove move : realmAvailableMoves)
            if (move.getId() == moveId) {
//                Log.d(TAG, "containsId ");
                return true;
            }
        return false;
    }

}