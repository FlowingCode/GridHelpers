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
