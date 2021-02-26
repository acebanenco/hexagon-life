package com.github.acebanenco.hexlife;

import com.github.acebanenco.hexlife.control.GridCellFactory;
import com.github.acebanenco.hexlife.control.ScaledShapeFactory;
import com.github.acebanenco.hexlife.grid.ShapeGridBindAction;
import com.github.acebanenco.hexlife.grid.ShapeGridInitAction;
import com.github.acebanenco.hexlife.grid.ShapeGridPopulateAction;
import com.github.acebanenco.hexlife.layout.ConnectedBorderStrategy;
import com.github.acebanenco.hexlife.layout.HexagonGridLayout;
import com.github.acebanenco.hexlife.layout.ShapeGridLayout;
import com.github.acebanenco.hexlife.model.HexagonLifeModel;
import com.github.acebanenco.hexlife.model.LifeGenerationLogic;
import com.github.acebanenco.hexlife.service.LifeModelUpdateService;
import com.github.acebanenco.hexlife.shape.HexagonShapeFactory;
import javafx.geometry.BoundingBox;

public class LifeGameFactory {

    private final LifeGameConfiguration configuration;
    private final GridCellFactory gridCellFactory;
    private final ShapeGridLayout.CrossBorderStrategy crossBorderStrategy;
    private final ShapeGridLayout gridLayout;
    private final LifeGenerationLogic generationLogic;
    private final HexagonLifeModel lifeModel;
    private final LifeModelUpdateService lifeModelUpdateService;
    private final ScaledShapeFactory scaledShapeFactory;
    private final ShapeGridBindAction shapeGridBindAction;
    private final ShapeGridPopulateAction shapeGridPopulateAction;
    private final ShapeGridInitAction shapeGridInitAction;

    public LifeGameFactory(LifeGameConfiguration configuration) {
        this.configuration = configuration;

        crossBorderStrategy = newCrossBorderStrategy();
        gridLayout = newGridLayout();

        generationLogic = newLifeGenerationLogic();
        lifeModel = newLifeModel();
        lifeModelUpdateService = newLifeModelUpdateService();

        scaledShapeFactory = newScaledShapeFactory();

        gridCellFactory = newGridCellFactory();

        shapeGridBindAction = newShapeGridController();
        shapeGridPopulateAction = newShapeGridPopulateAction();
        shapeGridInitAction = newShapeGridInitAction();
    }

    public ShapeGridLayout getGridLayout() {
        return gridLayout;
    }

    public HexagonLifeModel getLifeModel() {
        return lifeModel;
    }

    public LifeModelUpdateService getLifeModelUpdateService() {
        return lifeModelUpdateService;
    }

    public ShapeGridBindAction getShapeGridController() {
        return shapeGridBindAction;
    }

    public ShapeGridInitAction getShapeGridInitAction() {
        return shapeGridInitAction;
    }

    private GridCellFactory newGridCellFactory() {
        return new GridCellFactory(scaledShapeFactory);
    }

    private ScaledShapeFactory newScaledShapeFactory() {
        HexagonShapeFactory shapeFactory = new HexagonShapeFactory();
        return new ScaledShapeFactory(shapeFactory, configuration.getShapeWidth(), configuration.getShapeHeight());
    }

    private LifeModelUpdateService newLifeModelUpdateService() {
        return new LifeModelUpdateService(lifeModel::advanceModel);
    }

    private HexagonLifeModel newLifeModel() {
        return new HexagonLifeModel(generationLogic, gridLayout);
    }

    private LifeGenerationLogic newLifeGenerationLogic() {
        return new LifeGenerationLogic();
    }

    private ShapeGridLayout newGridLayout() {
        BoundingBox boundingBox = new BoundingBox(0, 0, configuration.getParentWidth(), configuration.getParentHeight());
        return new HexagonGridLayout(configuration.getShapeWidth(), configuration.getShapeHeight(), boundingBox, crossBorderStrategy);
    }

    private ShapeGridLayout.CrossBorderStrategy newCrossBorderStrategy() {
        //new ReflectionCrossBorderStrategy();
        return new ConnectedBorderStrategy();
    }

    private ShapeGridBindAction newShapeGridController() {
        return new ShapeGridBindAction(lifeModel);
    }

    private ShapeGridPopulateAction newShapeGridPopulateAction() {
        return new ShapeGridPopulateAction(gridCellFactory, gridLayout);
    }

    private ShapeGridInitAction newShapeGridInitAction() {
        return new ShapeGridInitAction(shapeGridBindAction, shapeGridPopulateAction);
    }
}
