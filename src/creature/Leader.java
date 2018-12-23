package creature;

import environment.Battlefield;
import environment.Formation;

import java.io.*;
import java.util.ArrayList;

enum LeaderEnum {
    grandpa("爷爷", "$"),
    snake("蛇精", "&");

    private String name;
    private String sign;

    LeaderEnum(String argName, String argSign) {
        name = argName;
        sign = argSign;
    }

    public String getName() {
        return name;
    }
    public String getSign() {
        return sign;
    }
}

public class Leader extends Creature {
    ArrayList<Formation> strategies;
    int nowPtr;
    LeaderEnum id;

    public Leader(String argName) {
        strategies = new ArrayList<>(2);
        nowPtr = 0;

        for (int i = 0; i < LeaderEnum.values().length; ++i) {
            if (LeaderEnum.values()[i].getName().equals(argName)) {
                id = LeaderEnum.values()[i];
                sign = id.getSign();
                return;
            }
        }
        id = null;
        sign = "!";
        System.out.println("Cannot find Leader:" + argName);
    }

    public void addFormation(Formation fm) {
        strategies.add(fm);
    }
    public void addFormation(File fp) {
        BufferedReader configFile;
        try {
            configFile = new BufferedReader(new FileReader(fp));
        }
        catch (FileNotFoundException e)
        {
            System.err.println("File Not Found:" + fp);
            return;
        }

        try {
            String sepStr = ",| ";
            while (true) {
                String str = configFile.readLine();
                if (str == null)
                    break;
                String[] input1 = str.split(sepStr);
                str = configFile.readLine();
                String[] input2 = str.split(sepStr);

                int[] leaderPos = new int[2];
                int[][] posData = new int[Formation.getSize()][Formation.getDimension()];
                for (int i = 0; i < Formation.getDimension(); ++i) {
                    leaderPos[i] = Integer.parseInt(input1[i + 1]);
                }

                for (int i = 0; i < Formation.getSize(); ++i) {
                    for (int j = 0; j < Formation.getDimension(); ++j) {
                        posData[i][j] = Integer.parseInt(input2[i * 2 + j]);
                    }
                }

                strategies.add(new Formation(posData, input1[0], leaderPos));
            }
        }
        catch (IOException ioe) {
            System.err.println("[Read Error]Cannot resolve the file: " + fp.getAbsolutePath());
        }
        catch (NumberFormatException nfe) {
            System.err.println("[FormatError]Cannot resolve the file:" + fp.getAbsolutePath());
        }
    }
    public void embattle(Battlefield world, Creature[] kids) {
        nowPtr = (nowPtr + 1) % strategies.size();
        Formation fm = strategies.get(nowPtr);

        move(world, fm.getLeaderPos()[0], fm.getLeaderPos()[1]);

        for (int i = 0; i < kids.length; ++i) {
            int[] loc = fm.getCoord(i);
            kids[i].move(world, loc[0], loc[1]);
        }
    }
}
