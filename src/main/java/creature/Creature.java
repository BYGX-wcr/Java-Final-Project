package main.java.creature;

import javafx.scene.image.Image;
import main.java.environment.Battlefield;
import main.java.environment.Game;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

abstract public class Creature implements Runnable {
    int x;
    int y;

    final String name;
    boolean alive;
    int atk;
    int life;
    Game.Camp campId;
    Image icon;

    final Battlefield groud;
    final Game world;

    public Creature(Game game, Battlefield bg, String str) {
        x = y = 0;
        alive = true;
        atk = 0;
        life = 0;
        groud = bg;
        world = game;
        name = str;
    }

    public void changeWorld() {
        world.updateView();
    }
    public void move(int arg1, int arg2) {
        //System.out.println(name + " move to " + "[" + arg1 + "," + arg2 + "]");
        if (groud.getCreature(x, y) == this) {
            groud.clear(x, y);
        }
        x = arg1;
        y = arg2;
        groud.setCreature(this);
    }
    @Nullable
    public Creature march() {
        final int c = groud.center();
        int dx = (c - x) > 0 ? 1 : -1;
        int dy = (c - y) > 0 ? 1 : -1;

        Random rand = new Random(System.currentTimeMillis());
        int direction = rand.nextInt() % 2;
        int nx = x;
        int ny = y;

        //0.5的概率向x方向移动，0.5的概率向y方向移动
        if (direction != 0) {
            if (dx != 0)
                nx = x + dx;
            else
                ny = y + dy;
        }
        else {
            if (dy != 0)
                ny = y + dy;
            else
                nx = x + dx;
        }

        //如果目标位置没人则进入位置，否则返回
        synchronized (groud) {
            Creature obstacle = groud.getCreature(nx, ny);
            if (obstacle != null) {
                return obstacle;
            } else {
                move(nx, ny);
                return null;
            }
        }
    }

    public void setLife(int arg) {
        life = arg;
    }
    public void setAtk(int arg) { atk = arg; }
    public void setCampId(Game.Camp arg) {
        campId = arg;
    }
    public void setIcon(String path) {
        icon = new Image(path);
    }
    public boolean hurt(int value) {
        if (alive == false) return false;
        life -= value;

        if (life <= 0) {
            alive = false;
            synchronized (groud) {
                System.out.println(name + " die at [" + x + "," + y + "]");
                groud.clear(x, y);
                groud.leaveCorpse(x, y);
                world.decNum(campId);
            }
            life = 0;
        }

        return alive;
    }
    public void kill() {
        alive = false;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getLife() { return life; }
    public int getAtk() { return atk; }
    public Game.Camp getCampId() { return campId; }
    public Image getIcon() { return icon; }
}
