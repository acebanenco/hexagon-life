package com.github.acebanenco.hexlife.control;

import com.github.acebanenco.hexlife.control.GridCell;
import com.github.acebanenco.hexlife.layout.ShapeGridLayout;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.List;

public class ShapeGridContainer extends Pane {
    private final ShapeGridLayout gridLayout;

    public ShapeGridContainer(ShapeGridLayout gridLayout) {
        this.gridLayout = gridLayout;
    }

    @Override
    protected void layoutChildren() {
        List<Node> children = getManagedChildren();
        children.stream()
                .filter(child -> child instanceof GridCell)
                .map(child -> (GridCell) child)
                .forEach(child -> {
                    ShapeGridLayout.GridLocation gridLocation = child.getGridLocation();
                    Point2D point = gridLayout.getPoint(gridLocation);
                    child.relocate(point.getX(), point.getY());
                });
    }
}
