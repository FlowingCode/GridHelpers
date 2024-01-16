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
import com.flowingcode.vaadin.addons.gridhelpers.LazyTestData.PersonFilter;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.function.Function;

@PageTitle("Empty Grid Label")
@DemoSource
@Route(value = "grid-helpers/empty-grid-label", layout = GridHelpersDemoView.class)
public class EmptyGridLabelDemo extends VerticalLayout {

  public EmptyGridLabelDemo() {
    LazyTestData data = new LazyTestData();

    setSpacing(false);
    setPadding(false);
    setSizeFull();

    TextField firstName = new TextField("Firstname");
    TextField lastName = new TextField("Lastname");

    Function<Query<Person, PersonFilter>, PersonFilter> filterBuilder = query -> {
      PersonFilter filter = query.getFilter().orElse(new PersonFilter());
      filter.setFirstName(firstName.getValue());
      filter.setLastName(lastName.getValue());
      return filter;
    };

    DataProvider<Person, PersonFilter> dataProvider = DataProvider.fromFilteringCallbacks(
        query -> data.filter(query.getOffset(), query.getPageSize(), filterBuilder.apply(query)),
        query -> data.count(filterBuilder.apply(query)));

    Grid<Person> grid = new Grid<>();
    grid.setDataProvider(dataProvider);
    grid.addColumn(Person::getFirstName).setHeader("First name");
    grid.addColumn(Person::getLastName).setHeader("Last name");
    grid.addColumn(Person::getCountry).setHeader("Country");
    grid.setSizeFull();

    Div emptyGridLabel = new Div(new Text("No records found"));
    emptyGridLabel.setSizeFull();
    emptyGridLabel.addClassName("empty-grid-label");
    emptyGridLabel.setVisible(false);
    GridHelper.setEmptyGridLabel(grid, emptyGridLabel);

    Button filterButton = new Button("Filter");
    filterButton.addClickShortcut(Key.ENTER);
    filterButton.addClickListener(e -> dataProvider.refreshAll());

    Div gridContainer = new Div(grid, emptyGridLabel);
    gridContainer.getStyle().set("position", "relative");
    gridContainer.setSizeFull();

    FormLayout filtersForm = new FormLayout(firstName, lastName);
    filtersForm.setResponsiveSteps(new ResponsiveStep("0", 2));
    filtersForm.setWidthFull();

    HorizontalLayout filters = new HorizontalLayout(filtersForm, filterButton);
    filters.setAlignItems(Alignment.END);
    filters.setWidthFull();
    filters.getStyle().set("padding-right", "4px");

    add(filters, gridContainer);
  }
}
