package environment;

import creature.*;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class Game {
    public static void main(String[] args) {
        Battlefield world = new Battlefield(7);

        CalabashBrother[] huluwa = {
                new CalabashBrother(6),
                new CalabashBrother(3),
                new CalabashBrother(0),
                new CalabashBrother(2),
                new CalabashBrother(4),
                new CalabashBrother(1),
                new CalabashBrother(5),
        };
        SortController.sort(huluwa);

        Monster[] evils = {
                new Monster("蝎子精", "@"),
                new Monster("小喽啰", "*"),
                new Monster("小喽啰", "*"),
                new Monster("小喽啰", "*"),
                new Monster("小喽啰", "*"),
                new Monster("小喽啰", "*"),
                new Monster("小喽啰", "*"),
        };

        Leader grandpa = new Leader("爷爷");
        Leader snaker = new Leader("蛇精");

        initializeFormation(snaker, grandpa);

        while (true) {
            snaker.embattle(world, evils);
            grandpa.embattle(world, huluwa);

            world.print();

            //Produce a time gap
            for (int i = 0; i < 1e+9; ++i);
            System.out.print("\n\n");
        }
    }

    static void initializeFormation(Leader snaker, Leader grandpa) {
        //Initialize formations
        snaker.addFormation(new File("res\\monsterFormationConfig.txt"));
        grandpa.addFormation(new File("res\\huluwaFormationConfig.txt"));
    }
}
