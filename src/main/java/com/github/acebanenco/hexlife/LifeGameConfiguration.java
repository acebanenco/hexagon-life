package com.github.acebanenco.hexlife;

public class LifeGameConfiguration {

    private double shapeWidth = 20;
    private double shapeHeight = (Math.sqrt(3) / 2) * shapeWidth;
    private int parentWidth = 1500;
    private int parentHeight = 750;

    public double getShapeWidth() {
        return shapeWidth;
    }

    public double getShapeHeight() {
        return shapeHeight;
    }

    public int getParentWidth() {
        return parentWidth;
    }

    public int getParentHeight() {
        return parentHeight;
    }

    public void setShapeWidth(double shapeWidth) {
        this.shapeWidth = shapeWidth;
    }

    public void setShapeHeight(double shapeHeight) {
        this.shapeHeight = shapeHeight;
    }

    public void setParentWidth(int parentWidth) {
        this.parentWidth = parentWidth;
    }

    public void setParentHeight(int parentHeight) {
        this.parentHeight = parentHeight;
    }
}
