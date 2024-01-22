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
import com.vaadin.flow.router.Route;

@PageTitle("Responsive Step")
@DemoSource
@Route(value = "grid-helpers/respoonsive-step", layout = GridHelpersDemoView.class)
public class ResponsiveStepDemo extends Div {

  public ResponsiveStepDemo() {
    setSizeFull();

    Grid<Person> grid = new Grid<>();
    grid.setSizeFull();
    grid.setItems(TestData.initializeData());

    Column<Person> firstNameColumn = grid.addColumn(Person::getFirstName).setHeader("First name");
    Column<Person> lastNameColumn = grid.addColumn(Person::getLastName).setHeader("Last name");
    Column<Person> countryColumn = grid.addColumn(Person::getCountry).setHeader("Country");
    grid.getStyle().set("visibility", "hidden");

    GridHelper.responsiveStep(grid, 0).hideAll()
        .addListener(e -> grid.getStyle().set("visibility", "hidden"));

    GridHelper.responsiveStep(grid, 850).show(firstNameColumn, lastNameColumn).hide(countryColumn)
        .addListener(e -> grid.getStyle().set("visibility", "visible"));

    GridHelper.responsiveStep(grid, 1000).show(firstNameColumn, lastNameColumn, countryColumn)
        .addListener(e -> grid.getStyle().set("visibility", "visible"));

    add(grid);
  }
}
