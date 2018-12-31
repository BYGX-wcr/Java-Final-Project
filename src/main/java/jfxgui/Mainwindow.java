package main.java.jfxgui;

import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;
import main.java.creature.Creature;
import main.java.environment.Battlefield;
import main.java.environment.Game;

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

        mainGame = new Game(this, resourcesPath);
        mainGame.start();
    }

    public void replayGame() {
        setInitSection(false);
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
        //System.out.println("ATK EFFECT");
        ImageView sign = new ImageView(sword);
        backPane.getChildren().add(sign);
        sign.setFitWidth(unitIconWidth * 0.5);
        sign.setFitWidth(unitIconHeight * 0.5);
        if (dsty > srcy) {
            sign.setRotate(90);
        }
        else if (dstx > srcx) {
            sign.setRotate(180);
        }
        else if (srcy > dsty) {
            sign.setRotate(270);
        }

        Path path = new Path();
        backPane.getChildren().add(path);
        path.getElements().add(new MoveTo(transformPosX(srcx, srcy), transformPosY(srcx, srcy)));
        path.getElements().add(new LineTo(transformPosX(dstx, srcy), transformPosY(dstx, dsty)));

        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(4000));
        pathTransition.setPath(path);
        pathTransition.setNode(sign);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.play();
        backPane.getChildren().remove(sign);
        backPane.getChildren().remove(path);
    }

    public double transformPosX(int x, int y) {
        return originX + x * (unitIconWidth + unitIconHSpace) + unitIconWidth / 2;
    }
    public double transformPosY(int x, int y) {
        return originY + y * (unitIconHeight + unitIconVSpace) + unitIconHeight / 2;
    }
}
