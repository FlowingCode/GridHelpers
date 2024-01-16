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

import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import java.io.Serializable;
import lombok.RequiredArgsConstructor;

/**
 * By default Vaadin hides select all checkbox when grid's data is NOT in-memory (lazy).
 * <p>
 * This grid helper allows showing or hiding select all checkbox no matter if data provider is or
 * not memory.
 * <p>
 * Note that enabling select all checkbox when grid uses a lazy data provider could lead to memory
 * and performance issues, given that all the items must be eagerly loaded at the moment user clicks
 * the checkbox.
 * 
 * @param <T> the type of items in grid
 */
@RequiredArgsConstructor
final class LazySelectAllGridHelper<T> implements Serializable {

  private static final long serialVersionUID = 1L;

  private final GridHelper<T> helper;

  /**
   * Toggles select all checkbox visibility.
   * 
   * @param visible true to show the select all checkbox, false to hide it.
   */
  void toggleSelectAllCheckbox(boolean visible) {
    if (SelectionMode.MULTI.equals(GridHelper.getSelectionMode(helper.getGrid()))) {
      GridMultiSelectionModel<T> model =
          (GridMultiSelectionModel<T>) helper.getGrid().getSelectionModel();
      if (visible) {
        model.setSelectAllCheckboxVisibility(
            GridMultiSelectionModel.SelectAllCheckboxVisibility.VISIBLE);
      } else {
        model.setSelectAllCheckboxVisibility(
            GridMultiSelectionModel.SelectAllCheckboxVisibility.HIDDEN);
      }
    }
  }



}
