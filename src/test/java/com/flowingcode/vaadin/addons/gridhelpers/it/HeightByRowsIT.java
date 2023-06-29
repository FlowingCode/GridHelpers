package com.flowingcode.vaadin.addons.gridhelpers.it;

import static org.junit.Assert.assertEquals;
import com.flowingcode.vaadin.addons.gridhelpers.HeightMode;
import com.flowingcode.vaadin.testbench.rpc.HasRpcSupport;
import com.vaadin.flow.component.grid.testbench.GridElement;
import org.junit.Test;

public class HeightByRowsIT extends AbstractViewTest implements HasRpcSupport {

  IntegrationViewCallables $server = createCallableProxy(IntegrationViewCallables.class);

  GridHelperElement grid;

  public HeightByRowsIT() {
    super("it");
  }

  @Override
  public void setup() throws Exception {
    super.setup();
    grid = new GridHelperElement($(GridElement.class).waitForFirst());
  }

  @Test
  public void test() {
    $server.setHeightMode(HeightMode.ROW);
    $server.setHeightByRows(10);
    assertEquals(10, grid.getLastVisibleRowIndex());
  }

}
