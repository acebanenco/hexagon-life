package com.github.acebanenco.hexlife.model;

import com.github.acebanenco.hexlife.layout.ShapeGridLayout;

import java.util.List;

public class LifeGenerationLogic {

    private final double[] beBornWeights = {
            0.000, //0
            0.000, //1
            0.900, //2
            0.900, //3
            0.000, //4
            0.000, //5
            0.000, //6
    };

    private final double[] surviveWeights = {
            0.000, //0
            0.000, //1
            0.900, //2
            0.900, //3
            0.000, //4
            0.000, //5
            0.000, //6
    };

    boolean shouldBeBorn(List<ShapeGridLayout.GridLocation> aliveNeighbours) {
        long count = aliveNeighbours.stream()
                .filter(l -> !(l.getColumn() == 0 &&
                        (l.getRow() == 2 || l.getRow() == -2)))
                .count();
        return beBornWeights[(int) count] > 0.5;
    }

    boolean shouldSurvive(List<ShapeGridLayout.GridLocation> aliveNeighbours) {
        long count = aliveNeighbours.size();
        return surviveWeights[(int) count] > 0.5;
    }
}
