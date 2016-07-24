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

import com.tt.whorlviewlibrary.WhorlView;

import java.util.List;

import pl.marchuck.catchemall.R;
import pl.marchuck.catchemall.adapters.RealmPokeAdapter;
import pl.marchuck.catchemall.configuration.RecyclerUtils;
import pl.marchuck.catchemall.data.PokeID;
import pl.marchuck.catchemall.download.PokeIDsDownloader;
import pl.marchuck.catchemall.fragments.Progressable;


public class RealmPokeFragment extends Fragment implements Progressable {

    public static final String TAG = RealmPokeFragment.class.getSimpleName();

    RealmPokeAdapter pokedexAdapter;
    RecyclerView recyclerView;
    WhorlView progressBar;

    public static RealmPokeFragment newInstance() {
        RealmPokeFragment fragment = new RealmPokeFragment();
        return fragment;
    }

    public RealmPokeFragment() {
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
        progressBar.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        pokedexAdapter = new RealmPokeAdapter(this) {
            @Override
            public void downloadNewPokemons(int fromThisPosition) {
                Log.d(TAG, "downloadNewPokemons ");
                new PokeIDsDownloader() {
                    @Override
                    public void onDataReceived(final List<PokeID> newPokes) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pokedexAdapter.dataset.clear();
                                pokedexAdapter.dataset.addAll(newPokes);
                                notifyDataSetChanged();
                                notifyItemRangeChanged(0, getItemCount());
                            }
                        });
                    }
                }.setup(RealmPokeFragment.this, fromThisPosition).download();
            }
        };
        recyclerView.setAdapter(pokedexAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated()");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void showProgressBar(boolean show) {
        Log.d(TAG, "showProgressBar ");
        int vis = show ? View.VISIBLE : View.GONE;
        progressBar.setVisibility(vis);
        if (!progressBar.isCircling() && progressBar.isShown()) {
            progressBar.start();
        } else if (!progressBar.isShown()) {
            progressBar.stop();
        }
        recyclerView.setOnTouchListener(RecyclerUtils.disableTouchEvents(show));
    }


    @Override
    public void setText(CharSequence s) {
        Log.d(TAG, "setText " + s);
    }
}
