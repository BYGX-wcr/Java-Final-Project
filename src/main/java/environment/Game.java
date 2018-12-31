package main.java.environment;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import main.java.creature.*;
import main.java.jfxgui.Mainwindow;
import main.java.tools.GameLogger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;

public class Game {
    public enum Camp { BAD, GOOD }
    public enum Behavior { ATTACK, ENHACING }

    private final String resourcesPath;
    private final String logPath;

    public final long timeGap = 1000;
    
    private Mainwindow view;
    private final Battlefield background;
    private final GameLogger gameLogger;
    private ExecutorService exec;

    private int numOfGood;
    private int numOfBad;
    
    public Game(String res, String log) {
        resourcesPath = res;
        logPath = log;
        gameLogger = new GameLogger(log);
        background = new Battlefield(11);
    }

    //The function used to start a new game
    public void start(Mainwindow scene) {
        //绑定controller
        view = scene;

        //初始化Log文件和Logger
        File logFile = new File(logPath + System.currentTimeMillis() + ".log");
        try {
            logFile.createNewFile();
        }
        catch (IOException ioe) {
            System.err.println("Cannot create log file");
            ioe.printStackTrace();
        }
        gameLogger.initialize(GameLogger.WRITE_LOG, logFile);

        //初始化葫芦娃
        CalabashBrother[] huluwa = {
                new CalabashBrother(this, background, 6),
                new CalabashBrother(this, background, 3),
                new CalabashBrother(this, background, 0),
                new CalabashBrother(this, background, 2),
                new CalabashBrother(this, background, 4),
                new CalabashBrother(this, background, 1),
                new CalabashBrother(this, background, 5),
        };
        SortController.sort(huluwa);
        for (int i = 0; i < huluwa.length; ++i) {
            huluwa[i].setCampId(Camp.GOOD);
            huluwa[i].setLife(120);
            huluwa[i].setAtk(30 + i);
            String iconPath = getClass().getResource(resourcesPath + Integer.toString(huluwa[i].getNum() + 1) + ".png").toString();
            huluwa[i].setIcon(iconPath);
        }

        //初始化反派战斗人员
        Monster[] evils = {
                new Monster(this, background, "蝎子精"),
                new Monster(this, background, "小喽啰1"),
                new Monster(this, background, "小喽啰2"),
                new Monster(this, background, "小喽啰3"),
                new Monster(this, background, "小喽啰4"),
                new Monster(this, background, "小喽啰5"),
                new Monster(this, background, "小喽啰6"),
        };
        //单独设置蝎子精
        evils[0].setCampId(Camp.BAD);
        evils[0].setLife(150);
        evils[0].setAtk(40);
        evils[0].setIcon(getClass().getResource( resourcesPath + "scorpion.png").toString());
        //设置小喽啰
        for (int i = 1; i < evils.length; ++i) {
            evils[i].setCampId(Camp.BAD);
            evils[i].setLife(100);
            evils[i].setAtk(20);
            evils[i].setIcon(getClass().getResource(resourcesPath + "monster.png").toString());
        }

        //初始化爷爷：技能-加血
        Leader grandpa = Leader.getInstance(this, background, "爷爷");
        grandpa.setCampId(Camp.GOOD);
        grandpa.setLife(100);
        grandpa.setIcon(getClass().getResource( resourcesPath +"grandpa.png").toString());
        grandpa.setBuff(obj -> { obj.setLife(obj.getLife() + 20); });
        grandpa.setBuffSign(new Image(getClass().getResource(resourcesPath + "lifeup.png").toString()));

        //初始化蛇精：技能-加攻击力
        Leader snaker = Leader.getInstance(this, background, "蛇精");
        snaker.setCampId(Camp.BAD);
        snaker.setLife(120);
        snaker.setIcon(getClass().getResource(resourcesPath + "snaker.png").toString());
        snaker.setBuff(obj -> { obj.setAtk(obj.getAtk() + 20); });
        snaker.setBuffSign(new Image(getClass().getResource(resourcesPath + "atkup.png").toString()));

        numOfGood = huluwa.length + 1;
        numOfBad = evils.length + 1;

        initializeFormation(background, snaker, evils, grandpa, huluwa);
        updateView();

        //启动所有线程，开始战斗
        System.out.println("Game start!");
        exec = Executors.newCachedThreadPool();
        exec.execute(grandpa);
        exec.execute(snaker);
        for (int i = 0; i < huluwa.length; ++i) {
            exec.execute(huluwa[i]);
        }
        for (int i = 0; i < evils.length; ++i) {
            exec.execute(evils[i]);
        }
        exec.shutdown();
    }

    //The function used to repaly a game
    public void replay(Mainwindow scene, String filename) {
        //绑定controller
        view = scene;

        //初始化Log文件和Logger
        File logFile = new File(filename);
        gameLogger.initialize(GameLogger.READ_LOG, logFile);

        //初始化葫芦娃
        CalabashBrother[] huluwa = {
                new CalabashBrother(this, background, 6),
                new CalabashBrother(this, background, 3),
                new CalabashBrother(this, background, 0),
                new CalabashBrother(this, background, 2),
                new CalabashBrother(this, background, 4),
                new CalabashBrother(this, background, 1),
                new CalabashBrother(this, background, 5),
        };
        SortController.sort(huluwa);
        for (int i = 0; i < huluwa.length; ++i) {
            huluwa[i].setCampId(Camp.GOOD);
            huluwa[i].setLife(120);
            huluwa[i].setAtk(30 + i);
            String iconPath = getClass().getResource(resourcesPath + Integer.toString(huluwa[i].getNum() + 1) + ".png").toString();
            huluwa[i].setIcon(iconPath);
        }

        //初始化反派战斗人员
        Monster[] evils = {
                new Monster(this, background, "蝎子精"),
                new Monster(this, background, "小喽啰1"),
                new Monster(this, background, "小喽啰2"),
                new Monster(this, background, "小喽啰3"),
                new Monster(this, background, "小喽啰4"),
                new Monster(this, background, "小喽啰5"),
                new Monster(this, background, "小喽啰6"),
        };
        //单独设置蝎子精
        evils[0].setCampId(Camp.BAD);
        evils[0].setLife(150);
        evils[0].setAtk(40);
        evils[0].setIcon(getClass().getResource( resourcesPath + "scorpion.png").toString());
        //设置小喽啰
        for (int i = 1; i < evils.length; ++i) {
            evils[i].setCampId(Camp.BAD);
            evils[i].setLife(100);
            evils[i].setAtk(20);
            evils[i].setIcon(getClass().getResource(resourcesPath + "monster.png").toString());
        }

        //初始化爷爷：技能-加血
        Leader grandpa = Leader.getInstance(this, background, "爷爷");
        grandpa.setCampId(Camp.GOOD);
        grandpa.setLife(100);
        grandpa.setIcon(getClass().getResource( resourcesPath +"grandpa.png").toString());
        grandpa.setBuff(obj -> { obj.setLife(obj.getLife() + 20); });
        grandpa.setBuffSign(new Image(getClass().getResource(resourcesPath + "lifeup.png").toString()));

        //初始化蛇精：技能-加攻击力
        Leader snaker = Leader.getInstance(this, background, "蛇精");
        snaker.setCampId(Camp.BAD);
        snaker.setLife(120);
        snaker.setIcon(getClass().getResource(resourcesPath + "snaker.png").toString());
        snaker.setBuff(obj -> { obj.setAtk(obj.getAtk() + 20); });
        snaker.setBuffSign(new Image(getClass().getResource(resourcesPath + "atkup.png").toString()));

        numOfGood = huluwa.length + 1;
        numOfBad = evils.length + 1;

        initializeFormation(background, snaker, evils, grandpa, huluwa);
        //view.update(background);
        updateView();

        HashMap<String, Creature> creatureHashMap = new HashMap<>();
        creatureHashMap.put(grandpa.getName(), grandpa);
        creatureHashMap.put(snaker.getName(), snaker);
        for (int i = 0; i < huluwa.length; ++i) {
            creatureHashMap.put(huluwa[i].getName(), huluwa[i]);
        }
        for (int i = 0; i < evils.length; ++i) {
            creatureHashMap.put(evils[i].getName(), evils[i]);
        }

        String log;
        while ((log = gameLogger.readLog()) != null) {
            String[] strings = log.split(" ");
            if (strings[1].equals(GameLogger.AtomicOptType.MOVE.toString())) {
                //a MOVE record
                String master = strings[0];
                String[] coords = strings[2].split(",");
                int x = Integer.parseInt(coords[0]);
                int y = Integer.parseInt(coords[1]);
                creatureHashMap.get(master).move(x, y);
            }
            else if (strings[1].equals(GameLogger.AtomicOptType.ATK.toString())) {
                //an ATK record
                String master = strings[0];
                String slave = strings[2];
                Fighter.class.cast(creatureHashMap.get(master)).attack(creatureHashMap.get(slave));
            }
            else if (strings[1].equals(GameLogger.AtomicOptType.ENHANCE.toString())) {
                //an ENHANCE record
                String master = strings[0];
                String slave = strings[2];
                Leader.class.cast(creatureHashMap.get(master)).enhance(creatureHashMap.get(slave));
            }
            else {
                System.out.println("Unkown record" + log);
            }
            updateView();
            try {
                Thread.yield();
                Thread.sleep(10);
            }
            catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    //Game Logic
    void initializeFormation(Battlefield bg, Leader snaker, Creature[] evils, Leader grandpa, Creature[] huluwa) {
        //Initialize formations

        //长蛇阵
        for (int i = 0; i < huluwa.length; ++i) {
            huluwa[i].move(0, i + 1);
        }
        grandpa.move(0, huluwa.length + 1);

        //衡轭阵
        int s = bg.getSize() - 3;
        for (int i = 0; i < evils.length; ++i) {
            evils[i].move(s + i % 2, i + 1);
        }
        snaker.move(s, evils.length + 1);
    }
    public void decNum(Camp campId) {
        synchronized (this) {
            switch (campId) {
                case BAD: {
                    numOfBad--;
                    if (numOfBad <= 0) {
                        //win
                        System.out.println("Win " + numOfBad + " : " + numOfGood);
                        background.destroy();
                        new Thread(new Task<Void>() {
                            @Override
                            public Void call() {
                                return null;
                            }

                            @Override
                            public void succeeded() {
                                gameLogger.endWriteLog();
                                updateView();
                                view.win();
                            }
                        }).start();
                    }
                    break;
                }
                case GOOD: {
                    numOfGood--;
                    if (numOfGood <= 0) {
                        //fail
                        System.out.println("Defeat " + numOfBad + " : " + numOfGood);
                        background.destroy();
                        new Thread(new Task<Void>() {
                            @Override
                            public Void call() {
                                return null;
                            }

                            @Override
                            public void succeeded() {
                                gameLogger.endWriteLog();
                                updateView();
                                view.fail();
                            }
                        }).start();
                    }
                }
            }
        }
    }

    //Activate UI
    public void updateView() {
        new Thread(new Task<Void>() {
            @Override
            public Void call() {
                return null;
            }

            @Override
            public void succeeded() {
                view.update(background);
            }
        }).start();
    }
    public void behave(Behavior type, Creature master, Creature slave) {
        switch (type) {
            case ATTACK: {
                new Thread(new Task<Void>() {
                    @Override
                    public Void call() {
                        return null;
                    }

                    @Override
                    public void succeeded() {
                        view.atkEffect(master.getX(), master.getY(), slave.getX(), slave.getY());
                    }
                }).start();
                break;
            }
            case ENHACING: {
                new Thread(new Task<Void>() {
                    @Override
                    public Void call() {
                        return null;
                    }

                    @Override
                    public void succeeded() {
                        view.enhanceEffect(slave.getX(), slave.getY(), Leader.class.cast(master).getBuffSign());
                    }
                }).start();
                break;
            }
        }
    }

    //Log
    public void outputRecord(String record) {
        gameLogger.writeLog(record);
    }
}
