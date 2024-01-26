package com.project.concessionario.sidebarState;

import javafx.animation.Transition;
import javafx.scene.Node;

public abstract class TransitionState implements State {
    protected int from;
    protected int to;
    protected Node node;
    protected Transition transition;

    public TransitionState() {}

    public TransitionState(Node node, Transition transition, int from, int to) {
        this.node = node;
        this.transition = transition;
        this.from = from;
        this.to = to;
    }
    public void setFrom(int from) {
        this.from = from;
    }
    public void setNode(Node node) {
        this.node = node;
    }
    public void setTo(int to) {
        this.to = to;
    }
    public void setTransition(Transition transition) {
        this.transition = transition;
    }

    public Node getNode() {
        return node;
    }

    public abstract void show();

    public abstract State nextState();
}
