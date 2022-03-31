package com.flowingcode.vaadin.addons.gridhelpers;

import com.flowingcode.vaadin.addons.demo.DemoSource;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;

@PageTitle("Enable Arrow Selection")
@DemoSource(
    "https://github.com/FlowingCode/GridHelpers/blob/master/src/test/java/com/flowingcode/vaadin/addons/gridhelpers/EnableArrowSelectionDemo.java")
public class EnableArrowSelectionDemo extends Div {
  
  public EnableArrowSelectionDemo() {
    setSizeFull();
    
    Grid<Person> grid = new Grid<>();
    grid.setItems(TestData.initializeData());
    
    grid.setSelectionMode(SelectionMode.SINGLE);

    grid.addColumn(Person::getFirstName).setHeader("First name");
    grid.addColumn(Person::getLastName).setHeader("Last name");
    grid.addColumn(p -> p.isActive() ? "Yes" : "No").setHeader("Active");
    
    GridHelper.setArrowSelectionEnabled(grid, true);    
    
    add(grid);    
  }

}
