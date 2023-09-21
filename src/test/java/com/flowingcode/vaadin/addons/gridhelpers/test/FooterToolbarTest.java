package com.flowingcode.vaadin.addons.gridhelpers.test;

import com.flowingcode.vaadin.addons.gridhelpers.GridHelper;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.junit.Before;
import org.junit.Test;
import org.junit.Test.None;

public class FooterToolbarTest {
  
  private Grid<Bean> grid;

  private HorizontalLayout toolbarFooter;
  
  private class Bean {}
  
  @Before
  public void before() {
    grid = new Grid<>(Bean.class, false);
    grid.addColumn(x -> x).setHeader("Header");
    toolbarFooter = new HorizontalLayout();    
  }
  
  @Test(expected = None.class)
  public void addToolbarFooter_toolbarFooterIsShown() {
    GridHelper.addToolbarFooter(grid, toolbarFooter);
  }

}
