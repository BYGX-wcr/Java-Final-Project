package main.java.jfxgui;

import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.Bloom;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import main.java.creature.Creature;
import main.java.environment.Battlefield;
import main.java.environment.Game;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Mainwindow implements Initializable {
    public AnchorPane backPane;
    public TextField gameTitle;
    public Button startButon;
    public Button replayButton;
    public ImageView backGroundImage;
    public ArrayList<VBox> icons = new ArrayList<>();

    Game mainGame;
    private final String resourcesPath = "../../resources/";
    private final String logPath = "log/";

    final int unitIconWidth = 50;
    final int unitIconHeight = 50;
    final int unitIconHSpace = (int)(unitIconWidth * 0.3);
    final int unitIconVSpace = (int)(unitIconHeight * 0.3);

    double originX = 720 - unitIconWidth * 5;
    double originY = 250;

    //Fixed Image resources
    final Image tombStone = new Image(getClass().getResource(resourcesPath + "tombstone.png").toString());
    final Image sword = new Image(getClass().getResource(resourcesPath + "sword.png").toString());

    public void initialize(URL location, ResourceBundle resources) {
        startButon.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startGame();
            }
        });

        replayButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                replayGame();
            }
        });
    }

    public void startGame() {
        setInitSection(false);

        mainGame = new Game(resourcesPath, logPath);
        mainGame.start(this);
    }

    public void replayGame() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("打开游戏记录");
        fileChooser.setInitialDirectory(new File(logPath));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("游戏记录文档", "*.log")
        );
        File selectedFile = fileChooser.showOpenDialog(backPane.getScene().getWindow());
        if (selectedFile != null) {
            setInitSection(false);
            mainGame = new Game(resourcesPath, logPath);
            mainGame.replay(this, selectedFile.getAbsolutePath());
        }
    }

    private void setInitSection(boolean flag) {
        //隐藏初始界面
        gameTitle.setVisible(flag);
        gameTitle.setManaged(flag);
        startButon.setVisible(flag);
        startButon.setManaged(flag);
        replayButton.setVisible(flag);
        replayButton.setManaged(flag);
    }
    private void clearIcons() {
        backPane.getChildren().removeAll(icons);
        icons.clear();
    }
    public void update(Battlefield bg) {
        clearIcons();
        bg.startOpt();
        try {
            for (int x = 0; x < bg.getSize(); ++x) {
                for (int y = 0; y < bg.getSize(); ++y) {
                    Creature obj = bg.getCreature(x, y);
                    if (obj != null) {
                        if (obj.getLife() == 0) {
                            bg.clear(obj);
                            continue;
                        }
                        //存在生物，绘制它的图像
                        VBox iconBox = new VBox();
                        icons.add(iconBox);
                        backPane.getChildren().add(iconBox);

                        iconBox.setLayoutX(originX + x * (unitIconWidth + unitIconHSpace));
                        iconBox.setLayoutY(originY + y * (unitIconHeight + unitIconVSpace));
                        iconBox.setMaxWidth(unitIconWidth);
                        iconBox.setMaxHeight(unitIconHeight);

                        Rectangle lifeBar = new Rectangle();
                        lifeBar.setWidth((bg.getCreature(x, y).getLife() / 150.0) * unitIconWidth);
                        lifeBar.setHeight(10);
                        lifeBar.setFill(Color.GREEN);
                        iconBox.getChildren().add(lifeBar);

                        ImageView image = new ImageView();
                        image.setImage(bg.getCreature(x, y).getIcon());
                        image.setFitHeight(unitIconHeight);
                        image.setFitWidth(unitIconWidth);
                        iconBox.getChildren().add(image);
                    } else if (bg.existCorpse(x, y)) {
                        //存在尸体，绘制墓碑
                        VBox iconBox = new VBox();
                        icons.add(iconBox);
                        backPane.getChildren().add(iconBox);

                        iconBox.setLayoutX(originX + x * (unitIconWidth + unitIconHSpace));
                        iconBox.setLayoutY(originY + y * (unitIconHeight + unitIconVSpace));
                        iconBox.setMaxWidth(unitIconWidth);
                        iconBox.setMaxHeight(unitIconHeight);

                        ImageView image = new ImageView();
                        image.setImage(tombStone);
                        image.setFitHeight(unitIconHeight);
                        image.setFitWidth(unitIconWidth);
                        iconBox.getChildren().add(image);
                    }
                }
            }
        }
        finally {
            bg.endOpt();
        }
    }

    public void renewWindow() {
        for (int i = 0; i < icons.size(); ++i) {
        }
    }

    public void win() {
        ImageView image = new ImageView();
        Image victory = new Image(getClass().getResource(resourcesPath + "victory.png").toString());
        image.setImage(victory);

        backPane.getChildren().add(image);
        image.setFitWidth(800);
        image.setFitHeight(500);
        image.setLayoutX((backPane.getWidth() - image.getFitWidth()) / 2);
        image.setLayoutY((backPane.getHeight() - image.getFitHeight()) / 2);
        image.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                backPane.getChildren().remove(image);
                recover();
            }
        });
    }
    public void fail() {
        ImageView image = new ImageView();
        Image defeat = new Image(getClass().getResource(resourcesPath + "defeat.png").toString());
        image.setImage(defeat);

        backPane.getChildren().add(image);
        image.setFitWidth(800);
        image.setFitHeight(500);
        image.setLayoutX((backPane.getWidth() - image.getFitWidth()) / 2);
        image.setLayoutY((backPane.getHeight() - image.getFitHeight()) / 2);
        image.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                backPane.getChildren().remove(image);
                recover();
            }
        });
    }
    public void recover() {
        clearIcons();
        setInitSection(true);
    }

    public void atkEffect(int srcx, int srcy, int dstx, int dsty) {
        ImageView sign = new ImageView(sword);
        backPane.getChildren().add(sign);
        sign.setFitWidth(unitIconWidth * 1);
        sign.setFitHeight(unitIconHeight * 0.5);

        Path path = new Path();
        backPane.getChildren().add(path);
        path.getElements().add(new MoveTo(transformPosX(srcx, srcy), transformPosY(srcx, srcy)));
        path.getElements().add(new LineTo(transformPosX(dstx, srcy), transformPosY(dstx, dsty)));

        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(500));
        pathTransition.setPath(path);
        pathTransition.setNode(sign);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(1);
        pathTransition.play();
        pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                backPane.getChildren().remove(sign);
                backPane.getChildren().remove(path);
            }
        });
    }

    public void enhanceEffect(int x, int y, Image buff) {
        ImageView sign = new ImageView(buff);
        backPane.getChildren().add(sign);
        sign.setFitWidth(unitIconWidth * 0.5);
        sign.setFitHeight(unitIconHeight * 0.5);
        sign.setLayoutX(transformPosX(x, y));
        sign.setLayoutY(transformPosY(x, y) - unitIconHeight / 2.0);

        Bloom bloom = new Bloom();
        bloom.setThreshold(4);
        sign.setEffect(bloom);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), sign);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(3);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();
        fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                backPane.getChildren().remove(sign);
            }
        });
    }

    public double transformPosX(int x, int y) {
        return originX + x * (unitIconWidth + unitIconHSpace) + unitIconWidth / 2;
    }
    public double transformPosY(int x, int y) {
        return originY + y * (unitIconHeight + unitIconVSpace) + unitIconHeight / 2;
    }
}
