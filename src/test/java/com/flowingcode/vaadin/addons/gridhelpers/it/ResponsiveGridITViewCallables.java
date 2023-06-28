package com.flowingcode.vaadin.addons.gridhelpers.it;

import com.flowingcode.vaadin.testbench.rpc.RmiCallable;
import com.flowingcode.vaadin.testbench.rpc.RmiRemote;

public interface ResponsiveGridITViewCallables extends RmiCallable {

  IResponsiveStep responsiveStep(int minWidth);

  int getCurrentStep();

  interface IResponsiveStep extends RmiRemote {
    void show(int colIndex);

    void hide(int colIndex);

    void showAll();

    void hideAll();

    void remove();

    IListenerRegistration addListener();
  }

  interface IListenerRegistration extends RmiRemote {
    int getCount();

    int getLastMinWidth();

    void remove();

    void cumulative();

  }

  default void roundtrip() {}

}
