package com.flowingcode.vaadin.addons.gridhelpers;

import com.flowingcode.vaadin.addons.demo.DemoSource;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Get Header or Footer")
@DemoSource
@Route(value = "grid-helpers/get-header-or-footer", layout = GridHelpersDemoView.class)
public class GetHeaderFooterDemo extends Div {

  public GetHeaderFooterDemo() {
    setSizeFull();

    Grid<Person> grid = new Grid<>();
    grid.setItems(TestData.initializeData());

    Column<Person> firstName = grid.addColumn(Person::getFirstName).setHeader("First name").setFooter("First name footer");
    Column<Person> lastName = grid.addColumn(Person::getLastName).setHeader("Last name").setFooter("Last name footer");;
    Column<Person> country = grid.addColumn(Person::getCountry).setHeader("Country").setFooter("Country footer");;

    HorizontalLayout hl = new HorizontalLayout(new Button(VaadinIcon.TOOLS.create(),ev->Notification.show("Not implemented")));
    hl.setSizeFull();
    hl.setJustifyContentMode(JustifyContentMode.END);

    Button getHeaders = new Button("Obtain headers", ev->Notification.show(
        "Header text column 1: " + GridHelper.getHeader(grid,firstName) + 
        ", Header text column 2: " + GridHelper.getHeader(grid,lastName) +
        ", Header text column 3: " + GridHelper.getHeader(grid,country)));
    Button getFooters = new Button("Obtain footers", ev->Notification.show(
        "Footer text column 1: " + GridHelper.getFooter(grid,firstName) + 
        ", Footer text column 2: " + GridHelper.getFooter(grid,lastName) +
        ", Footer text column 3: " + GridHelper.getFooter(grid,country)));

    grid.setHeightFull();
    add(grid, getHeaders, getFooters);
  }
}
