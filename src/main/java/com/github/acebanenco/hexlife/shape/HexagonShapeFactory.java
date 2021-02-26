package com.github.acebanenco.hexlife.shape;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.sqrt;

public class HexagonShapeFactory implements ShapeFactory {
    private final List<Double> sharedPoints;

    public HexagonShapeFactory() {
        double width = 2;
        double height = (sqrt(3) / 2) * width;
        sharedPoints = getSharedPoints(width, height,
                new Point2D(0., 1.),
                new Point2D(1., 2.),
                new Point2D(3., 2.),
                new Point2D(4., 1.),
                new Point2D(3., 0.),
                new Point2D(1., 0.),
                new Point2D(0., 1.));
    }

    private List<Double> getSharedPoints(double width,
                                         double height,
                                         Point2D... points) {
        DoubleSummaryStatistics xStat = Stream.of(points)
                .mapToDouble(Point2D::getX)
                .collect(DoubleSummaryStatistics::new,
                        DoubleSummaryStatistics::accept,
                        DoubleSummaryStatistics::combine);

        DoubleSummaryStatistics yStat = Stream.of(points)
                .mapToDouble(Point2D::getY)
                .collect(DoubleSummaryStatistics::new,
                        DoubleSummaryStatistics::accept,
                        DoubleSummaryStatistics::combine);

        double minX = xStat.getMin();
        double minY = yStat.getMin();
        double scaleX = width / (xStat.getMax() - minX);
        double scaleY = height / (yStat.getMax() - minY);

        return Stream.of(points)
                .flatMap(p -> Stream.of(
                        (p.getX() - minX) * scaleX,
                        (p.getY() - minY) * scaleY))
                .collect(Collectors.toList());
    }

    @Override
    public Polygon createUnscaled() {
        Polygon polygon = new Polygon();
        ObservableList<Double> points = polygon.getPoints();
        points.addAll(sharedPoints);
        return polygon;
    }
}
