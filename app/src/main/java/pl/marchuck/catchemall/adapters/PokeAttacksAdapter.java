package pl.marchuck.catchemall.adapters;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import pl.marchuck.catchemall.R;
import pl.marchuck.catchemall.data.PokeDetail;
import pl.marchuck.catchemall.data.PokeMove;
import pl.marchuck.catchemall.data.realm.DBManager;
import pl.marchuck.catchemall.data.realm.RealmPokeDetail;
import pl.marchuck.catchemall.download.MovesDownloader;
import pl.marchuck.catchemall.fragments.Progressable;

/**
 * Created by Lukasz Marczak on 2015-08-23.
 * adapter which contains all attacks of this pokemon
 */
public abstract class PokeAttacksAdapter extends RecyclerView.Adapter<PokeAttacksAdapter.ViewHolder> {
    public static final String TAG = PokeAttacksAdapter.class.getSimpleName();
    public List<PokeMove> dataset = new ArrayList<>();

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

    public PokeAttacksAdapter(final Progressable context, @Nullable PokeDetail detail, int currentPokemonLevel) {
         dataset = new ArrayList<>();
        List<Integer> movesAvailable = new ArrayList<>();
        if (detail == null) {
            Realm r = Realm.getInstance(context.getActivity());
            r.beginTransaction();
            RealmPokeDetail detail1 = r.where(RealmPokeDetail.class).findFirst();
            r.close();
            detail = DBManager.asPokeDetail(detail1);
        }
        Log.d(TAG, "moves : \'" + detail.getMoves() + "\'");
        String[] moves = detail.getMoves().split(",");
        //every move that is on higher level is on schema: 12&3 means move 12 is available on 3 level or higher
        for (String nextMove : moves) {
            if (nextMove.contains("&")) {
                Log.d(TAG, "with level \'" + nextMove + "\'");
                String[] moveAndLevel = nextMove.split("&");
                int level = -1;
                try {
                    level = Integer.valueOf(moveAndLevel[1]);
                } catch (NullPointerException | ArrayIndexOutOfBoundsException | NumberFormatException ex) {
                    Log.i(TAG, "wrong argument here");
                    level = -1;
                } finally {
                    if (level != -1) {
                        movesAvailable.add(Integer.valueOf(moveAndLevel[0]));
                    }
                }
            } else {
                Log.d(TAG, "next move: " + nextMove);
                int next = -1;
                try {
                    next = Integer.valueOf(nextMove);
                } catch (NullPointerException | NumberFormatException ex) {
                    Log.i(TAG, "wrong arg here");
                    next = -1;
                } finally {
                    if (next != -1) {
                        movesAvailable.add(next);
                    }
                }
            }
        }
        for (Integer i : movesAvailable) {
            Log.d(TAG, "PokeAttacksAdapter : available move: " + i);
        }
       context. showProgressBar(true);
        new MovesDownloader() {
            @Override
            public void onDataReceived(final List<PokeMove> moves) {
                Log.d(TAG, "onDataReceived, moves size = " + moves.size());
                context.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run ");
                        dataset.clear();
                        dataset.addAll(moves);
                        notifyDataSetChanged();
                        notifyItemRangeChanged(0, dataset.size());
                        context.showProgressBar(false);
                    }
                });
            }
        }.start(context, movesAvailable);
    }

    @Override
    public PokeAttacksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View v = android.view.LayoutInflater.from(parent.getContext())
                .inflate(R.layout.your_poke_item, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder vh, final int position) {
        Log.d(TAG, "binding [" + position + "]");

        if (dataset == null || dataset.size() <= position || dataset.get(position) == null)
            return;
        PokeMove poke = dataset.get(position);
        String pokeName = poke.getName();
        vh.name.setText(pokeName);
//        Picasso.with(context).load(PokeSpritesManager.getPokemonFrontByName(pokeName)).into(vh.image);
        vh.dataParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dataset == null) {
            Log.e(TAG, "getItemCount: mDataset is null!");
            return 0;
        }
        return dataset.size();
    }

    public abstract void onItemClick(int postion);
}