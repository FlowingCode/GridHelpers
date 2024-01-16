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
