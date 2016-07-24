package pl.marchuck.catchemall.data;

/**
 * Created by Lukasz Marczak on 2015-09-18.
 */
public class PokeMove {
    private int id;
    private int pp;
    private int power;
    private int accuracy;

    private String name;
    private String created;
    private String category;
    private String modified;
    private String description;
    private String resourceUri;

    @Override
    public String toString() {
        return "PokeMove{" +
                "id=" + id +
                ", pp=" + pp +
                ", power=" + power +
                ", accuracy=" + accuracy +
                ", name='" + name + '\'' +
                ", created='" + created + '\'' +
                ", category='" + category + '\'' +
                ", modified='" + modified + '\'' +
                ", description='" + description + '\'' +
                ", resourceUri='" + resourceUri + '\'' +
                '}';
    }

    public PokeMove(int id, int pp, int power, int accuracy, String name,
                    String created, String category, String modified,
                    String description, String resourceUri) {
        this.id = id;
        this.pp = pp;
        this.power = power;
        this.accuracy = accuracy;
        this.name = name;
        this.created = created;
        this.category = category;
        this.modified = modified;
        this.description = description;
        this.resourceUri = resourceUri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPp() {
        return pp;
    }

    public void setPp(int pp) {
        this.pp = pp;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }
}
