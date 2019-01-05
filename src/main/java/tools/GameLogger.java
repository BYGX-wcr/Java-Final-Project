package main.java.tools;

import main.java.creature.Creature;

import java.io.*;
import java.nio.Buffer;

public class GameLogger {
    public enum AtomicOptType {
        MOVE, ATK, ENHANCE;

        public String getStr() {
            return " " + toString() + " ";
        }
    }

    public static final boolean WRITE_LOG = true;
    public static final boolean READ_LOG = false;

    private String directory;
    private Boolean logFlag = null; //true-write log, false-read log
    private BufferedReader logReader = null;
    private BufferedWriter logWriter = null;

    public GameLogger(String dir) {
        directory = dir;
    }

    public void initialize(Boolean flag, File logFile) {
        logFlag = flag;
        if (flag == READ_LOG) {//read log
            try {
                logReader = new BufferedReader(new FileReader(logFile));
            }
            catch (FileNotFoundException fnfe) {
                System.err.println("Can not find file: " + logFile.getAbsolutePath());
                logReader = null;
            }
        }
        else {//write log
            try {
                logWriter = new BufferedWriter(new FileWriter(logFile));
            }
            catch (Exception e) {
                System.err.println("Can not find file: " + logFile.getAbsolutePath());
                logWriter = null;
            }
        }
    }

    public void writeLog(String info) {
        if (logFlag == READ_LOG) {
            return;
        }
        else if (logWriter == null) {
            System.err.println("Logger hasn't been initialized to write log!");
            return;
        }

        synchronized (this) {
            try {
                logWriter.write(info + "\n");
            } catch (IOException ioe) {
                System.err.println("Write Log Error!");
                ioe.printStackTrace();
            }
        }
    }

    public void endWriteLog() {
        if (logFlag == READ_LOG) return;

        synchronized (this) {
            try {
                logWriter.close();
            } catch (Exception e) {
                System.err.println("End Write Log Error!");
                e.printStackTrace();
            }
        }
        logWriter = null;
    }

    public String readLog() {
        if (logFlag == null || logReader == null) {
            System.err.println("Logger hasn't been initialized to read log!");
            return null;
        }

        String res = new String();
        try {
             res = logReader.readLine();
        }
        catch (IOException ioe) {
            System.err.println("Read Log Error!");
            ioe.printStackTrace();
        }

        return res;
    }
}
