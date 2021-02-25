package com.github.acebanenco.hexlife.layout;

import javafx.geometry.Point2D;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public interface ShapeGridLayout {

    Point2D getLocation(GridLocation locationOnGrid);

    Set<GridLocation> getNeighbours(GridLocation locationOnGrid, int level);

    Stream<GridLocation> locationsStream();

    int indexOf(GridLocation location);

    class GridLocation {
        private final int x;
        private final int y;

        public GridLocation(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            GridLocation that = (GridLocation) o;
            if (x != that.x) {
                return false;
            }
            return y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
