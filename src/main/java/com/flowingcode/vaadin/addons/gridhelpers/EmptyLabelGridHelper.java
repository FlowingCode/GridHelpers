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

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.DataProviderListener;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.shared.Registration;
import java.io.Serializable;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("serial")
@RequiredArgsConstructor
class EmptyLabelGridHelper implements Serializable {

  private final GridHelper<?> helper;

  private Component emptyLabel;
  private Registration registration;

  // - Show a meaningful message instead of an empty Grid
  // https://cookbook.vaadin.com/grid-message-when-empty
  void setEmptyGridLabel(Component component) {
    Grid<?> grid = helper.getGrid();

    emptyLabel = component;

    if (registration != null) {
      registration.remove();
      registration = null;
    }

    if (component != null) {
      DataProviderListener listener =
          ev -> component.setVisible(grid.getDataProvider().size(new Query<>()) == 0);
      registration = grid.getDataProvider().addDataProviderListener(listener);

      // Initial run of the listener, as there is no event fired for the initial state
      // of the data set that might be empty or not.
      listener.onDataChange(null);
    }
  }

  Component getEmptyGridLabel() {
    return emptyLabel;
  }
}
