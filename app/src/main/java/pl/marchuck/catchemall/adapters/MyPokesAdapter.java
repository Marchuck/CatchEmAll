package pl.marchuck.catchemall.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import pl.marchuck.catchemall.R;
import pl.marchuck.catchemall.connection.PokeSpritesManager;
import pl.marchuck.catchemall.data.TrainedPoke;


/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public abstract class MyPokesAdapter extends RecyclerView.Adapter<MyPokesAdapter.ViewHolder> {
    public static final String TAG = MyPokesAdapter.class.getSimpleName();
    public  List<TrainedPoke> dataSet;
    private Context context = null;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View v;
        public TextView name;
        public RelativeLayout dataParent;
        public ImageView image;

        public ViewHolder(View v) {
            super(v);
            this.v = v;

            dataParent = (RelativeLayout) v.findViewById(R.id.parent);
            name = (TextView) v.findViewById(R.id.pokeName);
            image = (ImageView) v.findViewById(R.id.pokeImage);
        }
    }

    public MyPokesAdapter(Context context) {
        this.context = context;
        dataSet = new ArrayList<>();
        dataSet.add(new TrainedPoke("caterpie"));
        dataSet.add(new TrainedPoke("mew"));
        dataSet.add(new TrainedPoke("voltorb"));
        dataSet.add(new TrainedPoke("eevee"));
        dataSet.add(new TrainedPoke("onix"));
        dataSet.add(new TrainedPoke("jolteon"));

    }

    @Override
    public MyPokesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v = android.view.LayoutInflater.from(parent.getContext())
                .inflate(R.layout.your_poke_item, parent, false);
        ViewHolder vh = new ViewHolder(v);


        setupClickListeners(vh);
        return vh;
    }

    private void setupClickListeners(final ViewHolder holder) {

    }


    @Override
    public void onBindViewHolder(final ViewHolder vh, final int position) {
        Log.d(TAG, "binding [" + position + "]");

        if ( dataSet == null || dataSet.size() <= position || dataSet.get(position) == null)
            return;
        TrainedPoke poke = dataSet.get(position);
        String pokeName = poke.getName();
        vh.name.setText(pokeName);
        Picasso.with(context).load(PokeSpritesManager.getPokemonFrontByName(pokeName)).into(vh.image);
        vh.dataParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dataSet == null) {
            Log.e(TAG, "getItemCount: mDataset is null!");
            return 0;
        }
        return dataSet.size();
    }
    public abstract void onItemClick(int postion);
}