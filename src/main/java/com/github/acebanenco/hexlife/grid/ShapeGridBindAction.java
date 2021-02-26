package com.github.acebanenco.hexlife.grid;

import com.github.acebanenco.hexlife.control.GridCell;
import com.github.acebanenco.hexlife.grid.ShapeGridContainer;
import com.github.acebanenco.hexlife.model.HexagonLifeModel;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.util.function.Consumer;

public class ShapeGridBindAction {
    private final HexagonLifeModel lifeModel;

    public ShapeGridBindAction(HexagonLifeModel lifeModel) {
        this.lifeModel = lifeModel;
    }

    public void apply(ShapeGridContainer shapeGridContainer) {
        ObservableList<Node> observableList = shapeGridContainer.getChildren();
        bindListAddRemove(observableList, lifeModel::addCell, lifeModel::removeCell);
    }

    private void bindListAddRemove(ObservableList<Node> observableList,
                                   Consumer<GridCell> addOp,
                                   Consumer<GridCell> removeOp) {
        observableList.addListener((ListChangeListener<Node>) c -> {
            while (c.next()) {
                c.getRemoved()
                        .stream()
                        .filter(n -> n instanceof GridCell)
                        .map(n -> (GridCell) n)
                        .forEach(removeOp);
                c.getAddedSubList()
                        .stream()
                        .filter(n -> n instanceof GridCell)
                        .map(n -> (GridCell) n)
                        .forEach(addOp);
            }
        });
    }

}
