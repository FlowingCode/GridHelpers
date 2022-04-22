/*-
 * #%L
 * Grid Helpers Add-on
 * %%
 * Copyright (C) 2022 Flowing Code
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

/*
 * This file incorporates work licensed under the Apache License, Version 2.0 from Selection Grid
 * https://vaadin.com/directory/component/selection-grid Copyright 2020 Vaadin Ltd.
 */
package com.flowingcode.vaadin.addons.gridhelpers;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.GridSelectionModel;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.data.provider.DataCommunicator;
import elemental.json.JsonObject;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.Setter;
import lombok.var;

@SuppressWarnings("serial")
class RangeSelectionHelper<T> implements Serializable {

  private static final String LAST_CLICKED_ITEM = "lastClickedItem";
  private final GridHelper<T> helper;

  @Getter
  @Setter
  private boolean enabled;

  public RangeSelectionHelper(GridHelper<T> helper, Grid<T> grid) {
    this.helper = helper;
    grid.getElement().addEventListener("fc-selectRange", e -> {
      JsonObject data = e.getEventData().getObject("event.detail");
      selectRange((int) data.getNumber("fromIndex"), (int) data.getNumber("toIndex"));
    }).addEventData("event.detail");

    grid.getElement().addEventListener("fc-selectRangeOnly", e -> {
      JsonObject data = e.getEventData().getObject("event.detail");
      selectRangeOnly((int) data.getNumber("fromIndex"), (int) data.getNumber("toIndex"));
    }).addEventData("event.detail");
  }

  boolean onItemClick(ItemClickEvent<T> ev) {
    Grid<T> grid = helper.getGrid();

    if (enabled && GridHelper.getSelectionMode(grid) == SelectionMode.MULTI) {
      T clickedItem = ev.getItem();

      if (!helper.canSelect(clickedItem)) {
        return true;
      }

      if (!ev.isCtrlKey() && !ev.isShiftKey() && grid.asMultiSelect().isSelected(clickedItem)) {
        // allows selecting text on selected items
        grid.getElement().executeJs("return !document.getSelection().toString().length")
            .then(Boolean.class, empty -> {
              if (empty) {
                boolean wasSelected = grid.asMultiSelect().isSelected(clickedItem);
                grid.asMultiSelect().clear();
                if (!wasSelected) {
                  ComponentUtil.setData(grid, LAST_CLICKED_ITEM, clickedItem);
                  grid.select(clickedItem);
                }
              }
            });
        return true;
      }

      grid.getElement().executeJs("document.getSelection().removeAllRanges()");

      if (!ev.isCtrlKey()) {
        grid.asMultiSelect().clear();
      }

      if (ev.isShiftKey()) {
        @SuppressWarnings("unchecked")
        T lastClickedItem = (T) ComponentUtil.getData(grid, LAST_CLICKED_ITEM);
        if (lastClickedItem != null) {
          var dataView = grid.getListDataView();
          int index1 = dataView.getItems().collect(Collectors.toList()).indexOf(lastClickedItem);
          int index2 = dataView.getItems().collect(Collectors.toList()).indexOf(clickedItem);

          IntStream.rangeClosed(Math.min(index1, index2), Math.max(index1, index2))
              .mapToObj(dataView::getItem).filter(helper::canSelect).forEach(grid::select);
          return true;
        }
      }

      if (ev.isShiftKey() || !grid.asMultiSelect().isSelected(clickedItem)) {
        ComponentUtil.setData(grid, LAST_CLICKED_ITEM, clickedItem);
        grid.select(clickedItem);
      } else {
        ComponentUtil.setData(grid, LAST_CLICKED_ITEM, null);
        grid.deselect(clickedItem);
      }

      return true;
    }

    return false;
  }

  @SuppressWarnings("unchecked")
  protected Stream<T> fetchFromProvider(int offset, int limit) {
    Grid<T> grid = helper.getGrid();
    DataCommunicator<T> dataCommunicator = grid.getDataCommunicator();
    Method fetchFromProvider;
    try {
      fetchFromProvider =
          DataCommunicator.class.getDeclaredMethod("fetchFromProvider", int.class, int.class);
      fetchFromProvider.setAccessible(true);
      return ((Stream<T>) fetchFromProvider.invoke(dataCommunicator, offset, limit)).limit(limit);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Select the range and keep the other items selected
   *
   * @param fromIndex
   * @param toIndex
   */
  private void selectRange(int fromIndex, int toIndex) {
    Grid<T> grid = helper.getGrid();
    GridSelectionModel<T> model = grid.getSelectionModel();
    if (model instanceof GridMultiSelectionModel) {
      int from = Math.min(fromIndex, toIndex);
      int to = Math.max(fromIndex, toIndex);
      grid.asMultiSelect()
          .select(fetchFromProvider(from, to - from + 1).collect(Collectors.toList()));
    }
  }

  /**
   * Select the range and deselect the other items
   *
   * @param fromIndex
   * @param toIndex
   */
  private void selectRangeOnly(int fromIndex, int toIndex) {
    Grid<T> grid = helper.getGrid();
    GridSelectionModel<T> model = grid.getSelectionModel();
    if (model instanceof GridMultiSelectionModel) {
      int from = Math.min(fromIndex, toIndex);
      int to = Math.max(fromIndex, toIndex);
      Set<T> newSelectedItems = fetchFromProvider(from, to - from + 1).collect(Collectors.toSet());
      HashSet<T> oldSelectedItems = new HashSet<>(grid.getSelectedItems());
      oldSelectedItems.removeAll(newSelectedItems);
      grid.asMultiSelect().updateSelection(newSelectedItems, oldSelectedItems);
    }
  }

}
