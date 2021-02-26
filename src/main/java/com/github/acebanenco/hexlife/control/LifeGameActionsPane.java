package com.github.acebanenco.hexlife.control;

import com.github.acebanenco.hexlife.model.HexagonLifeModel;
import com.github.acebanenco.hexlife.service.LifeModelUpdateService;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;

public class LifeGameActionsPane extends HBox {

    public LifeGameActionsPane(HexagonLifeModel lifeModel,
                               LifeModelUpdateService lifeModelUpdateService) {
        setSpacing(20);
        setAlignment(Pos.BASELINE_CENTER);
        setPadding(new Insets(20));

        Button clearButton = new Button("Clear");
        clearButton.setOnAction(actionEvent -> lifeModel.clear());

        CheckBox checkBox = new CheckBox("Run");
        BooleanProperty selectedProperty = checkBox.selectedProperty();
        selectedProperty.bindBidirectional(lifeModelUpdateService.autoStartUpdatesProperty());
        lifeModel.nextProperty()
                .addListener(observable -> {
                    boolean next = lifeModel.nextProperty().get();
                    boolean selected = selectedProperty.get();
                    if ( !next && selected) {
                        selectedProperty.set(false);
                    }
                });
        getChildren().setAll(clearButton, checkBox);
    }
}
