package com.github.acebanenco.hexlife;

import com.github.acebanenco.hexlife.animation.AnimationConsumer;
import com.github.acebanenco.hexlife.layout.HexagonGridLayout;
import com.github.acebanenco.hexlife.layout.ReflectionCrossBorderStrategy;
import com.github.acebanenco.hexlife.layout.ShapeGridLayout;
import com.github.acebanenco.hexlife.shape.HexagonShapeFactory;
import javafx.application.Application;
import javafx.geometry.BoundingBox;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HexagonLifeApplication extends Application {

    private final HexagonList hexagons;
    private final HexagonLifeActionsPane actions;

    public HexagonLifeApplication() {
        double shapeWidth = 20;
        int parentWidth = 800;
        int parentHeight = 500;

        BoundingBox boundingBox = new BoundingBox(0, 0, parentWidth, parentHeight);
        ShapeGridLayout.CrossBorderStrategy crossBorderStrategy = new ReflectionCrossBorderStrategy();
        ShapeGridLayout gridLayout = new HexagonGridLayout(shapeWidth, boundingBox, crossBorderStrategy);

        LifeGenerationLogic generationLogic = new LifeGenerationLogic();
        HexagonLifeModel lifeModel = new HexagonLifeModel(generationLogic, gridLayout);
        AnimationConsumer animationConsumer = new AnimationConsumer(lifeModel::advanceModel, Duration.millis(100.));

        HexagonShapeFactory shapeFactory = new HexagonShapeFactory();
        ProportionallyScaledShapeFactory scaledShapeFactory = new ProportionallyScaledShapeFactory(shapeWidth, shapeFactory);

        hexagons = new HexagonList(lifeModel, scaledShapeFactory, gridLayout, animationConsumer);
        actions = new HexagonLifeActionsPane(lifeModel, animationConsumer);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Group root = new Group();

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(hexagons.getHexagonPane());
        borderPane.setBottom(actions);
        root.getChildren().add(borderPane);

        Scene scene = new Scene(root);

        stage.setTitle("Hexagon Life Game - 1.0-SNAPSHOT");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }


}
