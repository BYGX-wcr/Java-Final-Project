package main.java.creature;

import main.java.environment.Battlefield;
import main.java.environment.Game;

import java.util.ArrayList;

public abstract class Fighter extends Creature {

    Fighter(Game game, Battlefield bg, String argName) {
        super(game, bg, argName);
    }

    public boolean attack(Creature obj) {
        synchronized (obj) {
            ArrayList<Creature> enemy = new ArrayList<>();
            enemy.add(obj);
            world.behave(Game.Behavior.ATTACK, this, enemy);
            return obj.hurt(atk);
        }
    }

    public void run() {
        while (alive) {
            try {
                Thread.sleep(world.timeGap);
            }
            catch (InterruptedException ie) {
                alive = false;
            }

            Creature obj = march();
            if (obj != null) {
                if (obj.getCampId() != campId) {
                    attack(obj);
                    changeWorld();
                }
            }
            else {
                changeWorld();
            }
            Thread.yield();
        }
    }
}
