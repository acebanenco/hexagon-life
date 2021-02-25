package com.github.acebanenco.hexlife;

import com.github.acebanenco.hexlife.layout.HexagonGridLayout;
import com.github.acebanenco.hexlife.layout.ShapeGridLayout;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class HexagonLifeApplication extends Application {

    private final HexagonList hexagons;
    private final HexagonListUpdate update;
    private final HexagonListActions actions;

    public HexagonLifeApplication() {
        // TODO make it dynamic
        int width = 40;
        int height = 80;
        int shapeSize = 20;

        ShapeGridLayout gridLayout = new HexagonGridLayout(shapeSize, width, height);
        LifeGenerationLogic generationLogic = new LifeGenerationLogic();
        HexagonLifeModel lifeModel = new HexagonLifeModel(generationLogic, width, height, gridLayout);
        hexagons = new HexagonList(lifeModel, width, height);
        actions = new HexagonListActions(hexagons, lifeModel);
        update = new HexagonListUpdate(lifeModel, hexagons);
    }

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

        Scene scene = new Scene(root);

        stage.setTitle("Hexagon Life Game - 1.0-SNAPSHOT");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }


}
