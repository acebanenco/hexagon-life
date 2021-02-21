package com.github.acebanenco;

import javafx.animation.FillTransition;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

public class HexagonList {
    private final HexagonLifeModel lifeModel;
    private final Pane hexagonPane;
    private final ArrayList<Polygon> hexagons;
    private final AtomicBoolean coloring = new AtomicBoolean();
    private final Color COLOR_ALIVE = Color.BLUE.brighter();
    private final Color COLOR_DEAD = Color.WHITE;
    private final HexagonFactory hexagonFactory = new HexagonFactory();
    private final double hexagonSize = 20.0;
    private final AtomicLong lastTimeMouseClicked = new AtomicLong();

    public HexagonList(HexagonLifeModel lifeModel, int WIDTH, int HEIGHT) {
        this.lifeModel = lifeModel;
        hexagons = new ArrayList<>(HEIGHT * WIDTH);

        for (int yn = 0; yn < HEIGHT; yn++) {
            for (int xn = 0; xn < WIDTH; xn++) {
                Polygon hexagon = getHexagon(xn, yn, WIDTH);
                hexagons.add(hexagon);
            }
        }
        hexagonPane = new Pane();
        hexagonPane.getChildren().addAll(hexagons);

        hexagonPane.setOnMouseDragged(event -> {
            lastTimeMouseClicked.set(System.currentTimeMillis());
            if (coloring.get()) {
                double x1 = event.getX();
                double y1 = event.getY();
                Point2D point2D = hexagonPane.sceneToLocal(x1, y1);
                IntStream.range(0, hexagons.size())
                        .filter(index -> hexagons.get(index).contains(point2D))
                        .forEach(index -> {
                            lifeModel.set(index);
                            hexagons.get(index).setFill(COLOR_ALIVE);
                        });
            }
        });
    }

    void reset() {
        hexagons.forEach(h -> h.setFill(COLOR_DEAD));
    }


    private Polygon getHexagon(int xn, int yn, int WIDTH) {
        double x0 = -hexagonSize / 4.0;
        double y0 = 0.0;

        double r = hexagonSize / 2.0;
        double dy = hexagonSize * Math.sqrt(3.0) / 4;

        double x = x0 + 3 * xn * r + (yn % 2) * 3 * r / 2;
        double y = y0 + yn * dy;

        Polygon hexagon = hexagonFactory.getHexagon(x, y, hexagonSize, hexagonSize);
        hexagon.setFill(COLOR_DEAD);
        //hexagon.setStroke(Color.BLACK);

        int index = xn + yn * WIDTH;
        hexagon.onMouseClickedProperty().set(event -> {
            lastTimeMouseClicked.set(System.currentTimeMillis());
            hexagon.setFill(lifeModel.flip(index) ? COLOR_ALIVE : COLOR_DEAD);
        });

        hexagon.onMousePressedProperty().set(event -> {
            lastTimeMouseClicked.set(System.currentTimeMillis());
            coloring.set(true);
            System.out.println("Coloring");
        });
        hexagon.onMouseReleasedProperty().set(event -> {
            lastTimeMouseClicked.set(System.currentTimeMillis());
            coloring.set(false);
            System.out.println("Stopped coloring");
        });

        return hexagon;
    }

    AtomicLong getLastTimeMouseClicked() {
        return lastTimeMouseClicked;
    }

    Polygon get(int bitIndex) {
        return hexagons.get(bitIndex);
    }

    public int size() {
        return hexagons.size();
    }

    FillTransition getFillTransition(BitSet nextFill, int bitIndex, Polygon hexagon, double nextGenerationInterval) {
        FillTransition ft;
        if (nextFill.get(bitIndex)) {
            ft = new FillTransition(Duration.millis(nextGenerationInterval), hexagon);
            // turn on
            ft.setToValue(COLOR_ALIVE/*.interpolate(COLOR_DEAD, neighbourCount/(double)6)*/);
        } else {
            ft = new FillTransition(Duration.millis(nextGenerationInterval), hexagon);
            // turn off
            ft.setToValue(COLOR_DEAD);
        }
        return ft;
    }

    Pane getHexagonPane() {
        return hexagonPane;
    }
}
