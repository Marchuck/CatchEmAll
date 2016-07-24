package pl.marchuck.catchemall.JsonArium;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import pl.marchuck.catchemall.data.PokeDetail;


/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public class PokeDetailDeserializer implements JsonDeserializer<PokeDetail> {

    private static Context context;
    public static final String TAG = PokeDetailDeserializer.class.getSimpleName();
    private static final PokeDetailDeserializer instance = new PokeDetailDeserializer();

    private PokeDetailDeserializer() {
    }

    public static PokeDetailDeserializer getInstance(Context c) {
        context = c;
        return instance;
    }

    @Override
    public PokeDetail deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext con) throws JsonParseException {

//        Log.d(TAG, "received json: " + json.toString());

        JsonObject object = json.getAsJsonObject();
        JsonArray abilities = object.get("abilities").getAsJsonArray();
//        JsonArray descriptions = object.get("descriptions").getAsJsonArray();
//        JsonArray egg_groups = object.get("egg_groups").getAsJsonArray();
        JsonArray evolutions = object.get("evolutions").getAsJsonArray();
        JsonArray moves = object.get("moves").getAsJsonArray();
        JsonArray types = object.get("types").getAsJsonArray();
        String abilitieS = "";
//        String descriptionS = "";
//        String egg_groupS = "";
        String evolutionS = "";
        String moveS = "";
        String typeS = "";
        if (moves.size() > 0)
            for (int x = 0; x < moves.size(); x++) {
                String moveId = moves.get(x).getAsJsonObject().get("resource_uri")
                        .getAsString().split("/")[4];
                String learnType = moves.get(x).getAsJsonObject().get("learn_type").getAsString();
                if (learnType.equals("level up"))
                    moveId += "&" + moves.get(x).getAsJsonObject().get("level").getAsString();
                moveS += moveId + ",";
            }
        if (types.size() > 0)
            for (int x = 0; x < types.size(); x++) {
                String typeId = types.get(x).getAsJsonObject().get("resource_uri").getAsString().split("/")[4];
                typeS += typeId + ",";
            }
        if (evolutions.size() > 0)
            for (int x = 0; x < evolutions.size(); x++) {
                String evolutionId = evolutions.get(x).getAsJsonObject().get("resource_uri").getAsString().split("/")[4];
                evolutionS += evolutionId + ",";
            }
        if (abilities.size() > 0)
            for (int x = 0; x < abilities.size(); x++) {
                String abilityId = abilities.get(x).getAsJsonObject().get("resource_uri").getAsString().split("/")[4];
                abilitieS += abilityId + ",";
            }

        int attack = object.get("attack").getAsInt();
        int catchRate = object.get("catch_rate").getAsInt();
        int defense = object.get("defense").getAsInt();
        int eggCycles = object.get("egg_cycles").getAsInt();
        int exp = object.get("exp").getAsInt();
        int happiness = object.get("happiness").getAsInt();
        int hp = object.get("hp").getAsInt();
        int nationalId = object.get("national_id").getAsInt();
        int pkdxId = object.get("pkdx_id").getAsInt();
        int spAtk = object.get("sp_atk").getAsInt();
        int spDef = object.get("sp_def").getAsInt();
        int speed = object.get("speed").getAsInt();
        int total = object.get("total").getAsInt();

        String created = object.get("created").getAsString();
        String evYield = object.get("ev_yield").getAsString();
        String growthRate = object.get("growth_rate").getAsString();
        String height = object.get("height").getAsString();
        String maleFemaleRatio = object.get("male_female_ratio").getAsString();
        String modified = object.get("modified").getAsString();
        String name = object.get("name").getAsString();
        String resourceUri = object.get("resource_uri").getAsString();
        String species = object.get("species").getAsString();
        String weight = object.get("weight").getAsString();

        return new PokeDetail(attack, catchRate, defense, eggCycles, exp, happiness, hp, nationalId,
                pkdxId, spAtk, spDef, speed, total, abilitieS, created, evYield, evolutionS, growthRate,
                height, maleFemaleRatio, modified, moveS, name, resourceUri, species, typeS, weight);
    }
}
