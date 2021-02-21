package com.github.acebanenco;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.scene.shape.Polygon;

import java.util.BitSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HexagonListUpdate {

    private final HexagonLifeModel lifeModel;
    private final HexagonList hexagons;

    private final long nextGenerationInterval = 700L;
    private final long interactionDelay = 5000L;
    private final int WIDTH;
    private final int HEIGHT;

    public HexagonListUpdate(HexagonLifeModel lifeModel, HexagonList hexagons, int WIDTH, int HEIGHT) {
        this.lifeModel = lifeModel;
        this.hexagons = hexagons;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
    }

    void scheduleUpdates() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(r1 -> {
            Thread thread = new Thread(r1);
            thread.setDaemon(true);
            return thread;
        });
        Runnable generationTask = () -> {
            long delay = interactionDelay + hexagons.getLastTimeMouseClicked().get() - System.currentTimeMillis();
            if (delay > 0L) {
                //hexagons.forEach(h -> h.setStroke(Color.gray(0.2)));
                return;
            }
            BitSet nextFill = lifeModel.getNextFill(WIDTH, HEIGHT);
            if (nextFill != null) {
                animate(nextFill);
                lifeModel.copyBits(nextFill);
            }
        };
        executorService.scheduleAtFixedRate(generationTask, 100L, nextGenerationInterval, TimeUnit.MILLISECONDS);
    }

    private void animate(BitSet nextFill) {

        ParallelTransition pt = new ParallelTransition();
        pt.getChildren().addAll(IntStream.range(0, hexagons.size())
                .mapToObj(bitIndex -> {
                    Polygon hexagon = hexagons.get(bitIndex);
                    if (nextFill.get(bitIndex) == lifeModel.get(bitIndex)) {
                        return null;
                    }
                    if ( nextFill.get(bitIndex) ) {
                        return null;
                    }
                    return hexagons.getFillTransition(nextFill, bitIndex, hexagon, nextGenerationInterval/2);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        pt.getChildren().addAll(IntStream.range(0, hexagons.size())
                .mapToObj(bitIndex -> {
                    Polygon hexagon = hexagons.get(bitIndex);
                    if (nextFill.get(bitIndex) == lifeModel.get(bitIndex)) {
                        return null;
                    }
                    if ( !nextFill.get(bitIndex) ) {
                        return null;
                    }
                    return hexagons.getFillTransition(nextFill, bitIndex, hexagon, nextGenerationInterval/2);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        pt.play();

    }}
