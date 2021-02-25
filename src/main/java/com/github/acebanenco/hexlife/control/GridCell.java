package com.github.acebanenco.hexlife.control;

import com.github.acebanenco.hexlife.animation.AnimationConsumer;
import com.github.acebanenco.hexlife.layout.ShapeGridLayout;
import javafx.animation.FillTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.util.Queue;

public class GridCell extends Control {

    private final Color COLOR_ALIVE = Color.BLUE.brighter();
    private final Color COLOR_DEAD = Color.WHITE;

    private final ShapeGridLayout.GridLocation gridLocation;
    private final Shape shape;

    private BooleanProperty aliveProperty;
    private AnimationConsumer animationConsumer;

    public GridCell(ShapeGridLayout.GridLocation gridLocation, Shape shape) {
        this.gridLocation = gridLocation;
        this.shape = shape;
        init();
    }

    public void setAnimationConsumer(AnimationConsumer animationConsumer) {
        this.animationConsumer = animationConsumer;
    }

    private void init() {
        this.shape.setFill(COLOR_DEAD);
        this.shape.setStroke(Color.gray(0.8));
        this.shape.setStrokeWidth(0.5);
    }

    public ShapeGridLayout.GridLocation getGridLocation() {
        return gridLocation;
    }

    public boolean isAlive() {
        return aliveProperty().get();
    }

    public void setAlive(boolean alive) {
        aliveProperty().set(alive);
    }

    public BooleanProperty aliveProperty() {
        if ( aliveProperty == null ) {
             aliveProperty = new BooleanPropertyBase() {
                 @Override
                 protected void invalidated() {
                     boolean alive = this.get();
                     updateShapeFill(alive);
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

    private void updateShapeFill(boolean alive) {
        Color newColor = alive ? COLOR_ALIVE : COLOR_DEAD;
        if ( animationConsumer == null ) {
            shape.setFill(newColor);
        } else {
            animationConsumer.addLocalFillTransition(this, newColor);
        }
    }

    @Override
    public Shape getStyleableNode() {
        return shape;
    }
}
