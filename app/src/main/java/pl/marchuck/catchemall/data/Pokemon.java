package pl.marchuck.catchemall.data;

import android.support.annotation.NonNull;

/**
 * Created by Lukasz Marczak on 2015-08-23.
 *
 *
 */
public class Pokemon {

    private final int imageResource;
    private final String name;
    private final int health;
    private final int defence;
    private final int force;
    private final int agile;
    private final String macAddress;

    public boolean equals(@NonNull Pokemon o) {
        return macAddress.equals(o.macAddress);
    }

    public Pokemon(Builder builder) {
        imageResource = builder.imageResource;
        name = builder.name;
        health = builder.health;
        defence = builder.defence;
        force = builder.force;
        agile = builder.agile;
        macAddress = builder.macAddress;
    }

    public static class Builder {

        private int imageResource;
        private String name;
        private int health;
        private int defence;
        private int force;
        private int agile;
        private final String macAddress;

        public Builder(final String mac) {
            imageResource = BeaconsInfo.PokeInterface.UNKNOWN_POKEMON_RESOURCE;
            name = "Unknown";
            health = 70;
            defence = 33;
            force = 33;
            agile = 33;
            macAddress = mac;
        }

        public Builder withImageRes(int res) {
            this.imageResource = res;
            return this;
        }

        public Builder withHealth(int health) {
            if (health > BeaconsInfo.PokeInterface.MAX_HEALTH)
                health = health % BeaconsInfo.PokeInterface.MAX_HEALTH;
            this.health = health;
            return this;
        }

        public Builder withDefence(int defence) {
            this.defence = defence;
            return this;
        }

        public Builder withAgile(int agile) {
            this.agile = agile;
            return this;
        }

        public Builder withForce(int force) {
            this.force = force;
            return this;
        }

        public Pokemon build() {
            int powers = force + agile + defence;
            int shortRef = BeaconsInfo.PokeInterface.SUM_POWERS;
            force = shortRef * force / powers;
            agile = shortRef * agile / powers;
            defence = shortRef * defence / powers;

            return new Pokemon(this);
        }
    }
}
