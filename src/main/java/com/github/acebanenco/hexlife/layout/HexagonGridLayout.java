package com.github.acebanenco.hexlife.layout;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class HexagonGridLayout implements ShapeGridLayout {

    private static final double SQRT_OF_THREE = Math.sqrt(3.0);
    private final double shapeSize;
    private final Bounds parentBounds;
    private final CrossBorderStrategy crossBorderStrategy;
    private final Set<GridLocation> evenTranslations;
    private final Set<GridLocation> oddTranslations;

    public HexagonGridLayout(double shapeSize, Bounds parentBounds, CrossBorderStrategy crossBorderStrategy) {
        this.shapeSize = shapeSize;
        this.parentBounds = parentBounds;
        this.crossBorderStrategy = crossBorderStrategy;

        evenTranslations = getEvenTranslations();
        oddTranslations = getOddTranslations();
    }

    @Override
    public int getRowCount() {
        return (int) (parentBounds.getHeight() / (shapeSize/SQRT_OF_THREE));
    }

    @Override
    public int getColumnCount() {
        return (int) (parentBounds.getWidth() / (1.5 * shapeSize));
    }

    @Override
    public Point2D getPoint(GridLocation location) {
        double x = getX(location);
        double y = getY(location);
        return new Point2D(x, y);
    }

    private double getX(GridLocation locationOnGrid) {
        int column = locationOnGrid.getColumn();
        if (isEvenRow(locationOnGrid)) {
            return 1.5 * (2 * column) * shapeSize/2 + parentBounds.getMinX();
        }
        return 1.5 * (2 * column + 1) * shapeSize/2 + parentBounds.getMinX();
    }

    private double getY(GridLocation locationOnGrid) {
        int row = locationOnGrid.getRow();
        return row *SQRT_OF_THREE/2 * shapeSize/2 + parentBounds.getMinY();
    }

    @Override
    public Stream<GridLocation> locationsStream() {
        int columnCount = getColumnCount();
        int rowCount = getRowCount();
        return IntStream.range(0, columnCount * rowCount)
                .mapToObj(index -> {
                    int column = index % columnCount;
                    int row = index / columnCount;
                    return new GridLocation(column, row);
                });
    }

    @Override
    public int indexOf(GridLocation location) {
        int column = location.getColumn();
        int row = location.getRow();
        return column + row * getColumnCount();
    }

    @Override
    public Set<GridLocation> getNeighbours(GridLocation locationOnGrid, int level) {
        if (level < 0) {
            throw new IllegalArgumentException("level = " + level);
        }
        if (level == 0) {
            return Collections.singleton(locationOnGrid);
        }
        if (level == 1) {
            GridSize size = getSize();
            return getCloseNeighbours(locationOnGrid, size);
        }

        Set<GridLocation> visitedNodes = new HashSet<>();
        visitedNodes.add(locationOnGrid);

        Set<GridLocation> nextLevelNeighbours = Collections.singleton(locationOnGrid);
        for (int levelIndex = 0; levelIndex < level; levelIndex++) {
            nextLevelNeighbours = getNextLevelNeighbours(nextLevelNeighbours, visitedNodes);
        }
        return nextLevelNeighbours;
    }

    private Set<GridLocation> getNextLevelNeighbours(Set<GridLocation> currentLevelNeighbours, Set<GridLocation> visitedNodes) {
        GridSize size = getSize();
        return currentLevelNeighbours.stream()
                .map(location -> getCloseNeighbours(location, size))
                .flatMap(Collection::stream)
                .filter(visitedNodes::add)
                .collect(Collectors.toSet());
    }

    private Set<GridLocation> getCloseNeighbours(GridLocation location, GridSize size) {
        return getCloseNeighboursUnsorted(location).stream()
                .map(neighbourLocation -> crossBorderStrategy.crossBorderLocation(neighbourLocation, size))
                .collect(Collectors.toSet());
    }

    private GridSize getSize() {
        int columnCount = getColumnCount();
        int rowCount = getRowCount();
        return new GridSize(columnCount, rowCount);
    }

    private Set<GridLocation> getCloseNeighboursUnsorted(GridLocation location) {
        Set<GridLocation> translations = getNeighbourTranslations(location);
        return locationTranslatedBy(location, translations);
    }

    private Set<GridLocation> getNeighbourTranslations(GridLocation location) {
        return isEvenRow(location)
                ? evenTranslations
                : oddTranslations;
    }

    private boolean isEvenRow(GridLocation location) {
        return location.getRow() % 2 == 0;
    }

    private Set<GridLocation> getOddTranslations() {
        return new HashSet<>(Arrays.asList(
                new GridLocation(+0, -1),
                new GridLocation(+0, +1),
                new GridLocation(+0, +2),
                new GridLocation(+1, +1),
                new GridLocation(+1, -1),
                new GridLocation(+0, -2)));
    }

    private Set<GridLocation> getEvenTranslations() {
        return new HashSet<>(Arrays.asList(
                new GridLocation(-1, -1),
                new GridLocation(-1, +1),
                new GridLocation(+0, +2),
                new GridLocation(+0, +1),
                new GridLocation(+0, -1),
                new GridLocation(+0, -2)));
    }


    private Set<GridLocation> locationTranslatedBy(GridLocation location, Set<GridLocation> translations) {
        return translations.stream()
                .map(translation -> locationTranslatedBy(location, translation))
                .collect(Collectors.toSet());
    }

    private GridLocation locationTranslatedBy(GridLocation location, GridLocation delta) {
        int locationRow = location.getRow();
        int locationColumn = location.getColumn();
        int row = locationRow + delta.getRow();
        int column = locationColumn + delta.getColumn();
        return row == locationRow && column == locationColumn ? location : new GridLocation(column, row);
    }

}
