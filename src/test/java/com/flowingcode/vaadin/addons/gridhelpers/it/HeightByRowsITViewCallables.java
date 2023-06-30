package com.flowingcode.vaadin.addons.gridhelpers.it;

import com.flowingcode.vaadin.addons.gridhelpers.HeightMode;
import com.flowingcode.vaadin.testbench.rpc.RmiCallable;

public interface HeightByRowsITViewCallables extends RmiCallable {

  double getHeightByRows();

  void setHeightByRows(int heightByRows);

  HeightMode getHeightMode();

  void setHeightMode(HeightMode row);

  void roundtrip();

}
