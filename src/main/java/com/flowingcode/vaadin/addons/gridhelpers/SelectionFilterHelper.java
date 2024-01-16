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

/*
 * This file incorporates work licensed under the Apache License, Version 2.0
 * from Vaadin Cookbook https://github.com/vaadin/cookbook
 *  Copyright 2020-2022 Vaadin Ltd.
 */

package com.flowingcode.vaadin.addons.gridhelpers;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.shared.Registration;
import java.io.Serializable;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("serial")
@RequiredArgsConstructor
class SelectionFilterHelper<T> implements Serializable {

  private final GridHelper<T> helper;

  private Registration selectionListenerRegistration;

  @Getter private SerializablePredicate<T> selectionFilter;

  // holds latest selected item or null
  private T currentSingleSelectedItem;

  public void setSelectionFilter(SerializablePredicate<T> predicate) {
    Optional.ofNullable(selectionListenerRegistration).ifPresent(Registration::remove);
    selectionListenerRegistration = null;

    Grid<T> grid = helper.getGrid();
    this.selectionFilter = predicate;
    currentSingleSelectedItem = null;
    if (predicate != null) {
      deselectIf(predicate.negate());
      helper.setHelperClassNameGenerator(
          this.getClass(), row -> predicate.test(row) ? null : "fcGh-noselect");
      selectionListenerRegistration = grid.addSelectionListener(this::onSelection);
    } else {
      helper.setHelperClassNameGenerator(this.getClass(), null);
    }
  }

  boolean canSelect(T item) {
    return selectionFilter == null || selectionFilter.test(item);
  }

  private void deselectIf(SerializablePredicate<T> predicate) {
    Grid<T> grid = helper.getGrid();
    switch (GridHelper.getSelectionMode(grid)) {
      case MULTI:
        grid.asMultiSelect()
            .deselect(
                grid.asMultiSelect().getSelectedItems().stream()
                    .filter(predicate)
                    .collect(Collectors.toList()));
        break;
      case SINGLE:
        grid.asSingleSelect()
            .getOptionalValue()
            .filter(predicate)
            .ifPresent(x -> grid.asSingleSelect().clear());
        break;
      default:
        break;
    }
  }

  private void onSelection(SelectionEvent<Grid<T>, T> event) {
    SelectionMode currentSelectionMode = GridHelper.getSelectionMode(event.getSource());
    if (event.isFromClient()) {
      if (SelectionMode.SINGLE.equals(currentSelectionMode)) {
        event.getFirstSelectedItem().ifPresent(item -> {
          if (canSelect(item)) {
            currentSingleSelectedItem = item;
          } else {
            // restore previous selected item
            if (currentSingleSelectedItem != null && canSelect(currentSingleSelectedItem)) {
              event.getSource().select(currentSingleSelectedItem);
            } else {
              event.getSource().deselect(item);
              currentSingleSelectedItem = null;
            }
          }
        });
      } else if (SelectionMode.MULTI.equals(currentSelectionMode)) {
        event.getAllSelectedItems().forEach(item -> {
          // Revert selection if item cannot be selected
          if (!canSelect(item)) {
            event.getSource().deselect(item);
          }
        });
      }
    }
  }

}
