package test.java;

import main.java.creature.CalabashBrother;
import main.java.environment.Battlefield;
import main.java.environment.Game;
import main.java.environment.SortController;
import org.junit.Test;
import static org.junit.Assert.*;

public class SortControllerTest {

    @Test
    public void testSort() {
        Game mainGame = new Game(".", ".");
        Battlefield background = new Battlefield(11);
        CalabashBrother[] huluwa = {
                new CalabashBrother(mainGame, background, 6),
                new CalabashBrother(mainGame, background, 3),
                new CalabashBrother(mainGame, background, 0),
                new CalabashBrother(mainGame, background, 2),
                new CalabashBrother(mainGame, background, 4),
                new CalabashBrother(mainGame, background, 1),
                new CalabashBrother(mainGame, background, 5),
        };

        SortController.sort(huluwa);
        for (int i = 0; i < huluwa.length; ++i) {
            assertEquals(huluwa[i].getNum(), i);
        }
    }
}
