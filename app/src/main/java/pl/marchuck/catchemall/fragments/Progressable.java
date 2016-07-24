package pl.marchuck.catchemall.fragments;

import android.app.Activity;

/**
 * Created by Lukasz Marczak on 2015-09-20.
 */
public interface Progressable {

    void showProgressBar(boolean show);

    Activity getActivity();

    void setText(CharSequence s);
}
