package com.github.acebanenco.hexlife;

import com.github.acebanenco.hexlife.shape.ShapeFactory;
import com.github.acebanenco.hexlife.shape.TransformedShapeFactory;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.shape.Shape;

public class ProportionallyScaledShapeFactory implements TransformedShapeFactory {
    private final ShapeFactory shapeFactory;
    private final double scale;

    public ProportionallyScaledShapeFactory(double shapeWidth, ShapeFactory shapeFactory) {
        this.shapeFactory = shapeFactory;
        Shape shape = shapeFactory.createUnscaled();
        Bounds referenceBounds = shape.getBoundsInLocal();
        scale = shapeWidth / referenceBounds.getWidth();
    }

    @Override
    public Shape createShapeAt(Point2D point) {
        Shape shape = shapeFactory.createUnscaled();
        shape.setScaleX(scale);
        shape.setScaleY(scale);
        shape.setTranslateX(point.getX());
        shape.setTranslateY(point.getY());
        return shape;
    }

}
