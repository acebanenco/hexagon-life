package com.github.acebanenco;

import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.shape.Polygon;

import java.util.Collection;
import java.util.List;

public class ShapeFactory {
    private final double shapeWidth;
    private final double shapeHeight;
    private final Polygon referenceShape;

    public ShapeFactory(double width, double height, ShapePoints shapePoints) {
        List<Double> points = shapePoints.getShapePoints();
        referenceShape = createShape(points);
        shapeWidth = width;
        shapeHeight = height;
    }

    Polygon createScaledShapeAt(double x, double y) {
        Polygon polygon = createScaledShape();
        polygon.setTranslateX(x);
        polygon.setTranslateY(y);
        return polygon;
    }

    private Polygon createScaledShape() {
        Bounds referenceBounds = referenceShape.getBoundsInLocal();
        double scaleX = shapeWidth / referenceBounds.getWidth();
        double scaleY = shapeHeight / referenceBounds.getHeight();

        Polygon polygon = createShape();
        polygon.setScaleX(scaleX);
        polygon.setScaleY(scaleY);
        return polygon;
    }

    private Polygon createShape() {
        return createShape(referenceShape.getPoints());
    }

    private Polygon createShape(Collection<Double> points) {
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(points);
        return polygon;
    }

}
