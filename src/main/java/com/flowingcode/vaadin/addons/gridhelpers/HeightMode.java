package com.flowingcode.vaadin.addons.gridhelpers;

/**
 * The modes for height calculation that are supported {@link GridHelper}.
 *
 * @see GridHelper#setHeightMode(HeightMode)
 */
public enum HeightMode {
  /**
   * 
   * The height of the Component is defined by a CSS-like value.
   */
  CSS,

  /**
   * The height of the Component is defined by a number of rows.
   */
  ROW;
}

