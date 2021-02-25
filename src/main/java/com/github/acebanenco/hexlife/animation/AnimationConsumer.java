package com.github.acebanenco.hexlife.animation;

import com.github.acebanenco.hexlife.control.GridCell;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class AnimationConsumer {
    private final Queue<FillTransition> localTransitions;
    private final AtomicLong resumeTimeMillis = new AtomicLong();
    private final Duration animationDuration;
    private final Runnable advanceModelTask;
    private final ScheduledExecutorService executorService;
    private ScheduledFuture<?> advanceModelFuture;
    private BooleanProperty autoStartUpdatesProperty;

    public AnimationConsumer(Runnable advanceModelTask, Duration animationDuration) {
        this.advanceModelTask = advanceModelTask;
        this.animationDuration = animationDuration;
        localTransitions = new ArrayDeque<>();

        executorService = Executors.newScheduledThreadPool(1, r1 -> {
            Thread thread = new Thread(r1);
            thread.setName("Advance-Life-Model");
            thread.setDaemon(true);
            return thread;
        });
    }

    public boolean isAutoStartUpdates() {
        return autoStartUpdatesProperty().get();
    }

    public BooleanProperty autoStartUpdatesProperty() {
        if ( autoStartUpdatesProperty == null ) {
            autoStartUpdatesProperty = new BooleanPropertyBase() {
                @Override
                protected void invalidated() {
                    boolean start = this.get();
                    if ( start ) {
                        scheduleModelUpdates();
                    } else {
                        cancelModelUpdates();
                    }
                }

                @Override
                public Object getBean() {
                    return AnimationConsumer.this;
                }

                @Override
                public String getName() {
                    return "autoStartUpdates";
                }
            };
        }
        return autoStartUpdatesProperty;
    }

    private void scheduleModelUpdates() {
        if ( advanceModelFuture != null && !advanceModelFuture.isCancelled() ) {
            // warning, already scheduled
            return;
        }
        long initialDelay = 100L;
        long nextGenerationInterval = 700L;
        advanceModelFuture = executorService.scheduleAtFixedRate(
                () -> {
                    if (System.currentTimeMillis() >= resumeTimeMillis.get()) {
                        advanceModelTask.run();
                        commitAnimationsAsync();
                    }
                },
                initialDelay,
                nextGenerationInterval,
                TimeUnit.MILLISECONDS);
    }

    private void cancelModelUpdates() {
        if ( advanceModelFuture == null || advanceModelFuture.isCancelled() ) {
            // warning, already canceled or not started at all
            return;
        }
        advanceModelFuture.cancel(false);
    }

    public void commitAnimationsAsync() {
        ArrayList<FillTransition> copy = new ArrayList<>(localTransitions);
        localTransitions.clear();
        playAnimationsAsync(copy);
    }

    public void freezeModelUpdates() {
        long freezeDelay = 5000L;
        resumeTimeMillis.set(System.currentTimeMillis() + freezeDelay);
    }

    private void playAnimationsAsync(List<FillTransition> transitions) {
        if (transitions.isEmpty()) {
            return;
        }
        if (transitions.size() == 1) {
            FillTransition fillTransition = transitions.get(0);
            fillTransition.play();
            return;
        }
        ParallelTransition parallelTransition = new ParallelTransition();
        ObservableList<Animation> childrenTransitions = parallelTransition.getChildren();
        childrenTransitions.setAll(transitions);
        parallelTransition.play();
    }

    public Queue<FillTransition> getLocalTransitions() {
        return localTransitions;
    }

    public void addLocalFillTransition(GridCell gridCell, Color toColor) {
        Shape shape = gridCell.getStyleableNode();
        FillTransition transition = new FillTransition(animationDuration, shape, null, toColor);
        localTransitions.add(transition);
    }
}
