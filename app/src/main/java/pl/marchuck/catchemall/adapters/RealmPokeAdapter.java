package pl.marchuck.catchemall.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import pl.marchuck.catchemall.R;
import pl.marchuck.catchemall.activities.PokeInfoActivity;
import pl.marchuck.catchemall.configuration.PokeConstants;
import pl.marchuck.catchemall.data.PokeID;
import pl.marchuck.catchemall.data.realm.DBManager;
import pl.marchuck.catchemall.data.realm.RealmID;
import pl.marchuck.catchemall.fragments.Progressable;

/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public abstract class RealmPokeAdapter extends RecyclerView.Adapter<RealmPokeAdapter.ViewHolder> {
    public static final String TAG = RealmPokeAdapter.class.getSimpleName();
    public  List<PokeID> dataset = new ArrayList<>();// = Collections.synchronizedList(new ArrayList<NetPoke>());
    private Progressable context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View v;
        public TextView name, id;
        public RelativeLayout dataParent;

        public ViewHolder(View v) {
            super(v);
            this.v = v;

            dataParent = (RelativeLayout) v.findViewById(R.id.parent);
            name = (TextView) v.findViewById(R.id.pokemon_name);
            id = (TextView) v.findViewById(R.id.pokemon_id);
        }
    }

    public RealmPokeAdapter(Progressable parent) {
        context = parent;
        List<RealmID> pokesUnSorted = Realm.getInstance(parent.getActivity()).where(RealmID.class)
                .findAllSorted("id");
        dataset.clear();
        for (RealmID poke : pokesUnSorted) {
//            if (!contains(dataSet, poke))
            dataset.add(DBManager.asPokeID(poke));
        }
    }

    @Override
    public RealmPokeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = android.view.LayoutInflater.from(parent.getContext())
                .inflate(R.layout.net_poke_item, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder vh, final int position) {
        Log.d(TAG, "binding [" + position + "]");

        if (dataset == null || dataset.size() <= position
                || dataset.get(position) == null) return;

        if (position == dataset.size() - 1) {
            Log.d(TAG, "loading more pokemons...");
            downloadNewPokemons(getItemCount() + 1);
            return;
        }

        final PokeID poke = dataset.get(position);
        vh.id.setText(String.valueOf(poke.getId()));
        vh.name.setText(poke.getName());
        vh.dataParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick " + position);
                if (position < 0)
                    return;
                Log.i(TAG, "clicked item " + position);
                Intent intent = new Intent(context.getActivity(), PokeInfoActivity.class);
                intent.putExtra(PokeConstants.ID, poke.getId());
                intent.putExtra(PokeConstants.NAME, poke.getName());
                context.getActivity().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dataset == null) {
            Log.e(TAG, "getItemCount: data set is null!");
            return 0;
        }
        return dataset.size();
    }

    public abstract void downloadNewPokemons(int fromThisPosition);
}