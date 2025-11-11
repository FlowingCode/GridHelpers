/*-
 * #%L
 * Grid Helpers Add-on
 * %%
 * Copyright (C) 2022 - 2025 Flowing Code
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

import com.flowingcode.vaadin.jsonmigration.JsonMigration;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.dom.DebouncePhase;
import com.vaadin.flow.shared.Registration;
import elemental.json.Json;
import elemental.json.JsonArray;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.NavigableMap;
import java.util.TreeMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("serial")
@RequiredArgsConstructor
class ResponsiveGridHelper<T> implements Serializable {

  private final GridHelper<T> helper;

  private boolean initialized;

  private NavigableMap<Integer, GridResponsiveStep<T>> steps = new TreeMap<>();

  @Getter
  private int currentMinWidth = -1;

  private Registration refreshRegistration;

  Grid<T> getGrid() {
    return helper.getGrid();
  }

  GridResponsiveStep<T> getOrCreate(int minWidth) {
    Grid<T> grid = helper.getGrid();

    if (minWidth < 0) {
      throw new IllegalArgumentException();
    }

    steps.computeIfAbsent(minWidth, w -> new GridResponsiveStep<>(w, this));

    if (initialized) {
      sendSteps();
    } else {
      initialized = true;
      grid.addAttachListener(ev -> initialize());
      if (grid.isAttached()) {
        initialize();
      }
    }

    return steps.get(minWidth);
  }

  private void initialize() {
    Grid<T> grid = helper.getGrid();
    grid.getElement().addEventListener("fcgh-responsive-step", ev -> {
      apply((int) JsonMigration.getEventData(ev).getNumber("event.detail.step"), false);
    }).addEventData("event.detail.step").debounce(200, DebouncePhase.TRAILING);
    sendSteps();
  }

  private void sendSteps() {
    JsonArray widths = Json.createArray();
    steps.keySet().forEach(w -> widths.set(widths.length(), w));
    helper.getGrid().getElement().executeJs("this.fcGridHelper._setResponsiveSteps($0)", widths);
  }

  private void refresh() {
    apply(currentMinWidth, true);
  }

  void requireRefresh(GridResponsiveStep<T> step) {
    if (step.getMinWidth() <= currentMinWidth) {
      Grid<T> grid = helper.getGrid();
      grid.getUI().ifPresent(ui -> {
        if (refreshRegistration != null) {
          refreshRegistration.remove();
        }
        refreshRegistration = ui.beforeClientResponse(grid, context -> refresh());
      });
    }
  }

  private void apply(int width, boolean force) {
    if (steps.floorKey(width) != null) {
      Grid<T> grid = helper.getGrid();
      GridResponsiveStep<T> step = new GridResponsiveStep<T>().show(grid.getColumns());
      steps.subMap(0, true, width, true).values().forEach(step::accumulate);
      if (step.getMinWidth() >= 0) {
        if (force || currentMinWidth != step.getMinWidth()) {
          currentMinWidth = step.getMinWidth();
          step.apply(helper.getGrid());
        }
      }
    } else {
      currentMinWidth = -1;
    }
  }

  void remove(GridResponsiveStep<?> step) {
    if (steps.remove(step.getMinWidth(), step)) {
      sendSteps();
    } else {
      throw new IllegalArgumentException("The responsive step is not connected to this grid");
    }
  }

  Collection<GridResponsiveStep<T>> getAll() {
    return Collections.unmodifiableCollection(steps.values());
  }

}

