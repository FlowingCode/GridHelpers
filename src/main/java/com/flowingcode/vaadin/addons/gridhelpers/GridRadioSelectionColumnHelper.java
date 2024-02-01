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

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import java.io.Serializable;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("serial")
@RequiredArgsConstructor
class GridRadioSelectionColumnHelper<T> implements Serializable {

  private final GridHelper<T> helper;

  private GridRadioSelectionColumn selectionColumn;

  public GridRadioSelectionColumn showRadioSelectionColumn() {
    remove();

    Grid<T> grid = helper.getGrid();
    grid.setSelectionMode(SelectionMode.SINGLE);

    selectionColumn = new GridRadioSelectionColumn();
    selectionColumn.setClassName("fc-grid-radio-selection-column");

    if (grid.getElement().getNode().isAttached()) {
      grid.getElement().insertChild(0, selectionColumn.getElement());
    } else {
      grid.getElement().getNode().runWhenAttached(ui -> {
        grid.getElement().insertChild(0, selectionColumn.getElement());
      });
    }

    return selectionColumn;
  }

  private void remove() {
    if (selectionColumn != null && selectionColumn.getElement().getNode().isAttached()) {
      helper.getGrid().getElement().removeChild(selectionColumn.getElement());
    }
  }

}
