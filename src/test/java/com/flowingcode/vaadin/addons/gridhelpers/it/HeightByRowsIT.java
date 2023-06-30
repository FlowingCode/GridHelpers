package com.flowingcode.vaadin.addons.gridhelpers.it;

import static com.flowingcode.vaadin.addons.gridhelpers.it.HeightByRowsITView.EMPTY;
import static com.flowingcode.vaadin.addons.gridhelpers.it.HeightByRowsITView.FULLSIZE;
import static org.junit.Assert.assertEquals;
import com.flowingcode.vaadin.addons.gridhelpers.HeightMode;
import com.flowingcode.vaadin.testbench.rpc.HasRpcSupport;
import com.vaadin.flow.component.grid.testbench.GridElement;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;

public class HeightByRowsIT extends AbstractViewTest implements HasRpcSupport {

  private static int ROWS = 10;

  HeightByRowsITViewCallables $server = createCallableProxy(HeightByRowsITViewCallables.class);

  GridHelperElement grid;

  public HeightByRowsIT() {
    super(HeightByRowsITView.ROUTE);
  }

  private void open(Object... args) {
    if (grid != null) {
      throw new IllegalStateException();
    }
    String params = Stream.of(args).map(Object::toString).collect(Collectors.joining(";"));
    getDriver().get(getDriver().getCurrentUrl() + "/" + params);
    grid = new GridHelperElement($(GridElement.class).waitForFirst());
    $server.roundtrip();
  }

  @Override
  public void setup() throws Exception {
    super.setup();
  }

  @Test
  public void testOpenWithItemsDefaultSize() {
    open(ROWS);
    assertEquals(ROWS, grid.getVisibleRowsCount());
    assertEquals(ROWS, $server.getHeightByRows(), 0);
    assertEquals(HeightMode.ROW, $server.getHeightMode());
    assertEquals(453, grid.getOffsetHeight());
    assertEquals("453px", grid.getHeightByRowsSize());
  }

  @Test
  public void testOpenWithItemsFullSize() {
    open(ROWS, FULLSIZE);
    assertEquals(ROWS, grid.getVisibleRowsCount());
    assertEquals(ROWS, $server.getHeightByRows(), 0);
    assertEquals(HeightMode.ROW, $server.getHeightMode());
    assertEquals(453, grid.getOffsetHeight());
    assertEquals("453px", grid.getHeightByRowsSize());
  }

  @Test
  public void testOpenEmptyDefaultSize() {
    open(ROWS, EMPTY);
    assertEquals(0, grid.getVisibleRowsCount());
    assertEquals(ROWS, $server.getHeightByRows(), 0);
    assertEquals(HeightMode.ROW, $server.getHeightMode());
    assertEquals(94, grid.getOffsetHeight());
    assertEquals("94px", grid.getHeightByRowsSize());
  }

  @Test
  public void testOpenEmptyFullSize() {
    open(ROWS, EMPTY, FULLSIZE);
    assertEquals(0, grid.getVisibleRowsCount());
    assertEquals(ROWS, $server.getHeightByRows(), 0);
    assertEquals(HeightMode.ROW, $server.getHeightMode());
    assertEquals(94, grid.getOffsetHeight());
    assertEquals("94px", grid.getHeightByRowsSize());
  }

}
