package pl.marchuck.catchemall.JsonArium;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public class PokeNetNameDeserializer implements JsonDeserializer<String> {

    public static final String TAG = PokeNetNameDeserializer.class.getSimpleName();
    public static JsonDeserializer INSTANCE = new PokeNetNameDeserializer();
    private PokeNetNameDeserializer(){}

    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        Log.d(TAG, "received json: " + json.toString());
        JsonObject object = json.getAsJsonObject();
        String name = object.get("pokemon").getAsJsonObject().get("name").getAsString();
        Log.d(TAG, "deserialized name: " + name);

        return name;
    }
}
