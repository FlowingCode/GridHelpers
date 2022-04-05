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
 * This file incorporates work licensed under the Apache License, Version 2.0
 * from Vaadin Cookbook https://github.com/vaadin/cookbook
 *  Copyright 2020-2022 Vaadin Ltd.
 */

package com.flowingcode.vaadin.addons.gridhelpers;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import java.io.Serializable;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("serial")
@RequiredArgsConstructor
class ColumnToggleHelper implements Serializable {

  private static final String GRID_HELPER_TOGGLE_THEME = "gridHelperToggle";

  private static final String TOGGLE_LABEL_DATA = GridHelper.class.getName() + "#TOGGLE_LABEL";

  private final GridHelper<?> helper;

  private Column<?> menuToggleColumn;

  public void setColumnToggleVisible(boolean visible) {
    // https://cookbook.vaadin.com/grid-column-toggle
    if (visible) {
      showColumnToggle();
    } else {
      hideColumnToggle();
    }
  }

  public boolean isColumnToggleVisible() {
    return menuToggleColumn != null && menuToggleColumn.isVisible();
  }

  private void showColumnToggle() {
    createMenuToggle()
        .ifPresent(
            toggle -> {
              Grid<?> grid = helper.getGrid();
              if (menuToggleColumn == null) {
                menuToggleColumn = grid.addColumn(t -> "").setWidth("auto").setFlexGrow(0);
              } else {
                menuToggleColumn.setVisible(true);
              }
              grid.getHeaderRows().get(0).getCell(menuToggleColumn).setComponent(toggle);
            });
  }

  private void hideColumnToggle() {
    if (menuToggleColumn != null) {
      menuToggleColumn.setVisible(false);
    }
  }

  private Optional<MenuBar> createMenuToggle() {
    Grid<?> grid = helper.getGrid();

    MenuBar menuBar = new MenuBar();
    menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
    MenuItem menuItem = menuBar.addItem(VaadinIcon.ELLIPSIS_DOTS_V.create());
    SubMenu subMenu = menuItem.getSubMenu();

    for (Column<?> column : grid.getColumns()) {
      getToggleLabel(column)
          .ifPresent(
              label -> {
                Checkbox checkbox = new Checkbox(label);
                checkbox.setValue(column.isVisible());
                checkbox.addValueChangeListener(e -> column.setVisible(e.getValue()));
                subMenu.addItem(checkbox);
              });
    }

    menuBar.getThemeNames().add(GRID_HELPER_TOGGLE_THEME);
    return Optional.of(menuBar).filter(_menuBar -> !_menuBar.getItems().isEmpty());
  }

  private Optional<String> getToggleLabel(@NonNull Column<?> column) {
    return Optional.ofNullable((String) ComponentUtil.getData(column, TOGGLE_LABEL_DATA));
  }

  public void setHidingToggleCaption(Column<?> column, String caption) {
    if (column != null) {
      Grid<?> grid = helper.getGrid();
      if (!grid.getColumns().contains(column)) {
        throw new IllegalArgumentException();
      }
      ComponentUtil.setData(column, TOGGLE_LABEL_DATA, caption);
      if (isColumnToggleVisible()) {
        showColumnToggle();
      }
    }
  }

  Column<?> getMenuToggleColumn() {
    return menuToggleColumn;
  }
}
