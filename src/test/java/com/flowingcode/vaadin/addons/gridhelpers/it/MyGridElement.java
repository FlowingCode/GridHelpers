/*-
 * #%L
 * Grid Helpers Add-on
 * %%
 * Copyright (C) 2022 - 2024 Flowing Code
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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
