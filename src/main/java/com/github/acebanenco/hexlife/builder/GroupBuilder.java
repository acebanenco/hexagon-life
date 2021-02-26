package com.github.acebanenco.hexlife.builder;

import javafx.scene.Group;
import javafx.scene.Node;

public class GroupBuilder {
    final Group group = new Group();

    public GroupBuilder add(Node child) {
        group.getChildren().add(child);
        return this;
    }

    public Group build() {
        return group;
    }
}
