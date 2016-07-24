package pl.marchuck.catchemall.JsonArium;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import pl.marchuck.catchemall.data.PokeType;


/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public class PokeTypeDeserializer implements JsonDeserializer<PokeType> {
    public static Context context;
    public static final String TAG = PokeTypeDeserializer.class.getSimpleName();

    public static PokeTypeDeserializer getInstance(Context c) {
        context = c;
        return instance;
    }

    private static final PokeTypeDeserializer instance = new PokeTypeDeserializer();

    private PokeTypeDeserializer() {
    }

    @Override
    public PokeType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext con) throws JsonParseException {

//        Log.d(TAG, "received json: " + json.toString());
        JsonObject obj = json.getAsJsonObject();
        int id = obj.get("id").getAsInt();
        String name = obj.get("name").getAsString();
        JsonArray in_effective = obj.get("ineffective").getAsJsonArray();
        JsonArray super_effective = obj.get("super_effective").getAsJsonArray();
        JsonArray weak_ness = obj.get("weakness").getAsJsonArray();

        String inEffective = "";
        String superEffective = "";
        String weak = "";

        if (in_effective != null && in_effective.size() > 0)
            for (int x = 0; x < in_effective.size(); x++)
                inEffective += in_effective.get(x).getAsJsonObject().get("name").getAsString() + ",";

        if (super_effective != null && super_effective.size() > 0)
            for (int x = 0; x < super_effective.size(); x++)
                superEffective += super_effective.get(x).getAsJsonObject().get("name").getAsString() + ",";

        if (weak_ness != null && weak_ness.size() > 0)
            for (int x = 0; x < weak_ness.size(); x++)
                weak += weak_ness.get(x).getAsJsonObject().get("name").getAsString() + ",";

        return new PokeType(id, name, weak, inEffective, superEffective);
    }
}
