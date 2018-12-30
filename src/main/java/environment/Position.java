package main.java.environment;

import main.java.creature.Creature;

public class Position <T extends Creature> {
    T conetnt = null;
    boolean vestige = false;

    public T getConetnt() {
        return conetnt;
    }
    public void setConetnt(T conetnt) {
        this.conetnt = conetnt;
    }

    public boolean getVestige() { return vestige; }
    public void setVestige(boolean flag) { vestige = flag; }
}
