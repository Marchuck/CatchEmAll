package pl.marchuck.catchemall.configuration;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import pl.marchuck.catchemall.App;
import pl.marchuck.catchemall.data.PokeID;
import pl.marchuck.catchemall.data.realm.RealmID;

/**
 * Created by Lukasz Marczak on 2015-08-24.
 */
public final class PokeUtils {
    public static final String TAG = PokeUtils.class.getSimpleName();

    public List<PokeID> netPokes = new ArrayList<>();

    public static String getPokeResByID(int ID) {
        if (ID < 1 || ID > 150)
            ID = 1;
        return "http://pokeapi.co/media/img/" + ID + ".png";

    }

    public static int getRandomPokemonID() {
        int randy = (int) System.currentTimeMillis() % 151 + 1;
        randy = randy < 0 ? -randy : randy;
        Log.d(TAG, "getRandomPokemonID " + randy);
        return randy;
    }

    public static String getPokemonNameFromId(Context context, final int pokemonID) {
        Log.d(TAG, "getPokemonNameFromId " + pokemonID);
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();
        RealmID pokeID = realm.where(RealmID.class).equalTo("id", pokemonID).findFirst();
        String pokeName = "pikachu";
        if (pokeID != null) {
            pokeName = pokeID.getName();
        }
        realm.commitTransaction();
        realm.close();
        return pokeName;
    }

    public static String getPrettyPokemonName(String pokemonName) {
        return pokemonName.substring(0, 1).toUpperCase() + pokemonName.substring(1);
    }

    public static int getPokemonIdFromName( String opponentName) {
        Realm realm = Realm.getInstance(App.ctx());
        realm.beginTransaction();
        RealmID pokeID = realm.where(RealmID.class).equalTo("name", opponentName, false).findFirst();
        int id = 25;
        if (pokeID != null) id = pokeID.getId();

        realm.commitTransaction();
        realm.close();

        return id;
    }
}
