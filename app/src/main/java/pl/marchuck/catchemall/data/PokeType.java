package pl.marchuck.catchemall.data;

/**
 * Created by Lukasz Marczak on 2015-09-18.
 */
public class PokeType {
    private Integer id;
    private String name;
    private String weakness;
    private String ineffective;
    private String superEffective;

    public PokeType(Integer id, String name, String weakness, String ineffective, String superEffective) {
        this.id = id;
        this.name = name;
        this.weakness = weakness;
        this.ineffective = ineffective;
        this.superEffective = superEffective;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeakness() {
        return weakness;
    }

    public void setWeakness(String weakness) {
        this.weakness = weakness;
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

    @Override
    public String toString() {
        return "PokeType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", weakness='" + weakness + '\'' +
                ", ineffective='" + ineffective + '\'' +
                ", superEffective='" + superEffective + '\'' +
                '}';
    }
}
