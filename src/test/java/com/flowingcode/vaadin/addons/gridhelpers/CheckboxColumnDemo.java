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
import com.flowingcode.vaadin.addons.gridhelpers.CheckboxColumn.CheckboxColumnConfiguration;
import com.flowingcode.vaadin.addons.gridhelpers.CheckboxColumn.CheckboxPosition;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Checkbox Column")
@DemoSource
@Route(value = "grid-helpers/checkbox-column", layout = GridHelpersDemoView.class)
public class CheckboxColumnDemo extends VerticalLayout {

  public CheckboxColumnDemo() {
    setSizeFull();

    Span activeCount = new Span();
    Span vipCount = new Span();

    Grid<Person> grid = new Grid<>();
    LazyTestData data = new LazyTestData();
    grid.setItems(DataProvider.fromCallbacks(
        query -> data.filter(query.getOffset(), query.getPageSize()), query -> data.count()));

    grid.addColumn(Person::getFirstName).setHeader("First name");
    grid.addColumn(Person::getLastName).setHeader("Last name");
    CheckboxColumn<Person> activeColumn = GridHelper
        .addCheckboxColumn(grid,
            new CheckboxColumnConfiguration<>(Person::isActive)
                .header("Active")
                .checkboxPosition(CheckboxPosition.LEFT)
                .selectAllCheckboxVisible(true))
        .checkboxClickListener((item,
            checked) -> Notification.show(item.getFirstName() + " " + item.getLastName()
                + " " + (checked ? "checked" : "unchecked")))
        .checkedCountListener(count -> activeCount.setText("Active count: " + count));
    GridHelper
        .addCheckboxColumn(grid,
            new CheckboxColumnConfiguration<>(Person::isVip)
                .header("VIP"))
        .checkboxClickListener((item, checked) -> Notification
            .show(item.getFirstName() + " " + item.getLastName()
                + " " + (checked ? "checked" : "unchecked")))
        .checkedCountListener(count -> vipCount.setText("VIP count: " + count));
    GridHelper
        .addCheckboxColumn(grid,
            new CheckboxColumnConfiguration<>(Person::isHidden)
                .header("Hidden")
                .readOnly(true));

    grid.setSizeFull();

    Button toggleVipColumn = new Button("Refresh grid data");
    toggleVipColumn.addClickListener(e -> {
      LazyTestData newData = new LazyTestData();
      grid.setItems(DataProvider.fromCallbacks(
          query -> newData.filter(query.getOffset(), query.getPageSize()),
          query -> newData.count()));
      activeColumn.refresh();
    });

    add(grid, toggleVipColumn, new HorizontalLayout(activeCount, vipCount));
  }
}
