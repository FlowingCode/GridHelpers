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

package com.flowingcode.vaadin.addons.gridhelpers.it;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.flowingcode.vaadin.testbench.rpc.HasRpcSupport;
import com.vaadin.flow.component.grid.testbench.GridElement;
import org.junit.Test;
import org.openqa.selenium.By;

public class EmptyLabelIT extends AbstractViewTest implements HasRpcSupport {

  IntegrationViewCallables $server = createCallableProxy(IntegrationViewCallables.class);
  GridHelperElement grid;

  public EmptyLabelIT() {
	super("it");
  }

  @Override
  public void setup() throws Exception {
	super.setup();
	grid = new GridHelperElement($(GridElement.class).waitForFirst());
  }

  private boolean findByText(String text) {
    return findElements(By.xpath(String.format("//*[text()='%s']", text))).stream().findFirst()
        .isPresent();
  }

  @Test
  public void testEmptyGridLabel() {
    String label = "EmptyGridLabel";
    $server.setEmptyGridLabel(label);

    // assert that label is not shown
    assertFalse("Label must not be visible", findByText(label));

    $server.removeAllItems();

    // assert that label is shown
    assertTrue("Label must be visible", findByText(label));
  }

  @Test
  public void replaceEmptyGridLabel() {
    String label1 = "EmptyGridLabel1";
    String label2 = "EmptyGridLabel2";
    $server.setEmptyGridLabel(label1);
    $server.setEmptyGridLabel(label2);
    $server.removeAllItems();

    // assert that only the second label is shown
    assertFalse("Label 1 must not be visible", findByText(label1));
    assertTrue("Label 2 must be visible", findByText(label2));

  }


}
