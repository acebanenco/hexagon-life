package com.github.acebanenco.hexlife.layout;

import javafx.geometry.Point2D;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class HexagonGridLayout implements ShapeGridLayout {

    private static final double SQRT_OF_THREE = Math.sqrt(3.0);
    private final double shapeSize;
    private final int width;
    private final int height;

    public HexagonGridLayout(double shapeSize, int width, int height) {
        this.shapeSize = shapeSize;
        this.width = width;
        this.height = height;
    }

    @Override
    public Point2D getLocation(GridLocation locationOnGrid) {
        Point2D point2D = getUnscaledLocation(locationOnGrid);
        return point2D.multiply(shapeSize / 2);
    }

    private Point2D getUnscaledLocation(GridLocation locationOnGrid) {
        double x = getX(locationOnGrid);
        double y = getY(locationOnGrid);
        return new Point2D(x, y);
    }

    private double getX(GridLocation locationOnGrid) {
        int xn = locationOnGrid.getX();
        if (isEvenLocation(locationOnGrid)) {
            return 1.5 * (2 * xn);
        }
        return 1.5 * (1 + 2 * xn);
    }

    private double getY(GridLocation locationOnGrid) {
        int yn = locationOnGrid.getY();
        return yn * (SQRT_OF_THREE / 2);
    }

    @Override
    public Stream<GridLocation> locationsStream() {
        return IntStream.range(0, width * height)
                .mapToObj(this::locaitonByIndex);
    }

    private GridLocation locaitonByIndex(int index) {
        int x = index % width;
        int y = index / width;
        return new GridLocation(x, y);
    }

    @Override
    public int indexOf(GridLocation location) {
        return location.getX() + location.getY() * width;
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
            return getCloseNeighbours(locationOnGrid);
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
        return currentLevelNeighbours.stream()
                .map(this::getCloseNeighbours)
                .flatMap(Collection::stream)
                .filter(visitedNodes::add)
                .collect(Collectors.toSet());
    }

    private Set<GridLocation> getCloseNeighbours(GridLocation location) {
        return getCloseNeighboursUnsorted(location).stream()
                .map(this::locationReflected)
                .collect(Collectors.toSet());
    }

    private Set<GridLocation> getCloseNeighboursUnsorted(GridLocation location) {
        Set<GridLocation> translations = getNeighbourTranslations(location);
        return locationTranslatedBy(location, translations);
    }

    private Set<GridLocation> getNeighbourTranslations(GridLocation location) {
        GridLocation[] translations = isEvenLocation(location) ? getEvenTranslations() : getOddTranslations();
        return new HashSet<>(Arrays.asList(translations));
    }

    private boolean isEvenLocation(GridLocation location) {
        return location.getY() % 2 == 0;
    }

    private GridLocation[] getOddTranslations() {
        return new GridLocation[]{
                new GridLocation(+0, -1),
                new GridLocation(+0, +1),
                new GridLocation(+0, +2),
                new GridLocation(+1, +1),
                new GridLocation(+1, -1),
                new GridLocation(+0, -2)};
    }

    private GridLocation[] getEvenTranslations() {
        return new GridLocation[]{
                new GridLocation(-1, -1),
                new GridLocation(-1, +1),
                new GridLocation(+0, +2),
                new GridLocation(+0, +1),
                new GridLocation(+0, -1),
                new GridLocation(+0, -2)};
    }


    private Set<GridLocation> locationTranslatedBy(GridLocation location, Set<GridLocation> translations) {
        return translations.stream()
                .map(tranlsation -> locationTranslatedBy(location, tranlsation))
                .collect(Collectors.toSet());
    }

    private GridLocation locationTranslatedBy(GridLocation location, GridLocation delta) {
        int xn = location.getX() + delta.getX();
        int yn = location.getY() + delta.getY();
        return new GridLocation(xn, yn);
    }

    private GridLocation locationTranslated(GridLocation location, int dx, int dy) {
        int xn = location.getX() + dx;
        int yn = location.getY() + dy;
        return new GridLocation(xn, yn);
    }

    private GridLocation locationReflected(GridLocation location) {
        int xn = location.getX();
        int yn = location.getY();

        int x = reflect(xn, width);
        int y = reflect(yn, height);

        if (x == xn && y == yn) {
            return location;
        }
        return new GridLocation(x, y);
    }

    private static int reflect(int i, int n) {
        if (i < 0) {
            return -i;
        }
        if (i < n) {
            return i;
        }
        return reflect(2 * n - i - 1, n);
    }

}
