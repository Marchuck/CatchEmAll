package pl.marchuck.catchemall.JsonArium;

import android.content.Context;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import pl.marchuck.catchemall.data.PokeAbility;


/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public class PokeAbilityDeserializer implements JsonDeserializer<PokeAbility> {

    public static final String TAG = PokeAbilityDeserializer.class.getSimpleName();
    private static PokeAbilityDeserializer instance = new PokeAbilityDeserializer();
    private static Context context;// = new PokeAbilityDeserializer();

    public static PokeAbilityDeserializer getInstance(Context c) {
        context = c;
        return instance;
    }

    @Override
    public PokeAbility deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext con) throws JsonParseException {

//        Log.d(TAG, "received json: " + json.toString());
        JsonObject obj = json.getAsJsonObject();

        int id = obj.get("id").getAsInt();

        String name = obj.get("name").getAsString();
        String created = obj.get("category").getAsString();
        String modified = obj.get("modified").getAsString();
        String description = obj.get("description").getAsString();
        String resourceUri = obj.get("resource_uri").getAsString();

//        Realm realm = Realm.getInstance(context);
//        realm.beginTransaction();
//
//        RealmAbility ability = realm.createObject(RealmAbility.class);
//        ability.setCreated(created);
//        ability.setName(name);
//        ability.setModified(modified);
//        ability.setDescription(description);
//        ability.setResourceUri(resourceUri);
//        realm.commitTransaction();
//        realm.close();

        return new PokeAbility(id, name, created, modified, description, resourceUri);
    }
}
