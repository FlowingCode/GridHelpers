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
package com.flowingcode.vaadin.addons.gridhelpers.it;

import com.flowingcode.vaadin.addons.gridhelpers.GridHelper;
import com.flowingcode.vaadin.addons.gridhelpers.GridStylesHelper;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.HeaderRow.HeaderCell;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import lombok.experimental.ExtensionMethod;

@SuppressWarnings("serial")
@Route(HeaderFooterStylesView.ROUTE)
@ExtensionMethod(GridHelper.class)
public class HeaderFooterStylesView extends Div implements HeaderFooterStylesCallables {

  public static final String ROUTE = "it/styles";

  private Grid<Integer> grid;

  public HeaderFooterStylesView() {
    setSizeFull();
    getElement().getStyle().set("flex-grow", "1");
    grid = new Grid<>();
    for (int i = 0; i < 5; i++) {
      grid.addColumn(x -> x).setHeader("col " + i);
    }
    grid.prependHeaderRow();
    add(grid);
  }

  @Override
  @ClientCallable
  public JsonValue $call(JsonObject invocation) {
    return HeaderFooterStylesCallables.super.$call(invocation);
  }


  @RequiredArgsConstructor
  private abstract class StylesWrapper {
    @Delegate
    private final GridStylesHelper styles;
  }

  private final class HeaderCellWrapperImpl extends StylesWrapper implements HeaderCellWrapper {
    public HeaderCellWrapperImpl(GridStylesHelper styles) {
      super(styles);
    }
  }

  private final class HeaderRowWrapperImpl extends StylesWrapper implements HeaderRowWrapper {
    private final HeaderRow row;

    public HeaderRowWrapperImpl(HeaderRow row) {
      super(GridHelper.getHeaderStyles(grid, row));
      this.row = row;
    }

    @Override
    public HeaderCellWrapper getCell(int columnIndex) {
      HeaderCell cell = row.getCell(grid.getColumns().get(columnIndex));
      return new HeaderCellWrapperImpl(grid.getHeaderStyles(cell));
    }

    @Override
    public HeaderCellWrapper join(int... columnIndexes) {
      HeaderCell cell = row.join(IntStream.of(columnIndexes)
        .mapToObj(grid.getColumns()::get)
        .toArray(Column[]::new));
      cell.setText("join " + IntStream.of(columnIndexes)
        .mapToObj(Integer::toString)
        .collect(Collectors.joining()));
      return new HeaderCellWrapperImpl(GridHelper.getHeaderStyles(grid, cell));
    }

  }

  @Override
  public HeaderRowWrapper getRow(int rowIndex) {
    HeaderRow row = grid.getHeaderRows().get(rowIndex);
    return new HeaderRowWrapperImpl(row);
  }

}
