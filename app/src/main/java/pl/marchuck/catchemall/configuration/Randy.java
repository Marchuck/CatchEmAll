package pl.marchuck.catchemall.configuration;

import pl.marchuck.catchemall.App;

/**
 * Created by Lukasz Marczak on 2015-09-14.
 * Apply randomness to game!!!
 */
public class Randy {

    public static int from(int j) {
        return App.getGenerator().nextInt(j < 0 ? -j : j);

    }

    public static boolean randomAnswer() {
        return App.getGenerator().nextBoolean();
    }

    public static boolean withAccuracy(int accuracy) {
        return from(101) < accuracy;
    }
}
