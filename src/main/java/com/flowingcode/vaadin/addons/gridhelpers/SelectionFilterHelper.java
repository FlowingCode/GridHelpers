package com.flowingcode.vaadin.addons.gridhelpers;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.function.SerializablePredicate;
import java.io.Serializable;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("serial")
@RequiredArgsConstructor
class SelectionFilterHelper<T> implements Serializable {

  private final GridHelper<T> helper;

  @Getter private SerializablePredicate<T> selectionFilter;

  public void setSelectionFilter(SerializablePredicate<T> predicate) {
    this.selectionFilter = predicate;
    if (predicate != null) {
      deselectIf(predicate.negate());
      helper.setHelperClassNameGenerator(
          this.getClass(), row -> predicate.test(row) ? null : "fcGh-noselect");
    } else {
      helper.setHelperClassNameGenerator(this.getClass(), null);
    }
  }

  boolean canSelect(T item) {
    return selectionFilter == null || selectionFilter.test(item);
  }

  private void deselectIf(SerializablePredicate<T> predicate) {
    Grid<T> grid = helper.getGrid();
    switch (GridHelper.getSelectionMode(grid)) {
      case MULTI:
        grid.asMultiSelect()
            .deselect(
                grid.asMultiSelect().getSelectedItems().stream()
                    .filter(predicate)
                    .collect(Collectors.toList()));
        break;
      case SINGLE:
        grid.asSingleSelect()
            .getOptionalValue()
            .filter(predicate)
            .ifPresent(x -> grid.asSingleSelect().clear());
        break;
      default:
        break;
    }
  }
}
