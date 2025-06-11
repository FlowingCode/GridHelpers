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

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.FooterRow.FooterCell;
import com.vaadin.flow.component.grid.Grid;
import java.io.Serializable;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("serial")
@RequiredArgsConstructor
class FooterToolbarGridHelper implements Serializable {

  private final GridHelper<?> helper;

  private FooterCell footerCell;

  public void setFooterToolbar(Component toolBar) {
    Objects.requireNonNull(toolBar, "Toolbar component must not be null");
    Grid<?> grid = helper.getGrid();
    if (grid.getColumns().isEmpty()) {
      throw new IllegalStateException("Cannot set footer toolbar: Grid columns have not been configured.");
    }
    if (grid.getFooterRows().isEmpty()) {
      // create a fake footer and hide it (workaround:
      // https://github.com/vaadin/flow-components/issues/1558#issuecomment-987783794)
      grid.appendFooterRow();
      grid.getElement().getThemeList().add("hide-first-footer");
    }
    if (footerCell == null) {
      FooterRow fr = grid.appendFooterRow();
      if(grid.getColumns().size() > 1) {
        footerCell = fr.join(grid.getColumns().toArray(new Grid.Column[0]));
      } else {
        footerCell = fr.getCell(grid.getColumns().get(0));
      }
    }
    toolBar.getElement().setAttribute("fcGh-footer", true);
    footerCell.setComponent(toolBar);
  }

}
