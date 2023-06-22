package com.flowingcode.vaadin.addons.gridhelpers;

import com.vaadin.flow.component.grid.Grid;
import java.util.EventObject;

/** An event that is fired when the responsive step of a Grid component changes. */
@SuppressWarnings("serial")
public class GridResponsiveStepEvent extends EventObject {

  private final int minWidth;

  /**
   * Constructs a {@code GridResponsiveStepEvent} object with the specified source grid and minimum
   * width for the step.
   *
   * @param source the {@code Grid} component that fired the event
   * @param minWidth the minimum width (in pixels) on which the step is applied
   */
  GridResponsiveStepEvent(Grid<?> source, int minWidth) {
    super(source);
    this.minWidth = minWidth;
  }

  /**
   * Returns the {@code Grid} component that fired the event.
   *
   * @return the source {@code Grid} component
   */
  @Override
  public Grid<?> getSource() {
    return (Grid<?>) super.getSource();
  }

  /**
   * Returns the minimum width (in pixels) on which the step is applied.
   *
   * @return the minimum width of the {@link GridResponsiveStep}
   */
  public int getMinWidth() {
    return minWidth;
  }

}
