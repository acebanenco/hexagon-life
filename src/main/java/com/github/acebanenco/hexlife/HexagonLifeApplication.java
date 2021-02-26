package com.github.acebanenco.hexlife;

import com.github.acebanenco.hexlife.animation.AnimationConsumer;
import com.github.acebanenco.hexlife.control.GridCell;
import com.github.acebanenco.hexlife.control.ShapeGridContainer;
import com.github.acebanenco.hexlife.layout.ConnectedBorderStrategy;
import com.github.acebanenco.hexlife.layout.HexagonGridLayout;
import com.github.acebanenco.hexlife.layout.ReflectionCrossBorderStrategy;
import com.github.acebanenco.hexlife.layout.ShapeGridLayout;
import com.github.acebanenco.hexlife.shape.HexagonShapeFactory;
import javafx.application.Application;
import javafx.geometry.BoundingBox;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class HexagonLifeApplication extends Application {

    private final HexagonLifeActionsPane actions;
    private final ShapeGridContainer hexagonPane;

    public HexagonLifeApplication() {
        double shapeWidth = 20;
        double shapeHeight = (Math.sqrt(3)/2) * shapeWidth;
        int parentWidth = 1600;
        int parentHeight = 1000;

        BoundingBox boundingBox = new BoundingBox(0, 0, parentWidth, parentHeight);
        ShapeGridLayout.CrossBorderStrategy crossBorderStrategy = new ConnectedBorderStrategy();//new ReflectionCrossBorderStrategy();
        ShapeGridLayout gridLayout = new HexagonGridLayout(shapeWidth, shapeHeight, boundingBox, crossBorderStrategy);

        LifeGenerationLogic generationLogic = new LifeGenerationLogic();
        HexagonLifeModel lifeModel = new HexagonLifeModel(generationLogic, gridLayout);
        AnimationConsumer animationConsumer = new AnimationConsumer(lifeModel::advanceModel);

        HexagonShapeFactory shapeFactory = new HexagonShapeFactory();
        ScaledShapeFactory scaledShapeFactory = new ScaledShapeFactory(shapeFactory, shapeWidth, shapeHeight);

        List<GridCell> gridCells = gridLayout.locationsStream()
                .map(location ->
                        new GridCell(location, scaledShapeFactory::createShape))
                .collect(Collectors.toList());
        lifeModel.setCells(gridCells);

        hexagonPane = new ShapeGridContainer(gridLayout);
        hexagonPane.getChildren()
                .addAll(gridCells);

        actions = new HexagonLifeActionsPane(lifeModel, animationConsumer);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Group root = new Group();

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(hexagonPane);
        borderPane.setBottom(actions);
        root.getChildren().add(borderPane);

        Scene scene = new Scene(root);

        stage.setTitle("Hexagon Life Game - 1.0-SNAPSHOT");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }


}
