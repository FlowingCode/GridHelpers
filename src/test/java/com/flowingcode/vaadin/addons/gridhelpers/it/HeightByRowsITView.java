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

import com.flowingcode.vaadin.addons.gridhelpers.GridHelper;
import com.flowingcode.vaadin.addons.gridhelpers.HeightMode;
import com.flowingcode.vaadin.jsonmigration.InstrumentedRoute;
import com.flowingcode.vaadin.jsonmigration.LegacyClientCallable;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(GridHelper.class)
@InstrumentedRoute(HeightByRowsITView.ROUTE)
public class HeightByRowsITView extends VerticalLayout
    implements HeightByRowsITViewCallables, HasUrlParameter<String> {

  public static final String ROUTE = "it/height-by-rows";

  public static final String EMPTY = "empty";

  public static final String FULLSIZE = "fullsize";

  public static final String NO_COLUMNS = "nocolumns";

  private Grid<Integer> grid;

  @Override
  public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
    removeAll();

    if (parameter == null) {
      return;
    }

    List<String> params = Arrays.asList(parameter.split(";"));

    if (params.contains(FULLSIZE)) {
      setSizeFull();
    }

    grid = new Grid<>();

    if (!params.contains(NO_COLUMNS)) {
      grid.addColumn(x -> x).setHeader("Header").setFooter("Footer");
    }

    if (!params.contains(EMPTY)) {
      grid.setItems(IntStream.range(1, 100).mapToObj(Integer::valueOf).toArray(Integer[]::new));
    } else {
      grid.getElement().getStyle().set("min-height", "0");
    }

    params.stream()
      .filter(s -> s.matches("\\d+"))
      .findFirst().map(Integer::valueOf)
      .ifPresent(rows -> {
        grid.setHeightMode(HeightMode.ROW);
          grid.setHeightByRows(rows);
        });

    add(grid);
  }

  @Override
  @LegacyClientCallable
  public JsonValue $call(JsonObject invocation) {
    return HeightByRowsITViewCallables.super.$call(invocation);
  }

  @Override
  public HeightMode getHeightMode() {
    return grid.getHeightMode();
  }

  @Override
  public void setHeightMode(HeightMode row) {
    grid.setHeightMode(row);
  }

  @Override
  public double getHeightByRows() {
    return grid.getHeightByRows();
  }

  @Override
  public void setHeightByRows(int rows) {
    grid.setHeightByRows(rows);
  }

  @Override
  public void roundtrip() {
    // do nothing
  }
}
