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

import static com.vaadin.flow.component.grid.Grid.SelectionMode.MULTI;
import static com.vaadin.flow.component.grid.Grid.SelectionMode.SINGLE;
import com.flowingcode.vaadin.addons.GithubLink;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.experimental.ExtensionMethod;

@SuppressWarnings("serial")
@PageTitle("All Features")
@GithubLink("https://github.com/FlowingCode/GridHelpers")
@StyleSheet("context://gridhelpers/styles.css")
@ExtensionMethod(GridHelper.class)
public class AllFeaturesDemo extends Div {

  // This demo is just for presentation purposes.
  // For examples on the normal API use of each feature, please see the other demos.
  public AllFeaturesDemo() {
    setSizeFull();

    getElement().getStyle().set("flex-grow", "1");

    Grid<Person> grid = new Grid<>();

    grid.addColumn(Person::getLastName).setHeader("Last name").setHidingToggleCaption("Last name column");
    grid.addColumn(Person::getFirstName)
        .setHeader("First name")
        .setHidingToggleCaption("First name column");
    grid.addColumn(p -> p.isActive() ? "Yes" : "No")
        .setHeader("Active")
        .setHidable(true);
    grid.addColumn(Person::getTitle).setHeader("Title").setHidable(true);
    grid.addColumn(Person::getCountry).setHeader("Country").setHidable(true);
    grid.addColumn(Person::getCity).setHeader("City").setHidable(true);
    grid.addColumn(Person::getStreetAddress)
        .setHeader("Street Address")
        .setHidable(true);
    grid.addColumn(Person::getPhoneNumber)
        .setHeader("Phone Number")
        .setHidable(true);
    grid.addColumn(Person::getEmailAddress)
        .setHeader("Email Address")
        .setHidable(true);
    grid.getColumns().forEach(c -> c.setAutoWidth(true));

    grid.setItems(TestData.initializeData());
    grid.setSelectionMode(SelectionMode.MULTI);

    grid.getElement().getStyle().set("flex-grow", "1");

    VerticalLayout features = new VerticalLayout();
    features.getStyle().set("margin-left", "4px");
    features.getStyle().set("margin-right", "-4px");
    features.getStyle().set("min-width", "310px");
    features.setPadding(false);
    features.setWidth(null);

    VerticalLayout layout = new VerticalLayout(grid);
    layout.setPadding(false);
    SplitLayout split = new SplitLayout(layout, features);
    split.setSplitterPosition(100);
    split.setHeightFull();
    add(split);

    grid.setSelectOnClick(true);
    grid.setColumnToggleVisible(true);

    GridHelper.addColumnToggleListener(grid, ev -> {
      String caption = GridHelper.getHidingToggleCaption(ev.getColumn());
      String message = caption + " is now " + (ev.getColumn().isVisible() ? "visible" : "invisible");
      new Notification(message).open();
    });

    Binder<Grid<Person>> binder = new Binder<>();
    binder.setBean(grid);
    Select<SelectionMode> select = new Select<>(SelectionMode.values());
    select.setLabel("Selection mode");
    binder.forField(select).bind(GridHelper::getSelectionMode, this::setSelectionMode);

    binder
        .forField(newCheckbox("Hide selection column", MULTI))
        .bind(GridHelper::isSelectionColumnHidden, GridHelper::setSelectionColumnHidden);
    binder
        .forField(newCheckbox("Freeze selection column", MULTI))
        .bind(GridHelper::isSelectionColumnFrozen, GridHelper::setSelectionColumnFrozen);
    binder
        .forField(newCheckbox("Arrow selection enabled", SINGLE, MULTI))
        .bind(GridHelper::isArrowSelectionEnabled, GridHelper::setArrowSelectionEnabled);
    binder
        .forField(newCheckbox("Enable selection by clicking row", MULTI))
        .bind(GridHelper::isSelectOnClick, GridHelper::setSelectOnClick);
    binder
        .forField(newCheckbox("Disallow selection of 'inactive' records", SINGLE, MULTI))
        .bind(this::hasSelectionFilter, this::setSelectionFilter);
    binder.forField(new Checkbox("Dense Theme")).bind(this::hasDenseTheme, this::setDenseTheme);

    binder.getFields().map(Component.class::cast).forEach(features::add);
    Label label = new Label("Features");
    label.addClassNames("label");

    features.addComponentAtIndex(1, label);
    setSelectionMode(grid, grid.getSelectionMode());

    HorizontalLayout hl = new HorizontalLayout();
    hl.setPadding(false);
    Div toolbarFooterLabel = new Div(new Text("Toolbar Footer"));
    toolbarFooterLabel.getStyle().set("margin", "auto");
    hl.setSizeFull();
    Button toolbarFooterButton = new Button(VaadinIcon.TOOLS.create(),
        ev -> Notification.show("You clicked a button in the Toolbar Footer"));
    toolbarFooterButton.getThemeNames().add("small");
    hl.add(toolbarFooterLabel, toolbarFooterButton);
    hl.setJustifyContentMode(JustifyContentMode.END);

    grid.addToolbarFooter(hl);
  }

  private final Map<Checkbox, List<SelectionMode>> checkboxes = new HashMap<>();

  private Checkbox newCheckbox(String labelText, SelectionMode... modes) {
    Checkbox checkbox = new Checkbox(labelText);
    if (modes.length > 0) {
      checkboxes.put(checkbox, Arrays.asList(modes));
    }
    return checkbox;
  }

  private void setSelectionMode(Grid<Person> grid, SelectionMode selectionMode) {
    grid.setSelectionMode(selectionMode);
    grid.setSelectionFilter(grid.getSelectionFilter());

    checkboxes.forEach((checkbox,modes)->{
      if (modes.contains(selectionMode)) {
        checkbox.getElement().removeAttribute("title");
      } else if (modes.size()==1) {
        checkbox.getElement().setAttribute("title",
            String.format("This feature only has effect in %s selection mode",
                modes.get(0).toString().toLowerCase()));
      } else if (modes.size()==2) {
        checkbox.getElement().setAttribute("title",
            String.format("This feature only has effect in %s and %s selection modes",
                modes.get(0).toString().toLowerCase(), modes.get(1).toString().toLowerCase()));
      }
    });

    // workaround for https://github.com/FlowingCode/GridHelpers/issues/11 in Vaadin 22
    if (selectionMode == MULTI) {
      grid.getStyle().remove("--fcgh-noselect-opacity");
    } else {
      grid.getStyle().set("--fcgh-noselect-opacity", "1");
    }

  }

  private void setSelectionFilter(Grid<Person> grid, boolean value) {
    // https://github.com/projectlombok/lombok/issues/3153
    // grid.setSelectionFilter(value?Person::isActive:null);

    if (value) {
      grid.setSelectionFilter(Person::isActive);
    } else {
      grid.setSelectionFilter(null);
    }
  }

  private boolean hasSelectionFilter(Grid<Person> grid) {
    return grid.getSelectionFilter() != null;
  }

  private void setDenseTheme(Grid<Person> grid, boolean value) {
    if (value) {
      grid.addThemeName(GridHelper.DENSE_THEME);
    } else {
      grid.removeThemeName(GridHelper.DENSE_THEME);
    }
  }

  private boolean hasDenseTheme(Grid<Person> grid) {
    return grid.hasThemeName(GridHelper.DENSE_THEME);
  }
}
