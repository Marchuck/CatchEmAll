package pl.marchuck.catchemall.fragments.main;

import android.util.Log;

import pl.marchuck.catchemall.configuration.Config;
import pl.marchuck.catchemall.data.BeaconsInfo;
import pl.marchuck.catchemall.game.WeakHandler;

/**
 * @author Lukasz Marczak
 * @since 24.07.16.
 */
public class Loop {
    private  int times = Config.SCAN_TIME;
    private static String TAG = Loop.class.getSimpleName();
    private boolean isStarted = false;
    private WeakHandler handler = new WeakHandler();
    private RangeFragment parent;
    public  boolean isActive = true;

    private Runnable stepTimer = new Runnable() {
        @Override
        public void run() {
            if (isActive) {
                parent.restartScan();
                times--;
                handler.postDelayed(this, 1000); //LoopAction() is invoked every 2 seconds

                if (times < 0) {
                    parent.buildNoPokemonsHereDialog();
                    isActive = false;
                } else if (BeaconsInfo.FORCE_STOP_SCAN)
                    isActive = false;
            } else {
                resetLoop();
            }
        }

    };

    private void resetLoop() {
        Log.d(TAG, "resetLoop()");
        handler.removeCallbacks(stepTimer);
        isStarted = false;
        isActive = true;
        times = Config.SCAN_TIME;
    }

    public void start(RangeFragment rangeFragment) {
        if (!isStarted) {
            handler.postDelayed(stepTimer, 100); //start looping after 100 ms of delay
            isStarted = true;
        } else return;

        parent = rangeFragment;
    }
}