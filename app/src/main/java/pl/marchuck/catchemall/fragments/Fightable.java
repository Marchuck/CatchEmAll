package pl.marchuck.catchemall.fragments;

/**
 * Created by Lukasz Marczak on 2015-09-20.
 */
public interface Fightable extends Progressable{

    void decreasePoke(int i);

    void decreaseOpponent(int i);
}
