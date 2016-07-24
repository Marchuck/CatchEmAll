package pl.marchuck.catchemall.data;

import java.util.ArrayList;
import java.util.List;

import pl.marchuck.catchemall.R;
import uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice;
import uk.co.alt236.bluetoothlelib.device.beacon.BeaconType;
import uk.co.alt236.bluetoothlelib.device.beacon.BeaconUtils;

/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public class BeaconsInfo {

    public static boolean FORCE_STOP_SCAN = false;
    public static int SCANTIME = 60; //how much seconds scanner should seek for beacons
    public static boolean NEW_FIGHT; //prevent to start 2 or more fights at the same time

    public interface MAC {
        String Primeape = "F2:8E:23:A3:17:E6";
        String Sandshrew = "D7:04:1D:B2:11:C9";
    }

    public static Boolean isBeacon(BluetoothLeDevice device) {
        return BeaconUtils.getBeaconType(device) == BeaconType.IBEACON;
    }

    public interface PokeInterface {
        int UNKNOWN_POKEMON_RESOURCE = R.drawable.unknown;
        int MAX_HEALTH = 102;
        int SUM_POWERS = 102;
        String POKEMON_NAME = "POKEMON_NAME";
        String POKEMON_ID = "POKEMON_ID";
    }

    private static List<Pokemon> pokemons = new ArrayList<>();

    public interface Bundler {
        String MAC = "MAC_ADDRESS";
        String RSSI = "RSSI";
        String MINOR = "MINOR";
        String MAJOR = "MAJOR";
        String CALIBRATED_POWER = "CALIBRATED_POWER";
        String ACCURACY = "ACCURACY";
    }

    public static class PokeManager {

        public static void addNewPoke(Pokemon newPokemon) {
            boolean canAdd = true;
            for (Pokemon poke : pokemons) {
                if (poke.equals(newPokemon))
                    canAdd = false;
            }
            if (canAdd)
                pokemons.add(newPokemon);
        }

        public static void deletePoke(Pokemon delPokemon) {

            List<Pokemon> tempList = new ArrayList<>();
            for (Pokemon poke : pokemons) {
                if (!poke.equals(delPokemon))
                    tempList.add(poke);
            }
            pokemons.clear();
            pokemons.addAll(tempList);
        }
    }
}
