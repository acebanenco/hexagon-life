package com.github.acebanenco.hexlife;

import com.github.acebanenco.hexlife.control.GridCell;
import com.github.acebanenco.hexlife.layout.ShapeGridLayout;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class HexagonLifeModel {
    private final LifeGenerationLogic generationLogic;
    private final ShapeGridLayout gridLayout;
    private Map<ShapeGridLayout.GridLocation, GridCell> cells;

    public HexagonLifeModel(LifeGenerationLogic generationLogic, ShapeGridLayout gridLayout) {
        this.generationLogic = generationLogic;
        this.gridLayout = gridLayout;
    }

    public void setCells(List<GridCell> cells) {
        this.cells = cells.stream()
                .collect(Collectors.toMap(
                        GridCell::getGridLocation,
                        UnaryOperator.identity()));
    }

    void advanceModel() {
        Set<GridCell> deadCells = cells.values()
                .stream()
                .filter(this::cellShouldDie)
                .collect(Collectors.toSet());

        Set<GridCell> bornCells = cells.values()
                .stream()
                .filter(this::cellShouldBeBorn)
                .collect(Collectors.toSet());

        deadCells.forEach(cell -> cell.setAlive(false));
        bornCells.forEach(cell -> cell.setAlive(true));
    }

    private boolean cellShouldBeBorn(GridCell gridCell) {
        if (gridCell.isAlive()) {
            return false;
        }
        ShapeGridLayout.GridLocation gridLocation = gridCell.getGridLocation();
        int aliveNeighbourCount = getAliveNeighbourCount(gridLocation);
        return generationLogic.shouldBeBorn(aliveNeighbourCount);
    }

    private boolean cellShouldDie(GridCell gridCell) {
        if (!gridCell.isAlive()) {
            return false;
        }
        ShapeGridLayout.GridLocation gridLocation = gridCell.getGridLocation();
        int aliveNeighbourCount = getAliveNeighbourCount(gridLocation);
        return !generationLogic.shouldSurvive(aliveNeighbourCount);
    }

    int getAliveNeighbourCount(ShapeGridLayout.GridLocation location) {
        Set<ShapeGridLayout.GridLocation> neighbours = gridLayout.getNeighbours(location, 1);
        return (int) neighbours.stream()
                .map(cells::get)
                .filter(Objects::nonNull)
                .filter(GridCell::isAlive)
                .count();
    }

    void clear() {
        cells.values()
                .forEach(cell -> cell.setAlive(false));
    }

}
