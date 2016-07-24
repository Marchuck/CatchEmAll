package pl.marchuck.catchemall.connection;

/**
 * Created by Lukasz Marczak on 2015-08-25.
 */
public class PokeSpritesManager {

    public static String getMainPokeByName(String pokemonName) {
        return "http://img.pokemondb.net/artwork/" + preparedName(pokemonName) + ".jpg";
    }

    public static String preparedName(String pokemonName) {
        if (pokemonName == null || pokemonName.length() < 3)
            pokemonName = "pikachu";
        return pokemonName.toLowerCase();
    }


    //493 pokemons available
    public static String getPokemonBackByName(String pokomonName) {
        return "http://img.pokemondb.net/sprites/heartgold-soulsilver/back-normal/" + preparedName(pokomonName) + ".png";
    }

    //493 pokemons available
    public static String getPokemonFrontByName(String pokomonName) {
        return "http://img.pokemondb.net/sprites/heartgold-soulsilver/normal/" + preparedName(pokomonName) + ".png";
    }

}
