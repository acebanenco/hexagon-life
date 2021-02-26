package com.github.acebanenco.hexlife;

import com.github.acebanenco.hexlife.builder.HexagonLifeSceneBuilder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LifeGameApplication extends Application {

    private final LifeGameFactory objectsFactory;

    public LifeGameApplication() {
        LifeGameConfiguration configuration = new LifeGameConfiguration();
        objectsFactory = new LifeGameFactory(configuration);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        HexagonLifeSceneBuilder sceneBuilder = new HexagonLifeSceneBuilder(objectsFactory);
        Scene scene = sceneBuilder.buildScene();

        stage.setTitle("Hexagon Life Game - 1.0-SNAPSHOT");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }


}
