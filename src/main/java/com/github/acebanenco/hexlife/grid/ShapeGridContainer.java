package com.github.acebanenco.hexlife.grid;

import com.github.acebanenco.hexlife.control.GridCell;
import com.github.acebanenco.hexlife.layout.ShapeGridLayout;
import com.github.acebanenco.hexlife.layout.ShapeGridLayout.GridLocation;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ShapeGridContainer extends Pane {
    private final ShapeGridLayout gridLayout;

    public ShapeGridContainer(ShapeGridLayout gridLayout) {
        this.gridLayout = gridLayout;
    }

    @Override
    protected double computeMinWidth(double height) {
        return super.computeMinWidth(height);
    }

    @Override
    protected double computeMinHeight(double width) {
        return super.computeMinHeight(width);
    }

    @Override
    protected double computePrefWidth(double height) {
        DoubleSummaryStatistics xStat = getChildren().stream()
                .filter(c -> c instanceof GridCell)
                .map(c -> (GridCell) c)
                .map(GridCell::getGridLocation)
                .map(gridLayout::getPoint)
                .map(Point2D::getX)
                .collect(DoubleSummaryStatistics::new,
                        DoubleSummaryStatistics::accept,
                        DoubleSummaryStatistics::combine);
        Insets insets = getInsets();
        double width = xStat.getMax() - xStat.getMin();
        double left = insets.getLeft();
        double right = insets.getRight();
        return left + width + right;
    }

    @Override
    protected double computePrefHeight(double width) {
        DoubleSummaryStatistics yStat = getChildren().stream()
                .filter(c -> c instanceof GridCell)
                .map(c -> (GridCell) c)
                .map(GridCell::getGridLocation)
                .map(gridLayout::getPoint)
                .map(Point2D::getY)
                .collect(DoubleSummaryStatistics::new,
                        DoubleSummaryStatistics::accept,
                        DoubleSummaryStatistics::combine);

        double height = yStat.getMax() - yStat.getMin();
        Insets insets = getInsets();
        double top = insets.getTop();
        double bottom = insets.getBottom();
        return top + height + bottom;
    }

    @Override
    protected double computeMaxWidth(double height) {
        return super.computeMaxWidth(height);
    }

    @Override
    protected double computeMaxHeight(double width) {
        return super.computeMaxHeight(width);
    }

    @Override
    protected void layoutChildren() {
        List<Node> children = getManagedChildren();
        getGridCellStream(children)
                .forEach(this::layoutChild);
    }

    private Stream<GridCell> getGridCellStream(List<Node> children) {
        return children.stream()
                .filter(child -> child instanceof GridCell)
                .map(child -> (GridCell) child);
    }

    private void layoutChild(GridCell child) {
        GridLocation gridLocation = child.getGridLocation();
        Point2D point = gridLayout.getPoint(gridLocation);
        child.relocate(point.getX(), point.getY());
    }
}
