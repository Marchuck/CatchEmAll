package pl.marchuck.catchemall.data;

/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public class NetPoke {

    private int ID;

    private String name;
//    private Bitmap

    public int getID() {
        return ID;
    }

    public String getName() {
        return  name;
    }

    public NetPoke(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
