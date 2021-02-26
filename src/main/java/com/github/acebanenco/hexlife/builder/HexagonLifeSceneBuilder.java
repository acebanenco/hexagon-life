package com.github.acebanenco.hexlife.builder;

import com.github.acebanenco.hexlife.LifeGameFactory;
import com.github.acebanenco.hexlife.control.LifeGameActionsPane;
import com.github.acebanenco.hexlife.grid.ShapeGridContainer;
import com.github.acebanenco.hexlife.grid.ShapeGridInitAction;
import com.github.acebanenco.hexlife.layout.ShapeGridLayout;
import com.github.acebanenco.hexlife.model.HexagonLifeModel;
import com.github.acebanenco.hexlife.service.LifeModelUpdateService;
import javafx.scene.Scene;

public class HexagonLifeSceneBuilder {
    private final LifeGameFactory objectsFactory;

    public HexagonLifeSceneBuilder(LifeGameFactory objectsFactory) {
        this.objectsFactory = objectsFactory;
    }

    public Scene buildScene() {
        return new Scene(
                new GroupBuilder()
                        .add(new BorderPaneBuilder()
                                .center(newShapeGridContainer())
                                .bottom(newHexagonLifeActionsPane())
                                .build())
                        .build());
    }

    private LifeGameActionsPane newHexagonLifeActionsPane() {
        HexagonLifeModel lifeModel = objectsFactory.getLifeModel();
        LifeModelUpdateService lifeModelUpdateService = objectsFactory.getLifeModelUpdateService();
        return new LifeGameActionsPane(lifeModel, lifeModelUpdateService);
    }

    private ShapeGridContainer newShapeGridContainer() {
        ShapeGridLayout gridLayout = objectsFactory.getGridLayout();
        ShapeGridInitAction gridInitAction = objectsFactory.getShapeGridInitAction();

        ShapeGridContainer shapeGridContainer = new ShapeGridContainer(gridLayout);
        gridInitAction.apply(shapeGridContainer);
        return shapeGridContainer;
    }


}
