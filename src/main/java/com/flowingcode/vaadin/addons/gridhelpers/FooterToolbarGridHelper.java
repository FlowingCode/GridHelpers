package com.flowingcode.vaadin.addons.gridhelpers;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.FooterRow.FooterCell;
import com.vaadin.flow.component.grid.Grid;
import java.io.Serializable;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("serial")
@RequiredArgsConstructor
class FooterToolbarGridHelper implements Serializable {

  private final GridHelper<?> helper;

  private FooterCell footerCell;

  public void setFooterToolbar(Component toolBar) {
    Grid<?> grid = helper.getGrid();
    if (grid.getFooterRows().isEmpty()) {
      // create a fake footer and hide it (workaround:
      // https://github.com/vaadin/flow-components/issues/1558#issuecomment-987783794)
      grid.appendFooterRow();
      grid.getElement().getThemeList().add("hide-first-footer");
    }
    if (footerCell == null) {
      FooterRow fr = grid.appendFooterRow();
      footerCell = fr.join(grid.getColumns().toArray(new Grid.Column[0]));
    }
    toolBar.getElement().setAttribute("fcGh-footer", true);
    footerCell.setComponent(toolBar);
  }

}
