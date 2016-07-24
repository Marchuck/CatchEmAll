package pl.marchuck.catchemall.connection;

import pl.marchuck.catchemall.data.PokeAbility;
import pl.marchuck.catchemall.data.PokeDetail;
import pl.marchuck.catchemall.data.PokeID;
import pl.marchuck.catchemall.data.PokeMove;
import pl.marchuck.catchemall.data.PokeType;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public interface PokeApi {
    String POKEMON_API_ENDPOINT = "http://pokeapi.co";
    int POKEMONS_COUNT = 493;
    int MOVES_COUNT = 626;
    int ABILITIES_COUNT = 248;
    int TYPES_COUNT = 18;

    @GET("/api/v1/sprite/{id}")
    rx.Observable<Response> getPokemonNameByID(@Path("id") Integer id);

    @GET("/api/v1/pokemon/{id}")
    rx.Observable<PokeID> getPokemonByID(@Path("id") Integer id);

    //718 pokemons available, but we download only 493 due to limited sprites available
    @GET("/api/v1/pokemon/{id}/")
    rx.Observable<PokeDetail> getPokemonDetail(@Path("id") Integer id);

    //18 types available
    @GET("/api/v1/type/{id}/")
    rx.Observable<PokeType> getPokemonType(@Path("id") Integer id);

    //626 moves available
    @GET("/api/v1/move/{id}/")
    rx.Observable<PokeMove> getPokemonMove(@Path("id") Integer id);

    //248 abilitiess available
    @GET("/api/v1/ability/{id}/")
    rx.Observable<PokeAbility> getPokemonAbility(@Path("id") Integer id);
}
