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

/**
 * The modes for height calculation that are supported {@link GridHelper}.
 *
 * @see GridHelper#setHeightMode(HeightMode)
 */
public enum HeightMode {
  /**
   * 
   * The height of the Component is defined by a CSS-like value.
   */
  CSS,

  /**
   * The height of the Component is defined by a number of rows.
   */
  ROW;
}

