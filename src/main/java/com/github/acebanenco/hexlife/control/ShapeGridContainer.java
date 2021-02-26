package com.github.acebanenco.hexlife.control;

import com.github.acebanenco.hexlife.layout.ShapeGridLayout;
import com.github.acebanenco.hexlife.layout.ShapeGridLayout.GridLocation;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

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
        List<Bounds> boundsList = getChildren().stream()
                .map(Node::getLayoutBounds)
                .collect(Collectors.toList());
        double min = boundsList.stream()
                .mapToDouble(Bounds::getMinX)
                .min()
                .orElse(0);
        double max = boundsList.stream()
                .mapToDouble(Bounds::getMaxX)
                .max()
                .orElse(0);
        double w = max - min;

        Insets insets = getInsets();
        return insets.getLeft() + w + insets.getRight();
    }

    @Override
    protected double computePrefHeight(double width) {
        List<Bounds> boundsList = getChildren().stream()
                .map(Node::getLayoutBounds)
                .collect(Collectors.toList());
        double min = boundsList.stream()
                .mapToDouble(Bounds::getMinY)
                .min()
                .orElse(0);
        double max = boundsList.stream()
                .mapToDouble(Bounds::getMaxY)
                .max()
                .orElse(0);
        double h = max - min;
        return getInsets().getTop() + h + getInsets().getBottom();
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
