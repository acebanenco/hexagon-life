package com.github.acebanenco.hexlife;

import com.github.acebanenco.hexlife.animation.AnimationConsumer;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;

public class HexagonLifeActionsPane extends HBox {

    public HexagonLifeActionsPane(HexagonLifeModel lifeModel, AnimationConsumer animationConsumer) {
        Button clearButton = new Button("Clear");
        clearButton.setOnAction(actionEvent -> {
            animationConsumer.freezeModelUpdates();
            lifeModel.clear();
            animationConsumer.commitAnimationsAsync();
        });
        getChildren().add(clearButton);

        CheckBox checkBox = new CheckBox("Auto-start updates");
        animationConsumer.autoStartUpdatesProperty().bind(checkBox.selectedProperty());
        getChildren().add(checkBox);
    }
}
