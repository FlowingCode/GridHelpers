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

import java.io.Serializable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("serial")
@RequiredArgsConstructor
class SelectionColumnHelper implements Serializable {

  private final GridHelper<?> helper;

  @Getter private boolean selectionColumnHidden;

  public void setSelectionColumnHidden(boolean value) {
    // https://cookbook.vaadin.com/grid-multiselect-no-selectcolumn
    selectionColumnHidden = value;
    helper
        .getGrid()
        .getElement()
        .executeJs(
            "this.getElementsByTagName('vaadin-grid-flow-selection-column')[0].hidden = $0;",
            value);
  } 

  void onAttach() {
    if (isSelectionColumnHidden()) {
      setSelectionColumnHidden(true);
    }
  }
}
