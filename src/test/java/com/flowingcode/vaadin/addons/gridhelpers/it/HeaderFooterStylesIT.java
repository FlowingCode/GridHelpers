/*-
 * #%L
 * Grid Helpers Add-on
 * %%
 * Copyright (C) 2022 - 2025 Flowing Code
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

import static org.junit.Assert.assertEquals;
import com.flowingcode.vaadin.addons.gridhelpers.it.HeaderFooterStylesCallables.HeaderCellWrapper;
import com.flowingcode.vaadin.addons.gridhelpers.it.HeaderFooterStylesCallables.HeaderRowWrapper;
import com.flowingcode.vaadin.testbench.rpc.HasRpcSupport;
import com.vaadin.flow.component.grid.testbench.GridElement;
import org.junit.Test;


public class HeaderFooterStylesIT extends AbstractViewTest implements HasRpcSupport {

  HeaderFooterStylesCallables $server = createCallableProxy(HeaderFooterStylesCallables.class);

  GridHelperElement grid;

  public HeaderFooterStylesIT() {
    super(HeaderFooterStylesView.ROUTE);
  }

  @Override
  public void setup() throws Exception {
    super.setup();
    grid = new GridHelperElement($(GridElement.class).waitForFirst());
  }

  @Test
  public void testHeaderClassesApplied() {
    HeaderRowWrapper row0 = $server.getRow(0);
    row0.join(0, 1).setClassName("row0-cell0");
    row0.join(2, 3).setClassName("row0-cell1");

    HeaderRowWrapper row1 = $server.getRow(1);
    for (int i = 0; i < 5; i++) {
      row1.getCell(i).setClassName("row1-cell" + i);
    }

    assertEquals("row0-cell0", grid.getHeaderCellAt(0, 0).getAttribute("class"));
    assertEquals("row0-cell1", grid.getHeaderCellAt(0, 1).getAttribute("class"));

    for (int i = 0; i < 5; i++) {
      assertEquals("row1-cell" + i, grid.getHeaderCellAt(1, i).getAttribute("class"));
    }
  }

  @Test
  public void testHeaderCellMutability() {
    // https://github.com/FlowingCode/GridHelpers/issues/134
    // setColumnOrder and join reuse existing HeaderCell instances
    HeaderRowWrapper row0 = $server.getRow(0);
    HeaderCellWrapper header0 = row0.join(0, 1);
    HeaderCellWrapper header1 = row0.join(2, 3);
    $server.setColumnOrder(2, 3, 0, 1, 4);
    header0.setClassName("row0-cell0");
    header1.setClassName("row0-cell1");
    assertEquals("row0-cell1", grid.getHeaderCellAt(0, 0).getAttribute("class"));
    assertEquals("row0-cell0", grid.getHeaderCellAt(0, 1).getAttribute("class"));
  }

}
