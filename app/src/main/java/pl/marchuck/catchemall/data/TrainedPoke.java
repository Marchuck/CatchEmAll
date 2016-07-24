package pl.marchuck.catchemall.data;

/**
 * Created by Lukasz Marczak on 2015-09-16.
 */
public class TrainedPoke {
    private String name;
    private int maxHP;
    private int currentHP;

    public TrainedPoke(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TrainedPoke{" +
                "name='" + name + '\'' +
                ", maxHP=" + maxHP +
                ", currentHP=" + currentHP +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }
}
