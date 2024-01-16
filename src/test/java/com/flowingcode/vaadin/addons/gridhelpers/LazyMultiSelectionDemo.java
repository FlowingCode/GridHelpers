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

import com.flowingcode.vaadin.addons.demo.DemoSource;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Lazy Multi Selection")
@DemoSource
@Route(value = "grid-helpers/lazy-multi-selection", layout = GridHelpersDemoView.class)
public class LazyMultiSelectionDemo extends VerticalLayout {

  public LazyMultiSelectionDemo() {
    setSizeFull();

    Grid<Person> grid = new Grid<>();
    LazyTestData data = new LazyTestData();
    grid.setItems(DataProvider.fromCallbacks(
        query -> data.filter(query.getOffset(), query.getPageSize()), query -> data.count()));

    grid.setSelectionMode(SelectionMode.MULTI);

    grid.addColumn(Person::getFirstName).setHeader("First name");
    grid.addColumn(Person::getLastName).setHeader("Last name");
    grid.addColumn(Person::getCountry).setHeader("Country");

    grid.setSizeFull();

    Checkbox toggleSelectAllCheckbox = new Checkbox("Show Select All checkbox");
    toggleSelectAllCheckbox.setValue(false);
    toggleSelectAllCheckbox
        .addValueChangeListener(e -> GridHelper.toggleSelectAllCheckbox(grid, e.getValue()));

    add(grid, toggleSelectAllCheckbox);
    setFlexGrow(1, grid);
  }
}
