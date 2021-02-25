package com.github.acebanenco.hexlife;

import com.github.acebanenco.hexlife.animation.AnimationConsumer;
import com.github.acebanenco.hexlife.control.GridCell;
import com.github.acebanenco.hexlife.control.ShapeGridContainer;
import com.github.acebanenco.hexlife.layout.ShapeGridLayout;
import com.github.acebanenco.hexlife.shape.TransformedShapeFactory;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class HexagonList {
    private final Pane hexagonPane;
    private final List<GridCell> gridCells;
    private final AtomicBoolean coloring = new AtomicBoolean();
    private final TransformedShapeFactory transformedShapeFactory;
    private final AnimationConsumer animationConsumer;

    public HexagonList(HexagonLifeModel lifeModel,
                       TransformedShapeFactory transformedShapeFactory,
                       ShapeGridLayout gridLayout,
                       AnimationConsumer animationConsumer) {
        this.transformedShapeFactory = transformedShapeFactory;

        gridCells = gridLayout.locationsStream()
                .map(this::getGridCells)
                .collect(Collectors.toList());
        lifeModel.setCells(gridCells);

        this.animationConsumer = animationConsumer;

        hexagonPane = new ShapeGridContainer(gridLayout);
        hexagonPane.getChildren()
                .addAll(gridCells);

        hexagonPane.setOnMouseDragged(event -> {
            animationConsumer.freezeModelUpdates();
            if (coloring.get()) {
                double x1 = event.getX();
                double y1 = event.getY();
                Point2D point2D = hexagonPane.sceneToLocal(x1, y1);
                getCellAt(point2D)
                        .ifPresent(cell -> cell.setAlive(true));
                animationConsumer.commitAnimationsAsync();
            }
        });
    }

    private Optional<GridCell> getCellAt(Point2D point2D) {
        return gridCells.stream()
                // TODO optimize
                // split grid into the 10x20 boxes with
                // calculated bounds and know list of cells.
                // Then you can quickly find a box and after
                // number of cells to iterate will be 10-20 times less
                .filter(cell -> cell.contains(point2D))
                .findAny();
    }

    private GridCell getGridCells(ShapeGridLayout.GridLocation location) {
        Shape shape = transformedShapeFactory.createShapeAt(new Point2D(0., 0.));
        GridCell gridCell = new GridCell(location, shape);

        gridCell.setAnimationConsumer(animationConsumer);
        gridCell.setOnMouseClicked(event -> {
            // do not belong here
            animationConsumer.freezeModelUpdates();
            // move to constructor
            GridCell source = (GridCell) event.getSource();
            source.setAlive(!source.isAlive());
            animationConsumer.commitAnimationsAsync();
        });
        return gridCell;
    }

    Pane getHexagonPane() {
        return hexagonPane;
    }
}
