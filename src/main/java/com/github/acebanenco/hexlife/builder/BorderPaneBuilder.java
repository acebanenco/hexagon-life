package com.github.acebanenco.hexlife.builder;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public class BorderPaneBuilder {
    private final BorderPane borderPane;

    public BorderPaneBuilder() {
        borderPane = new BorderPane();
    }

    public BorderPaneBuilder center(Node node) {
        borderPane.setCenter(node);
        return this;
    }

    public BorderPaneBuilder bottom(Node node) {
        borderPane.setBottom(node);
        return this;
    }

    public BorderPane build() {
        return borderPane;
    }
}
