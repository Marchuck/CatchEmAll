package pl.marchuck.catchemall.fragments.fight;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import pl.marchuck.catchemall.R;
import pl.marchuck.catchemall.activities.FightActivity;
import pl.marchuck.catchemall.configuration.Config;
import pl.marchuck.catchemall.configuration.PokeUtils;
import pl.marchuck.catchemall.connection.PokeSpritesManager;
import pl.marchuck.catchemall.data.BeaconsInfo;


public class StartFightFragment extends Fragment {
    private static final String TAG = StartFightFragment.class.getSimpleName();
    private ImageView wildPokemon;
    private TextView title;
    private FightActivity parentActivity;
    private String pokemonName;
    private int pokemonID = 1;
    private Handler handlarz = new Handler();

    public static StartFightFragment newInstance(int ID) {
        StartFightFragment fragment = new StartFightFragment();
        Bundle args = new Bundle();
//        args.putString(BeaconsInfo.PokeInterface.POKEMON_NAME, pokemonName);
        args.putInt(BeaconsInfo.PokeInterface.POKEMON_ID, ID);
        fragment.setArguments(args);

        return fragment;
    }

    public StartFightFragment() {
        // Required empty public constructor
    }

    //    public void showProgressBar(boolean show) {
//        Log.d(TAG, "showProgressBar " + show);
//        int vis = show ? View.VISIBLE : View.GONE;
//        int inv = !show ? View.VISIBLE : View.GONE;
//        progressBarLayout.setVisibility(vis);
//        main.setVisibility(inv);
//    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate ");
        if (getArguments() != null) {
            pokemonID = getArguments().getInt(BeaconsInfo.PokeInterface.POKEMON_ID, 12);
        }
        pokemonName = PokeUtils.getPokemonNameFromId(this.getActivity(), pokemonID);
        Log.d(TAG, "onCreate ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        wildPokemon = (ImageView) view.findViewById(R.id.wild_pokemon);

        title = (TextView) view.findViewById(R.id.title);
        wildPokemon.setVisibility(View.GONE);
        title.setText("Wild " + PokeUtils.getPrettyPokemonName(pokemonName) + " appeared!!!");
        String image = PokeSpritesManager.getMainPokeByName(PokeUtils.getPokemonNameFromId(getActivity(), pokemonID));
        Log.d(TAG, "fetching image: " + image);
        Picasso.with(parentActivity).load(image).into(wildPokemon);
        wildPokemon.setVisibility(View.VISIBLE);

        return view;
    }

    private void startFight() {
        Log.d(TAG, "startFight()");
        handlarz.postDelayed(fightBody(), 3000); //delay of 2 seconds
    }

    private Runnable fightBody() {
        return new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run ");
                Log.d(TAG, "fight Body()");

                parentActivity.switchToFragment(Config.FRAGMENT.RUNNING_FIGHT, pokemonName);
            }
        };
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated ");
        startFight();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = (FightActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
