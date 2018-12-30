package main.java.creature;

import main.java.environment.Battlefield;
import main.java.environment.Game;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

enum LeaderEnum {
    grandpa("爷爷"),
    snake("蛇精");

    private String name;

    LeaderEnum(String argName) {
        name = argName;
    }

    public String getName() {
        return name;
    }
}

public class Leader extends Creature {
    LeaderEnum id;
    Enhancing buff = null;

    private Leader(Game game, Battlefield bg, String argName) {
        super(game, bg, argName);
        for (int i = 0; i < LeaderEnum.values().length; ++i) {
            if (LeaderEnum.values()[i].getName().equals(argName)) {
                id = LeaderEnum.values()[i];
                return;
            }
        }
        id = null;
    }

    public void setBuff(Enhancing skill) {
        buff = skill;
    }

    public void strengthen() {
        for (int i = x - 1; i <= x +1; ++i) {
            for (int j = y - 1; j <= y + 1; ++j) {
                synchronized (groud) {
                    Creature obj = groud.getCreature(i, j);
                    if (obj != this && obj != null && obj.getCampId() == campId) {
                        synchronized (obj) {
                            buff.enhance(obj);
                        }
                    }
                }
            }
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

            Random rand = new Random(System.currentTimeMillis());
            int prob = rand.nextInt(10);
            if (prob < 3) {
                strengthen();
                changeWorld();
            }

            if (march() == null) {
                changeWorld();
            }
            Thread.yield();
        }
    }

    //Factory Method
    @Nullable
    public static Leader getInstance(Game game, Battlefield bg, String name) {
        Leader newMember = new Leader(game, bg, name);
        if (newMember.id == null) {
            System.out.println("Cannot find Leader:" + name);
            return null;
        }
        else {
            return newMember;
        }
    }
}
