package pl.marchuck.catchemall.game;

import android.os.Handler;
import android.util.Log;

import java.util.List;

import pl.marchuck.catchemall.configuration.Config;
import pl.marchuck.catchemall.configuration.Randy;
import pl.marchuck.catchemall.data.PokeDetail;
import pl.marchuck.catchemall.data.PokeMove;
import pl.marchuck.catchemall.data.realm.DBManager;
import pl.marchuck.catchemall.download.MovesDownloader;
import pl.marchuck.catchemall.fragments.Fightable;
import pl.marchuck.catchemall.fragments.fight.FightRunningFragment;


/**
 * Created by Lukasz Marczak on 2015-09-20.
 */
public class Engine {
    public static final String TAG = Engine.class.getSimpleName();

    private final Player yourPoke;
    private final Player opponentPoke;

    public Player getYourPoke() {
        return yourPoke;
    }

    public Player getOpponentPoke() {
        return opponentPoke;
    }

    private Engine() {
        yourPoke = new Player();
        opponentPoke = new Player();
    }

    public void setOpponentDetail(PokeDetail detail) {
        Log.d(TAG, "setOpponentDetail ");
        opponentPoke.setDetail(detail);

    }

    public void setYourPokeDetail(PokeDetail detail) {
        Log.d(TAG, "setOpponentDetail ");
        yourPoke.setDetail(detail);
    }

    public void allowMoveOpponent(final Fightable fightable) {
        Log.d(TAG, "allowMoveOpponent ");
        Config.IDLE_STATE = true;
        new WeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fightable.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fightable.setText("???");
                    }
                });
            }
        }, 500);

        final List<Integer> moves = DBManager.extractMoves(getOpponentPoke().getDetail().getMoves());

        new WeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run ");
                new MovesDownloader() {
                    @Override
                    public void onDataReceived(List<PokeMove> moves) {
                        Log.d(TAG, "onDataReceived ");
                        final PokeMove randomMove = moves.get(Randy.from(moves.size()));
                        Log.d(TAG, "Opponent used " + randomMove.getName());
                        fightable.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fightable.setText("Opponent used " + randomMove.getName());
                                fightable.decreasePoke(randomMove.getPower() / 3);
                            }
                        });
                    }
                }.start(fightable, moves);
            }
        }, 2000);


        Config.IDLE_STATE = false;
    }

    public static Engine create() {
        return new Engine();
    }
}
