package com.github.acebanenco;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class HexagonLifeApplication extends Application {

    private final int HEIGHT = 80;
    private final int WIDTH = 40;
    private final LifeGenerationLogic generationLogic = new LifeGenerationLogic();
    private final HexagonLifeModel lifeModel = new HexagonLifeModel(generationLogic, WIDTH, HEIGHT);
    private final HexagonList hexagons = new HexagonList(lifeModel, WIDTH, HEIGHT);
    private final HexagonListUpdate update = new HexagonListUpdate(lifeModel, hexagons, WIDTH, HEIGHT);
    private final HexagonListActions actions = new HexagonListActions(hexagons, lifeModel);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Group root = new Group();

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(hexagons.getHexagonPane());
        borderPane.setBottom(actions.getActionPane());
        root.getChildren().add(borderPane);

        update.scheduleUpdates();

        Scene scene = new Scene(root, 600, 300);

        stage.setTitle("Hexagon Life Game - 1.0-SNAPSHOT");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }


}
