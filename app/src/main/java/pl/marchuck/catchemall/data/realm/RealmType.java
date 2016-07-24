package pl.marchuck.catchemall.data.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Lukasz Marczak on 2015-09-13.
 */
public class RealmType extends RealmObject {
    private int id;
    @PrimaryKey
    private String uuid;
    private String name;
    private String ineffective;
    private String superEffective;
    private String weakness;

    public RealmType() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getIneffective() {
        return ineffective;
    }

    public void setIneffective(String ineffective) {
        this.ineffective = ineffective;
    }

    public String getSuperEffective() {
        return superEffective;
    }

    public void setSuperEffective(String superEffective) {
        this.superEffective = superEffective;
    }

    public String getWeakness() {
        return weakness;
    }

    public void setWeakness(String weakness) {
        this.weakness = weakness;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
