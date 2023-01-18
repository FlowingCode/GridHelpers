package com.flowingcode.vaadin.addons.gridhelpers.it;

import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.grid.testbench.GridTRElement;
import com.vaadin.testbench.TestBenchElement;

/*
 * Workaround for bug https://github.com/vaadin/flow-components/issues/1598
 */
public class MyGridElement extends GridElement {

  @Override
  public void select(int rowIndex) {
    if (isMultiselect()) {
      // call @ClientCallable Grid.select() on server-side which will fire the selection event.
      GridTRElement row = getRow(rowIndex);
      executeScript("arguments[0].$server.select(arguments[1]._item.key)", this, row);
    } else {
      super.select(rowIndex);
    }
  }

  private boolean isMultiselect() {
    return (boolean) executeScript(
        "return arguments[0]._getColumns().filter(function(col) { return typeof col.selectAll != 'undefined';}).length > 0",
        this);
  }

  public void deselectAll() {
    TestBenchElement selectionCol = $("vaadin-grid-flow-selection-column").first();
    executeScript("arguments[0].$server.deselectAll()", selectionCol);
  }

}
