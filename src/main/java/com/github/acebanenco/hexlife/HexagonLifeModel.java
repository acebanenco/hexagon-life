package com.github.acebanenco.hexlife;

import com.github.acebanenco.hexlife.layout.ShapeGridLayout;

import java.util.BitSet;
import java.util.Set;
import java.util.stream.IntStream;

public class HexagonLifeModel {
    private final BitSet currentFill;
    private final LifeGenerationLogic generationLogic;
    private final ShapeGridLayout gridLayout;

    public HexagonLifeModel(LifeGenerationLogic generationLogic,
                            int WIDTH, int HEIGHT,
                            ShapeGridLayout gridLayout) {
        this.generationLogic = generationLogic;
        this.gridLayout = gridLayout;
        currentFill = new BitSet(HEIGHT * WIDTH);
    }

    BitSet getNextFill() {
        BitSet nextFill = BitSet.valueOf(currentFill.toLongArray());
        gridLayout.locationsStream()
                .forEach(location -> {
                    int aliveNeighbours = getAliveNeighbourCount(location);
                    int currentIndex = gridLayout.indexOf(location);
                    boolean alive = currentFill.get(currentIndex);
                    if (alive && !generationLogic.shouldSurvive(aliveNeighbours)) {
                        nextFill.set(currentIndex, false);
                    }
                    if (!alive && generationLogic.shouldBeBorn(aliveNeighbours)) {
                        nextFill.set(currentIndex, true);
                    }
                });
        if (nextFill.equals(currentFill)) {
            return null;
        }
        return nextFill;
    }

    void copyBitsFrom(BitSet nextFill) {
        IntStream.range(0, nextFill.size())
                .forEach(bitIndex ->
                        currentFill.set(bitIndex, nextFill.get(bitIndex)));
    }

    int getAliveNeighbourCount(ShapeGridLayout.GridLocation location) {
        Set<ShapeGridLayout.GridLocation> neighbours = gridLayout.getNeighbours(location, 1);
        return (int) neighbours.stream()
                .map(gridLayout::indexOf)
                .filter(currentFill::get)
                .count();
    }

    void clear() {
        currentFill.clear();
    }

    void set(int index) {
        currentFill.set(index, true);
    }

    boolean flip(int index) {
        currentFill.flip(index);
        return currentFill.get(index);
    }

    boolean get(int index) {
        return currentFill.get(index);
    }

}
