package com.flowingcode.vaadin.addons.gridhelpers;

import com.flowingcode.vaadin.addons.demo.DemoSource;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;

@PageTitle("Freeze Selection Column")
@DemoSource(
    "https://github.com/FlowingCode/GridHelpers/blob/master/src/test/java/com/flowingcode/vaadin/addons/gridhelpers/FreezeSelectionColumnDemo.java")
public class FreezeSelectionColumnDemo extends Div {

  public FreezeSelectionColumnDemo() {
    setSizeFull();

    Grid<Person> grid = new Grid<>();
    grid.setItems(TestData.initializeData());

    grid.setSelectionMode(SelectionMode.MULTI);

    grid.addColumn(Person::getFirstName).setHeader("First name");
    grid.addColumn(Person::getLastName).setHeader("Last name");
    grid.addColumn(p -> p.isActive() ? "Yes" : "No").setHeader("Active");
    grid.addColumn(Person::getTitle).setHeader("Title");
    grid.addColumn(Person::getCountry).setHeader("Country");
    grid.addColumn(Person::getCity).setHeader("City");
    grid.addColumn(Person::getStreetAddress).setHeader("Street Address");
    grid.addColumn(Person::getPhoneNumber).setHeader("Phone Number");
    grid.getColumns().forEach(c -> c.setAutoWidth(true));

    GridHelper.setSelectionColumnFrozen(grid, true);

    add(grid);
  }
}
