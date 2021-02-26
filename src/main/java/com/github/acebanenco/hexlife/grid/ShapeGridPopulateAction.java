package com.github.acebanenco.hexlife.grid;

import com.github.acebanenco.hexlife.control.GridCellFactory;
import com.github.acebanenco.hexlife.grid.ShapeGridContainer;
import com.github.acebanenco.hexlife.layout.ShapeGridLayout;

import java.util.stream.Collectors;

public class ShapeGridPopulateAction {
    private final GridCellFactory gridCellFactory;
    private final ShapeGridLayout gridLayout;

    public ShapeGridPopulateAction(GridCellFactory gridCellFactory, ShapeGridLayout gridLayout) {
        this.gridCellFactory = gridCellFactory;
        this.gridLayout = gridLayout;
    }

    public void apply(ShapeGridContainer shapeGridContainer) {
        shapeGridContainer.getChildren()
                .setAll(gridLayout.locationsStream()
                        .map(gridCellFactory::gridCell)
                        .collect(Collectors.toList()));
    }
}
