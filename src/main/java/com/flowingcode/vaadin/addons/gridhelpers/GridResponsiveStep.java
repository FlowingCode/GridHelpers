package com.flowingcode.vaadin.addons.gridhelpers;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.shared.Registration;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NonNull;

/**
 * Class used in describing the responsive layouting behavior of a {@link Grid}
 *
 * @param <T> the data type for Grid
 */
@SuppressWarnings("serial")
public class GridResponsiveStep<T> implements Serializable {

  private int minWidth;

  private final ResponsiveGridHelper<T> helper;

  private final Map<Column<T>, Boolean> columns = new HashMap<>();

  private Boolean allColumnsVisible;

  @Getter
  private Renderer<T> itemDetailsRenderer;

  private boolean itemDetailsRendererSet;

  private final Map<SerializableConsumer<GridResponsiveStepEvent>, Boolean> listeners =
      new LinkedHashMap<>();

  GridResponsiveStep() {
    minWidth = -1;
    helper = null;
  }

  GridResponsiveStep(int minWidth, @NonNull ResponsiveGridHelper<T> helper) {
    if (minWidth < 0) {
      throw new IllegalArgumentException();
    }
    this.minWidth = minWidth;
    this.helper = helper;
  }

  private void requireRefresh() {
    if (helper != null) {
      helper.requireRefresh(this);
    }
  }

  /**
   * Return the minimum width (in pixels) after which this responsive step is to be applied.
   */
  public int getMinWidth() {
    return minWidth;
  }

  /**
   * Accumulates the settings from the provided step into this instance.
   *
   * <pre>{@code
   * var result = new GridResponsiveStep(0, helper);
   * for (var step : steps)
   *   result = result.reduce(result, element);
   * return result;
   * }</pre>
   */
  void accumulate(GridResponsiveStep<T> step) {
    if (helper != null || step.minWidth <= minWidth) {
      throw new IllegalArgumentException();
    }

    minWidth = step.minWidth;

    if (step.itemDetailsRendererSet) {
      itemDetailsRendererSet = true;
      itemDetailsRenderer = step.itemDetailsRenderer;
    }

    if (step.allColumnsVisible != null) {
      columns.keySet().forEach(c -> columns.put(c, step.allColumnsVisible));
    }

    itemDetailsRenderer = step.itemDetailsRenderer;
    columns.putAll(step.columns);

    listeners.entrySet().removeIf(e -> !e.getValue());
    listeners.putAll(step.listeners);
  }

  /**
   * Applies the settings from this responsive step.
   */
  void apply(Grid<T> grid) {
    if (helper != null) {
      throw new IllegalStateException();
    }

    boolean hasChanges = false;

    if (itemDetailsRendererSet) {
      grid.setItemDetailsRenderer(itemDetailsRenderer);
      grid.setDetailsVisibleOnClick(itemDetailsRenderer != null);
    }

    for (Map.Entry<Column<T>, Boolean> e : columns.entrySet()) {
      Grid.Column<T> c = e.getKey();
      if (!e.getValue().equals(c.isVisible()) && !GridHelper.isMenuToggleColumn(c)) {
        e.getKey().setVisible(e.getValue());
        hasChanges = true;
      }
    }

    if (hasChanges && GridHelper.isColumnToggleVisible(grid)) {
      // refresh the toggle column
      GridHelper.setColumnToggleVisible(grid, true);
    }

    GridResponsiveStepEvent ev = new GridResponsiveStepEvent(grid, minWidth);
    listeners.keySet().forEach(listener -> listener.accept(ev));
  }

  /**
   * Set the renderer to use for displaying the item details rows when this responsive step is
   * applied.
   *
   * @param renderer the renderer to use for displaying item details rows, {@code null} to use no
   *        detail renderer
   * @return the instance of GridResponsiveStep for chaining method calls
   * @see Grid#setItemDetailsRenderer(Renderer)
   */
  public GridResponsiveStep<T> setItemDetailsRenderer(Renderer<T> renderer) {
    itemDetailsRenderer = renderer;
    itemDetailsRendererSet = true;
    requireRefresh();
    return this;
  }

  /**
   * Set the renderer to use for displaying the item details rows when this responsive step is
   * applied.
   *
   * @param componentFunction the function to set item details with a Component
   * @param <C> the type of component returned by {@code componentFunction}
   * @return the instance of GridResponsiveStep for chaining method calls
   * @see Grid#setItemDetailsRenderer(Renderer)
   */
  public <C extends Component> GridResponsiveStep<T> setItemDetailsRenderer(
      @NonNull SerializableFunction<T, C> componentFunction) {
    return setItemDetailsRenderer(new ComponentRenderer<>(componentFunction));
  }

  /**
   * Set the columns that should be visible when this responsive step is applied.
   *
   * @param columnsToShow columns that should be visible
   * @return the instance of GridResponsiveStep for chaining method calls
   */
  public GridResponsiveStep<T> setColumns(Grid.Column<?>... columnsToShow) {
    hideAll();
    show(columnsToShow);
    return this;
  }

  /**
   * Set the columns that should be visible when this responsive step is applied.
   *
   * @param columnsToShow columns that should be visible
   * @return the instance of GridResponsiveStep for chaining method calls
   */
  public GridResponsiveStep<T> setColumns(List<Grid.Column<T>> columnsToShow) {
    return setColumns(columnsToShow.toArray(Grid.Column<?>[]::new));
  }

  /**
   * Show the given columns when this responsive step is applied.
   *
   * @param columnsToShow columns to show
   * @return the instance of GridResponsiveStep for chaining method calls
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public GridResponsiveStep<T> show(Grid.Column<?>... columnsToShow) {
    return show((List) Arrays.asList(columnsToShow));
  }

  /**
   * Show the given columns when this responsive step is applied.
   *
   * @param columnsToShow the columns to show
   * @return the instance of GridResponsiveStep for chaining method calls
   */
  public GridResponsiveStep<T> show(List<Grid.Column<T>> columnsToShow) {
    return setVisibility(columnsToShow, true);
  }

  /**
   * Hide the given columns when this responsive step is applied.
   *
   * @param columnsToHide the columns to hide
   * @return the instance of GridResponsiveStep for chaining method calls
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public GridResponsiveStep<T> hide(Grid.Column<?>... columnsToHide) {
    return hide((List) Arrays.asList(columnsToHide));
  }

  /**
   * Hide the given columns when this responsive step is applied.
   *
   * @param columnsToHide the columns to hide
   * @return the instance of GridResponsiveStep for chaining method calls
   */
  public GridResponsiveStep<T> hide(List<Grid.Column<T>> columnsToHide) {
    return setVisibility(columnsToHide, false);
  }

  private GridResponsiveStep<T> setVisibility(List<Grid.Column<T>> columnsToSet, Boolean value) {
    if (helper != null && !helper.getGrid().getColumns().containsAll(columnsToSet)) {
      throw new IllegalArgumentException("Some columns are not contained in the grid");
    }
    columnsToSet.forEach(c -> columns.put(c, value));
    requireRefresh();
    return this;
  }

  /**
   * Hide all the grid columns when this responsive step is applied.
   *
   * @return the instance of GridResponsiveStep for chaining method calls
   */
  public GridResponsiveStep<T> hideAll() {
    columns.clear();
    allColumnsVisible = false;
    requireRefresh();
    return this;
  }

  /**
   * Show all the grid columns when this responsive step is applied.
   *
   * @return the instance of GridResponsiveStep for chaining method calls
   */
  public GridResponsiveStep<T> showAll() {
    columns.clear();
    allColumnsVisible = true;
    requireRefresh();
    return this;
  }

  /**
   * Adds a listener which is invoked when the layout changes because of grid resizing. By default,
   * the listener will be "non-cummulative" (i.e. it will only be fired when the exact step is
   * selected).
   *
   * @param listener to add
   * @return handle to unregister the listener or make it cummulative
   * @see GridResponsiveStepListenerRegistration#cummulative()
   */
  public GridResponsiveStepListenerRegistration addListener(
      @NonNull SerializableConsumer<GridResponsiveStepEvent> listener) {
    listeners.put(listener, Boolean.FALSE);
    return new GridResponsiveStepListenerRegistration() {
      @Override
      public void remove() {
        listeners.remove(listener);
      }

      @Override
      public GridResponsiveStepListenerRegistration cummulative() {
        listeners.computeIfPresent(listener, (k, v) -> Boolean.TRUE);
        return this;
      }
    };
  }

  /** Remove this responsive step and update the Grid. */
  public void remove() {
    helper.remove(this);
  }

  /**
   * A registration object for removing or configuring a {@link GridResponsiveStep} listener.
   */
  public interface GridResponsiveStepListenerRegistration extends Registration {
    /**
     * Set the registered listener as cummulative. A cummulative listener will be fired when the
     * minimum width is met. Non-cummulative listeners will only be fired when the exact step is
     * selected.
     *
     * @return this registration instance.
     */
    public GridResponsiveStepListenerRegistration cummulative();
  }

}