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

package com.flowingcode.vaadin.addons.gridhelpers;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.FooterRow.FooterCell;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.GridSelectionModel;
import com.vaadin.flow.component.grid.GridSingleSelectionModel;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.HeaderRow.HeaderCell;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.shared.Registration;
import java.io.Serializable;
import java.util.Collection;
import lombok.AccessLevel;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
@JsModule("./fcGridHelper/connector.js")
@CssImport(value = "./fcGridHelper/vaadin-menu-bar.css", themeFor = "vaadin-menu-bar")
@CssImport(value = GridHelper.GRID_STYLES, themeFor = "vaadin-grid")
@CssImport(value = "./fcGridHelper/vaadin-context-menu-item.css",
    themeFor = "vaadin-context-menu-item")
@CssImport(value = "./fcGridHelper/vaadin-context-menu-list-box.css",
    themeFor = "vaadin-context-menu-list-box")
public final class GridHelper<T> implements Serializable {

  private static final Logger logger = LoggerFactory.getLogger(GridHelper.class);
  
  private static final String ARROW_SELECTION_PROPERTY = "_fcghArrowSelection";

  public static final String GRID_STYLES = "./fcGridHelper/vaadin-grid.css";

  /** Compact row styling for Vaadin Grid */
  // https://cookbook.vaadin.com/grid-dense-theme
  public static final String DENSE_THEME = "fcGh-dense";

  @Getter(value = AccessLevel.PACKAGE)
  private final Grid<T> grid;

  private final GridHelperClassNameGenerator<T> helperClassNameGenerator;

  protected void setHelperClassNameGenerator(
      Class<?> clazz, SerializableFunction<T, String> generator) {
    getHelper(grid).helperClassNameGenerator.setHelperClassNameGenerator(clazz, generator);
    grid.getDataCommunicator().reset();
  }

  private boolean selectOnClick;

  private GridHelper(Grid<T> grid) {
    this.grid = grid;
    this.helperClassNameGenerator = new GridHelperClassNameGenerator<>();
    setClassNameGenerator(grid.getClassNameGenerator());
    grid.addItemClickListener(this::onItemClick);
    grid.addAttachListener(this::onAttach);
  }

  @SuppressWarnings("unchecked")
  private static <T> GridHelper<T> getHelper(Column<T> column) {
    return getHelper((Grid<T>) column.getGrid());
  }

  private static <T> GridHelper<T> getHelper(Grid<T> grid) {
    @SuppressWarnings("unchecked")
    GridHelper<T> helper = ComponentUtil.getData(grid, GridHelper.class);
    if (helper == null) {
      helper = new GridHelper<>(grid);
      ComponentUtil.setData(grid, GridHelper.class, helper);
    }
    return helper;
  }

  private void initConnector() {
    grid.getUI()
        .orElseThrow(
            () ->
                new IllegalStateException("Connector can only be initialized for an attached Grid"))
        .getPage()
        .executeJs("window.Vaadin.Flow.fcGridHelperConnector.initLazy($0)", grid.getElement());
  }

  private void onAttach(AttachEvent event) {
    initConnector();
    selectionColumnHelper.onAttach();
  }

  private void onItemClick(ItemClickEvent<T> event) {
    T item = event.getItem();
    if (selectOnClick && getSelectionMode(grid) == SelectionMode.MULTI) {
      // https://cookbook.vaadin.com/grid-conditional-select
      if (!selectionFilterHelper.canSelect(item)) {
        return;
      }

      if (grid.getSelectedItems().contains(item)) {
        grid.deselect(item);
      } else {
        grid.select(item);
      }
    }
  }

  /** Return the grid selection mode */
  public static SelectionMode getSelectionMode(Grid<?> grid) {
    GridSelectionModel<?> model = grid.getSelectionModel();
    if (model instanceof GridSingleSelectionModel) {
      return SelectionMode.SINGLE;
    }
    if (model instanceof GridMultiSelectionModel) {
      return SelectionMode.MULTI;
    }
    return SelectionMode.NONE;
  }

  /**
   * Sets the function that is used for generating CSS class names for all the cells in the rows in
   * this grid. Returning {@code null} from the generator results in no custom class name being set.
   * Multiple class names can be returned from the generator as space-separated.
   *
   * <p>If {@link Column#setClassNameGenerator(SerializableFunction)} is used together with this
   * method, resulting class names from both methods will be effective. Class names generated by
   * grid are applied to the cells before the class names generated by column. This means that if
   * the classes contain conflicting style properties, column's classes will win.
   *
   * @param classNameGenerator the class name generator to set, not {@code null}
   * @throws NullPointerException if {@code classNameGenerator} is {@code null}
   * @see Column#setClassNameGenerator(SerializableFunction)
   */
  public void setClassNameGenerator(SerializableFunction<T, String> classNameGenerator) {
    grid.setClassNameGenerator(this.helperClassNameGenerator);
    if (classNameGenerator instanceof GridHelperClassNameGenerator) {
      helperClassNameGenerator.setGridClassNameGenerator(
          ((GridHelperClassNameGenerator<T>) classNameGenerator).getGridClassNameGenerator());
    } else {
      helperClassNameGenerator.setGridClassNameGenerator(classNameGenerator);
    }
  }

  // Select on click
  public static void setSelectOnClick(Grid<?> grid, boolean selectOnClick) {
    getHelper(grid).selectOnClick = selectOnClick;
    if (selectOnClick && GridHelper.isEnhancedSelectionEnabled(grid)) {
      logger.warn(
          "Please disable Enhanced Selection feature when enabling Select On Click to avoid unwanted side effects.");
    }
  }

  public static boolean isSelectOnClick(Grid<?> grid) {
    return getHelper(grid).selectOnClick;
  }

  // Arrow Selection

  /** Allows Grid rows to be selected using up/down arrow keys. */
  public static void setArrowSelectionEnabled(Grid<?> grid, boolean value) {
    getHelper(grid);
    grid.getElement().setProperty(ARROW_SELECTION_PROPERTY, value);
    if (value && GridHelper.isEnhancedSelectionEnabled(grid)) {
      logger.warn(
          "Please disable Enhanced Selection feature when enabling Arrow Selection to avoid unwanted side effects.");
    }
  }

  /** Returns whether Grid rows can be selected using up/down arrow keys. */
  public static boolean isArrowSelectionEnabled(Grid<?> grid) {
    return grid.getElement().getProperty(ARROW_SELECTION_PROPERTY, false);
  }

  // Selection Column

  private final SelectionColumnHelper selectionColumnHelper = new SelectionColumnHelper(this);

  /** Sets whether the multiselect selection column is hidden. */
  public static void setSelectionColumnHidden(Grid<?> grid, boolean value) {
    getHelper(grid).selectionColumnHelper.setSelectionColumnHidden(value);
  }

  /** Sets whether the multiselect selection column is frozen. */
  @Deprecated
  public static void setSelectionColumnFrozen(Grid<?> grid, boolean value) {
    if (getSelectionMode(grid) == SelectionMode.MULTI) {
      // https://cookbook.vaadin.com/grid-frozen-selection-column
      ((GridMultiSelectionModel<?>) grid.getSelectionModel())
              .setSelectionColumnFrozen(value);
    }
  }

  /** Returns whether the multiselect selection column is hidden. */
  public static boolean isSelectionColumnHidden(Grid<?> grid) {
    return getHelper(grid).selectionColumnHelper.isSelectionColumnHidden();
  }

  /** Returns whether the multiselect selection column is frozen. */
  @Deprecated
  public static boolean isSelectionColumnFrozen(Grid<?> grid) {
    return getSelectionMode(grid) == SelectionMode.MULTI && ((GridMultiSelectionModel<?>) grid.getSelectionModel())
            .isSelectionColumnFrozen();
  }

  // Selection Filter

  private final SelectionFilterHelper<T> selectionFilterHelper = new SelectionFilterHelper<>(this);

  /**
   * Sets a predicate for determining which rows are selectable.
   * <p>
   *
   * After a call to {@link Grid#setSelectionMode(SelectionMode)} the selection filter is lost and
   * it has to be configured again:
   *
   * <pre>
   * GridHelper.setSelectionFilter(grid, GridHelper.getSelectionFilter(grid)); // static call
   * grid.setSelectionFilter(grid.getSelectionFilter()); // with lombok extension
   * </pre>
   */
  public static <T> void setSelectionFilter(Grid<T> grid, SerializablePredicate<T> predicate) {
    getHelper(grid).selectionFilterHelper.setSelectionFilter(predicate);
  }

  /** Returns the predicate for determining which rows are selectable. */
  public static <T> SerializablePredicate<T> getSelectionFilter(Grid<T> grid) {
    return getHelper(grid).selectionFilterHelper.getSelectionFilter();
  }

  // Column Toggle

  private final ColumnToggleHelper<T> columnToggleHelper = new ColumnToggleHelper<>(this);

  /** Shows a menu to toggle the visibility of grid columns. */
  public static void setColumnToggleVisible(Grid<?> grid, boolean visible) {
    getHelper(grid).columnToggleHelper.setColumnToggleVisible(visible);
  }

  /** Returns whether the menu to toggle the visibility of grid columns is visible. */
  public static boolean isColumnToggleVisible(Grid<?> grid) {
    return getHelper(grid).columnToggleHelper.isColumnToggleVisible();
  }

  /**Returns whether this column can be hidden by the user. Default is {@code false}.
   *
   * @return {@code true} if the user can hide the column, {@code false} if not.*/
  public static <T> boolean isHidable(Column<T> column) {
    return getHelper(column.getGrid()).columnToggleHelper.isHidable(column);
  }

  /**Sets whether this column can be hidden by the user. Hidable columns can be hidden and shown via the sidebar menu.
   * @param column the column to be configured
   * @param hidable {@code true} if the column may be hidden by the user via UI interaction
   *
   * @return the column.
   */
  public static <T> Column<T> setHidable(Column<T> column, boolean hidable) {
    getHelper(column.getGrid()).columnToggleHelper.setHidable(column, hidable);
    return column;
  }

  /**
   * Adds a listener that is notified when column visibility is modified through the sidebar menu.
   */
  public static <T> Registration addColumnToggleListener(Grid<T> grid,
      ComponentEventListener<ColumnToggleEvent<T>> listener) {
    return getHelper(grid).columnToggleHelper.addColumnToggleListener(listener);
  }

  /**
   * Sets the caption of the hiding toggle for this column. Shown in the toggle for this column in the grid's sidebar when the column is {@linkplain #isHidable(Column) hidable}.
   *
   * <p>If the value is <code>null</code>, the column cannot be hidden via the sidebar menu.
   *
   * @param hidingToggleCaption the text to show in the column hiding toggle
   */
  public static <T> void setHidingToggleCaption(Column<T> column, String caption) {
    getHelper(column.getGrid()).columnToggleHelper.setHidingToggleCaption(column, caption);
  }

  /**
   * Returns the caption of the hiding toggle for this column.
   *
   * @return the text shown in the column hiding toggle
   */
  public static <T> String getHidingToggleCaption(Column<T> column) {
    return getHelper(column.getGrid()).columnToggleHelper.getHidingToggleCaption(column);
  }

  public static boolean isMenuToggleColumn(Column<?> column) {
    return column == getHelper(column).columnToggleHelper.getMenuToggleColumn();
  }

  // Empty Label

  private final EmptyLabelGridHelper emptyLabel = new EmptyLabelGridHelper(this);

  /** Sets a component that is displayed when the Grid would show an empty data set. */
  public static void setEmptyGridLabel(Grid<?> grid, Component component) {
    getHelper(grid).emptyLabel.setEmptyGridLabel(component);
  }

  /** Returns the component that is displayed when the Grid would show an empty data set. */
  public static Component getEmptyGridLabel(Grid<?> grid) {
    return getHelper(grid).emptyLabel.getEmptyGridLabel();
  }

  // FooterToolbar

  private final FooterToolbarGridHelper footerToolbar = new FooterToolbarGridHelper(this);


  public static void addToolbarFooter(Grid<?> grid, Component toolBar) {
    getHelper(grid).footerToolbar.setFooterToolbar(toolBar);
  }
 
  @Deprecated
  public static String getHeader(Grid<?> grid, Column<?> column) {
    return column.getHeaderText();
  }

  @Deprecated
  public static String getFooter(Grid<?> grid, Column<?> column) {
    return column.getFooterText();
  }
  
  private final EnhancedSelectionGridHelper<T> enhancedSelectionGridHelper = new EnhancedSelectionGridHelper<>(this);

  /**
   * When enabled, enhances grid row selection support adding support for these combinations: click, arrow
   * up/down, shift+click, shift+arrow up/down, ctrl+click and ctrl+space.
   * 
   * @param grid
   * @param enabled
   */
  public static final void setEnhancedSelectionEnabled(Grid<?> grid, boolean enabled) {
    if (enabled) {
      getHelper(grid).enhancedSelectionGridHelper.enableEnhancedSelection();
      
      if (GridHelper.isArrowSelectionEnabled(grid) || GridHelper.isSelectOnClick(grid)) {
        logger.warn(
            "Please disable Arrow Selection and Select On Click features when enabling Enhanced Selection to avoid unwanted side effects.");
      }
    } else {
      getHelper(grid).enhancedSelectionGridHelper.disableEnhancedSelection();
    }
  }

  /** Returns whether the enhanced selection is enabled. */
  public static boolean isEnhancedSelectionEnabled(Grid<?> grid) {
    return getHelper(grid).enhancedSelectionGridHelper.isEnhancedSelectionEnabled();
  }


  // HeaderFooterVisibilityHelper

  private final HeaderFooterVisibilityHelper headerFooterVisibility =
      new HeaderFooterVisibilityHelper(this);

  public static void setHeaderVisible(Grid<?> grid, boolean visible) {
    getHelper(grid).headerFooterVisibility.setHeaderVisible(visible);
  }

  /**
   * Returns the visibility of the header section.
   *
   * @return true if visible, false otherwise.
   */
  public static boolean isHeaderVisible(Grid<?> grid) {
    return getHelper(grid).headerFooterVisibility.isHeaderVisible();
  }

  /**
   * Sets the visibility of the footer section.
   *
   * @param visible true to show footer section, false to hide
   */
  public static void setFooterVisible(Grid<?> grid, boolean visible) {
    getHelper(grid).headerFooterVisibility.setFooterVisible(visible);
  }

  /**
   * Returns the visibility of the footer section.
   *
   * @return true if visible, false otherwise.
   */
  public static boolean isFooterVisible(Grid<?> grid) {
    return getHelper(grid).headerFooterVisibility.isFooterVisible();
  }


  private final HeaderFooterStylesHelper headerFooterStylesHelper =
      new HeaderFooterStylesHelper(this);

  public static GridStylesHelper getHeaderStyles(Grid<?> grid, HeaderRow row) {
    return getHelper(grid).headerFooterStylesHelper.getStyles(row);
  }

  public static GridStylesHelper getFooterStyles(Grid<?> grid, FooterRow row) {
    return getHelper(grid).headerFooterStylesHelper.getStyles(row);
  }

  public static GridStylesHelper getHeaderStyles(Grid<?> grid, HeaderCell cell) {
    return getHelper(grid).headerFooterStylesHelper.getStyles(cell);
  }

  public static GridStylesHelper getFooterStyles(Grid<?> grid, FooterCell cell) {
    return getHelper(grid).headerFooterStylesHelper.getStyles(cell);
  }


  private final HeightByRowsHelper heightByRowsHelper = new HeightByRowsHelper(this);

  /**
   * Sets the number of rows that should be visible in Grid's body.
   *
   * The algorithm assumes that all data rows have the same height and considers headers, footers,
   * and the horizontal scrollbar when the method is called. However, if data rows, headers, or
   * footers are inserted or removed after the initial calculation, the grid may not automatically
   * adjust the size of the grid to accommodate the changed number of rows.
   *
   * @param rows The height in terms of number of rows displayed in Grid's body. If Grid doesn't
   *        contain enough rows, white space is displayed instead.
   * @throws IllegalArgumentException if {@code rows} is zero or less
   * @throws IllegalArgumentException if {@code rows} is {@link Double#isInfinite(double) infinite}
   * @throws IllegalArgumentException if {@code rows} is {@link Double#isNaN(double) NaN}
   */
  public static void setHeightByRows(Grid<?> grid, double rows) {
    getHelper(grid).heightByRowsHelper.setHeightByRows(rows);
  }

  /**
   * Sets the number of rows that should be visible in Grid's body, while
   * {@link #getHeightMode()} is {@link HeightMode#ROW}.
   * <p>
   * If Grid is currently not in {@link HeightMode#ROW}, the given value is
   * remembered, and applied once the mode is applied.
   *
   * @See {@link #setHeightByRows(Grid, double)}
   */
  public static void setHeightByRows(Grid<?> grid, int rows) {
    // this overload is a workaround for a lombok issue "bad type on operand stack"
    // when setHeightByRows(Grid, double) is called with actual parameter of type int
    setHeightByRows(grid, (double) rows);
  }

  /**
   * Gets the amount of rows in Grid's body that are shown,
   * while {@link #getHeightMode()} is {@link HeightMode#ROW}.
   *
   * @return the amount of rows that are being shown in Grid's body
   * @see #setHeightByRows(double)
   */
  public static double getHeightByRows(Grid<?> grid) {
    return getHelper(grid).heightByRowsHelper.getHeightByRows();
  }

  /**
   * Defines the mode in which the Grid's height is calculated.
   * <p>
   * If {@link HeightMode#CSS} is given, Grid will respect the values given via a
   * {@code setHeight}-method, and behave as a traditional Component.
   * <p>
   * If {@link HeightMode#ROW} is given, Grid will make sure that the body will display as many rows
   * as {@link #getHeightByRows()} defines.
   *
   * @param heightMode the mode in to which Grid should be set
   */
  public static void setHeightMode(Grid<?> grid, HeightMode heightMode) {
    getHelper(grid).heightByRowsHelper.setHeightMode(heightMode);
  }

  /**
   * Defines the mode in which the Grid's height is calculated.
   * <p>
   * If {@link HeightMode#CSS} is given, Grid will respect the CSS height as a traditional Component.
   * <p>
   * If {@link HeightMode#ROW} is given, Grid will make sure that the body will display as many rows
   * as {@link #getHeightByRows()} defines.
   *
   * @param heightMode the mode in to which Grid should be set
   * @return
   */
  public static HeightMode getHeightMode(Grid<?> grid) {
    return getHelper(grid).heightByRowsHelper.getHeightMode();
  }

  private final ResponsiveGridHelper<T> responsiveGridHelper = new ResponsiveGridHelper<>(this);

  /**
   * Get or create a responsive steps for the given {@code grid} and minimum width.
   */
  public static <T> GridResponsiveStep<T> responsiveStep(Grid<T> grid, int minWidth) {
    return getHelper(grid).responsiveGridHelper.getOrCreate(minWidth);
  }

  /** Return the responsive steps of the given {@code grid}. */
  public static <T> Collection<GridResponsiveStep<T>> getResponsiveSteps(Grid<T> grid) {
    return getHelper(grid).responsiveGridHelper.getAll();
  }

}
