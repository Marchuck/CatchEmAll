package pl.marchuck.catchemall;

import android.app.Application;
import android.content.Context;

import java.util.Random;

import pl.marchuck.catchemall.configuration.PokeUtils;

/**
 * @author Lukasz Marczak
 * @since 24.07.16.
 */
public class App extends Application {
    public static App instance;
    private Random generator = new Random();
    private PokeUtils pokeUtils = new PokeUtils();

    public static Random getGenerator() {
        return instance.generator;
    }

    public String lastpokeName;

    public static void setLatPokemonName(String name) {

        instance.lastpokeName = name;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context ctx() {
        return instance.getApplicationContext();
    }

    public static PokeUtils pokeUtils() {
        return instance.pokeUtils;
    }

    public static String lastpokeName() {
        return instance.lastpokeName;
    }
}
