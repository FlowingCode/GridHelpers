package com.flowingcode.vaadin.addons.gridhelpers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import com.flowingcode.vaadin.addons.gridhelpers.GridHelper;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.function.SerializablePredicate;
import lombok.experimental.ExtensionMethod;
import org.junit.Before;
import org.junit.Test;

@ExtensionMethod(GridHelper.class)
public class GridHelperTest {

  private static final String HEADER = "HEADER";

  private Grid<Bean> grid;

  private Column<Bean> col0;

  private Column<Bean> col1;

  private class Bean {}

  @Before
  public void before() {
    grid = new Grid<>();
    col0 = grid.addColumn(x -> x).setHeader(HEADER);
    col1 = grid.addColumn(x -> x);
  }

  @Test
  public void testSelectionColumnFrozen() {
    assertTrue(!grid.isSelectionColumnFrozen());

    grid.setSelectionColumnFrozen(true);
    assertTrue(grid.isSelectionColumnFrozen());

    grid.setSelectionColumnFrozen(false);
    assertTrue(!grid.isSelectionColumnFrozen());
  }

  @Test
  public void testSelectionFilter() {
    SerializablePredicate<Bean> predicate = t->true;
    assertNull(grid.getSelectionFilter());

    grid.setSelectionFilter(predicate);
    assertEquals(predicate, grid.getSelectionFilter());

    grid.setSelectionFilter(null);
    assertNull(grid.getSelectionFilter());
  }

  @Test
  public void testHidingToggleCaption() {
    String caption = "caption";
    assertEquals(HEADER, col0.getHidingToggleCaption());

    col0.setHidingToggleCaption(caption);
    assertEquals(caption, col0.getHidingToggleCaption());

    col0.setHidingToggleCaption(null);
    assertEquals(HEADER, col0.getHidingToggleCaption());
  }

  @Test
  public void testSelectionMode() {
    for (SelectionMode selectionMode : SelectionMode.values()) {
      grid.setSelectionMode(selectionMode);
      assertEquals(selectionMode, grid.getSelectionMode());
    }
  }

  @Test
  public void testEmptyGridLabel() {
    Component label = new Span("Empty");
    assertNull(grid.getEmptyGridLabel());

    grid.setEmptyGridLabel(label);
    assertEquals(label, grid.getEmptyGridLabel());

    grid.setEmptyGridLabel(null);
    assertNull(grid.getEmptyGridLabel());
  }

  @Test
  public void testHeader() {
    assertEquals(HEADER, grid.getHeader(col0));
    assertEquals("", grid.getHeader(col1));
  }

  @Test
  public void testFooter() {
    String footer = "footer";
    assertNull(grid.getFooter(col0));

    col0.setFooter(footer);
    assertEquals(footer, grid.getFooter(col0));
  }

  @Test
  public void testHidable() {
    assertFalse(col0.isHidable());

    col0.setHidable(true);
    assertTrue(col0.isHidable());

    col0.setHidable(false);
    assertFalse(col0.isHidable());
  }

  @Test
  public void testSelectionColumnHidden() {
    assertFalse(grid.isSelectionColumnHidden());

    grid.setSelectionColumnHidden(true);
    assertTrue(grid.isSelectionColumnHidden());

    grid.setSelectionColumnHidden(false);
    assertFalse(grid.isSelectionColumnHidden());
  }

  @Test
  public void testColumnToggleVisible() {
    assertFalse(grid.isColumnToggleVisible());

    grid.setColumnToggleVisible(true);
    assertTrue(grid.isColumnToggleVisible());

    grid.setColumnToggleVisible(false);
    assertFalse(grid.isColumnToggleVisible());
  }

  @Test
  public void testArrowSelectionEnabled() {
    assertFalse(grid.isArrowSelectionEnabled());

    grid.setArrowSelectionEnabled(true);
    assertTrue(grid.isArrowSelectionEnabled());

    grid.setArrowSelectionEnabled(false);
    assertFalse(grid.isArrowSelectionEnabled());
  }

  @Test
  public void testSelectOnClick() {
    assertFalse(grid.isSelectOnClick());

    grid.setSelectOnClick(true);
    assertTrue(grid.isSelectOnClick());

    grid.setSelectOnClick(false);
    assertFalse(grid.isSelectOnClick());
  }

  @Test
  public void testMenuToggleColumn() {
    grid.setColumnToggleVisible(true);

    Column<Bean> toggleColumn=grid.getColumns().get(grid.getColumns().size()-1);
    assertTrue(GridHelper.isMenuToggleColumn(toggleColumn));
  }

}
