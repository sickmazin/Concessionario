package com.project.concessionario.sidebarState;


import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

public class HideState extends TransitionState {

    public HideState() {
        super();
    }
    public HideState(Node node, TranslateTransition transition, int from, int to) {
        super(node, transition, from, to);
    }

    @Override
    public void show() {
        TranslateTransition tt=(TranslateTransition) transition;

        node.setTranslateX(from);
        tt.setToX(to);
        tt.play();

        tt.setOnFinished(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                node.setDisable(true);
                node.setVisible(true);
            }
        });
    }

    @Override
    public State nextState() {
        TranslateTransition tt=(TranslateTransition) transition;
        return new ShowState(node, tt, to, from);
    }
}
