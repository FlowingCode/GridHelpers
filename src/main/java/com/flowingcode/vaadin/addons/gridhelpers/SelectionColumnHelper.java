package com.flowingcode.vaadin.addons.gridhelpers;

import java.io.Serializable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("serial")
@RequiredArgsConstructor
class SelectionColumnHelper implements Serializable {

  private final GridHelper<?> helper;

  @Getter
  private boolean selectionColumnHidden;

  @Getter
  private boolean selectionColumnFrozen;

  public void setSelectionColumnHidden(boolean value) {
    // https://cookbook.vaadin.com/grid-multiselect-no-selectcolumn
    selectionColumnHidden = value;
    helper.getGrid().getElement().executeJs(
        "this.getElementsByTagName('vaadin-grid-flow-selection-column')[0].hidden = $0;", value);
  }

  public void setSelectionColumnFrozen(boolean value) {
    // https://cookbook.vaadin.com/grid-frozen-selection-column
    selectionColumnFrozen = value;
    helper.getGrid().getElement()
        .executeJs("this.querySelector('vaadin-grid-flow-selection-column').frozen = $0", value);
  }

  void onAttach() {
    if (isSelectionColumnFrozen()) {
      setSelectionColumnFrozen(true);
    }

    if (isSelectionColumnHidden()) {
      setSelectionColumnHidden(true);
    }
  }

}
