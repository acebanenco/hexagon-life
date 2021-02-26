package com.github.acebanenco.hexlife.layout;

import com.github.acebanenco.hexlife.layout.ShapeGridLayout.GridLocation;
import com.github.acebanenco.hexlife.layout.ShapeGridLayout.GridSize;

public class ConnectedBorderStrategy implements ShapeGridLayout.CrossBorderStrategy {

    @Override
    public GridLocation crossBorderLocation(GridLocation location, GridSize size) {
        int locationColumn = location.getColumn();
        int sizeColumns = size.getColumns();
        int column = locationColumn < 0
                ? locationColumn + sizeColumns
                : locationColumn < sizeColumns
                ? locationColumn
                : locationColumn - sizeColumns;

        int locationRow = location.getRow();
        int sizeRows = size.getRows();
        int row = locationRow < 0
                ? locationRow + sizeRows
                : locationRow < sizeRows
                ? locationRow
                : locationRow - sizeRows;

        if ( column == locationColumn && row == locationRow ) {
            return location;
        }
        return new GridLocation(column, row);
    }
}
