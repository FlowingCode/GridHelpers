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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import com.flowingcode.vaadin.testbench.rpc.HasRpcSupport;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.testbench.GridElement;
import java.util.Arrays;
import java.util.List;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;

public class SelectionFilterIT extends AbstractViewTest implements HasRpcSupport {

  IntegrationViewCallables $server = createCallableProxy(IntegrationViewCallables.class);
  GridHelperElement grid;

  public SelectionFilterIT() {
	super("it");
  }

  @Override
  public void setup() throws Exception {
	super.setup();
	grid = new GridHelperElement($(GridElement.class).waitForFirst());
  }

  @SafeVarargs
  private static <T> Matcher<? super List<T>> equalToList(T... items) {
    return Matchers.equalTo(Arrays.asList(items));
  }

  @Test
  public void testFilteredSelectionMulti() {
    $server.setSelectionMode(SelectionMode.MULTI);
    $server.setSelectionFilterEnabled(true);
    grid.select(0);
    assertThat($server.getSelectedRows(), equalToList(0));

    // odd items cannot be selected
    grid.select(1);
    assertThat($server.getSelectedRows(), equalToList(0));

    grid.select(2);
    assertThat($server.getSelectedRows(), equalToList(0, 2));
  }

  @Test
  public void testFilteredSelectionSingle() {
    $server.setSelectionMode(SelectionMode.SINGLE);
    $server.setSelectionFilterEnabled(true);
    grid.select(0);
    assertThat($server.getSelectedRows(), equalToList(0));

    // odd items cannot be selected
    grid.select(1);
    // TODO should it be cleared?
    // assertThat($server.getSelectedRows(), equalToList(0));
    assertThat($server.getSelectedRows(), empty());

    grid.select(2);
    assertThat($server.getSelectedRows(), equalToList(2));
  }

  @Test
  public void testUnfilteredSelection() {
    $server.setSelectionMode(SelectionMode.SINGLE);
    $server.setSelectionFilterEnabled(true);
    $server.setSelectionFilterEnabled(false);
    grid.getCell(0, 1).click();
    assertThat($server.getSelectedRows(), equalToList(0));
    grid.getCell(1, 1).click();
    assertThat($server.getSelectedRows(), equalToList(1));
  }

}
