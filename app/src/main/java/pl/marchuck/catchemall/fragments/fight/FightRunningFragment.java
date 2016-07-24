package pl.marchuck.catchemall.fragments.fight;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tt.whorlviewlibrary.WhorlView;

import java.util.List;

import io.realm.Realm;
import pl.marchuck.catchemall.App;
import pl.marchuck.catchemall.R;
import pl.marchuck.catchemall.activities.FightActivity;
import pl.marchuck.catchemall.configuration.Config;
import pl.marchuck.catchemall.configuration.PokeUtils;
import pl.marchuck.catchemall.configuration.Randy;
import pl.marchuck.catchemall.connection.PokeDetailDownloader;
import pl.marchuck.catchemall.connection.PokeSpritesManager;
import pl.marchuck.catchemall.data.PokeDetail;
import pl.marchuck.catchemall.data.PokeMove;
import pl.marchuck.catchemall.data.realm.DBManager;
import pl.marchuck.catchemall.data.realm.RealmPokeDetail;
import pl.marchuck.catchemall.fragments.Fightable;
import pl.marchuck.catchemall.fragments.Progressable;
import pl.marchuck.catchemall.game.Engine;
import pl.marchuck.catchemall.game.SelectMenuEngine;
import pl.marchuck.catchemall.game.WeakHandler;

public class FightRunningFragment extends Fragment implements Fightable {

    final static String TAG = FightRunningFragment.class.getSimpleName();
    private ImageView yourPokemon, opponentPokemon;
    private WhorlView action;
    private FightActivity parentActivity;
    private String opponentName = "";
    private boolean strongPokemon = Randy.randomAnswer();
    private ProgressBar opponentHP;
    private ProgressBar yourPokeHP;
    private TextView yourPokeNameTV;
    private TextView question, message;
    private Engine engineInstance;
    private boolean nothingWasClicked = true;
    private SelectMenuEngine.POKEMON pokemonSelector;
    private SelectMenuEngine.FIGHT fightInstance;

    public static FightRunningFragment newInstance(String opponent) {
        FightRunningFragment fragment = new FightRunningFragment();
        fragment.opponentName = opponent;
        return fragment;
    }

    public FightRunningFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchPokemonDetails(opponentName);
    }

    private void fetchNewSelectedPokemonData(Fightable context, String pokemonName) {
        Log.d(TAG, "fetchNewSelectedPokemonData ");
        int yourPokeID = PokeUtils.getPokemonIdFromName(pokemonName);
        Config.IDLE_STATE = true;
        //check if pokemon has already downloaded moves
        Realm realm = Realm.getInstance(context.getActivity());
        realm.beginTransaction();
        RealmPokeDetail detail = realm.where(RealmPokeDetail.class).equalTo("name", pokemonName, false).findFirst();
        realm.commitTransaction();
        if (detail == null) {
            new PokeDetailDownloader() {
                @Override
                public void onDataReceived(PokeDetail detail) {
                    Log.i(TAG, "onDataReceived " + detail.getName());
                    engineInstance.setYourPokeDetail(detail);
                    Config.IDLE_STATE = false;
                }
            }.start(this, yourPokeID);
        } else {
            Log.d(TAG, "already downloaded data, return from database");
            engineInstance.setYourPokeDetail(DBManager.asPokeDetail(detail));
            Config.IDLE_STATE = false;
        }
        engineInstance.allowMoveOpponent(this);
    }

    private void fetchPokemonDetails(String opponentName) {
        Log.d(TAG, "fetchPokemonDetails ");
        if (engineInstance == null) engineInstance = Engine.create();
        int pokemonID = PokeUtils.getPokemonIdFromName( opponentName);
        int yourPokeID = PokeUtils.getPokemonIdFromName( App.lastpokeName());
        RealmPokeDetail poke = Realm.getInstance(getActivity()).where(RealmPokeDetail.class)
                .equalTo("pkdxId", pokemonID).findFirst();
        if (poke == null) {
            Log.e(TAG, "poke detail " + pokemonID + " is null, fetching in progress...");
            new PokeDetailDownloader() {
                @Override
                public void onDataReceived(PokeDetail detail) {
                    Log.i(TAG, "onDataReceived " + detail.getName());
                    engineInstance.setOpponentDetail(detail);
                }
            }.start(this, pokemonID);

            new PokeDetailDownloader() {
                @Override
                public void onDataReceived(PokeDetail detail) {
                    Log.i(TAG, "onDataReceived " + detail.getName());
                    engineInstance.setYourPokeDetail(detail);
                }
            }.start(this, yourPokeID);
        } else {
            engineInstance.setOpponentDetail(DBManager.asPokeDetail(poke));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fight_running, container, false);
        action = (WhorlView) view.findViewById(R.id.action);
        yourPokemon = (ImageView) view.findViewById(R.id.your_pokemon_back);
        opponentPokemon = (ImageView) view.findViewById(R.id.opponent_front);
        TextView fight = (TextView) view.findViewById(R.id.fight);
        TextView bag = (TextView) view.findViewById(R.id.bag);
        TextView select = (TextView) view.findViewById(R.id.select_pokemon);
        message = (TextView) view.findViewById(R.id.announce);
        TextView run = (TextView) view.findViewById(R.id.run);
        question = (TextView) view.findViewById(R.id.question);
        question.setText("What will \n " + PokeUtils.getPrettyPokemonName(App.lastpokeName()) + "\n do?");

        fight.setOnTouchListener(getTouchListener(0, fight));
        bag.setOnTouchListener(getTouchListener(1, bag));
        select.setOnTouchListener(getTouchListener(2, select));
        run.setOnTouchListener(getTouchListener(3, run));

        RelativeLayout includedOpponentBar = (RelativeLayout) view.findViewById(R.id.opponent_front_status);
        RelativeLayout yourPokemonBar = (RelativeLayout) view.findViewById(R.id.your_pokemon_back_status);
        TextView opponentNameTV = (TextView) includedOpponentBar.findViewById(R.id.pokemon_name);
        yourPokeNameTV = (TextView) yourPokemonBar.findViewById(R.id.pokemon_name);

        TextView opponentLevel = (TextView) includedOpponentBar.findViewById(R.id.level);
        TextView yourPokeLevel = (TextView) yourPokemonBar.findViewById(R.id.level);

        //HP indicators
        opponentHP = (ProgressBar) includedOpponentBar.findViewById(R.id.progressBar2);
        yourPokeHP = (ProgressBar) yourPokemonBar.findViewById(R.id.progressBar2);

        yourPokeHP.setProgress(100);
        opponentHP.setProgress(100);

        opponentLevel.setText("Level " + Randy.from(10));
        yourPokeLevel.setText("Level 4");

        opponentNameTV.setText(PokeUtils.getPrettyPokemonName(opponentName));
        yourPokeNameTV.setText(PokeUtils.getPrettyPokemonName(App.lastpokeName() ));

        Picasso.with(getActivity()).load(getOpponentPokemonResource()).into(opponentPokemon);
        Picasso.with(getActivity()).load(getYourPokemonResource()).into(yourPokemon);
        return view;
    }

    private View.OnTouchListener getTouchListener(final int mode, final TextView view) {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch");
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_UP: {
                        Log.d(TAG, "action up");
                        view.setBackgroundColor(Color.TRANSPARENT);
                        break;
                    }
                    case MotionEvent.ACTION_DOWN: {
                        Log.d(TAG, "action down");
                        switch (mode) {
                            case 0: {
                                if (!Config.IDLE_STATE) {
                                    view.setBackgroundColor(Color.LTGRAY);
                                    Log.d(TAG, "onTouch select attack");
                                    fightInstance = new SelectMenuEngine.FIGHT((Progressable) FightRunningFragment.this) {

                                        @Override
                                        public void showProgressBar(boolean show) {

                                        }

                                        @Override
                                        public Activity getActivity() {
                                            return FightRunningFragment.this.getActivity();
                                        }

                                        @Override
                                        public void setText(CharSequence s) {

                                        }

                                        @Override
                                        public void onAttackChosen(int position) {
                                            final PokeMove move = fightInstance.getAdapter().dataset.get(position);
                                            Log.d(TAG, "Player " + engineInstance.getYourPoke().getName() + " used "
                                                    + move.getName() + "!!!");
                                            if (engineInstance.getOpponentPoke().getHp() > 0) {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        decreaseOpponent(move.getPower() / 3);
                                                    }
                                                });
                                                performInjuryAnimation(opponentPokemon);
                                            }else{
                                                switchToWinnerState();return;
                                            }
                                            engineInstance.allowMoveOpponent(FightRunningFragment.this);
                                        }

                                        @Override
                                        public PokeDetail getPokeDetail() {
                                            Log.d(TAG, "getPokeDetail ");
                                            return engineInstance.getYourPoke().getDetail();
                                        }

                                        @Override
                                        public int getCurrentPokemonLevel() {
                                            Log.d(TAG, "getCurrentPokemonLevel ");
                                            return engineInstance.getYourPoke().getCurrentLevel();
                                        }
                                    };
                                    break;
                                }
                            }
                            case 1: {
                                Log.d(TAG, "BAG not supported yet");
                                view.setBackgroundColor(Color.LTGRAY);

                                break;
                            }
                            case 2: {
                                if (!Config.IDLE_STATE) {
                                    new SelectMenuEngine.POKEMON(getActivity()) {
                                        @Override
                                        public void onPokemonChosen(final int position) {
                                            Log.d(TAG, "onPokemonChosen ");
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Log.d(TAG, "run ");
                                                    String newPokeName = fightInstance.getAdapter().dataset.get(position).getName();
                                                    String newPokeResource = PokeSpritesManager
                                                            .getPokemonBackByName(newPokeName);
                                                    Picasso.with(getActivity()).load(newPokeResource).into(yourPokemon);
                                                    String prettyName = PokeUtils.getPrettyPokemonName(newPokeName);
                                                    yourPokeNameTV.setText(prettyName);
                                                    question.setText("What will \n" + prettyName + " do?");
                                                    fetchNewSelectedPokemonData(FightRunningFragment.this, newPokeName);
                                                }
                                            });
                                        }
                                    };
                                    view.setBackgroundColor(Color.LTGRAY);
                                    break;
                                }
                            }
                            case 3: {
                                if (!Config.IDLE_STATE)
                                    if (!strongPokemon) parentActivity.onBackPressed();
                                    else
                                        Snackbar.make(getView(), "Cannot run from this fight!", Snackbar.LENGTH_LONG).show();
                                view.setBackgroundColor(Color.LTGRAY);
                                break;
                            }
                            default:
                                Log.d(TAG, "mode " + mode);
                        }
                        break;
                    }
                }
                return true;
            }
        };
    }

    private void performDeathAnimation(final boolean yours) {
        new WeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (yours) {
                    yourPokemon.animate().alpha(0).xBy(-100).yBy(100).setDuration(1800).start();
                } else {
                    opponentPokemon.animate().alpha(0).xBy(100).yBy(-100).setDuration(1800).start();
                }
            }
        }, 300);
    }

    private void performInjuryAnimation(final View opponentPokemon) {
        for (int j = 0; j < 500; j += 100) {
            new WeakHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    opponentPokemon.setVisibility(View.INVISIBLE);
                }
            }, j);
            new WeakHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    opponentPokemon.setVisibility(View.VISIBLE);
                }
            }, j + 50);

        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

    public String getYourPokemonResource() {
        return PokeSpritesManager.getPokemonBackByName(App.lastpokeName());
    }

    public String getOpponentPokemonResource() {
        return PokeSpritesManager.getPokemonFrontByName(opponentName);
    }

    @Override
    public void showProgressBar(final boolean show) {
        Log.d(TAG, "showProgressBar " + show);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int vis = show ? View.VISIBLE : View.INVISIBLE;
                if (action != null) {
                    action.setVisibility(vis);
                    if (!action.isCircling() && action.isShown())
                        action.start();
                    else if (!action.isShown())
                        action.stop();
                }
            }
        });
    }

    @Override
    public void setText(CharSequence s) {
        Log.d(TAG, "setText ");
        message.setText(s);
    }

    @Override
    public void decreasePoke(int i) {
        performInjuryAnimation(yourPokemon);
        Log.d(TAG, "decreasePoke " + i);
        int total = engineInstance.getYourPoke().getHp() - i;
        engineInstance.getYourPoke().setHp(total);
        if (total < 0) {
            switchToDeathPokeState();
            total = 0;
        }
        yourPokeHP.setProgress(total);

    }

    public void switchToDeathPokeState() {
        Log.d(TAG, "switchToDeathPokeState ");
        Config.deadPokemons.add(engineInstance.getYourPoke().getName());
        performDeathAnimation(true);
        Snackbar.make(getView(), "Oops! Your " + engineInstance.getYourPoke().getName() + " fainted. Continue? ", Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick ");
                nothingWasClicked = false;
                pokemonSelector = new SelectMenuEngine.POKEMON(getActivity()) {
                    @Override
                    public void onPokemonChosen(int position) {
                        Log.d(TAG, "onPokemonChosen " + position);
                        String newPokeName = pokemonSelector.pokesAdapter.dataSet.get(position).getName();
                        if (contains(Config.deadPokemons, newPokeName)) {
                            Log.i(TAG, "cannot choose this pokemon!!!");
                            Snackbar.make(getView(), "This pokemon is fainted!!!", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        String newPokeResource = PokeSpritesManager.getPokemonBackByName(newPokeName);
                        Picasso.with(getActivity()).load(newPokeResource).into(yourPokemon);
                        String prettyName = PokeUtils.getPrettyPokemonName(newPokeName);
                        yourPokeNameTV.setText(prettyName);
                        question.setText("What will " + prettyName + " do?");
                        fetchNewSelectedPokemonData(FightRunningFragment.this, newPokeName);
                    }
                };
            }
        }).show();
        new WeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (nothingWasClicked)
                    getActivity().onBackPressed();
            }
        }, 5000);
    }

    private boolean contains(List<String> deadPokemons, String newPokeName) {
        for (String s : deadPokemons) {
            if (s.equals(newPokeName))
                return true;
        }
        return false;
    }

    @Override
    public void decreaseOpponent(int i) {
        Log.d(TAG, "decreaseOpponent " + i);
        int total = engineInstance.getOpponentPoke().getHp() - i;
        engineInstance.getOpponentPoke().setHp(total);
        if (total < 0) {
            switchToWinnerState();
            total = 0;
        }
        opponentHP.setProgress(total);
    }

    public void switchToWinnerState() {
        performDeathAnimation(false);
        Log.d(TAG, "switchToWinnerState ");
        Snackbar.make(getView(), "Congratulations! You have beaten " + opponentName, Snackbar.LENGTH_LONG).show();
        engineInstance = null;
        new WeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getActivity().onBackPressed();
            }
        }, 15000);
    }
}
