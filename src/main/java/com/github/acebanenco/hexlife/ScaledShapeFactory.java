package com.github.acebanenco.hexlife;

import com.github.acebanenco.hexlife.shape.ShapeFactory;
import com.github.acebanenco.hexlife.shape.TransformedShapeFactory;
import javafx.geometry.Bounds;
import javafx.scene.shape.Shape;

public class ScaledShapeFactory implements TransformedShapeFactory {
    private final ShapeFactory shapeFactory;
    private final double scaleX;
    private final double scaleY;

    public ScaledShapeFactory(ShapeFactory shapeFactory, double shapeWidth, double shapeHeight) {
        this.shapeFactory = shapeFactory;
        Shape shape = shapeFactory.createUnscaled();
        Bounds referenceBounds = shape.getBoundsInLocal();
        scaleX = shapeWidth / referenceBounds.getWidth();
        scaleY = shapeHeight / referenceBounds.getHeight();
    }

    @Override
    public Shape createShape() {
        Shape shape = shapeFactory.createUnscaled();
        shape.setScaleX(scaleX);
        shape.setScaleY(scaleY);
        return shape;
    }

}
