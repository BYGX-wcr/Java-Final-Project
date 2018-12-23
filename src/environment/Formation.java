package environment;

import java.io.*;

public class Formation {
    final static int num = 7;
    final static int dimension = 2;
    String name;
    int[] leaderPos;
    int[][] array;
    {
        array = new int[num][];
        for (int i = 0; i < num; ++i) {
            array[i] = new int[dimension];
        }
        leaderPos = new int[dimension];
    }

    public Formation(int[][] argArray, String str, int[] argLeaderPos) {
        for (int i = 0; i < num; ++i) {
            for (int j = 0; j < dimension; ++j) {
                array[i][j] = argArray[i][j];
            }
        }
        name = str;
        leaderPos = argLeaderPos;
    }

    static public int getSize() {
        return num;
    }
    static public int getDimension() {
        return dimension;
    }
    public int[] getCoord(int n) {
        if (n < 0 || n >= num)
            return null;
        else
            return array[n];
    }
    public int[] getLeaderPos() {
        return leaderPos;
    }
}
