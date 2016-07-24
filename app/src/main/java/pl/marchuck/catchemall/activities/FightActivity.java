package pl.marchuck.catchemall.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;

import pl.marchuck.catchemall.R;
import pl.marchuck.catchemall.configuration.Config;
import pl.marchuck.catchemall.configuration.PokeUtils;
import pl.marchuck.catchemall.data.BeaconsInfo;
import pl.marchuck.catchemall.fragments.fight.FightRunningFragment;
import pl.marchuck.catchemall.fragments.fight.StartFightFragment;


public class FightActivity extends AppCompatActivity {

    public static final String TAG = FightActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_fight);
        switchToFragment(Config.FRAGMENT.START_FIGHT, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fight, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + ")");
        switch (requestCode) {
            case Config.IntentCode.START_WILD_FIGHT: {

                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "result OK");
                }
                if (resultCode == RESULT_CANCELED) {
                    Log.d(TAG, "there's no result");
                }
                break;
            }
            default: {
                Log.d(TAG, "nothing");
            }
        }
    }

    public void switchToFragment(int fragmentId, String opponentName) {
        Fragment fragment;
        int[] animRes;
        switch (fragmentId) {
            case Config.FRAGMENT.START_FIGHT:
                fragment = StartFightFragment.newInstance(PokeUtils.getRandomPokemonID());
                animRes = new int[]{R.anim.fab_in, R.anim.fab_out};
                break;
            case Config.FRAGMENT.RUNNING_FIGHT:
                fragment = FightRunningFragment.newInstance(opponentName);
                animRes = new int[]{R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_shrink_fade_out_from_bottom};
                break;
            default:
                fragment = StartFightFragment.newInstance(PokeUtils.getRandomPokemonID());
                animRes = new int[]{R.anim.fab_in, R.anim.fab_out};
        }

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(animRes[0], animRes[1], animRes[0], animRes[1])
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed()");
        BeaconsInfo.FORCE_STOP_SCAN = false;
        BeaconsInfo.NEW_FIGHT = false;
    }
}
