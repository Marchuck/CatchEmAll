package pl.marchuck.catchemall.configuration;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import pl.marchuck.catchemall.data.realm.RealmMove;

/**
 * Created by Lukasz Marczak on 2015-09-21.
 */
public class RecyclerUtils {
    public static final String TAG = RecyclerUtils.class.getSimpleName();

    public static View.OnTouchListener disableTouchEvents(final boolean disallow) {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return disallow;
            }
        };
    }

    /**
     * @param context   -needed for realm queries
     * @param encoded - list of moves: 2,3,5.. etc
     * @return - readableMoves
     */
    public static String readableMoves(Context context, String encoded) {
        Log.d(TAG, "readableMoves " + encoded);
        String result = "";
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();

        List<Integer> moves = getMovesFromString(encoded);

        for (Integer nextId : moves) {
            RealmMove moveIds = realm.where(RealmMove.class).equalTo("id", nextId).findFirst();
            if (moveIds != null) {
                result += moveIds.getName();
            }
        }

        realm.commitTransaction();
        realm.close();
        return result;
    }

    private static List<Integer> getMovesFromString(String adjective) {
        List<Integer> types = new ArrayList<>();
        String[] vals = adjective.split(",");
        for (String value : vals) {
            int j = -1;
            try {
                j = Integer.valueOf(value);
            } catch (NumberFormatException ex) {
                Log.e(TAG, "failed to convert " + value);
                j = -1;
            } finally {
                if (j != -1)
                    types.add(j);
            }
        }
        return types;
    }
}
