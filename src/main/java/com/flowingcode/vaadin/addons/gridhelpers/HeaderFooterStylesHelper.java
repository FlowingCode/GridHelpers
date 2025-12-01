/*-
 * #%L
 * Grid Helpers Add-on
 * %%
 * Copyright (C) 2022 - 2025 Flowing Code
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

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.FooterRow.FooterCell;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.HeaderRow.HeaderCell;
import com.vaadin.flow.dom.ClassList;
import com.vaadin.flow.dom.Element;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.Delegate;

@SuppressWarnings("serial")
@RequiredArgsConstructor
class HeaderFooterStylesHelper implements Serializable {

  private final GridHelper<?> helper;

  private final Map<Object, GridStylesHelper> styles = new HashMap<>();

  @FunctionalInterface
  private interface SelectorSupplier extends Serializable {
    Serializable[] getSelector();
  }


  @RequiredArgsConstructor
  private abstract class RowSelector<ROW> implements SelectorSupplier {

    protected abstract List<ROW> getRows();

    protected abstract String getSelectorTemplate();

    protected abstract ROW getRow();

    public int getRowIndex() {
      int i = -1;
      for (ROW row : getRows()) {
        ++i;
        if (row == getRow()) {
          break;
        }
      }
      return i;
    }

    @Override
    public final Serializable[] getSelector() {
      int i = getRowIndex();
      return i < 0 ? null : new Serializable[] {getSelectorTemplate(), i};
    }

  }


  @RequiredArgsConstructor
  private final class HeaderRowSelector extends RowSelector<HeaderRow> {

    @Getter
    final HeaderRow row;

    @Override
    protected List<HeaderRow> getRows() {
      return helper.getGrid().getHeaderRows();
    }

    @Override
    protected String getSelectorTemplate() {
      return "this.shadowRoot.querySelectorAll('thead tr:not([hidden])')[i]";
    }

  }


  @RequiredArgsConstructor
  private final class FooterRowSelector extends RowSelector<FooterRow> {

    @Getter
    private final FooterRow row;

    @Override
    protected List<FooterRow> getRows() {
      return helper.getGrid().getFooterRows();
    }

    @Override
    protected String getSelectorTemplate() {
      return "this.shadowRoot.querySelectorAll('tfoot tr:not([hidden])')[i]";
    }

  }

  private static final Method AbstractCell_getColumn;

  static {
    Method method = null;
    try {
      Class<?> AbstractCell =
          Class.forName("com.vaadin.flow.component.grid.AbstractRow$AbstractCell");
      method = AbstractCell.getDeclaredMethod("getColumn");
      method.setAccessible(true);
    } catch (ClassNotFoundException | NoSuchMethodException e) {
      // Will use cell identity; keep field null.
    }

    AbstractCell_getColumn = method;
  }

  private abstract class CellSelector<ROW, CELL> implements SelectorSupplier {

    @Getter
    // AbstractColumn (or CELL if reflection is not available)
    private final Object column;

    CellSelector(CELL cell) {
      column = Objects.requireNonNull(getColumn(cell));
    }

    @SneakyThrows
    protected final Object getColumn(CELL cell) {
      if (AbstractCell_getColumn != null) {
        return AbstractCell_getColumn.invoke(cell);
      } else {
        return cell;
      }
    }

    protected abstract RowSelector<ROW> getRowSelector();

    protected abstract CELL getCell(ROW row, Column<?> c);

    private int getColumnIndex() {
      ROW row = getRowSelector().getRow();
      int j = -1;

      Object last = null;
      Object target = getColumn();
      for (Column<?> c : helper.getGrid().getColumns()) {
        if (c.isVisible()) {
          Object curr = getColumn(getCell(row, c));
          if (curr != last) {
            ++j;
            last = curr;
            if (curr == target) {
              return j;
            }
          }
        }
      }
      return -1;
    }

    @Override
    public final Serializable[] getSelector() {
      int i = getRowSelector().getRowIndex();
      int j = getColumnIndex();
      return j < 0 ? null : new Serializable[] {getSelectorTemplate(), i, j + 1};
    }

    protected String getSelectorTemplate() {
      return getRowSelector().getSelectorTemplate() + ".querySelector(`th:nth-child(${j})`)";
    }

  }


  private final class HeaderCellSelector extends CellSelector<HeaderRow, HeaderCell> {

    @Getter
    final HeaderRowSelector rowSelector;

    public HeaderCellSelector(HeaderCell cell) {
      super(cell);
      for (HeaderRow row : helper.getGrid().getHeaderRows()) {
        if (row.getCells().contains(cell)) {
          rowSelector = new HeaderRowSelector(row);
          return;
        }
      }
      throw new IllegalArgumentException();
    }

    @Override
    protected HeaderCell getCell(HeaderRow row, Column<?> c) {
      return row.getCell(c);
    }

  }


  private final class FooterCellSelector extends CellSelector<FooterRow, FooterCell> {

    @Getter
    final FooterRowSelector rowSelector;

    public FooterCellSelector(FooterCell cell) {
      super(cell);
      for (FooterRow row : helper.getGrid().getFooterRows()) {
        if (row.getCells().contains(cell)) {
          rowSelector = new FooterRowSelector(row);
          return;
        }
      }
      throw new IllegalArgumentException();
    }

    @Override
    protected FooterCell getCell(FooterRow row, Column<?> c) {
      return row.getCell(c);
    }

  }

  private final class ClassListImpl extends AbstractSet<String> implements ClassList {

    final List<String> classes = new ArrayList<>();

    final SelectorSupplier selectorSupplier;

    private ClassListImpl(SelectorSupplier selectorSupplier) {
      this.selectorSupplier = selectorSupplier;

      Grid<?> grid = helper.getGrid();
      grid.addAttachListener(ev -> {
        if (!classes.isEmpty()) {
          String classNames = classes.stream().collect(Collectors.joining(" "));
          executeJs("className=$0", classNames, true);
        }
      });
    }

    private void validateClassName(String className) {
      for (int i = 0; i < className.length(); i++) {
        if (Character.isWhitespace(className.charAt(i))) {
          throw new IllegalArgumentException(
              "The classname provided contains HTML space characters, which are not valid in tokens");
        }
      }
    }

    @Override
    public boolean add(String className) {
      validateClassName(className);
      if (classes.add(className)) {
        beforeResponse("classList.add($0)", className);
        return true;
      }

      return false;
    }

    private void beforeResponse(String expression, String parameter) {
      Grid<?> grid = helper.getGrid();
      grid.getUI().ifPresent(ui -> {
        ui.beforeClientResponse(grid, ctx -> {
          executeJs(expression, parameter, false);
        });
      });
    }

    private void executeJs(String expression, String parameter, boolean deferred) {

      Serializable[] selector = selectorSupplier.getSelector();
      if (selector != null) {
        expression = selector[0] + "." + expression;
        expression = "var i=$1, j=$2, h;"
            + "if ((h=this.getElementsByTagName('vaadin-grid-flow-selection-column')[0]) && !h.hidden) ++j;"
            + expression;

        if (deferred) {
          expression = String.format("setTimeout(()=>{%s})", expression);
        }

        selector[0] = parameter;
        helper.getGrid().getElement().executeJs(expression, selector);
      }
    }

    @Override
    public int size() {
      return classes.size();
    }

    @Override
    public Iterator<String> iterator() {
      return new WrappedIterator(classes.iterator()) {
        @Override
        protected void onRemove(String className) {
          beforeResponse("classList.remove($0)", className);
        }
      };
    }
  };

  @RequiredArgsConstructor
  private static class WrappedIterator implements Iterator<String> {

    private final Iterator<String> it;

    private String last;

    protected void onRemove(String last) {}

    @Override
    public boolean hasNext() {
      return it.hasNext();
    }

    @Override
    public String next() {
      return last = it.next();
    }

    @Override
    public void remove() {
      it.remove();
      onRemove(last);
    }
  }

  @RequiredArgsConstructor
  private final class GridStylesHelperImpl implements GridStylesHelper {
    @Delegate(types = GridStylesHelper.class)
    final HasStyle classList;
  }

  private final class HasStyleImpl implements HasStyle {
    private final ClassList classList;

    HasStyleImpl(HeaderRow row) {
      this(new HeaderRowSelector(row));
    }

    HasStyleImpl(FooterRow row) {
      this(new FooterRowSelector(row));
    }

    HasStyleImpl(HeaderCell cell) {
      this(new HeaderCellSelector(cell));
    }

    HasStyleImpl(FooterCell cell) {
      this(new FooterCellSelector(cell));
    }

    private HasStyleImpl(SelectorSupplier selectorSupplier) {
      classList = new ClassListImpl(selectorSupplier);
    }

    @Override
    public Element getElement() {
      throw new UnsupportedOperationException();
    }

    @Override
    public ClassList getClassNames() {
      return classList;
    }

    @Override
    public void setClassName(String className) {
      classList.clear();
      addClassName(className);
    }

    @Override
    public String getClassName() {
      return StringUtils.trimToNull(classList.stream().collect(Collectors.joining(" ")));
    }
  }

  GridStylesHelper getStyles(HeaderRow row) {
    Grid<?> grid = helper.getGrid();
    if (!grid.getHeaderRows().contains(row)) {
      throw new IllegalArgumentException("HeaderRow does not belong to Grid");
    }
    return styles.computeIfAbsent(row, x -> new GridStylesHelperImpl(new HasStyleImpl(row)));
  }

  GridStylesHelper getStyles(FooterRow row) {
    Grid<?> grid = helper.getGrid();
    if (!grid.getFooterRows().contains(row)) {
      throw new IllegalArgumentException("FooterRow does not belong to Grid");
    }
    return styles.computeIfAbsent(row, x -> new GridStylesHelperImpl(new HasStyleImpl(row)));
  }

  GridStylesHelper getStyles(HeaderCell cell) {
    Grid<?> grid = helper.getGrid();
    if (grid.getHeaderRows().stream().noneMatch(row -> row.getCells().contains(cell))) {
      throw new IllegalArgumentException("HeaderCell does not belong to Grid");
    }
    return styles.computeIfAbsent(cell, x -> new GridStylesHelperImpl(new HasStyleImpl(cell)));
  }

  GridStylesHelper getStyles(FooterCell cell) {
    Grid<?> grid = helper.getGrid();
    if (grid.getFooterRows().stream().noneMatch(row -> row.getCells().contains(cell))) {
      throw new IllegalArgumentException("FooterCell does not belong to Grid");
    }
    return styles.computeIfAbsent(cell, x -> new GridStylesHelperImpl(new HasStyleImpl(cell)));
  }

}
