
package com.flowingcode.vaadin.addons.gridhelpers;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridLazyDataView;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.data.provider.BackEndDataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.shared.Registration;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Optional;
import java.util.stream.IntStream;

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
          GridListDataView<T> listDataView = null;
          GridLazyDataView<T> lazyDataView = null;
          if (grid.getDataProvider() instanceof ListDataProvider) {
            listDataView = grid.getListDataView();
          } else if(grid.getDataProvider() instanceof BackEndDataProvider) {
            lazyDataView = grid.getLazyDataView();
          }

          if(listDataView != null) {
            int index1 = listDataView.getItems().toList().indexOf(lastClickedItem);
            int index2 = listDataView.getItems().toList().indexOf(clickedItem);
            GridListDataView<T> finalListDataView = listDataView;
            IntStream.rangeClosed(Math.min(index1, index2), Math.max(index1, index2))
                    .forEach(i -> grid.select(finalListDataView.getItem(i)));
          } else if(lazyDataView != null) {
            int index1 = lazyDataView.getItems().toList().indexOf(lastClickedItem);
            int index2 = lazyDataView.getItems().toList().indexOf(clickedItem);
            GridLazyDataView<T> finalLazyDataView = lazyDataView;
            IntStream.rangeClosed(Math.min(index1, index2), Math.max(index1, index2))
                    .forEach(i -> grid.select(finalLazyDataView.getItem(i)));
          }
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
              GridListDataView<T> listDataView = null;
              GridLazyDataView<T> lazyDataView = null;
              if (grid.getDataProvider() instanceof ListDataProvider) {
                listDataView = grid.getListDataView();
              } else if(grid.getDataProvider() instanceof BackEndDataProvider) {
                lazyDataView = grid.getLazyDataView();
              }

              Optional<T> newFocusedItemMaybe = Optional.empty();
              int newFocusedItemIndex = (int) ev.getEventData().getNumber(KEY_UP_ELEMENT_FOCUSED_ITEM_INDEX);
              if (newFocusedItemIndex >= 0) {
                if (listDataView != null) {
                  newFocusedItemMaybe = Optional.ofNullable(listDataView.getItems().toList().get(newFocusedItemIndex));
                } else if(lazyDataView != null) {
                  newFocusedItemMaybe = Optional.ofNullable(lazyDataView.getItems().toList().get(newFocusedItemIndex));
                }
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
                    int lastFocusedItemIndex;
                    if (listDataView != null) {
                      lastFocusedItemIndex = listDataView.getItems().toList().indexOf(lastFocusedItem);
                      GridListDataView<T> finalListDataView = listDataView;
                      IntStream.rangeClosed(Math.min(lastFocusedItemIndex, newFocusedItemIndex),
                                      Math.max(lastFocusedItemIndex, newFocusedItemIndex))
                              .forEach(i -> grid.select(finalListDataView.getItem(i)));
                    } else {

                      lastFocusedItemIndex = lazyDataView.getItems().toList().indexOf(lastFocusedItem);
                      GridLazyDataView<T> finalLazyDataView = lazyDataView;
                      IntStream.rangeClosed(Math.min(lastFocusedItemIndex, newFocusedItemIndex),
                                      Math.max(lastFocusedItemIndex, newFocusedItemIndex))
                              .forEach(i -> grid.select(finalLazyDataView.getItem(i)));
                    }
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
