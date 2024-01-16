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

package com.flowingcode.vaadin.addons.gridhelpers.it;

import static org.junit.Assert.assertEquals;
import com.flowingcode.vaadin.testbench.rpc.HasRpcSupport;
import com.vaadin.flow.component.grid.testbench.GridElement;
import org.junit.Test;

public class FooterToolbarIT extends AbstractViewTest implements HasRpcSupport {

  IntegrationViewCallables $server = createCallableProxy(IntegrationViewCallables.class);
  GridHelperElement grid;

  public FooterToolbarIT() {
	super("it");
  }

  @Override
  public void setup() throws Exception {
	super.setup();
	grid = new GridHelperElement($(GridElement.class).waitForFirst());
  }

  @Test
  public void testToolbarFooter() {
    String footer = "footer";
    $server.addToolbarFooter(footer);
    assertEquals(footer, grid.getToolbarFooter().getText());
  }

}
