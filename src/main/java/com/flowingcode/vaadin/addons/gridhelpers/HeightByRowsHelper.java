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
import java.io.Serializable;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("serial")
@RequiredArgsConstructor
class HeightByRowsHelper implements Serializable {

  private static final String THEME = "height-by-rows";

  private final GridHelper<?> helper;

  @Getter
  private double heightByRows;

  private HeightMode heightMode;

  void setHeightByRows(double rows) {
    if (rows <= 0.0d) {
      throw new IllegalArgumentException("More than zero rows must be shown.");
    } else if (Double.isInfinite(rows)) {
      throw new IllegalArgumentException("Grid doesn't support infinite heights");
    } else if (Double.isNaN(rows)) {
      throw new IllegalArgumentException("NaN is not a valid row count");
    }

    heightByRows = rows;

    Grid<?> grid = helper.getGrid();
    if (heightMode == HeightMode.ROW && grid.isAttached()) {
      grid.getElement().executeJs("this.fcGridHelper.setHeightByRows($0)", rows);
      grid.setThemeName(THEME, heightMode == HeightMode.ROW);
    }
  }

  void setHeightMode(HeightMode heightMode) {
    if (this.heightMode == null) {
      helper.getGrid().addAttachListener(ev -> {
        if (heightMode == HeightMode.ROW) {
          setHeightByRows(heightByRows);
        }
      });
    }

    this.heightMode = heightMode;
    if (heightMode == HeightMode.ROW && heightByRows != 0) {
      setHeightByRows(heightByRows);
      helper.getGrid().addThemeName(THEME);
    } else {
      helper.getGrid().removeThemeName(THEME);
    }
  }

  HeightMode getHeightMode() {
    return Optional.ofNullable(heightMode).orElse(HeightMode.CSS);
  }

}
