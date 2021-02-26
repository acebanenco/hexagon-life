package com.github.acebanenco.hexlife.control;

import com.github.acebanenco.hexlife.layout.ShapeGridLayout;

public class GridCellFactory {

    private final ScaledShapeFactory scaledShapeFactory;

    public GridCellFactory(ScaledShapeFactory scaledShapeFactory) {
        this.scaledShapeFactory = scaledShapeFactory;
    }

    public GridCell gridCell(ShapeGridLayout.GridLocation location) {
        return new GridCell(location, scaledShapeFactory::createShape);
    }
}
