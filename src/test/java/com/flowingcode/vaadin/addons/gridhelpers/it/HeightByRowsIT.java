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

import static com.flowingcode.vaadin.addons.gridhelpers.it.HeightByRowsITView.EMPTY;
import static com.flowingcode.vaadin.addons.gridhelpers.it.HeightByRowsITView.FULLSIZE;
import static org.junit.Assert.assertEquals;
import com.flowingcode.vaadin.addons.gridhelpers.HeightMode;
import com.flowingcode.vaadin.testbench.rpc.HasRpcSupport;
import com.vaadin.flow.component.grid.testbench.GridElement;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;

public class HeightByRowsIT extends AbstractViewTest implements HasRpcSupport {

  private static int ROWS = 10;

  HeightByRowsITViewCallables $server = createCallableProxy(HeightByRowsITViewCallables.class);

  GridHelperElement grid;

  public HeightByRowsIT() {
    super(HeightByRowsITView.ROUTE);
  }

  private void open(Object... args) {
    if (grid != null) {
      throw new IllegalStateException();
    }
    String params = Stream.of(args).map(Object::toString).collect(Collectors.joining(";"));
    getDriver().get(getDriver().getCurrentUrl() + "/" + params);
    grid = new GridHelperElement($(GridElement.class).waitForFirst());
    $server.roundtrip();
  }

  @Override
  public void setup() throws Exception {
    super.setup();
  }

  @Test
  public void testOpenWithItemsDefaultSize() {
    open(ROWS);
    assertEquals(ROWS, grid.getVisibleRowsCount());
    assertEquals(ROWS, $server.getHeightByRows(), 0);
    assertEquals(HeightMode.ROW, $server.getHeightMode());
    assertEquals(453, grid.getOffsetHeight());
    assertEquals("453px", grid.getHeightByRowsSize());
  }

  @Test
  public void testOpenWithItemsFullSize() {
    open(ROWS, FULLSIZE);
    assertEquals(ROWS, grid.getVisibleRowsCount());
    assertEquals(ROWS, $server.getHeightByRows(), 0);
    assertEquals(HeightMode.ROW, $server.getHeightMode());
    assertEquals(453, grid.getOffsetHeight());
    assertEquals("453px", grid.getHeightByRowsSize());
  }

  @Test
  public void testOpenEmptyDefaultSize() {
    open(ROWS, EMPTY);
    assertEquals(0, grid.getVisibleRowsCount());
    assertEquals(ROWS, $server.getHeightByRows(), 0);
    assertEquals(HeightMode.ROW, $server.getHeightMode());
    assertEquals(94, grid.getOffsetHeight());
    assertEquals("94px", grid.getHeightByRowsSize());
  }

  @Test
  public void testOpenEmptyFullSize() {
    open(ROWS, EMPTY, FULLSIZE);
    assertEquals(0, grid.getVisibleRowsCount());
    assertEquals(ROWS, $server.getHeightByRows(), 0);
    assertEquals(HeightMode.ROW, $server.getHeightMode());
    assertEquals(94, grid.getOffsetHeight());
    assertEquals("94px", grid.getHeightByRowsSize());
  }

}
