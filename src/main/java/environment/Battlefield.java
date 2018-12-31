package main.java.environment;

import main.java.creature.*;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Battlefield {
    private Position<Creature>[][] matrix;
    private final int size;
    private Lock operationLock;

    public Battlefield(int sz) {
        size = sz;
        matrix = new Position[size][];
        for (int i = 0; i < size; ++i) {
            matrix[i] = new Position[size];
            for (int j = 0; j < size; ++j) {
                matrix[i][j] = new Position<>();
            }
        }

        operationLock = new ReentrantLock();
    }

    public int getSize() {
        return size;
    }

    public void setCreature(Creature object) {
        int x = object.getX();
        int y = object.getY();

        matrix[x][y].setConetnt(object);
    }
    public Creature getCreature(int x, int y) {
        if (x < 0 || x >= size || y < 0 || y >= size)
            return null;

        return matrix[x][y].getConetnt();
    }
    public void clear(int x, int y) {
        matrix[x][y].setConetnt(null);
    }
    public void clear(Creature ref) {
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (matrix[i][j].getConetnt() == ref)
                    matrix[i][j].setConetnt(null);
            }
        }
    }
    public void destroy() {
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                Creature obj = matrix[i][j].getConetnt();
                if (obj != null) {
                    obj.kill();
                    if (obj.getLife() == 0)
                        matrix[i][j].setConetnt(null);
                }
            }
        }
    }

    public void leaveCorpse(int x, int y) { matrix[x][y].setVestige(true); }
    public boolean existCorpse(int x, int y) {
        return matrix[x][y].getVestige();
    }

    public void startOpt() {
        operationLock.lock();
    }
    public void endOpt() { operationLock.unlock(); }

    public int center() { return size / 2; }
}
