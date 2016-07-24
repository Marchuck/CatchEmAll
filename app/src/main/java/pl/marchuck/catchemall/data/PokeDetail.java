package pl.marchuck.catchemall.data;

/**
 * Created by Lukasz Marczak on 2015-09-13.
 */
public class PokeDetail {

    private int attack;
    private int catchRate;
    private int defense;
    private int eggCycles;
    private int exp;
    private int happiness;
    private int hp;
    private int nationalId;
    private int pkdxId;
    private int spAtk;
    private int spDef;
    private int speed;
    private int total;

    private String abilities;
    private String created;
    //    private String descriptions;
//    private String eggGroups;
    private String evYield;
    private String evolutions;
    private String growthRate;
    private String height;
    private String maleFemaleRatio;
    private String modified;
    private String moves;
    private String name;
    private String resourceUri;
    private String species;
    //    private String sprites;
    private String types;
    private String weight;

    @Override
    public String toString() {
        return "PokeDetail{" +
                ", attack=" + attack +
                ", catchRate=" + catchRate +
                ", defense=" + defense +
                ", eggCycles=" + eggCycles +
                ", exp=" + exp +
                ", happiness=" + happiness +
                ", hp=" + hp +
                ", nationalId=" + nationalId +
                ", pkdxId=" + pkdxId +
                ", spAtk=" + spAtk +
                ", spDef=" + spDef +
                ", speed=" + speed +
                ", total=" + total +
                ", abilities='" + abilities + '\'' +
                ", created='" + created + '\'' +
                ", evYield='" + evYield + '\'' +
                ", evolutions='" + evolutions + '\'' +
                ", growthRate='" + growthRate + '\'' +
                ", height='" + height + '\'' +
                ", maleFemaleRatio='" + maleFemaleRatio + '\'' +
                ", modified='" + modified + '\'' +
                ", moves='" + moves + '\'' +
                ", name='" + name + '\'' +
                ", resourceUri='" + resourceUri + '\'' +
                ", species='" + species + '\'' +
                ", types='" + types + '\'' +
                ", weight='" + weight + '\'' +
                '}';
    }

    public PokeDetail(PokeDetail poke) {
        this(poke.getAttack(), poke.getCatchRate(), poke.getDefense(), poke.getEggCycles(), poke.getExp(), poke.getHappiness(),
                poke.getHp(), poke.getNationalId(), poke.getPkdxId(), poke.getSpAtk(), poke.getSpDef(), poke.getSpeed(), poke.getTotal(), poke.getAbilities(),
                poke.getCreated(), poke.getEvYield(), poke.getEvolutions(), poke.getGrowthRate(), poke.getHeight(), poke.getMaleFemaleRatio(),
                poke.getModified(), poke.getMoves(), poke.getName(), poke.getResourceUri(), poke.getSpecies(), poke.getTypes(), poke.getWeight());
    }

    public PokeDetail(int attack, int catchRate, int defense, int eggCycles, int exp,
                      int happiness, int hp, int nationalId, int pkdxId, int spAtk, int spDef, int speed, int total, String abilities, String created, String evYield, String evolutions, String growthRate, String height, String maleFemaleRatio, String modified, String moves, String name, String resourceUri, String species, String types, String weight) {
        this.attack = attack;
        this.catchRate = catchRate;
        this.defense = defense;
        this.eggCycles = eggCycles;
        this.exp = exp;
        this.happiness = happiness;
        this.hp = hp;
        this.nationalId = nationalId;
        this.pkdxId = pkdxId;
        this.spAtk = spAtk;
        this.spDef = spDef;
        this.speed = speed;
        this.total = total;
        this.abilities = abilities;
        this.created = created;
        this.evYield = evYield;
        this.evolutions = evolutions;
        this.growthRate = growthRate;
        this.height = height;
        this.maleFemaleRatio = maleFemaleRatio;
        this.modified = modified;
        this.moves = moves;
        this.name = name;
        this.resourceUri = resourceUri;
        this.species = species;
        this.types = types;
        this.weight = weight;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getCatchRate() {
        return catchRate;
    }

    public void setCatchRate(int catchRate) {
        this.catchRate = catchRate;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getEggCycles() {
        return eggCycles;
    }

    public void setEggCycles(int eggCycles) {
        this.eggCycles = eggCycles;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getHappiness() {
        return happiness;
    }

    public void setHappiness(int happiness) {
        this.happiness = happiness;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getNationalId() {
        return nationalId;
    }

    public void setNationalId(int nationalId) {
        this.nationalId = nationalId;
    }

    public int getPkdxId() {
        return pkdxId;
    }

    public void setPkdxId(int pkdxId) {
        this.pkdxId = pkdxId;
    }

    public int getSpAtk() {
        return spAtk;
    }

    public void setSpAtk(int spAtk) {
        this.spAtk = spAtk;
    }

    public int getSpDef() {
        return spDef;
    }

    public void setSpDef(int spDef) {
        this.spDef = spDef;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getAbilities() {
        return abilities;
    }

    public void setAbilities(String abilities) {
        this.abilities = abilities;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getEvYield() {
        return evYield;
    }

    public void setEvYield(String evYield) {
        this.evYield = evYield;
    }

    public String getEvolutions() {
        return evolutions;
    }

    public void setEvolutions(String evolutions) {
        this.evolutions = evolutions;
    }

    public String getGrowthRate() {
        return growthRate;
    }

    public void setGrowthRate(String growthRate) {
        this.growthRate = growthRate;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getMaleFemaleRatio() {
        return maleFemaleRatio;
    }

    public void setMaleFemaleRatio(String maleFemaleRatio) {
        this.maleFemaleRatio = maleFemaleRatio;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getMoves() {
        return moves;
    }

    public void setMoves(String moves) {
        this.moves = moves;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public PokeDetail() {
    }

//    @Override
//    public String toString() {
//        String softTypes = "", evolves = "";
//        String evolvesInto = "", _types = "";
//
//
//        if (!types.isEmpty()) {
//            for (int k = 0; k < types.size(); k++) {
//                String s = types.get(k);
//                if (k != types.size() - 1)
//                    softTypes += s + ",";
//                else softTypes += s;
//
//            }
//            _types = "types : " + softTypes;
//
//        }
//
//
//        if (!evolvesIntoList.isEmpty()) {
//            for (int k = 0; k < evolvesIntoList.size(); k++) {
//                String s = evolvesIntoList.get(k);
//                if (k != evolvesIntoList.size() - 1)
//                    evolves += s + ",";
//                else evolves += s;
//
//            }
//            evolvesInto = "Evolves into " + evolves;
//
//        }
//
//        return "attack : " + attack + "\n"
//                + "defense : " + defense + "\n"
//                + "HP : " + hp + "\n"
//                + "height : " + height + "\n"
//                + "weight : " + weight + "\n"
//                + "speed : " + speed + "\n"
//                + _types + "\n"
//                + evolvesInto;
//    }
}
