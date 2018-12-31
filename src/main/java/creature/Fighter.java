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
            } catch (InterruptedException ie) {
                alive = false;
            }

            groud.startOpt();
            try {
                synchronized (this) {
                    march();
                    int dx = (campId == Game.Camp.GOOD) ? 1 : -1;
                    if (groud.getCreature(x + dx, y) != null && groud.getCreature(x + dx, y).getCampId() != campId)
                        attack(groud.getCreature(x + dx, y));
                    else if (groud.getCreature(x, y + 1) != null && groud.getCreature(x, y + 1).getCampId() != campId)
                        attack(groud.getCreature(x, y + 1));
                    else if (groud.getCreature(x, y - 1) != null && groud.getCreature(x, y - 1).getCampId() != campId)
                        attack(groud.getCreature(x, y - 1));
                }
            }
            finally {
                groud.endOpt();
            }
            changeWorld();
            Thread.yield();
        }
    }
}
