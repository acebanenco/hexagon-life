package com.github.acebanenco.hexlife;

import javafx.animation.ParallelTransition;
import javafx.scene.shape.Shape;

import java.util.BitSet;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
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

    public HexagonListUpdate(HexagonLifeModel lifeModel, HexagonList hexagons) {
        this.lifeModel = lifeModel;
        this.hexagons = hexagons;
    }

    void scheduleUpdates() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(r1 -> {
            Thread thread = new Thread(r1);
            thread.setDaemon(true);
            return thread;
        });

        // Use
        Runnable generationTask = () -> {
            try {
                advanceModel();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        };
        executorService.scheduleAtFixedRate(
                generationTask,
                100L,
                nextGenerationInterval,
                TimeUnit.MILLISECONDS);
    }

    private void advanceModel() {
        long delay = interactionDelay + hexagons.getLastTimeMouseClicked().get() - System.currentTimeMillis();
        if (delay > 0L) {
            //hexagons.forEach(h -> h.setStroke(Color.gray(0.2)));
            return;
        }
        BitSet nextFill = lifeModel.getNextFill();
        if (nextFill != null) {
            animate(nextFill);
            lifeModel.copyBitsFrom(nextFill);
        }
    }

    private void animate(BitSet nextFill) {
        ParallelTransition pt = new ParallelTransition();
        pt.getChildren().addAll(IntStream.range(0, hexagons.size())
                .mapToObj(bitIndex -> {
                    Shape hexagon = hexagons.get(bitIndex);
                    if (nextFill.get(bitIndex) == lifeModel.get(bitIndex)) {
                        return null;
                    }
                    if (nextFill.get(bitIndex)) {
                        return null;
                    }
                    return hexagons.getFillTransition(nextFill, bitIndex, hexagon, nextGenerationInterval / 2.0);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        pt.getChildren().addAll(IntStream.range(0, hexagons.size())
                .mapToObj(bitIndex -> {
                    Shape hexagon = hexagons.get(bitIndex);
                    if (nextFill.get(bitIndex) == lifeModel.get(bitIndex)) {
                        return null;
                    }
                    if (!nextFill.get(bitIndex)) {
                        return null;
                    }
                    return hexagons.getFillTransition(nextFill, bitIndex, hexagon, nextGenerationInterval / 2.0);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        pt.play();

    }
}
