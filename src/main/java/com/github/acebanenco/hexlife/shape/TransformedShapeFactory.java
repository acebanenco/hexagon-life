package com.github.acebanenco.hexlife.shape;

import javafx.scene.shape.Shape;

public interface TransformedShapeFactory {
    Shape createShapeAt(double x, double y);
}
