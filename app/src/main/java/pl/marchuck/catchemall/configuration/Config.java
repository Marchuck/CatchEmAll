package pl.marchuck.catchemall.configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public class Config {

    public static final int SCAN_TIME = 60;
    public static String[] HEADERS = {"Trip", "Pokedex", "Range", "Options", "Stats", "All pokes", "Types", "Exit"};
    public final static int MORE_POKES = 20;
    public final static int MAX_POKES = 150;
    public static boolean IDLE_STATE = false;

    public static List<String> deadPokemons = new ArrayList<>();

    public interface FRAGMENT {
        int TRIP = 0;
        int POKEDEX = 1;
        int RANGE = 2;
        int OPTIONS = 3;
        int STATS = 4;
        int ALL_POKEMONS = 5;
        int ALL_POKEMON_TYPES = 6;
        int EXIT = 7;
        int START_FIGHT = 8;
        int RUNNING_FIGHT = 9;
    }

    public static int CURRENT_FRAGMENT;

    public interface IntentCode {
        int FAILED_WILD_FIGHT = 1;
        int FAILED_PERSONAL_FIGHT = 2;
        int WON_WILD_FIGHT = 3;
        int WON_PERSONAL_FIGHT = 4;
        int START_WILD_FIGHT = 5;
        int START_PERSONAL_FIGHT = 6;
        int EXIT_WILD_FIGHT = 7;
    }
}
