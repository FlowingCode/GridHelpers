/*-
 * #%L
 * Grid Helpers Add-on
 * %%
 * Copyright (C) 2022 Flowing Code
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
import com.vaadin.flow.router.Route;

@SuppressWarnings("serial")
@Route(value = "grid-helpers", layout = DemoLayout.class)
@GithubLink("https://github.com/FlowingCode/GridHelpers")
@StyleSheet("context://gridhelpers/styles.css")
public class GridHelpersDemoView extends TabbedDemo {

  public GridHelpersDemoView() {
    setSizeFull();
    addDemo(new AllFeaturesDemo());
    addDemo(new ColumnToggleMenuDemo());
    addDemo(new HideSelectionColumnDemo());
    addDemo(new FreezeSelectionColumnDemo());
    addDemo(new EnableArrowSelectionDemo());
    addDemo(new EnableSelectionOnClickDemo());
    addDemo(new SelectionFilterDemo());
    addDemo(new DenseThemeDemo());
    addDemo(new LombokDemo());
    addDemo(new AddToolbarFooterDemo());
    addDemo(new GetHeaderFooterDemo());
  }
}
