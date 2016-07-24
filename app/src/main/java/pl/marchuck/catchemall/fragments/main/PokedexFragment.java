package pl.marchuck.catchemall.fragments.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tt.whorlviewlibrary.WhorlView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import pl.marchuck.catchemall.JsonArium.PokeNetNameDeserializer;
import pl.marchuck.catchemall.R;
import pl.marchuck.catchemall.adapters.PokedexAdapter;
import pl.marchuck.catchemall.configuration.Config;
import pl.marchuck.catchemall.connection.PokeApi;
import pl.marchuck.catchemall.connection.SimpleRestAdapter;
import pl.marchuck.catchemall.data.NetPoke;
import retrofit.client.Response;
import retrofit.converter.ConversionException;
import retrofit.converter.GsonConverter;
import rx.Observable;
import rx.Subscriber;

public class PokedexFragment extends Fragment {

    public static final String TAG = PokedexFragment.class.getSimpleName();

    private PokedexAdapter pokedexAdapter;
    private List<NetPoke> dataset;
    private RecyclerView recyclerView;
    private WhorlView progressBar;

    public static PokedexFragment newInstance() {
        PokedexFragment fragment = new PokedexFragment();
        return fragment;
    }

    public PokedexFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pokedex, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        progressBar = (WhorlView) view.findViewById(R.id.whorlView);
        showProgressBar();

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        dataset = new ArrayList<>();
        for (int j = 0; j < 150; j++) {
            dataset.add(new NetPoke(j + 1, ""));
        }
        pokedexAdapter = new PokedexAdapter(this, dataset, recyclerView) {
            @Override
            public void loadNewNetPokes(int position, PokedexAdapter pokedexAdapter) {
                showProgressBar();
                downloadMorePokes(position, pokedexAdapter);
            }
        };
        recyclerView.setAdapter(pokedexAdapter);
        setupRxQuerying(pokedexAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated()");
    }

    private void downloadMorePokes(int position, PokedexAdapter adapter) {
        Log.d(TAG, "downloadMorePokes()");
        PokeApi service = new SimpleRestAdapter(PokeApi.POKEMON_API_ENDPOINT,
                new TypeToken<String>() {
                }.getType(), PokeNetNameDeserializer.INSTANCE).getPokedexService();

        int maxSize = (position + Config.MORE_POKES) > Config.MAX_POKES
                ? Config.MAX_POKES : position + Config.MORE_POKES;
        List<rx.Observable<Response>> observables = new ArrayList<>();


        Log.d(TAG, "downloading items: (" + position + ", " + maxSize + ")");

        for (int j = position; j < maxSize; j++) {
            rx.Observable<Response> newPokemon = service.getPokemonNameByID(j + 1)
                    .onErrorResumeNext(Observable.<Response>empty());
            observables.add(newPokemon);
        }
        Observable.merge(observables).subscribe(doHttpGETs(adapter));
    }

    private void setupRxQuerying(PokedexAdapter adapter) {
        Log.d(TAG, "setupRxQuerying()");
        List<rx.Observable<Response>> observables = new ArrayList<>();

        PokeApi service = new SimpleRestAdapter(
                PokeApi.POKEMON_API_ENDPOINT, Response.class,
                PokeNetNameDeserializer.INSTANCE).getPokedexService();

        for (int j = 0; j < 20; j++) {
            rx.Observable<Response> newPokemon = service.getPokemonNameByID(j + 1)
                    .onErrorResumeNext(Observable.<Response>empty());
            observables.add(newPokemon);
        }
        Observable.merge(observables).subscribe(doHttpGETs(adapter));
    }

    private Subscriber<Response> doHttpGETs(final PokedexAdapter adapter) {
        return new Subscriber<Response>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted()");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataset();
                        dismissProgressBar();
                        adapter.unLockTouchEvents();
                        PokedexAdapter.downloadDone = true;
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError()");
                Log.e(TAG, "message : " + e.getMessage());
                Log.e(TAG, "cause : " + e.getCause());
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataset();
                        dismissProgressBar();
                        adapter.unLockTouchEvents();
                    }
                });
            }

            @Override
            public void onNext(Response s) {
                Log.d(TAG, "onNext()");
                consumeResponse(s);
            }
        };
    }

    public void consumeResponse(Response response) {
        if (response.getBody() == null) {
            Log.e(TAG, "null body at " + response.getUrl());
            return;
        }

        Type collectionType = new TypeToken<String>() {
        }.getType();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(collectionType, PokeNetNameDeserializer.INSTANCE);
        Gson gson = gsonBuilder.create();
        GsonConverter converter = new GsonConverter(gson);

        String result = null;
        try {
            result = (String) converter.fromBody(response.getBody(), collectionType);
        } catch (ConversionException e) {
            e.printStackTrace();
            Log.d(TAG, "still null ;(");
        } finally {
            if (result != null) {

                String[] slices = response.getUrl().split("sprite");
                String str = slices[1].replaceAll("/", "");

                int position = Integer.valueOf(str);
                Log.d(TAG, "setting name: " + result + ", for position: " + (position - 1));
                pokedexAdapter.getDataset().get(position - 1).setName(result);
            }
        }
    }

    public void dismissProgressBar() {
        progressBar.setVisibility(View.GONE);
        progressBar.stop();
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        if (!progressBar.isCircling())
            progressBar.start();
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
