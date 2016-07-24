package pl.marchuck.catchemall.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.tt.whorlviewlibrary.WhorlView;

import pl.marchuck.catchemall.App;
import pl.marchuck.catchemall.R;
import pl.marchuck.catchemall.configuration.Config;
import pl.marchuck.catchemall.configuration.PokeConstants;
import pl.marchuck.catchemall.data.AppFirstLauncher;
import pl.marchuck.catchemall.download.First151Downloader;
import pl.marchuck.catchemall.fragments.main.Changeable;
import pl.marchuck.catchemall.fragments.main.PokeTypesFragment;
import pl.marchuck.catchemall.fragments.main.PokedexFragment;
import pl.marchuck.catchemall.fragments.main.RangeFragment;
import pl.marchuck.catchemall.fragments.main.RealmPokeFragment;
import pl.marchuck.catchemall.fragments.main.TripFragment;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout progressBarLayout;
    private FrameLayout main;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] headers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent data = getIntent();
        App.setLatPokemonName(data.getStringExtra(PokeConstants.NAME));

        mTitle = mDrawerTitle = getTitle();
        headers = Config.HEADERS;//getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        progressBarLayout = (RelativeLayout) findViewById(R.id.progressBarLayout);
        main = (FrameLayout) findViewById(R.id.content_frame);
        WhorlView whorl = (WhorlView) findViewById(R.id.progressBar_main);
        whorl.start();
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, headers));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.abc_action_bar_home_description,  /* "open drawer" description for accessibility */
                R.string.abc_search_hint  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {

                //getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }

        boolean isFirstLaunch = AppFirstLauncher.ifAppFirstLaunched();
        Log.i(TAG, "isFirstLaunch : " + isFirstLaunch);
        if (isFirstLaunch) {
            AppFirstLauncher.setup();
            downloadAllData();
        } else {
            showProgressBar(false);
        }
    }

    public void showProgressBar(boolean show) {
        Log.d(TAG, "showProgressBar " + show);
        int vis = show ? View.VISIBLE : View.GONE;
        progressBarLayout.setVisibility(vis);
    }

    private void downloadAllData() {
        Log.d(TAG, "download Pokedex ");

        First151Downloader.downloadData(new Changeable() {
            @Override
            public void onChange(final boolean flag) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgressBar(flag);
                    }
                });
            }
        });
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    public void selectItem(int position) {
        Log.d(TAG, "selectItem " + position);
        Fragment fragment = getFragmentForPosition(position);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out)
                .replace(R.id.content_frame, fragment)
                .commit();

        mDrawerList.setItemChecked(position, true);
        setTitle(headers[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private Fragment getFragmentForPosition(int selectedFragment) {
        Fragment fragment;
        Config.CURRENT_FRAGMENT = selectedFragment;
        switch (selectedFragment) {
            case Config.FRAGMENT.TRIP:

                Log.v(TAG, "switchToFragment - trip");
                fragment = TripFragment.newInstance(App.lastpokeName());
                break;
            case Config.FRAGMENT.RANGE:
                Log.v(TAG, "switchToFragment - range");
                fragment = RangeFragment.newInstance();
                break;
            case Config.FRAGMENT.POKEDEX:
                Log.v(TAG, "switchToFragment - POKEDEX");
                fragment = PokedexFragment.newInstance();
                break;
            case Config.FRAGMENT.ALL_POKEMONS:
                Log.v(TAG, "switchToFragment - POKEDEX");
                fragment = RealmPokeFragment.newInstance();
                break;
            case Config.FRAGMENT.ALL_POKEMON_TYPES:
                Log.v(TAG, "switchToFragment - POKEDEX");
                fragment = PokeTypesFragment.newInstance();
                break;
            default:
                Log.v(TAG, "switchToFragment - main fragment");
                fragment = TripFragment.newInstance(App.lastpokeName());
                break;
        }
        return fragment;
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        if (getActionBar() != null)
            getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "request code: " + requestCode);
        Log.d(TAG, "result code: " + resultCode);
    }

    @Override
    public void onBackPressed() {
        if (Config.CURRENT_FRAGMENT != 0) {
            selectItem(0);
        } else super.onBackPressed();
    }

    public void setActionBarTitle(String s) {
        Log.d(TAG, "setActionBarTitle " + s);
        if (getSupportActionBar() != null) setTitle(s);
        else Log.e(TAG, "action bar is null!!!");
    }
}
