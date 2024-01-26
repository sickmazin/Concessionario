package com.project.concessionario.sidebarState;

public interface State {
    void show();
    State nextState();
}
