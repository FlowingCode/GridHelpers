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

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;
import com.flowingcode.vaadin.testbench.rpc.HasRpcSupport;
import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.testbench.TestBenchElement;
import java.time.Duration;
import org.junit.Test;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HeaderFooterVisibilityIT extends AbstractViewTest implements HasRpcSupport {

  IntegrationViewCallables $server = createCallableProxy(IntegrationViewCallables.class);

  GridHelperElement grid;

  public HeaderFooterVisibilityIT() {
    super("it");
  }

  @Override
  public void setup() throws Exception {
    super.setup();
    grid = new GridHelperElement($(GridElement.class).waitForFirst());
  }

  @Test
  public void testHeaderVisible() {
    TestBenchElement thead = grid.$("thead").first();
    assertTrue(thead.isDisplayed());
    $server.setHeaderVisible(false);
    new WebDriverWait(getDriver(), Duration.of(2, SECONDS)).until(invisibilityOf(thead));
  }

  @Test
  public void testFooterVisible() {
    $server.addToolbarFooter("footer");
    TestBenchElement tfoot = grid.$("tfoot").first();
    new WebDriverWait(getDriver(), Duration.of(2, SECONDS)).until(visibilityOf(tfoot));
    $server.setFooterVisible(false);
    new WebDriverWait(getDriver(), Duration.of(2, SECONDS)).until(invisibilityOf(tfoot));
  }
}
