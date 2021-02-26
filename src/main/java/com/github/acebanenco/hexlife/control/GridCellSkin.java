package com.github.acebanenco.hexlife.control;

import javafx.scene.control.SkinBase;

public class GridCellSkin extends SkinBase<GridCell> {

    private final GridCellBehavior behavior;

    public GridCellSkin(GridCell gridCell) {
        super(gridCell);
        behavior = new GridCellBehavior(gridCell);
    }

    @Override
    public void dispose() {
        super.dispose();
        behavior.dispose();
    }
}
