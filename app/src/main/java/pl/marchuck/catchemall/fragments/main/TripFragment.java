package pl.marchuck.catchemall.fragments.main;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import pl.marchuck.catchemall.R;
import pl.marchuck.catchemall.configuration.PokeConstants;
import pl.marchuck.catchemall.connection.PokeSpritesManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link TripFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripFragment extends Fragment {

    String pokeName;

    public static TripFragment newInstance(String pokename) {
        TripFragment fragment = new TripFragment();
        Bundle args = new Bundle();
        args.putString(PokeConstants.NAME, pokename);
        fragment.setArguments(args);


        return fragment;
    }

    public TripFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pokeName = getArguments().getString(PokeConstants.NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trip, container, false);
        ImageView choose = (ImageView) view.findViewById(R.id.choose);
        FrameLayout parent = (FrameLayout) view.findViewById(R.id.parent);
        Picasso.with(getActivity()).load(PokeSpritesManager.getMainPokeByName(pokeName)).into(choose);

        if (pokeName.startsWith("b"))
            parent.setBackgroundColor(Color.parseColor("#6cff6d"));
        else if (pokeName.startsWith("c"))
            parent.setBackgroundColor(Color.parseColor("#ffffa148"));
        else
            parent.setBackgroundColor(Color.parseColor("#80cbff"));

        return view;
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
