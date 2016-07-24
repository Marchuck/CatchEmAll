package pl.marchuck.catchemall.data;


/**
 * Created by Lukasz Marczak on 2015-09-19.
 */
public class PokeAbility {

    private int id;
    private String name;
    private String created;
    private String modified;
    private String description;
    private String resourceUri;

    @Override
    public String toString() {
        return "PokeAbility{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", created='" + created + '\'' +
                ", modified='" + modified + '\'' +
                ", description='" + description + '\'' +
                ", resourceUri='" + resourceUri + '\'' +
                '}';
    }

    public PokeAbility(int id, String name, String created, String modified, String description, String resourceUri) {
        this.id = id;
        this.name = name;
        this.created = created;
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
