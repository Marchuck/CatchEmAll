package pl.marchuck.catchemall.JsonArium;

import android.content.Context;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import pl.marchuck.catchemall.data.PokeID;


/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public class PokeIdDeserializer implements JsonDeserializer<PokeID> {

    public static final String TAG = PokeIdDeserializer.class.getSimpleName();
    private static PokeIdDeserializer instance = new PokeIdDeserializer();

    public static PokeIdDeserializer getInstance(Context c) {
        return instance;
    }

    @Override
    public PokeID deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext con) throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();

        String name = obj.get("name").getAsString();
        int id = obj.get("pkdx_id").getAsInt();

        return new PokeID(id, name);
    }
}
