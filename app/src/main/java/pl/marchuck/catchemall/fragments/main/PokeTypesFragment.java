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

import pl.marchuck.catchemall.R;
import pl.marchuck.catchemall.activities.MainActivity;
import pl.marchuck.catchemall.adapters.PokeTypesAdapter;


public class PokeTypesFragment extends Fragment {

    public static final String TAG = PokeTypesFragment.class.getSimpleName();

   private PokeTypesAdapter adapter;
   private RecyclerView recyclerView;
   private WhorlView progressBar;

    public static PokeTypesFragment newInstance() {
        return new PokeTypesFragment();
    }

    public PokeTypesFragment() {
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
        Log.d(TAG, "onCreateView ");
        View view = inflater.inflate(R.layout.fragment_pokedex, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        progressBar = (WhorlView) view.findViewById(R.id.whorlView);
        progressBar.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        adapter = new PokeTypesAdapter(this);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated()");
        ((MainActivity) getActivity()).setActionBarTitle("Pokemon types");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void showProgressBar(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        progressBar.setVisibility(visibility);
        if (!progressBar.isCircling())
            progressBar.start();
        else if (!progressBar.isShown())
            progressBar.stop();
    }
}
