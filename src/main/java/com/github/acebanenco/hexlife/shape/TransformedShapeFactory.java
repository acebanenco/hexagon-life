package com.github.acebanenco.hexlife.shape;

import javafx.geometry.Point2D;
import javafx.scene.shape.Shape;

public interface TransformedShapeFactory {
    Shape createShapeAt(Point2D point);
}
