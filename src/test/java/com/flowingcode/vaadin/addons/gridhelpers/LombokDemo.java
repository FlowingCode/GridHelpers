package com.flowingcode.vaadin.addons.gridhelpers;

import com.flowingcode.vaadin.addons.demo.DemoSource;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import lombok.experimental.ExtensionMethod;

@PageTitle("Using Lombok")
@DemoSource(
    "https://github.com/FlowingCode/GridHelpers/blob/master/src/test/java/com/flowingcode/vaadin/addons/gridhelpers/LombokDemo.java")
@ExtensionMethod(GridHelper.class)
public class LombokDemo extends Div {

  public LombokDemo() {
    setSizeFull();

    Grid<Person> grid = new Grid<>();
    grid.setItems(TestData.initializeData());

    grid.setSelectionMode(SelectionMode.MULTI);

    grid.addColumn(Person::getFirstName)
        .setHeader("First name")
        .setHidingToggleCaption("First name");
    grid.addColumn(Person::getLastName).setHeader("Last name").setHidingToggleCaption("Last name");
    grid.addColumn(p -> p.isActive() ? "Yes" : "No").setHeader("Active");
    grid.addColumn(Person::getTitle).setHeader("Title");
    grid.addColumn(Person::getCountry).setHeader("Country");
    grid.addColumn(Person::getCity).setHeader("City");
    grid.addColumn(Person::getStreetAddress).setHeader("Street Address");
    grid.getColumns().forEach(c -> c.setAutoWidth(true));

    grid.setColumnToggleVisible(true);
    grid.setSelectionColumnFrozen(true);
    grid.setSelectOnClick(true);

    add(grid);
  }
}
