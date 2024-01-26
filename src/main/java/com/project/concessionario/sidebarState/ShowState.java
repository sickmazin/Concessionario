package com.project.concessionario.sidebarState;


import javafx.animation.TranslateTransition;
import javafx.scene.Node;

public class ShowState extends TransitionState {

    public ShowState(Node node, TranslateTransition translateTransition, int from, int to) {
        super(node, translateTransition, from, to);
    }

    @Override
    public void show() {
        node.setDisable(false);
        node.setVisible(true);
        TranslateTransition tt=(TranslateTransition) transition;
        tt.setOnFinished(null);
        node.setTranslateX(from);
        tt.setToX(to);
        tt.play();
    }

    @Override
    public State nextState() {
        TranslateTransition tt=(TranslateTransition) transition;
        return new HideState(node, tt, to, from);
    }
}
