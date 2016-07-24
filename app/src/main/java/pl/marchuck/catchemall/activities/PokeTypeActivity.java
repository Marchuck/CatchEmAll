package pl.marchuck.catchemall.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import io.realm.Realm;
import pl.marchuck.catchemall.R;
import pl.marchuck.catchemall.configuration.PokeConstants;
import pl.marchuck.catchemall.data.realm.RealmType;

public class PokeTypeActivity extends AppCompatActivity {
    public static final String TAG = PokeTypeActivity.class.getSimpleName();

   private TextView ineffective, superEffective, weakness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_type);
        injectViews();

        Intent receivedType = getIntent();
        int id = receivedType.getIntExtra(PokeConstants.ID, 1);
        Realm realm = Realm.getInstance(this);
        realm.beginTransaction();
        RealmType typo = realm.where(RealmType.class).equalTo("id", id).findFirst();
        if (typo != null) {
            setTitle(typo.getName());
            superEffective.setText("SuperEffective: " + typo.getSuperEffective());
            weakness.setText("Weakness: " + typo.getWeakness());
            ineffective.setText("InEffective: " + typo.getIneffective());
            superEffective.setBackgroundColor(getResources().getColor(R.color.superEffective));
            weakness.setBackgroundColor(getResources().getColor(R.color.weakness));
            ineffective.setBackgroundColor(getResources().getColor(R.color.ineffective));
        }
        realm.commitTransaction();
        realm.close();
    }

    private void injectViews() {
        Log.d(TAG, "injectViews ");
        superEffective = (TextView) findViewById(R.id.supereff);
        weakness = (TextView) findViewById(R.id.weakness);
        ineffective = (TextView) findViewById(R.id.ineff);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_poke_type, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
