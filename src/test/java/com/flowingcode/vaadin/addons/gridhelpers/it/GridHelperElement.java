/*-
 * #%L
 * Grid Helpers Add-on
 * %%
 * Copyright (C) 2022 - 2026 Flowing Code
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

import com.flowingcode.vaadin.testbench.rpc.Version;
import com.vaadin.flow.component.checkbox.testbench.CheckboxElement;
import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.menubar.testbench.MenuBarElement;
import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.ElementQuery.AttributeMatch.Comparison;
import com.vaadin.testbench.TestBenchElement;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.NonNull;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

public class GridHelperElement extends MyGridElement {

  private Version version;

  private GridHelperElement(GridHelperElement e, Version version) {
    init(e.getWrappedElement(), e.getCommandExecutor());
    this.version = version;
  }

  public GridHelperElement(GridElement e) {
    init(e.getWrappedElement(), e.getCommandExecutor());
    version = null;
  }

  public GridHelperElement withVersion(@NonNull Version version) {
    return new GridHelperElement(this, version);
  }

  @Override
  public Object executeScript(String script, Object... arguments) {
    script = String.format(
        "return function(arguments){arguments.pop(); %s}.bind(arguments[arguments.length-1])([].slice.call(arguments))",
        script);
    arguments = Arrays.copyOf(arguments, arguments.length + 1);
    arguments[arguments.length - 1] = this;
    return getCommandExecutor().executeScript(script, arguments);
  }

  private MenuBarElement getMenuBar() {
    return $(MenuBarElement.class).waitForFirst(0);
  }

  public TestBenchElement getColumnToggleButton() {
    try {
      return getMenuBar().getButtons().get(0);
    } catch (TimeoutException e) {
      return null;
    }
  }

  public List<CheckboxElement> getColumnToggleElements() {
    try {
      ElementQuery<TestBenchElement> query;
      if (version == null || version.getMajorVersion() < 25) {
        query = new ElementQuery<>(TestBenchElement.class,
            "vaadin-context-menu-overlay, vaadin-menu-bar-overlay");
      } else {
        query = new ElementQuery<>(TestBenchElement.class, "vaadin-menu-bar")
            .withAttribute("theme", "gridHelperToggle", Comparison.CONTAINS_WORD);
      }
      return query
          .context(getDriver())
          .waitForFirst(100)
          .$(CheckboxElement.class).all();
    } catch (NoSuchElementException e) {
      return Collections.emptyList();
    }
  }

  public TestBenchElement getToolbarFooter() {
    List<WebElement> elements = findElements(By.cssSelector("[fcgh-footer]"));
    return (TestBenchElement) elements.stream().findFirst().orElse(null);
  }

  public TestBenchElement getSelectionCheckbox(int rowIndex) {
    // assumes that Grid is in multi-selection mode
    TestBenchElement cell = getSlottedCell(getRow(rowIndex));
    List<WebElement> elements = cell.findElements(By.tagName("vaadin-checkbox"));
    return (TestBenchElement) elements.stream().findFirst().orElse(null);
  }

  public TestBenchElement getSelectionRadioButton(int rowIndex) {
    // assumes that Grid is in single-selection mode
    TestBenchElement cell = getSlottedCell(getRow(rowIndex));
    List<WebElement> elements = cell.findElements(By.tagName("vaadin-radio-button"));
    return (TestBenchElement) elements.stream().findFirst().orElse(null);
  }


  private TestBenchElement getSlottedCell(WebElement e) {
    String slot = e.findElement(By.tagName("slot")).getAttribute("name");
    return findElement(By.cssSelector(String.format("[slot='%s']", slot)));
  }

  public boolean hasColumn(String headerText) {
    return getVisibleColumns().stream()
        .filter(column -> headerText.equals(column.getHeaderCell().getText())).findFirst()
        .isPresent();
  }

  @Override
  public int getFirstVisibleRowIndex() {
    Long result = (Long) executeScript("return this._firstVisibleIndex");
    return Optional.ofNullable(result).orElse(0L).intValue();
  }

  @Override
  public int getLastVisibleRowIndex() {
    Long result = (Long) executeScript("return this._lastVisibleIndex");
    return Optional.ofNullable(result).orElse(-1L).intValue();
  }

  public Object getVisibleRowsCount() {
    return getLastVisibleRowIndex() - getFirstVisibleRowIndex() + 1;
  }

  public int getOffsetHeight() {
    return ((Long) executeScript("return this.offsetHeight")).intValue();
  }

  public String getHeightByRowsSize() {
    return (String) executeScript("return this.style.getPropertyValue('--height-by-rows')");
  }

  public WebElement getHeaderRow(int rowIndex) {
    WebElement thead = $("*").id("header");
    List<WebElement> headerRows = thead.findElements(By.tagName("tr"));
    return headerRows.get(rowIndex);
  }

  public WebElement getHeaderCellAt(int rowIndex, int columnIndex) {
    List<WebElement> headerCells = getHeaderRow(rowIndex).findElements(By.tagName("th"));
    return headerCells.get(columnIndex);
  }
}
