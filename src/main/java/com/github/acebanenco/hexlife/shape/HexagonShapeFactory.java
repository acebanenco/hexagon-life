package com.github.acebanenco.hexlife.shape;

import javafx.collections.ObservableList;
import javafx.scene.shape.Polygon;

import java.util.Arrays;
import java.util.List;

public class HexagonShapeFactory implements ShapeFactory {
    private final static double SQUARE_OF_THREE = Math.sqrt(3);
    private final List<Double> sharedPoints;

    public HexagonShapeFactory() {
        sharedPoints = Arrays.asList(
                -2.0, 0.0,
                -1.0, +SQUARE_OF_THREE,
                +1.0, +SQUARE_OF_THREE,
                +2.0, 0.0,
                +1.0, -SQUARE_OF_THREE,
                -1.0, -SQUARE_OF_THREE,
                -2.0, 0.0);
    }

    @Override
    public Polygon createUnscaled() {
        Polygon polygon = new Polygon();
        ObservableList<Double> points = polygon.getPoints();
        points.addAll(sharedPoints);
        return polygon;
    }
}
