package main.java.creature;

import main.java.environment.Battlefield;
import main.java.environment.Game;
import main.java.tools.AtomicOperation;
import main.java.tools.GameLogger;
import main.java.tools.ThreadOperation;

public abstract class Fighter extends Creature {

    Fighter(Game game, Battlefield bg, String argName) {
        super(game, bg, argName);
    }

    @AtomicOperation(type = GameLogger.AtomicOptType.ATK, operatorType = Fighter.class)
    public void attack(Creature obj) {
        if (!alive) return;
        synchronized (obj) {
            world.outputRecord(name + GameLogger.AtomicOptType.ATK.getStr() + obj.getName());
            obj.hurt(atk);
        }
    }

    @ThreadOperation(operatorType = Fighter.class)
    public void tour() {
        int dx = (campId == Game.Camp.GOOD) ? 1 : -1;
        if (groud.getCreature(x + dx, y) != null && groud.getCreature(x + dx, y).getCampId() != campId) {
            Creature obj = groud.getCreature(x + dx, y);
            world.behave(Game.Behavior.ATTACK, this, obj);
            attack(obj);
        }
        else if (groud.getCreature(x, y + 1) != null && groud.getCreature(x, y + 1).getCampId() != campId) {
            Creature obj = groud.getCreature(x, y + 1);
            world.behave(Game.Behavior.ATTACK, this, obj);
            attack(obj);
        }
        else if (groud.getCreature(x, y - 1) != null && groud.getCreature(x, y - 1).getCampId() != campId) {
            Creature obj = groud.getCreature(x, y - 1);
            world.behave(Game.Behavior.ATTACK, this, obj);
            attack(obj);
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
                    tour();
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
