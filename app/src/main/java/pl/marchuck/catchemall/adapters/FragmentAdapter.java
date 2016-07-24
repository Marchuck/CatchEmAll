package pl.marchuck.catchemall.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import pl.marchuck.catchemall.fragments.ChoosePokemonFragment;


/**
 * Created by Lukasz Marczak on 2015-09-15.
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    private final String startPokes[] = {"Bulbasaur", "Charmander", "Squirtle"};

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return ChoosePokemonFragment.newInstance("Fragment " + i, startPokes[i]);
    }

    @Override
    public int getCount() {
        return startPokes.length;
    }

}
