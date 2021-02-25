package com.github.acebanenco.hexlife;

import com.github.acebanenco.hexlife.layout.HexagonGridLayout;
import com.github.acebanenco.hexlife.layout.ShapeGridLayout;
import com.github.acebanenco.hexlife.shape.HexagonShapeFactory;
import com.github.acebanenco.hexlife.shape.TransformedShapeFactory;
import javafx.animation.FillTransition;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.util.BitSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HexagonList {
    private final HexagonLifeModel lifeModel;
    private final Pane hexagonPane;
    private final List<Shape> hexagons;
    private final AtomicBoolean coloring = new AtomicBoolean();
    private final Color COLOR_ALIVE = Color.BLUE.brighter();
    private final Color COLOR_DEAD = Color.WHITE;
    private final TransformedShapeFactory transformedShapeFactory;
    private final ShapeGridLayout gridLayout;
    private final AtomicLong lastTimeMouseClicked = new AtomicLong();

    public HexagonList(HexagonLifeModel lifeModel, int WIDTH, int HEIGHT) {
        this.lifeModel = lifeModel;

        HexagonShapeFactory shapeFactory = new HexagonShapeFactory();
        transformedShapeFactory = new ProportionallyScaledShapeFactory(WIDTH, shapeFactory);
        gridLayout = new HexagonGridLayout(20, WIDTH, HEIGHT);


        hexagons = gridLayout.locationsStream()
                .map(this::getHexagon)
                .collect(Collectors.toList());
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


    private Shape getHexagon(ShapeGridLayout.GridLocation location) {
        Point2D point2D = gridLayout.getLocation(location);

        Shape hexagon = transformedShapeFactory.createShapeAt(point2D.getX(), point2D.getY());
        hexagon.setFill(COLOR_DEAD);
        //hexagon.setStroke(Color.BLACK);

        int index = gridLayout.indexOf(location);
        hexagon.onMouseClickedProperty().set(event -> {
            lastTimeMouseClicked.set(System.currentTimeMillis());
            hexagon.setFill(lifeModel.flip(index) ? COLOR_ALIVE : COLOR_DEAD);
        });

        hexagon.onMousePressedProperty().set(event -> {
            lastTimeMouseClicked.set(System.currentTimeMillis());
            coloring.set(true);
        });
        hexagon.onMouseReleasedProperty().set(event -> {
            lastTimeMouseClicked.set(System.currentTimeMillis());
            coloring.set(false);
        });

        return hexagon;
    }

    AtomicLong getLastTimeMouseClicked() {
        return lastTimeMouseClicked;
    }

    Shape get(int bitIndex) {
        return hexagons.get(bitIndex);
    }

    public int size() {
        return hexagons.size();
    }

    FillTransition getFillTransition(BitSet nextFill, int bitIndex, Shape hexagon, double nextGenerationInterval) {
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
