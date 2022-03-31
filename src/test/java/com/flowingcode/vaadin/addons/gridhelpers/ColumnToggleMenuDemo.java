package com.flowingcode.vaadin.addons.gridhelpers;

import com.flowingcode.vaadin.addons.demo.DemoSource;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;

@PageTitle("Column Toggle Menu")
@DemoSource(
    "https://github.com/FlowingCode/GridHelpers/blob/master/src/test/java/com/flowingcode/vaadin/addons/gridhelpers/ColumnToggleMenuDemo.java")
public class ColumnToggleMenuDemo extends Div {
  
  public ColumnToggleMenuDemo() {
    setSizeFull();
    
    Grid<Person> grid = new Grid<>();   
    grid.setItems(TestData.initializeData());
    
    Column<Person> firstNameColumn = grid.addColumn(Person::getFirstName).setHeader("First name");
    Column<Person> lastNameColumn = grid.addColumn(Person::getLastName).setHeader("Last name");
    Column<Person> activeNameColumn = grid.addColumn(p -> p.isActive() ? "Yes" : "No").setHeader("Active");
    
    GridHelper.setHidingToggleCaption(firstNameColumn, "First name");
    GridHelper.setHidingToggleCaption(lastNameColumn, "Last name");
    GridHelper.setHidingToggleCaption(activeNameColumn, "Active");
        
    GridHelper.setColumnToggleVisible(grid, isVisible());
    
    add(grid);    
  }

}
