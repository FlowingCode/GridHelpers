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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import com.flowingcode.vaadin.testbench.rpc.HasRpcSupport;
import com.vaadin.flow.component.grid.testbench.GridElement;
import org.junit.Test;

public class ColumnToggleIT extends AbstractViewTest implements HasRpcSupport {

  IntegrationViewCallables $server = createCallableProxy(IntegrationViewCallables.class);
  GridHelperElement grid;

  public ColumnToggleIT() {
	super("it");
  }

  @Override
  public void setup() throws Exception {
	super.setup();
	grid = new GridHelperElement($(GridElement.class).waitForFirst());
  }

  @Test
  public void testColumnToggleVisible() {
    int nColumns = grid.getVisibleColumns().size();
    assertNull("ColumnToggle should be absent", grid.getColumnToggleButton());

    $server.setColumnToggleVisible(true);
    assertNotNull("ColumnToggle should be present", grid.getColumnToggleButton());
    assertThat(grid.getVisibleColumns(), hasSize(nColumns + 1));

    $server.setColumnToggleVisible(false);
    assertNull("ColumnToggle should be absent", grid.getColumnToggleButton());
    assertThat(grid.getVisibleColumns(), hasSize(nColumns));
  }

  @Test
  public void testColumnToggleClick() {
    int nColumns = grid.getVisibleColumns().size();

    $server.setColumnToggleVisible(true);
    grid.getColumnToggleButton().click();

    // the toggle is rendered in its own column
    assertThat(grid.getColumnToggleElements(), hasSize(nColumns));
    assertThat(grid.getVisibleColumns(), hasSize(++nColumns));

    grid.getColumnToggleElements().get(0).setChecked(false);
    assertThat(grid.getVisibleColumns(), hasSize(nColumns - 1));

    grid.getColumnToggleElements().get(0).setChecked(true);
    assertThat(grid.getVisibleColumns(), hasSize(nColumns));
  }

  @Test
  public void testColumnToggleListener() {
    $server.setColumnToggleVisible(true);
    assertNull($server.getToggledColumn());

    grid.getColumnToggleButton().click();

    grid.getColumnToggleElements().get(0).setChecked(false);
    assertEquals((Integer) 0, $server.getToggledColumn());

    grid.getColumnToggleElements().get(1).setChecked(false);
    assertEquals((Integer) 1, $server.getToggledColumn());

    grid.getColumnToggleElements().get(0).setChecked(true);
    assertEquals((Integer) 0, $server.getToggledColumn());
  }


  @Test
  public void testColumnHidable() {
    int nColumns = grid.getVisibleColumns().size();
    $server.setColumnToggleVisible(true);
    $server.setHidable(0, true);

    grid.getColumnToggleButton().click();
    assertThat(grid.getColumnToggleElements(), hasSize(nColumns));

    $server.setHidable(0, false);
    grid.getColumnToggleButton().click();
    assertThat(grid.getColumnToggleElements(), hasSize(nColumns - 1));
  }

  @Test
  public void testHidingToggleCaption() {
    String caption = "caption";
    $server.setHidingToggleCaption(0, caption);
    $server.setColumnToggleVisible(true);
    grid.getColumnToggleButton().click();
    assertEquals(caption, grid.getColumnToggleElements().get(0).getLabel());
  }

  @Test
  public void testCustomHidingToggleCaption() {
    String caption = "HidingToggleCaption";
    $server.setHidingToggleCaption(0, caption);
    $server.setColumnToggleVisible(true);
    grid.getColumnToggleButton().click();
    assertEquals(caption, grid.getColumnToggleElements().get(0).getLabel());
  }

}
