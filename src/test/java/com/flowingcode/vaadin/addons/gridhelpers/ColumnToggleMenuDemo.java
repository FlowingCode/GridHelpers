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

package com.flowingcode.vaadin.addons.gridhelpers;

import com.flowingcode.vaadin.addons.demo.DemoSource;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;

@PageTitle("Column Toggle Menu")
@DemoSource
public class ColumnToggleMenuDemo extends Div {

  public ColumnToggleMenuDemo() {
    setSizeFull();

    Grid<Person> grid = new Grid<>();
    grid.setItems(TestData.initializeData());

    Column<Person> firstNameColumn = grid.addColumn(Person::getFirstName).setHeader("First name");
    Column<Person> lastNameColumn = grid.addColumn(Person::getLastName).setHeader("Last name");
    Column<Person> countryColumn = grid.addColumn(Person::getCountry).setHeader("Country");

    GridHelper.setHidingToggleCaption(firstNameColumn, "First name");
    GridHelper.setHidingToggleCaption(lastNameColumn, "Last name");
    GridHelper.setHidingToggleCaption(countryColumn, "Country");

    GridHelper.setColumnToggleVisible(grid, isVisible());

    grid.setHeightFull();
    add(grid);
  }
}
