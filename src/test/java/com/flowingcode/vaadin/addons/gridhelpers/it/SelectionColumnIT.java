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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import com.flowingcode.vaadin.testbench.rpc.HasRpcSupport;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.testbench.GridElement;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class SelectionColumnIT extends AbstractViewTest implements HasRpcSupport {

  IntegrationViewCallables $server = createCallableProxy(IntegrationViewCallables.class);
  GridHelperElement grid;

  public SelectionColumnIT() {
	super("it");
  }

  @Override
  public void setup() throws Exception {
	super.setup();
	grid = new GridHelperElement($(GridElement.class).waitForFirst());
  }

  @Test
  public void testSelectionColumnHidden() {
    $server.setSelectionMode(SelectionMode.MULTI);
    // assert that the first column is the one with checkboxes
    assertNotNull("the first column does not have checkboxes", grid.getSelectionCheckbox(0));

    // assert that the first column is not the one with checkboxes
    $server.setSelectionColumnHidden(true);
    assertNull("the first column has checkboxes", grid.getSelectionCheckbox(0));
  }

  @Test
  public void testSelectionColumnFrozen() {
    $server.setSelectionMode(SelectionMode.MULTI);  
    
    String frozen = "return arguments[0].frozen";
    
    // find selection column
    WebElement idColumn = grid.findElements(By.tagName("vaadin-grid-flow-selection-column")).get(0);
    
    // assert that the selection column is not frozen by default
    assertEquals("Selection column is frozen", false, (Boolean) executeScript(frozen, idColumn));
    
    // make selection column frozen
    $server.setSelectionColumnFrozen(true);

    // assert that the selection column is now frozen 
    assertEquals("Selection column is not frozen", true, (Boolean) executeScript(frozen, idColumn));
  }

}
