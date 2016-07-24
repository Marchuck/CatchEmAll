package pl.marchuck.catchemall.data;


/**
 * Created by Lukasz Marczak on 2015-09-22.
 */
public class PlayerDetail extends PokeDetail {

    int currentLevel;
    PokeType pokemonType;
    PokeMove currentMoveUsed;

    public PokeMove getCurrentMoveUsed() {
        return currentMoveUsed;
    }

    public void setCurrentMoveUsed(PokeMove currentMoveUsed) {
        this.currentMoveUsed = currentMoveUsed;
    }

    public PlayerDetail(PokeDetail detail, int currentLevel) {
        super(detail);
        this.currentLevel = currentLevel;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public PokeType getPokemonType() {
        return pokemonType;
    }

    public void setPokemonType(PokeType pokemonType) {
        this.pokemonType = pokemonType;
    }
}

