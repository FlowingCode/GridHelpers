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
