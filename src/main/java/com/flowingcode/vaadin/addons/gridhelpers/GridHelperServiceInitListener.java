/*-
 * #%L
 * Grid Helpers Add-on
 * %%
 * Copyright (C) 2022 - 2026 Flowing Code
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

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;

/**
 * Ensures that the addon's grid styles are always included in the production bundle.
 *
 * <p>{@code VaadinServiceInitListener} subtypes are discovered via ClassGraph reflection and are
 * always treated as entry points, regardless of how consumers use the addon. Placing the
 * {@code @CssImport} annotation here guarantees that the grid styles are included even when
 * {@link GridHelper} is not reachable through bytecode analysis.
 *
 * @see <a href="https://github.com/FlowingCode/GridHelpers/issues/171">Issue #171</a>
 */
@CssImport(value = GridHelper.GRID_STYLES, themeFor = "vaadin-grid")
public class GridHelperServiceInitListener implements VaadinServiceInitListener {

  @Override
  public void serviceInit(ServiceInitEvent event) {
    // No initialization needed; this class exists solely to anchor the
    // @CssImport annotation so it is always picked up during the production
    // bundle build via ClassGraph entry-point seeding.
  }
}
