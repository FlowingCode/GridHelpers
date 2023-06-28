/*-
 * #%L
 * Grid Helpers Add-on
 * %%
 * Copyright (C) 2022 Flowing Code
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
import static org.junit.Assert.assertTrue;
import com.flowingcode.vaadin.addons.gridhelpers.it.ResponsiveGridITViewCallables.IListenerRegistration;
import com.flowingcode.vaadin.testbench.rpc.HasRpcSupport;
import com.vaadin.flow.component.grid.testbench.GridElement;
import org.junit.Test;

public class ResponsiveGridIT extends AbstractViewTest implements HasRpcSupport {

  ResponsiveGridITViewCallables $server = createCallableProxy(ResponsiveGridITViewCallables.class);
  GridHelperElement grid;

  public ResponsiveGridIT() {
    super(ResponsiveGridITView.ROUTE);
  }

  @Override
  public void setup() throws Exception {
    super.setup();
    grid = new GridHelperElement($(GridElement.class).waitForFirst());
    setWidth(500);
  }

  private void setWidth(int w) {
    executeScript("arguments[0].parentElement.style.width=" + w + "+'px';", grid);
    $server.roundtrip();
  }

  @Test
  public void testCreateResponsiveStep() {
    $server.responsiveStep(300);
    $server.roundtrip();
    assertEquals(300, $server.getCurrentStep());
  }

  @Test
  public void testHide() {
    $server.responsiveStep(0).hide(0);
    $server.roundtrip();
    assertTrue(!grid.hasColumn("Col 0"));
  }

  @Test
  public void testHideAll() {
    $server.responsiveStep(0).hideAll();
    $server.roundtrip();
    assertTrue(grid.getVisibleColumns().isEmpty());
  }

  @Test
  public void testShow() {
    $server.responsiveStep(0).hide(0);
    $server.roundtrip();
    assertTrue(!grid.hasColumn("Col 0"));

    $server.responsiveStep(100).show(0);
    $server.roundtrip();
    assertTrue(grid.hasColumn("Col 0"));
  }

  @Test
  public void testShowAll() {
    $server.responsiveStep(0).hide(0);
    $server.roundtrip();
    assertTrue(!grid.hasColumn("Col 0"));

    $server.responsiveStep(100).showAll();
    $server.roundtrip();
    assertTrue(grid.hasColumn("Col 0"));
  }

  @Test
  public void testRemove() {
    $server.responsiveStep(300);
    $server.responsiveStep(400);
    $server.roundtrip();
    assertEquals(400, $server.getCurrentStep());

    $server.responsiveStep(400).remove();
    $server.roundtrip();
    assertEquals(300, $server.getCurrentStep());
  }

  @Test
  public void testAddListenerFireImmediately() {
    IListenerRegistration listener = $server.responsiveStep(200).addListener();
    assertEquals(1, listener.getCount());
    assertEquals(200, listener.getLastMinWidth());
  }

  @Test
  public void testAddListenerAndResize() {
    setWidth(200);
    IListenerRegistration listener = $server.responsiveStep(300).addListener();
    assertEquals(0, listener.getCount());
    setWidth(400);
    $server.roundtrip();
    assertEquals(1, listener.getCount());
    assertEquals(300, listener.getLastMinWidth());
  }

  @Test
  public void testListenerRemove() {
    setWidth(200);
    IListenerRegistration listener = $server.responsiveStep(300).addListener();
    listener.remove();
    setWidth(400);
    $server.roundtrip();
    assertEquals(0, listener.getCount());
    assertEquals(-1, listener.getLastMinWidth());
  }

  @Test
  public void testListenerCumulative() {
    setWidth(200);
    IListenerRegistration listener = $server.responsiveStep(300).addListener();
    $server.responsiveStep(400);
    listener.cumulative();

    setWidth(500);
    $server.roundtrip();
    assertEquals(1, listener.getCount());
    assertEquals(400, listener.getLastMinWidth());
  }

  @Test
  public void testListenerCumulativeFireImmediately() {
    $server.responsiveStep(300).addListener();
    IListenerRegistration listener = $server.responsiveStep(200).addListener();
    assertEquals(0, listener.getCount());

    listener.cumulative();
    assertEquals(1, listener.getCount());
    assertEquals(300, listener.getLastMinWidth());
  }


}
