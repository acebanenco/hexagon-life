package com.github.acebanenco;

import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;

import java.util.stream.DoubleStream;

public class HexagonFactory {

    Double[] getHexagonPoints(double x, double y, double width, double height) {
        double squareOfThree = Math.sqrt(3.0);
        double a = width / 2;
        double b = height * squareOfThree / 4;
        double x0 = 0.0;
        double y0 = 0.0;

        double dx = x0 + x;
        double dy = y0 + y;

        double[] hexagonCoordinates = {
                dx - a, dy,
                dx - a / 2, dy - b,
                dx + a / 2, dy - b,
                dx + a, dy,
                dx + a / 2, dy + b,
                dx - a / 2, dy + b,
                dx - a, dy,
        };
        return DoubleStream.of(hexagonCoordinates)
                .boxed()
                .toArray(Double[]::new);
    }

    Polygon getHexagon(double x, double y, double width, double height) {
        Double[] points = getHexagonPoints(x, y, width, height);
        Polygon hexagonLine = new Polygon();
        hexagonLine.getPoints().addAll(points);
        return hexagonLine;
    }

    private Polyline getHexagonLine(double x, double y, double width, double height) {
        Double[] points = getHexagonPoints(x, y, width, height);
        Polyline hexagonLine = new Polyline();
        hexagonLine.getPoints().addAll(points);
        return hexagonLine;
    }
}
