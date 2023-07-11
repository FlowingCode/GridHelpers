/*-
 * #%L
 * Grid Helpers Add-on
 * %%
 * Copyright (C) 2022 Flowing Code
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

import com.flowingcode.vaadin.addons.gridhelpers.CheckboxColumn.CheckboxColumnConfiguration;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.data.provider.BackEndDataProvider;
import java.io.Serializable;
import lombok.RequiredArgsConstructor;

/**
 * Helper class to create a grid column where boolean value is rendered as a {@link Checkbox}.
 * <p/>
 * Note that using this helper with a {@link BackEndDataProvider} could lead to performance
 * penalties as the total amount of items must be fetched from backend.
 * 
 * @param <T>
 */
@SuppressWarnings("serial")
@RequiredArgsConstructor
final class CheckboxColumnGridHelper<T> implements Serializable {

  private final GridHelper<T> helper;

  /**
   * Creates a {@link CheckboxColumn} using a given configuration.
   * 
   * @param config configuration to apply.
   * @return the created {@link CheckboxColumn}
   */
  public CheckboxColumn<T> addCheckboxColumn(
      CheckboxColumnConfiguration<T> config) {
    return new CheckboxColumn<>(helper.getGrid(), config);
  }
}
