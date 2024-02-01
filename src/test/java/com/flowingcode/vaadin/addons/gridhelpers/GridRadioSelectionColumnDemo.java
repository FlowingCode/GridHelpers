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
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSingleSelectionModel;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Radio Selection Column")
@DemoSource
@Route(value = "grid-helpers/radio-selection-column", layout = GridHelpersDemoView.class)
public class GridRadioSelectionColumnDemo extends VerticalLayout {

  Div selection = new Div();

  public GridRadioSelectionColumnDemo() {
    setSizeFull();

    Grid<Person> grid = new Grid<>();
    LazyTestData data = new LazyTestData();
    grid.setItems(DataProvider.fromCallbacks(
        query -> data.filter(query.getOffset(), query.getPageSize()), query -> data.count()));

    grid.addColumn(Person::getFirstName).setHeader("First name");
    grid.addColumn(Person::getLastName).setHeader("Last name");
    grid.addColumn(Person::getPhoneNumber).setHeader("Phone");

    GridHelper.showRadioSelectionColumn(grid).setFrozen(true);

    grid.setSizeFull();
    ((GridSingleSelectionModel<Person>) grid.getSelectionModel()).setDeselectAllowed(false);
    grid.asSingleSelect().addValueChangeListener(e -> {
      if (e.getValue() != null) {
        selection.setText(
            "Selected row: " + e.getValue().getFirstName() + " " + e.getValue().getLastName());
      }
    });

    add(grid, selection);
  }

}
