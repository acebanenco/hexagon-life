package com.github.acebanenco.hexlife.model;

import com.github.acebanenco.hexlife.control.GridCell;
import com.github.acebanenco.hexlife.layout.ShapeGridLayout;
import com.github.acebanenco.hexlife.layout.ShapeGridLayout.GridLocation;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;

import java.util.*;
import java.util.stream.Collectors;

public class HexagonLifeModel {
    private final LifeGenerationLogic generationLogic;
    private final ShapeGridLayout gridLayout;
    private final Map<GridLocation, GridCell> cellsByLocationMap = new HashMap<>();

    private BooleanProperty nextProperty;

    public HexagonLifeModel(LifeGenerationLogic generationLogic, ShapeGridLayout gridLayout) {
        this.generationLogic = generationLogic;
        this.gridLayout = gridLayout;
    }

    public BooleanProperty nextProperty() {
        if ( nextProperty == null ) {
            nextProperty = new BooleanPropertyBase() {
                @Override
                public Object getBean() {
                    return HexagonLifeModel.this;
                }
                @Override
                public String getName() {
                    return "next";
                }
            };
        }
        return nextProperty;
    }

    public void addCell(GridCell cell) {
        cellsByLocationMap.put(cell.getGridLocation(), cell);
    }

    public void removeCell(GridCell cell) {
        cellsByLocationMap.remove(cell.getGridLocation());
    }

    public void advanceModel() {
        Set<GridCell> deadCells = cellsByLocationMap.values()
                .stream()
                .filter(this::cellShouldDie)
                .collect(Collectors.toSet());

        Set<GridCell> bornCells = cellsByLocationMap.values()
                .stream()
                .filter(this::cellShouldBeBorn)
                .collect(Collectors.toSet());

        deadCells.forEach(cell -> cell.setAlive(false));
        bornCells.forEach(cell -> cell.setAlive(true));

        nextProperty().set(!deadCells.isEmpty() || !bornCells.isEmpty());
    }

    private boolean cellShouldBeBorn(GridCell gridCell) {
        if (gridCell.isAlive()) {
            return false;
        }
        GridLocation gridLocation = gridCell.getGridLocation();
        List<GridLocation> aliveNeighbours = getAliveNeighbours(gridLocation);
        return generationLogic.shouldBeBorn(aliveNeighbours);
    }

    private boolean cellShouldDie(GridCell gridCell) {
        if (!gridCell.isAlive()) {
            return false;
        }
        GridLocation gridLocation = gridCell.getGridLocation();
        List<GridLocation> aliveNeighbours = getAliveNeighbours(gridLocation);
        return !generationLogic.shouldSurvive(aliveNeighbours);
    }

    List<GridLocation> getAliveNeighbours(GridLocation location) {
        Set<GridLocation> neighbours = gridLayout.getNeighbours(location, 1);
        return neighbours.stream()
                .map(cellsByLocationMap::get)
                .filter(Objects::nonNull)
                .filter(GridCell::isAlive)
                .map(GridCell::getGridLocation)
                .map(neighbourLocation -> neighbourLocation.minus(location))
                .collect(Collectors.toList());
    }

    public void clear() {
        cellsByLocationMap.values()
                .forEach(cell -> cell.setAlive(false));
    }

}
