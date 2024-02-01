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

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import com.flowingcode.vaadin.testbench.rpc.HasRpcSupport;
import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.testbench.TestBenchElement;
import java.time.Duration;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GridRadioSelectionColumnIT extends AbstractViewTest implements HasRpcSupport {

  IntegrationViewCallables $server = createCallableProxy(IntegrationViewCallables.class);
  GridHelperElement grid;

  public GridRadioSelectionColumnIT() {
    super("it");
  }

  @Override
  public void setup() throws Exception {
    super.setup();
    grid = new GridHelperElement($(GridElement.class).waitForFirst());
  }

  @Test
  public void testRadioSelectionColumnShown() {
    // assert that the first column is not the one with radio button
    assertNull("the first column has radio buttons", grid.getSelectionRadioButton(0));

    $server.showRadioSelectionColumn();

    waitUntil(ExpectedConditions
        .presenceOfElementLocated(By.tagName("grid-flow-radio-selection-column")));

    // assert that the first column is the one with radio buttons
    assertNotNull("the first column has not any radio buttons", grid.getSelectionRadioButton(0));
  }

  @Test
  public void testShouldSelectRowOnClick() {
    $server.showRadioSelectionColumn();

    waitUntil(ExpectedConditions
        .presenceOfElementLocated(By.tagName("grid-flow-radio-selection-column")));

    assertThat($server.getSelectedRows(), empty());

    grid.getCell(0, 1).click();
    assertThat($server.getSelectedRows(), contains(0));

    grid.getCell(4, 1).click();
    assertThat($server.getSelectedRows(), contains(4));
  }

  @Test
  public void testShouldSelectRowOnRadioButtonClick() {
    $server.showRadioSelectionColumn();

    waitUntil(ExpectedConditions
        .presenceOfElementLocated(By.tagName("grid-flow-radio-selection-column")));


    assertThat($server.getSelectedRows(), empty());

    TestBenchElement radioButton = grid.getSelectionRadioButton(0);
    radioButton.click();
    assertThat($server.getSelectedRows(), contains(0));


    radioButton = grid.getSelectionRadioButton(4);
    radioButton.click();
    assertThat($server.getSelectedRows(), contains(4));
  }

  @Test
  public void testRadioSelectionColumnFrozen() {
    $server.showRadioSelectionColumn();

    waitUntil(ExpectedConditions
        .presenceOfElementLocated(By.tagName("grid-flow-radio-selection-column")));

    TestBenchElement selectionColElement = grid.$("grid-flow-radio-selection-column").first();

    String frozen = "return arguments[0].frozen";


    new WebDriverWait(getDriver(), Duration.of(2, SECONDS))
        .until(ExpectedConditions.attributeContains(selectionColElement, "frozen", "false"));

    // assert that the selection column is not frozen by default
    assertFalse("Selection column is frozen", (Boolean) executeScript(frozen, selectionColElement));

    // make selection column frozen
    selectionColElement.setProperty("frozen", true);

    new WebDriverWait(getDriver(), Duration.of(2, SECONDS))
        .until(ExpectedConditions.attributeContains(selectionColElement, "frozen", "true"));

    // assert that the selection column is now frozen
    assertTrue("Selection column is not frozen",
        (Boolean) executeScript(frozen, selectionColElement));
  }

}
