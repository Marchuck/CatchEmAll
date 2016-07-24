package pl.marchuck.catchemall.game;

/**
 * Created by Lukasz Marczak on 2015-09-22.
 */
public class MovesHelper {

    public static boolean missable(int accuracy) {
        return !(accuracy == 100 || accuracy == 0);
    }

    public interface MoveID {
        int DEFENSE = 0;
        int ATTACK = 1;
        int UNKNOWN = 2;
    }

    public static int check(int moveId) {
        switch (moveId) {
            case 1:
                return MoveID.ATTACK;
        }
        return MoveID.UNKNOWN;
    }

}
