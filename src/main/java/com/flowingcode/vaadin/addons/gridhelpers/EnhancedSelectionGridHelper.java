
package com.flowingcode.vaadin.addons.gridhelpers;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.shared.Registration;
import java.io.Serializable;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;

/**
 * Add support for multiple items selection using click, arrow up/down, shift+click, shift+arrow
 * up/down, ctrl+click and ctrl+space.
 * 
 */
@SuppressWarnings("serial")
@RequiredArgsConstructor
class EnhancedSelectionGridHelper<T> implements Serializable {

  private static final String KEY_UP_EVENT_SHIFT_KEY = "event.shiftKey";

  private static final String KEY_UP_EVENT_ALT_KEY = "event.altKey";

  private static final String KEY_UP_EVENT_CTRL_KEY = "event.ctrlKey";

  private static final String KEY_UP_EVENT_META_KEY = "event.metaKey";

  private static final String KEY_UP_ELEMENT_FOCUSED_ITEM_INDEX = "element._focusedItemIndex";

  private static final String KEY_UP_EVENT_KEY = "event.key";

  private static final String LAST_FOCUSED_ITEM = "lastFocusedItem";

  private static final String SPACEBAR_KEY = " ";

  private final GridHelper<T> helper;

  private Registration itemClickRegistration;

  private Registration keyUpRegistration;

  void enableEnhancedSelection() {
    disableEnhancedSelection();
    clearTextSelection();

    Grid<T> grid = helper.getGrid();
    // disable text selection
    grid.getStyle().set("user-select", "none");

    itemClickRegistration = grid.addItemClickListener(ev -> {
      boolean isSpecialKey = ev.isCtrlKey() || ev.isMetaKey() || ev.isAltKey();
      T clickedItem = ev.getItem();
      Object lastClickedItem = ComponentUtil.getData(grid, LAST_FOCUSED_ITEM);

      if (ev.isShiftKey()) {
        grid.asMultiSelect().clear();
        if (lastClickedItem != null) {
          GridListDataView<T> dataView = grid.getListDataView();
          int index1 = dataView.getItems().collect(Collectors.toList()).indexOf(lastClickedItem);
          int index2 = dataView.getItems().collect(Collectors.toList()).indexOf(clickedItem);

          IntStream.rangeClosed(Math.min(index1, index2), Math.max(index1, index2))
              .forEach(i -> grid.select(dataView.getItem(i)));

          return;
        }
      } else if (isSpecialKey && !GridHelper.isSelectOnClick(grid)) {
        if (grid.asMultiSelect().isSelected(clickedItem)) {
          grid.deselect(clickedItem);
        } else {
          grid.select(clickedItem);
        }
      } else if (!GridHelper.isSelectOnClick(grid)) {
        grid.asMultiSelect().clear();
        grid.select(clickedItem);
      }
      ComponentUtil.setData(grid, LAST_FOCUSED_ITEM, clickedItem);
    });

    keyUpRegistration = grid.getElement().addEventListener("keyup", ev -> {
      String keyUp = ev.getEventData().getString(KEY_UP_EVENT_KEY);
      boolean arrowsKey = "ArrowDown".equals(keyUp) || "ArrowUp".equals(keyUp);

      GridListDataView<T> dataView = grid.getListDataView();

      Optional<T> newFocusedItemMaybe = Optional.empty();
      int newFocusedItemIndex =
          (int) ev.getEventData().getNumber(KEY_UP_ELEMENT_FOCUSED_ITEM_INDEX);
      if (newFocusedItemIndex >= 0) {
        newFocusedItemMaybe = Optional.ofNullable(dataView.getItem(newFocusedItemIndex));
      }

      if (newFocusedItemMaybe.isPresent()) {
        T newFocusedItem = newFocusedItemMaybe.get();
        boolean isSpecialKey = ev.getEventData().getBoolean(KEY_UP_EVENT_META_KEY)
            || ev.getEventData().getBoolean(KEY_UP_EVENT_CTRL_KEY)
            || ev.getEventData().getBoolean(KEY_UP_EVENT_ALT_KEY);

        Object lastFocusedItem = ComponentUtil.getData(grid, LAST_FOCUSED_ITEM);
        boolean shiftKey = ev.getEventData().getBoolean(KEY_UP_EVENT_SHIFT_KEY);
        if (shiftKey) {
          if (lastFocusedItem == null) {
            ComponentUtil.setData(grid, LAST_FOCUSED_ITEM, newFocusedItem);
          }

          if (arrowsKey) {
            grid.asMultiSelect().clear();
            int lastFocusedItemIndex =
                dataView.getItems().collect(Collectors.toList()).indexOf(lastFocusedItem);

            IntStream
                .rangeClosed(Math.min(lastFocusedItemIndex, newFocusedItemIndex),
                    Math.max(lastFocusedItemIndex, newFocusedItemIndex))
                .forEach(i -> grid.select(dataView.getItem(i)));

            return;
          }
        } else if (arrowsKey && !isSpecialKey) {
          grid.asMultiSelect().clear();
          grid.select(newFocusedItem);
          ComponentUtil.setData(grid, LAST_FOCUSED_ITEM, newFocusedItem);
        } else if (isSpecialKey && SPACEBAR_KEY.equals(keyUp)) {
          boolean isItemSelected = grid.asMultiSelect().isSelected(newFocusedItem);
          if (isItemSelected) {
            grid.deselect(newFocusedItem);
          } else {
            grid.select(newFocusedItem);
          }
          ComponentUtil.setData(grid, LAST_FOCUSED_ITEM, newFocusedItem);
        }
      }
    })
        .addEventData(KEY_UP_EVENT_META_KEY)
        .addEventData(KEY_UP_EVENT_CTRL_KEY)
        .addEventData(KEY_UP_EVENT_SHIFT_KEY)
        .addEventData(KEY_UP_EVENT_ALT_KEY)
        .addEventData(KEY_UP_EVENT_KEY)
        .addEventData(KEY_UP_ELEMENT_FOCUSED_ITEM_INDEX);

  }

  void disableEnhancedSelection() {
    if (itemClickRegistration != null) {
      itemClickRegistration.remove();
      itemClickRegistration = null;
    }
    if (keyUpRegistration != null) {
      keyUpRegistration.remove();
      keyUpRegistration = null;
    }
    // restore text selection
    helper.getGrid().getStyle().set("user-select", "auto");
  }

  boolean isEnhancedSelectionEnabled() {
    return itemClickRegistration != null && keyUpRegistration != null;
  }

  /**
   * Clear text selection.
   */
  private void clearTextSelection() {
    UI.getCurrent().getPage().executeJs(""
        + "const sel = window.getSelection ? window.getSelection() : document.selection;" +
        "  if (sel) {" +
        "    if (sel.removeAllRanges) {" +
        "      sel.removeAllRanges();" +
        "    } else if (sel.empty) {" +
        "      sel.empty();" +
        "    }" +
        "  }");
  }

}
