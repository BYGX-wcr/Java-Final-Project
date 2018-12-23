package environment;

import creature.Creature;

public class Position <T extends Creature> {
    T conetnt = null;

    public T getConetnt() {
        return conetnt;
    }

    public void setConetnt(T conetnt) {
        this.conetnt = conetnt;
    }
}
