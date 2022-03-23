package com.flowingcode.vaadin.addons.gridhelpers;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.shared.Registration;
import java.io.Serializable;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("serial")
@RequiredArgsConstructor
class EmptyLabelGridHelper implements Serializable {

  private final GridHelper<?> helper;

  private Component emptyLabel;
  private Registration registration;

  // - Show a meaningful message instead of an empty Grid
  // https://cookbook.vaadin.com/grid-message-when-empty
  void setEmptyGridLabel(Component component) {
    Grid<?> grid = helper.getGrid();

    if (emptyLabel != null) {
      emptyLabel.getElement().removeFromParent();
      emptyLabel = null;
    }

    if (registration != null) {
      registration.remove();
      registration = null;
    }

    if (component != null) {
      emptyLabel = component;
      registration = grid.getDataProvider().addDataProviderListener(ev -> {
        component.setVisible(grid.getDataProvider().size(new Query<>()) == 0);
      });
    }
  }

  Component getEmptyGridLabel() {
    return emptyLabel;
  }

}
