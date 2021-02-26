package com.github.acebanenco.hexlife.control;

import com.github.acebanenco.hexlife.layout.ShapeGridLayout;
import com.github.acebanenco.hexlife.layout.ShapeGridLayout.GridLocation;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.css.PseudoClass;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import java.util.function.Supplier;

import static javafx.scene.paint.Color.rgb;

public class GridCell extends Control {

    private static final String DEFAULT_STYLE_CLASS = "grid-cell";
    private static final PseudoClass PSEUDO_CLASS_ALIVE =
            PseudoClass.getPseudoClass("alive");

    private final GridLocation gridLocation;
    private Shape shape;
    private final Supplier<Shape> shapeSupplier;
    private BooleanProperty aliveProperty;

    public GridCell(GridLocation gridLocation,
                    Supplier<Shape> shapeSupplier) {
        this.gridLocation = gridLocation;
        this.shapeSupplier = shapeSupplier;
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    public GridLocation getGridLocation() {
        return gridLocation;
    }

    public boolean isAlive() {
        return aliveProperty().get();
    }

    public void setAlive(boolean alive) {
        aliveProperty().set(alive);
    }

    @Override
    protected void layoutChildren() {
        if ( shape == null ) {
            shape = shapeSupplier.get();
            shape.getStyleClass().add("shape");
            shape.setStrokeWidth(0.1);

            getChildren().setAll(shape);
        }
        super.layoutChildren();
    }

    @Override
    protected double computeMinWidth(double height) {
        return super.computeMinWidth(height);
    }

    @Override
    protected double computeMinHeight(double width) {
        return super.computeMinHeight(width);
    }

    @Override
    protected double computeMaxWidth(double height) {
        return super.computeMaxWidth(height);
    }

    @Override
    protected double computeMaxHeight(double width) {
        return super.computeMaxHeight(width);
    }

    @Override
    protected double computePrefWidth(double height) {
        return super.computePrefWidth(height);
    }

    @Override
    protected double computePrefHeight(double width) {
        return super.computePrefHeight(width);
    }

    public BooleanProperty aliveProperty() {
        if (aliveProperty == null) {
            aliveProperty = new BooleanPropertyBase() {
                @Override
                protected void invalidated() {
                    boolean alive = get();
                    pseudoClassStateChanged(PSEUDO_CLASS_ALIVE, alive);
                    if ( alive ) {
                        shape.setFill(rgb(135, 206, 250));
                    } else {
                        shape.setFill(rgb(255, 250, 240));
                    }
                }

                @Override
                public Object getBean() {
                    return GridCell.this;
                }

                @Override
                public String getName() {
                    return "alive";
                }
            };
        }
        return aliveProperty;
    }

    @Override
    public String getUserAgentStylesheet() {
        return getClass().getResource("/com/github/acebanenco/hexlife/control/GridCell.css").toExternalForm();
    }
}
