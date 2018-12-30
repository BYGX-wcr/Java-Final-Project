package main.java.jfxgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("mainwindow.fxml"));
        Scene scene = new Scene(root, 1400, 720);

        stage.setTitle("Java Final Project Application");
        stage.setScene(scene);
        stage.setWidth(1440);
        stage.setHeight(900);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}