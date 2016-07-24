package pl.marchuck.catchemall.game;

import android.util.Log;

import pl.marchuck.catchemall.configuration.Randy;
import pl.marchuck.catchemall.data.PlayerDetail;


/**
 * Created by Lukasz Marczak on 2015-09-22.
 * does boring calculations what attack does with opponent and player
 */
public class HitCalculator {
    public static final HitCalculator INSTANCE = new HitCalculator();
    public static final String TAG = HitCalculator.class.getSimpleName();

    private HitCalculator() {
    }

    /**
     * @param yourPoke     - reference to YOU, current PLayer instance
     * @param opponentPoke - pokemon used by opponent
     * @param opponentHits - means this hit was made by opponent
     * @return
     */
    public void onHitPerformed(PlayerDetail yourPoke, PlayerDetail opponentPoke, boolean opponentHits) {
        Log.d(TAG, "calculateHitPower ");
        if (opponentHits) {

            boolean accuracySuccess = true;
            int accuracy = opponentPoke.getCurrentMoveUsed().getAccuracy();
            if (MovesHelper.missable(accuracy)) {
                accuracySuccess = Randy.withAccuracy(accuracy);
            }
            if(accuracySuccess){
                Log.i(TAG, "");
            }else{

            }

        } else {

        }
    }

}
