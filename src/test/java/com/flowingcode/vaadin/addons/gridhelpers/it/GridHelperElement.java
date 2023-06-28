package com.flowingcode.vaadin.addons.gridhelpers.it;

import com.vaadin.flow.component.checkbox.testbench.CheckboxElement;
import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.menubar.testbench.MenuBarElement;
import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.TestBenchElement;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

public class GridHelperElement extends MyGridElement {

  public GridHelperElement(GridElement e) {
    init(e.getWrappedElement(), e.getCommandExecutor());
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
      return new ElementQuery<>(TestBenchElement.class,
          "vaadin-context-menu-overlay, vaadin-menu-bar-overlay")
          .context(getDriver())
          .waitForFirst(0)
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

  private TestBenchElement getSlottedCell(WebElement e) {
    String slot = e.findElement(By.tagName("slot")).getAttribute("name");
    return findElement(By.cssSelector(String.format("[slot='%s']", slot)));
  }

  public boolean hasColumn(String headerText) {
    return getVisibleColumns().stream()
        .filter(column -> headerText.equals(column.getHeaderCell().getText())).findFirst()
        .isPresent();
  }

}
