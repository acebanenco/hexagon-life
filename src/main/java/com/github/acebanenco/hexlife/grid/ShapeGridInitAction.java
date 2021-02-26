package com.github.acebanenco.hexlife.grid;

public class ShapeGridInitAction {

    private final ShapeGridBindAction bindAction;
    private final ShapeGridPopulateAction populateAction;

    public ShapeGridInitAction(ShapeGridBindAction bindAction,
                               ShapeGridPopulateAction populateAction) {
        this.bindAction = bindAction;
        this.populateAction = populateAction;
    }

    public void apply(ShapeGridContainer gridContainer) {
        bindAction.apply(gridContainer);
        populateAction.apply(gridContainer);
    }
}
