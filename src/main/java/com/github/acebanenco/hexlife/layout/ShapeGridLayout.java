package com.github.acebanenco.hexlife.layout;

import javafx.geometry.Point2D;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public interface ShapeGridLayout {

    int getRowCount();

    int getColumnCount();

    Point2D getPoint(GridLocation locationOnGrid);

    Set<GridLocation> getNeighbours(GridLocation locationOnGrid, int level);

    Stream<GridLocation> locationsStream();

    interface CrossBorderStrategy {
        GridLocation crossBorderLocation(GridLocation location, GridSize size);
    }

    class GridLocation {
        private final int column;
        private final int row;

        public GridLocation(int column, int row) {
            this.column = column;
            this.row = row;
        }

        public int getColumn() {
            return column;
        }

        public int getRow() {
            return row;
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
            if (column != that.column) {
                return false;
            }
            return row == that.row;
        }

        @Override
        public int hashCode() {
            return Objects.hash(column, row);
        }

        public GridLocation minus(GridLocation location) {
            return new GridLocation(column - location.column, row - location.row);
        }
    }

    class GridSize {
        private final int columns;
        private final int rows;

        public GridSize(int columns, int rows) {
            this.columns = columns;
            this.rows = rows;
        }

        public int getColumns() {
            return columns;
        }

        public int getRows() {
            return rows;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            GridSize that = (GridSize) o;
            if (columns != that.columns) {
                return false;
            }
            return rows == that.rows;
        }

        @Override
        public int hashCode() {
            return Objects.hash(columns, rows);
        }
    }
}
