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

package com.flowingcode.vaadin.addons.gridhelpers;

import com.flowingcode.vaadin.addons.DemoLayout;
import com.flowingcode.vaadin.addons.GithubLink;
import com.flowingcode.vaadin.addons.demo.TabbedDemo;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.Route;

@SuppressWarnings("serial")
@ParentLayout(DemoLayout.class)
@Route("grid-helpers")
@GithubLink("https://github.com/FlowingCode/GridHelpers")
@StyleSheet("context://gridhelpers/styles.css")
public class GridHelpersDemoView extends TabbedDemo {

  public GridHelpersDemoView() {
    setSizeFull();
    addDemo(AllFeaturesDemo.class);
    addDemo(ColumnToggleMenuDemo.class);
    addDemo(HeightByRowsDemo.class);
    addDemo(HideSelectionColumnDemo.class);
    addDemo(FreezeSelectionColumnDemo.class);
    addDemo(EnableArrowSelectionDemo.class);
    addDemo(EnableSelectionOnClickDemo.class);
    addDemo(EnableEnhancedSelectionDemo.class);
    addDemo(SelectionFilterDemo.class);
    addDemo(DenseThemeDemo.class);
    addDemo(LombokDemo.class);
    addDemo(AddToolbarFooterDemo.class);
    addDemo(GetHeaderFooterDemo.class);
    addDemo(LazyMultiSelectionDemo.class);
    addDemo(CheckboxColumnDemo.class);
    addDemo(EmptyGridLabelDemo.class);
    addDemo(GridRadioSelectionColumnDemo.class);
  }
}
