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

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.data.provider.BackEndDataProvider;
import com.vaadin.flow.data.provider.InMemoryDataProvider;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.ValueProvider;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * A grid column where boolean value is rendered as a {@link Checkbox}.
 * <p/>
 * By default select all checkbox is visible when grid uses an {@link InMemoryDataProvider}, or it's
 * hidden when a {@link BackEndDataProvider} is used.
 * <p/>
 * When select all checkbox is visible, {@link CheckboxColumn} must be explicitly refreshed if grid
 * dataprovider changes after column creation using {@code CheckboxColumn.refresh()} method.
 * 
 * @param <T>
 */
@SuppressWarnings("serial")
@RequiredArgsConstructor
@Accessors(fluent = true)
public final class CheckboxColumn<T> implements Serializable {

  @RequiredArgsConstructor
  @Accessors(fluent = true)
  public static class CheckboxColumnConfiguration<T> implements Serializable {
    private final ValueProvider<T, Boolean> getter;
    @Setter
    private String header;
    @Setter
    private boolean readOnly = false;
    @Setter
    private Boolean selectAllCheckboxVisible;
    @Setter
    private CheckboxPosition checkboxPosition = CheckboxPosition.TOP;
  }

  @AllArgsConstructor
  public enum CheckboxPosition {
    TOP("fcgh-checkbox-top"), RIGHT("fcgh-checkbox-right"), BOTTOM("fcgh-checkbox-bottom"), LEFT("fcgh-checkbox-left");

    private String theme;
  }

  private final Grid<T> grid;
  @Getter
  private final Column<T> column;
  private final Map<T, Boolean> itemsState = new HashMap<>();
  private final transient IntSupplier totalItemsCount;
  private final transient IntSupplier selectedItemsCount;
  private final Checkbox selectAllCheckbox;
  private final CheckboxColumnConfiguration<T> config;
  @Setter
  private SerializableBiConsumer<T, Boolean> checkboxClickListener;
  @Setter
  private SerializableConsumer<Integer> checkedCountListener;

  CheckboxColumn(Grid<T> grid, CheckboxColumnConfiguration<T> config) {
    this.grid = grid;
    this.config = config;

    if (config.selectAllCheckboxVisible == null) {
      config.selectAllCheckboxVisible = grid.getDataProvider().isInMemory();
    }

    if (!config.readOnly && Boolean.TRUE.equals(config.selectAllCheckboxVisible)) {
      this.totalItemsCount = () -> getItemsCount(grid);
      this.selectedItemsCount =
          () -> (int) itemsState.values().stream().filter(Boolean.TRUE::equals).count();
    } else {
      this.totalItemsCount = () -> 0;
      this.selectedItemsCount = () -> 0;
    }

    this.selectAllCheckbox = buildSelectAllCheckbox(config, grid).orElse(null);

    this.column = grid
        .addComponentColumn(item -> buildCheckbox(config, item))
        .setTextAlign(ColumnTextAlign.CENTER);

    if (config.readOnly) {
      this.column.setHeader(config.header);
    } else {
      initializeNotReadOnly(grid, config);
    }
  }

  private void initializeNotReadOnly(Grid<T> grid, CheckboxColumnConfiguration<T> config) {
    if (selectAllCheckbox != null) {
      this.column.setHeader(selectAllCheckbox);
    } else {
      this.column.setHeader(config.header);
    }

    this.column.addAttachListener(e -> {
      if (e.isInitialAttach()) {
        this.initItemsState(grid, config.getter);
        initializeSelectAllCheckbox(grid, selectAllCheckbox, config.getter);
      }
    });
  }

  /**
   * Refreshes select all checkbox status to reflect the underlying data.
   * <p/>
   * This method must be invoked when grid's dataprovider changes.
   */
  public void refresh() {
    this.initItemsState(grid, config.getter);
    initializeSelectAllCheckbox(grid, selectAllCheckbox, config.getter);
  }

  private void initializeSelectAllCheckbox(Grid<T> grid, Checkbox selectAllCheckbox,
      ValueProvider<T, Boolean> getter) {
    if (selectAllCheckbox != null) {
      int initialSelectedItemsCountValue = getInitialSelectItemsCount(grid, getter);
      int totalItemsCountValue = this.totalItemsCount.getAsInt();
      selectAllCheckbox.setValue(initialSelectedItemsCountValue > 0);
      selectAllCheckbox.setIndeterminate(initialSelectedItemsCountValue < totalItemsCountValue);
    }
  }

  /**
   * Returns the items whose corresponding checkbox is checked.
   * 
   * @return selected items.
   */
  public Set<T> getSelectedItems() {
    return itemsState.entrySet().stream().filter(Entry::getValue).map(Entry::getKey)
        .collect(Collectors.toSet());
  }

  private int getItemsCount(Grid<T> grid) {
    return grid.getDataProvider().isInMemory() ? grid.getListDataView().getItemCount()
        : (int) grid.getLazyDataView().getItems().count();
  }

  private int getInitialSelectItemsCount(Grid<T> grid, ValueProvider<T, Boolean> filter) {
    return grid.getDataProvider().isInMemory()
        ? (int) grid.getListDataView().getItems().filter(filter::apply).count()
        : (int) grid.getLazyDataView().getItems().filter(filter::apply).count();
  }

  private void initItemsState(Grid<T> grid, ValueProvider<T, Boolean> getter) {
    itemsState.clear();

    if (grid.getDataProvider().isInMemory()) {
      grid.getListDataView().getItems()
          .forEach(item -> itemsState.computeIfAbsent(item, getter));
    } else {
      grid.getLazyDataView().getItems()
          .forEach(item -> itemsState.computeIfAbsent(item, getter));
    }
  }

  private Optional<Checkbox> buildSelectAllCheckbox(CheckboxColumnConfiguration<T> config,
      Grid<T> grid) {
    if (config.readOnly || Boolean.TRUE.equals(!config.selectAllCheckboxVisible)) {
      return Optional.empty();
    }

    Checkbox checkbox = new Checkbox(config.header);
    checkbox.setIndeterminate(someItemsSelected());
    checkbox.addValueChangeListener(e -> {
      if (!e.isFromClient()) {
        return;
      }
      if (someItemsSelected() && Boolean.TRUE.equals(!e.getValue())) {
        checkbox.setValue(true);
      }
      Stream<T> items = grid.getDataProvider().isInMemory() ? grid.getListDataView().getItems()
          : grid.getLazyDataView().getItems();
      itemsState.clear();
      items.forEach(item -> itemsState.put(item, checkbox.getValue()));
      grid.getDataProvider().refreshAll();
      checkbox.setIndeterminate(someItemsSelected());

      notifyCheckedCountChanged(checkedCountListener);
    });

    checkbox.getElement().getThemeList().add("fcgh-small");

    if (config.header != null && config.checkboxPosition.theme != null) {
      checkbox.getElement().getThemeList().add(config.checkboxPosition.theme);
    }

    return Optional.of(checkbox);
  }

  private void notifyCheckboxClicked(SerializableBiConsumer<T, Boolean> listener, T item,
      boolean value) {
    Optional.ofNullable(listener).ifPresent(l -> l.accept(item, value));
  }

  private void notifyCheckedCountChanged(SerializableConsumer<Integer> listener) {
    Optional.ofNullable(listener).ifPresent(l -> l.accept(getSelectedItems().size()));
  }

  private boolean someItemsSelected() {
    return selectedItemsCount.getAsInt() > 0
        && selectedItemsCount.getAsInt() < totalItemsCount.getAsInt();
  }

  private boolean allItemsSelected() {
    return totalItemsCount.getAsInt() == selectedItemsCount.getAsInt();
  }

  private Checkbox buildCheckbox(CheckboxColumnConfiguration<T> config, T item) {
    Checkbox checkbox = new Checkbox(itemsState.computeIfAbsent(item, config.getter::apply));
    checkbox.setReadOnly(config.readOnly);

    if (!config.readOnly) {
      checkbox.addValueChangeListener(e -> {
        itemsState.put(item, e.getValue());
        if (selectAllCheckbox != null) {
          if (allItemsSelected()) {
            selectAllCheckbox.setValue(true);
            selectAllCheckbox.setIndeterminate(false);
          } else if (someItemsSelected()) {
            selectAllCheckbox.setValue(true);
            selectAllCheckbox.setIndeterminate(true);
          } else {
            selectAllCheckbox.setValue(false);
            selectAllCheckbox.setIndeterminate(false);
          }
        }
        notifyCheckboxClicked(checkboxClickListener, item, e.getValue());
        notifyCheckedCountChanged(checkedCountListener);
      });
    }
    return checkbox;
  }
}
