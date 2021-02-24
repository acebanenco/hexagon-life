package com.github.acebanenco;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HexagonShapePoints implements ShapePoints {
    private final static double SQUARE_OF_THREE = Math.sqrt(3);

    @Override
    public List<Double> getShapePoints() {
        return Arrays.stream(new double[]{
                -2, 0,
                -1, -SQUARE_OF_THREE,
                +1, -SQUARE_OF_THREE,
                +2, 0,
                +1, +SQUARE_OF_THREE,
                -1, +SQUARE_OF_THREE,
                -2, 0,})
                .boxed()
                .collect(Collectors.toList());
    }
}
