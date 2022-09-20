package com.flowingcode.vaadin.addons.gridhelpers;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;

/**
 * Event describing a change in column visibility through the toggle menu.
 *
 * @see GridHelper#addColumnToggleListener(Grid, com.vaadin.flow.component.ComponentEventListener)
 *
 * @param <T> the grid bean type
 */
@SuppressWarnings("serial")
public class ColumnToggleEvent<T> extends ComponentEvent<Grid<T>> {

  private Column<T> column;

  public ColumnToggleEvent(Grid<T> source, Column<T> column, boolean fromClient) {
    super(source, fromClient);
    this.column = column;
  }

  /** Returns the column whose visibility was toggled. */
  public Column<T> getColumn() {
    return column;
  }
}
