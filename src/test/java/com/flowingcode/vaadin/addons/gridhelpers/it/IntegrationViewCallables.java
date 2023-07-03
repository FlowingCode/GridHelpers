package com.flowingcode.vaadin.addons.gridhelpers.it;

import com.flowingcode.vaadin.testbench.rpc.JsonArrayList;
import com.flowingcode.vaadin.testbench.rpc.RmiCallable;
import com.vaadin.flow.component.grid.Grid.SelectionMode;

public interface IntegrationViewCallables extends RmiCallable {

    void setColumnToggleVisible(boolean value);

    void setSelectOnClick(boolean value);

    void setSelectionMode(SelectionMode selectionMode);

    JsonArrayList<Integer> getSelectedRows();

    Integer getToggledColumn();

    void setHidable(int columnIndex, boolean hidable);

    void setSelectionFilterEnabled(boolean enabled);

    void setColumnHeader(int i, String header);

    void setHidingToggleCaption(int i, String header);

    void setEmptyGridLabel(String label);

    void setSelectionColumnHidden(boolean b);

    void setSelectionColumnFrozen(boolean b);

    void setArrowSelectionEnabled(boolean b);

    void addToolbarFooter(String footer);

    void removeAllItems();

    void setHeaderVisible(boolean visible);

    void setFooterVisible(boolean visible);

}
