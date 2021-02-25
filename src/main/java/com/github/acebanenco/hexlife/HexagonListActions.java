package com.github.acebanenco.hexlife;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class HexagonListActions {
    private final HBox actionPane;
    private final HexagonList hexagons;
    private final HexagonLifeModel lifeModel;

    public HexagonListActions(HexagonList hexagons, HexagonLifeModel lifeModel) {
        this.hexagons = hexagons;
        this.lifeModel = lifeModel;
        this.actionPane = new HBox();
        Button clearButton = new Button("Clear");

        clearButton.setOnAction(actionEvent -> {
            this.hexagons.getLastTimeMouseClicked().set(System.currentTimeMillis());
            this.lifeModel.clear();
            this.hexagons.reset();
        });

        actionPane.getChildren().add(clearButton);
    }

    public HBox getActionPane() {
        return actionPane;
    }
}
