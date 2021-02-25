package com.github.acebanenco.hexlife;

import com.github.acebanenco.hexlife.shape.ShapeFactory;
import com.github.acebanenco.hexlife.shape.TransformedShapeFactory;
import javafx.geometry.Bounds;
import javafx.scene.shape.Shape;

public class ProportionallyScaledShapeFactory implements TransformedShapeFactory {
    private final ShapeFactory shapeFactory;
    private final double scale;

    public ProportionallyScaledShapeFactory(double shapeWidth, ShapeFactory shapeFactory) {
        this.shapeFactory = shapeFactory;
        Shape shape = shapeFactory.createUnscaled();
        Bounds referenceBounds = shape.getBoundsInLocal();
        scale = shapeWidth/2 / referenceBounds.getWidth();
    }

    @Override
    public Shape createShapeAt(double x, double y) {
        Shape shape = shapeFactory.createUnscaled();
        shape.setScaleX(scale);
        shape.setScaleY(scale);
        shape.setTranslateX(x);
        shape.setTranslateY(y);
        return shape;
    }

}
