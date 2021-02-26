package com.github.acebanenco.hexlife.animation;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class AnimationConsumer {
    private final AtomicLong resumeTimeMillis = new AtomicLong();
    private final Runnable advanceModelTask;
    private final ScheduledExecutorService executorService;
    private ScheduledFuture<?> advanceModelFuture;
    private BooleanProperty autoStartUpdatesProperty;

    public AnimationConsumer(Runnable advanceModelTask) {
        this.advanceModelTask = advanceModelTask;

        executorService = Executors.newScheduledThreadPool(1, r1 -> {
            Thread thread = new Thread(r1);
            thread.setName("Advance-Life-Model");
            thread.setDaemon(true);
            return thread;
        });
    }

    public BooleanProperty autoStartUpdatesProperty() {
        if (autoStartUpdatesProperty == null) {
            autoStartUpdatesProperty = new BooleanPropertyBase() {
                @Override
                protected void invalidated() {
                    boolean start = this.get();
                    if (start) {
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
        if (advanceModelFuture != null && !advanceModelFuture.isCancelled()) {
            // warning, already scheduled
            return;
        }
        long initialDelay = 100L;
        long nextGenerationInterval = 700L;
        advanceModelFuture = executorService.scheduleAtFixedRate(
                () -> {
                    if (System.currentTimeMillis() >= resumeTimeMillis.get()) {
                        advanceModelTask.run();
                    }
                },
                initialDelay,
                nextGenerationInterval,
                TimeUnit.MILLISECONDS);
    }

    private void cancelModelUpdates() {
        if (advanceModelFuture == null || advanceModelFuture.isCancelled()) {
            // warning, already canceled or not started at all
            return;
        }
        advanceModelFuture.cancel(false);
    }

    public void freezeModelUpdates() {
        long freezeDelay = 5000L;
        resumeTimeMillis.set(System.currentTimeMillis() + freezeDelay);
    }
}
