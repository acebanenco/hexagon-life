package com.github.acebanenco.hexlife.control;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class GridCellBehavior {

    private final GridCell gridCell;
    private final EventHandler<MouseEvent> onMouseClicked;

    public GridCellBehavior(GridCell gridCell) {
        this.gridCell = gridCell;
        this.onMouseClicked = this::onMouseClicked;
        gridCell.setOnMouseClicked(onMouseClicked);
    }

    private void onMouseClicked(MouseEvent mouseEvent) {
        GridCell gridCell = (GridCell) mouseEvent.getSource();
        boolean alive = gridCell.isAlive();
        gridCell.setAlive(!alive);
    }

    public void dispose() {
        if ( gridCell.getOnMouseClicked() == onMouseClicked ) {
            gridCell.setOnMouseClicked(null);
        }
    }
}
