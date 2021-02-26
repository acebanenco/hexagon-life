package com.github.acebanenco.hexlife;

public class LifeGenerationLogic {

    private final double[] beBornWeights = {
            0.000, //0
            0.900, //1
            0.000, //2
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

    boolean shouldBeBorn(int aliveNeighbours) {
        double probability = 1;//RANDOM.nextDouble();
        return beBornWeights[aliveNeighbours] > probability * 0.5;
    }

    boolean shouldSurvive(int aliveNeighbours) {
        double probability = 1;//RANDOM.nextDouble();
        return surviveWeights[aliveNeighbours] > probability * 0.5;
    }
}
