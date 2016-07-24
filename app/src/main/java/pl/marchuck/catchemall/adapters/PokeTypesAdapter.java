package pl.marchuck.catchemall.adapters;

import android.content.Context;
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
import pl.marchuck.catchemall.activities.PokeTypeActivity;
import pl.marchuck.catchemall.configuration.PokeConstants;
import pl.marchuck.catchemall.data.PokeType;
import pl.marchuck.catchemall.data.realm.RealmType;
import pl.marchuck.catchemall.download.TypesDownloader;
import pl.marchuck.catchemall.fragments.main.PokeTypesFragment;

/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public class PokeTypesAdapter extends RecyclerView.Adapter<PokeTypesAdapter.ViewHolder> {
    public static final String TAG = PokeTypesAdapter.class.getSimpleName();
    private List<PokeType> dataset = new ArrayList<>();// = Collections.synchronizedList(new ArrayList<NetPoke>());
    private Context context = null;
    private PokeTypesFragment parent;

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

    public PokeTypesAdapter(final PokeTypesFragment parent) {
        Log.d(TAG, "PokeTypesAdapter created");
        this.context = parent.getActivity();
        this.parent = parent;

        Realm r = Realm.getInstance(parent.getActivity());
        r.beginTransaction();
        List<RealmType> types = r.where(RealmType.class).findAllSorted("id");
        r.commitTransaction();

        if (types != null && types.size() > 0) {
            Log.d(TAG, "types are not null");
            for (RealmType t : types) {
                dataset.add(new PokeType(t.getId(), t.getName(), t.getWeakness(), t.getIneffective(), t.getSuperEffective()));
            }
            notifyDataSetChanged();
            notifyItemRangeChanged(0, dataset.size());
        } else {
            Log.e(TAG, "types not yet fetched, downloading in progress...");
            new TypesDownloader() {
                @Override
                public void onDataReceived(final List<PokeType> list) {
                    Log.d(TAG, "onDataReceived ");
                    parent.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dataset.addAll(list);
                            notifyDataSetChanged();
                            notifyItemRangeChanged(0, dataset.size());
                        }
                    });
                }
            }.start(parent);
        }
    }

    @Override
    public PokeTypesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
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

        //// TODO: 2015-09-20 customize layout !!!
        final PokeType poke = dataset.get(position);
        vh.id.setText(String.valueOf(poke.getId()));
        vh.name.setText(poke.getName());
        vh.dataParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick " + position);
                if (position < 0)
                    return;
                Log.i(TAG, "clicked item " + position);
                Intent intent = new Intent(context, PokeTypeActivity.class);
                intent.putExtra(PokeConstants.ID, poke.getId());
                context.startActivity(intent);
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
}