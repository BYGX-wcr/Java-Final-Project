package environment;

import creature.*;

public class Battlefield {
    Position<Creature>[][] matrix;
    final int size;

    public Battlefield(int sz)
    {
        size = sz;
        matrix = new Position[size][];
        for (int i = 0; i < size; ++i) {
            matrix[i] = new Position[size];
            for (int j = 0; j < size; ++j) {
                matrix[i][j] = new Position<>();
            }
        }
    }

    public void setCreature(Creature object) {
        int x = object.getX();
        int y = object.getY();

        matrix[x][y].setConetnt(object);
    }
    public Creature getCreature(int x, int y) {
        return matrix[x][y].getConetnt();
    }
    public void clear(int x, int y) {
        matrix[x][y].setConetnt(null);
    }

    public void print() {
        for (int i = size - 1; i >= 0; --i) {
            for (int j = 0; j < size; ++j) {
                if (matrix[j][i].getConetnt() != null) {
                    matrix[j][i].getConetnt().display();
                    System.out.print("  ");
                }
                else {
                    System.out.print("   ");
                }
            }
            System.out.print('\n');
        }
    }
}
