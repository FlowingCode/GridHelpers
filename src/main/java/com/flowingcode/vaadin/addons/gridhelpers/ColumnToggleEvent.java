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
