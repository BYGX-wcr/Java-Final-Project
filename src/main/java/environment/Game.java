package main.java.environment;

import javafx.concurrent.Task;
import main.java.creature.*;
import main.java.jfxgui.Mainwindow;

import java.util.ArrayList;
import java.util.concurrent.*;

public class Game {
    public enum Camp { BAD, GOOD }
    public enum Behavior { ATTACK, ENHACING }

    private final String resourcesPath;

    public final long timeGap = 1000;
    
    private final Mainwindow view;
    private final Battlefield background = new Battlefield(11);
    private ExecutorService exec;

    private int numOfGood;
    private int numOfBad;
    
    public Game(Mainwindow scene, String res) {
        resourcesPath = res;
        view = scene;
    }

    public void start() {
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
        for (int i = 0; i < huluwa.length; ++i) {
            huluwa[i].setCampId(Camp.GOOD);
            huluwa[i].setLife(120);
            huluwa[i].setAtk(30 + i);
            String iconPath = getClass().getResource(resourcesPath + Integer.toString(i + 1) + ".png").toString();
            huluwa[i].setIcon(iconPath);
        }

        //初始化反派战斗人员
        Monster[] evils = {
                new Monster(this, background, "蝎子精"),
                new Monster(this, background, "小喽啰"),
                new Monster(this, background, "小喽啰"),
                new Monster(this, background, "小喽啰"),
                new Monster(this, background, "小喽啰"),
                new Monster(this, background, "小喽啰"),
                new Monster(this, background, "小喽啰"),
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
        grandpa.setBuff((obj -> { obj.setLife(obj.getLife() + 20); }));

        //初始化蛇精：技能-加攻击力
        Leader snaker = Leader.getInstance(this, background, "蛇精");
        snaker.setCampId(Camp.BAD);
        snaker.setLife(120);
        snaker.setIcon(getClass().getResource(resourcesPath + "snaker.png").toString());
        snaker.setBuff((obj -> { obj.setAtk(obj.getAtk() + 20); }));

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

    public void behave(Behavior type, Creature master, ArrayList<Creature> slaves) {
        switch (type) {
            case ATTACK: {
                new Thread(new Task<Void>() {
                    @Override
                    public Void call() {
                        return null;
                    }

                    @Override
                    public void succeeded() {
                        view.atkEffect(master.getX(), master.getY(), slaves.get(0).getX(), slaves.get(0).getY());
                    }
                }).start();
                break;
            }
        }
    }

    public void decNum(Camp campId) {
        synchronized (this) {
            switch (campId) {
                case BAD: {
                    numOfBad--;
                    if (numOfBad <= 0) {
                        //win
                        System.out.println("Win:" + numOfBad + " : " + numOfGood);
                        background.destroy();
                        new Thread(new Task<Void>() {
                            @Override
                            public Void call() {
                                return null;
                            }

                            @Override
                            public void succeeded() {
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
                        System.out.println("Defeat:" + numOfBad + " : " + numOfGood);
                        background.destroy();
                        new Thread(new Task<Void>() {
                            @Override
                            public Void call() {
                                return null;
                            }

                            @Override
                            public void succeeded() {
                                updateView();
                                view.fail();
                            }
                        }).start();
                    }
                }
            }
            System.out.println(numOfBad + " : " + numOfGood);
        }
    }
}
