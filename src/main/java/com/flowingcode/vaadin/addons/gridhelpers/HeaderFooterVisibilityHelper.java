package com.flowingcode.vaadin.addons.gridhelpers;

import java.io.Serializable;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("serial")
@RequiredArgsConstructor
class HeaderFooterVisibilityHelper implements Serializable {

  private final GridHelper<?> helper;

  private static final String HIDE_HEADERS = "fcGh-hide-headers";

  private static final String HIDE_FOOTERS = "fcGh-hide-footers";

  private void setThemeName(String themeName, boolean set) {
    helper.getGrid().setThemeName(themeName, set);
  }

  private boolean hasThemeName(String themeName) {
    return helper.getGrid().hasThemeName(themeName);
  }

  public void setHeaderVisible(boolean visible) {
    setThemeName(HIDE_HEADERS, !visible);
  }

  public boolean isHeaderVisible() {
    return !hasThemeName(HIDE_HEADERS);
  }

  public void setFooterVisible(boolean visible) {
    setThemeName(HIDE_FOOTERS, !visible);
  }

  public boolean isFooterVisible() {
    return !hasThemeName(HIDE_FOOTERS);
  }

}
