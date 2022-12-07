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
import com.flowingcode.vaadin.testbench.rpc.JsonArrayList;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.testbench.GridElement;
import java.util.Arrays;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;

public class SelectOnClickIT extends AbstractViewTest implements HasRpcSupport {

  IntegrationViewCallables $server = createCallableProxy(IntegrationViewCallables.class);
  GridHelperElement grid;

  public SelectOnClickIT() {
	super("it");
  }

  @Override
  public void setup() throws Exception {
	super.setup();
	grid = new GridHelperElement($(GridElement.class).waitForFirst());
  }

  @SafeVarargs
  private static <T> Matcher<? super JsonArrayList<T>> equalToList(T... items) {
    return Matchers.equalTo(Arrays.asList(items));
  }

  @Test
  public void testSelectOnClickMulti() {
    assertThat($server.getSelectedRows(), empty());
    $server.setSelectionMode(SelectionMode.MULTI);
    $server.setSelectOnClick(true);
    grid.getCell(0, 1).click();
    assertThat($server.getSelectedRows(), equalToList(0));
    grid.getCell(1, 1).click();
    assertThat($server.getSelectedRows(), equalToList(0, 1));
    grid.getCell(3, 1).click();
    assertThat($server.getSelectedRows(), equalToList(0, 1, 3));
  }

  @Test
  public void testSelectOnClickSingle() {
    assertThat($server.getSelectedRows(), empty());
    $server.setSelectionMode(SelectionMode.SINGLE);
    $server.setSelectOnClick(true);
    grid.getCell(0, 1).click();
    assertThat($server.getSelectedRows(), equalToList(0));
    grid.getCell(1, 1).click();
    assertThat($server.getSelectedRows(), equalToList(1));
    grid.getCell(3, 1).click();
    assertThat($server.getSelectedRows(), equalToList(3));
  }

  @Test
  public void testSelectOnClickNone() {
    assertThat($server.getSelectedRows(), empty());
    $server.setSelectionMode(SelectionMode.NONE);
    $server.setSelectOnClick(true);
    grid.getCell(0, 1).click();
    assertThat($server.getSelectedRows(), empty());
  }

}
