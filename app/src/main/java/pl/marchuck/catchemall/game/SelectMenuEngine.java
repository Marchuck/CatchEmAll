package pl.marchuck.catchemall.game;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import pl.marchuck.catchemall.R;
import pl.marchuck.catchemall.adapters.MyPokesAdapter;
import pl.marchuck.catchemall.adapters.PokeAttacksAdapter;
import pl.marchuck.catchemall.configuration.Config;
import pl.marchuck.catchemall.configuration.RecyclerUtils;
import pl.marchuck.catchemall.data.PokeDetail;
import pl.marchuck.catchemall.fragments.Progressable;


/**
 * Created by Lukasz Marczak on 2015-09-16.
 */
public class SelectMenuEngine {

    private SelectMenuEngine() {
    }

    public void RUN() {
        //go away, if opponent is too strong
    }

    public static abstract class POKEMON {

        public abstract void onPokemonChosen(final int position);

        public MyPokesAdapter pokesAdapter;

        public POKEMON(Context context) {
//        AlertDialog.Builder selectPokeWindow= new AlertDialog.Builder(context);
            final Dialog selectPokeWindow = new Dialog(context);

            //disables title
            selectPokeWindow.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //this makes cardview look
            selectPokeWindow.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            selectPokeWindow.setContentView(R.layout.select_poke_window);
            TextView cancelButton = (TextView) selectPokeWindow.findViewById(R.id.cancel1);
            RecyclerView pokesRecyclerView = (RecyclerView) selectPokeWindow.findViewById(R.id.recycler_view1);
            pokesRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            pokesAdapter = new MyPokesAdapter(context) {
                @Override
                public void onItemClick(int position) {
                    Log.d(TAG, "onItemClick ");
                    onPokemonChosen(position);
                    selectPokeWindow.dismiss();
                }
            };
            pokesRecyclerView.setAdapter(pokesAdapter);

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectPokeWindow.dismiss();
                }
            });
            if (!Config.IDLE_STATE)
                selectPokeWindow.show();
        }

    }

    public static abstract class FIGHT implements Progressable {
        public static final String TAG = FIGHT.class.getSimpleName();

        public abstract void onAttackChosen(final int position);

        public abstract PokeDetail getPokeDetail();

        public abstract int getCurrentPokemonLevel();

        public PokeAttacksAdapter getAdapter() {
            return mAdapter;
        }

        ProgressBar bar;
        PokeAttacksAdapter mAdapter;

        public FIGHT(final Progressable context) {
            Log.d(TAG, "FIGHT: " + context.toString());
            final Dialog selectAttackWindow = new Dialog(context.getActivity());
            //disables title
            selectAttackWindow.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //this makes cardview look
            selectAttackWindow.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            selectAttackWindow.setContentView(R.layout.select_poke_window);

            TextView cancelButton = (TextView) selectAttackWindow.findViewById(R.id.cancel1);
            TextView title1 = (TextView) selectAttackWindow.findViewById(R.id.title1);
            final RecyclerView attacksRecycler = (RecyclerView) selectAttackWindow.findViewById(R.id.recycler_view1);
            bar = (ProgressBar) selectAttackWindow.findViewById(R.id.progressBarLayout1);
//            bar.setVisibility(View.VISIBLE);
            attacksRecycler.setLayoutManager(new LinearLayoutManager(context.getActivity()));
            title1.setText("Select attack");
            Progressable nestedProgressable = new Progressable() {
                @Override
                public void showProgressBar(final boolean show) {
                    Log.d(TAG, "showProgressBar ");
                    context.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "hiding progressBar from Menu");
                            int visbl = show ? View.VISIBLE : View.GONE;
                            bar.setVisibility(visbl);
                            attacksRecycler.setOnTouchListener(RecyclerUtils.disableTouchEvents(show));
                        }
                    });
                }
                @Override
                public Activity getActivity() {
                    return context.getActivity();
                }

                @Override
                public void setText(CharSequence s) {
                    Log.d(TAG, "setText: " + s);
                }
            };
            mAdapter = new PokeAttacksAdapter(nestedProgressable, getPokeDetail(), getCurrentPokemonLevel()) {
                @Override
                public void onItemClick(int postion) {
                    Log.d(TAG, "onItemClick " + postion);
                    onAttackChosen(postion);
                    selectAttackWindow.dismiss();
                }
            };
            attacksRecycler.setAdapter(mAdapter);


            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectAttackWindow.dismiss();
                }
            });
            if (!Config.IDLE_STATE)
                selectAttackWindow.show();
        }
    }

    public void BAG() {
        //open bag to select pokeBalls or potions or other stuff you have
    }

}
