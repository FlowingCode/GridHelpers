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
package com.flowingcode.vaadin.addons.gridhelpers.it;

import com.flowingcode.vaadin.testbench.rpc.JsonArrayList;
import com.flowingcode.vaadin.testbench.rpc.RmiCallable;
import com.vaadin.flow.component.grid.Grid.SelectionMode;

public interface IntegrationViewCallables extends RmiCallable {

    void setColumnToggleVisible(boolean value);

    void setSelectOnClick(boolean value);

    void setSelectionMode(SelectionMode selectionMode);

    JsonArrayList<Integer> getSelectedRows();

    Integer getToggledColumn();

    void setHidable(int columnIndex, boolean hidable);

    void setSelectionFilterEnabled(boolean enabled);

    void setColumnHeader(int i, String header);

    void setHidingToggleCaption(int i, String header);

    void setEmptyGridLabel(String label);

    void setSelectionColumnHidden(boolean b);

    void setSelectionColumnFrozen(boolean b);

    void setArrowSelectionEnabled(boolean b);

    void addToolbarFooter(String footer);

    void removeAllItems();

    void setHeaderVisible(boolean visible);

    void setFooterVisible(boolean visible);

}
