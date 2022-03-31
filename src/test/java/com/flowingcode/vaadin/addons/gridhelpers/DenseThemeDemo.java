package com.flowingcode.vaadin.addons.gridhelpers;

import com.flowingcode.vaadin.addons.demo.DemoSource;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;

@PageTitle("Dense Theme")
@DemoSource(
    "https://github.com/FlowingCode/GridHelpers/blob/master/src/test/java/com/flowingcode/vaadin/addons/gridhelpers/DenseThemeDemo.java")
public class DenseThemeDemo extends Div {
  
  public DenseThemeDemo() {
    setSizeFull();
    
    Grid<Person> grid = new Grid<>();
    grid.setItems(TestData.initializeData());
    
    grid.setSelectionMode(SelectionMode.SINGLE);

    grid.addColumn(Person::getFirstName).setHeader("First name");
    grid.addColumn(Person::getLastName).setHeader("Last name");
    grid.addColumn(p -> p.isActive() ? "Yes" : "No").setHeader("Active");
    
    grid.addThemeName(GridHelper.DENSE_THEME);   
    
    add(grid);    
  }

}
