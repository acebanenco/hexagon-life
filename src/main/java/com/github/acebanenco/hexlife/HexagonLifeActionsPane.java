package com.github.acebanenco.hexlife;

import com.github.acebanenco.hexlife.animation.AnimationConsumer;
import com.sun.javafx.beans.event.AbstractNotifyListener;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;

public class HexagonLifeActionsPane extends HBox {

    public HexagonLifeActionsPane(HexagonLifeModel lifeModel, AnimationConsumer animationConsumer) {
        Button clearButton = new Button("Clear");
        clearButton.setOnAction(actionEvent -> {
            lifeModel.clear();
        });
        getChildren().add(clearButton);

        CheckBox checkBox = new CheckBox("Auto-start updates");
//        checkBox.selectedProperty().bind(animationConsumer.autoStartUpdatesProperty());
//        animationConsumer.autoStartUpdatesProperty()
//                .bindBidirectional(checkBox.selectedProperty());
        BooleanProperty selectedProperty = checkBox.selectedProperty();
        selectedProperty.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                animationConsumer.autoStartUpdatesProperty()
                        .set(selectedProperty.get());
            }
        });
        getChildren().add(checkBox);
    }
}
