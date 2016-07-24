package pl.marchuck.catchemall.data;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import pl.marchuck.catchemall.App;

public class AppFirstLauncher {

    public static void setup() {
        Log.d("AppFirstLauncher", "setup ");
        SharedPreferences.Editor editor = App.ctx().getSharedPreferences("IF_APP_FIRST_LAUNCHED", Context.MODE_PRIVATE).edit();
        editor.putBoolean("IF_APP_FIRST_LAUNCHED", Boolean.FALSE);
        editor.apply(); /**difference is here: editor.commit() was previous.
         people say apply() is faster than commit(). because apply is asynchronus */
    }

    public static boolean ifAppFirstLaunched() {
        Log.d("AppFirstLauncher", "ifAppFirstLaunched ");
        return App.ctx().getSharedPreferences("IF_APP_FIRST_LAUNCHED", Context.MODE_PRIVATE)
                .getBoolean("IF_APP_FIRST_LAUNCHED", true);
    }
}