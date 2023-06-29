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
