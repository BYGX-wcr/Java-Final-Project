package test.java;

import main.java.creature.Creature;
import main.java.environment.Battlefield;
import main.java.environment.Game;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatureTest {
    @Test
    public void testHurt() {
        Creature testObj = new Creature(new Game(".", "."), new Battlefield(11), "Tester") {
            @Override
            public void run() {

            }
        };
        testObj.setCampId(Game.Camp.GOOD);

        testObj.setLife(50);
        assertEquals(testObj.hurt(100), false);

        testObj = new Creature(new Game(".", "."), new Battlefield(11), "Tester") {
            @Override
            public void run() {

            }
        };
        testObj.setCampId(Game.Camp.GOOD);

        testObj.setLife(50);
        assertEquals(testObj.hurt(20), true);
    }
}
