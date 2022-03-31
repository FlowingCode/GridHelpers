package com.flowingcode.vaadin.addons.gridhelpers;

import com.flowingcode.vaadin.addons.demo.DemoSource;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;

@PageTitle("Hide Selection Column")
@DemoSource(
    "https://github.com/FlowingCode/GridHelpers/blob/master/src/test/java/com/flowingcode/vaadin/addons/gridhelpers/HideSelectionColumnDemo.java")
public class HideSelectionColumnDemo extends Div {
  
  public HideSelectionColumnDemo() {
    setSizeFull();
    
    Grid<Person> grid = new Grid<>();
    grid.setItems(TestData.initializeData());
    
    grid.setSelectionMode(SelectionMode.MULTI);

    grid.addColumn(Person::getFirstName).setHeader("First name");
    grid.addColumn(Person::getLastName).setHeader("Last name");
    grid.addColumn(p -> p.isActive() ? "Yes" : "No").setHeader("Active");
    
    GridHelper.setSelectionColumnHidden(grid, true);    
    
    add(grid);    
  }

}
