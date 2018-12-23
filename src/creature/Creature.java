package creature;

import environment.Battlefield;

public class Creature {
    int x;
    int y;
    String sign;

    public Creature()
    {
        sign = " ";
    }

    public void display() {
        System.out.print(sign);
    }
    public void move(Battlefield world, int arg1, int arg2) {
        if (world.getCreature(x, y) == this) {
            world.clear(x, y);
        }
        x = arg1;
        y = arg2;
        world.setCreature(this);
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
