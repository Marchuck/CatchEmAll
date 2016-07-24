package pl.marchuck.catchemall.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import pl.marchuck.catchemall.R;
import pl.marchuck.catchemall.activities.MainActivity;
import pl.marchuck.catchemall.configuration.PokeConstants;
import pl.marchuck.catchemall.configuration.PokeUtils;
import pl.marchuck.catchemall.connection.PokeSpritesManager;


public class ChoosePokemonFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = ChoosePokemonFragment.class.getSimpleName();

    private String fragmentName;
    private String pokeName;

    public static ChoosePokemonFragment newInstance(String param1, String param2) {
        ChoosePokemonFragment fragment = new ChoosePokemonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ChoosePokemonFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fragmentName = getArguments().getString(ARG_PARAM1);
            pokeName = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose_pokemon, container, false);
        Log.d(TAG, "onCreateView : " + fragmentName);
        ImageView picassoView = (ImageView) view.findViewById(R.id.choose);
        TextView pokemonName = (TextView) view.findViewById(R.id.pokemon_name);

        Picasso.with(getActivity()).load(PokeSpritesManager.getMainPokeByName(pokeName)).into(picassoView);
        pokemonName.setText(PokeUtils.getPrettyPokemonName(pokeName));

        picassoView.setOnClickListener(switchToActivityListener());
        pokemonName.setOnClickListener(switchToActivityListener());

        return view;
    }

    private View.OnClickListener switchToActivityListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick ");
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra(PokeConstants.NAME, pokeName);
                startActivity(intent);
            }
        };
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
