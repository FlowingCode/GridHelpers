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

import '@vaadin/radio-group/vaadin-radio-button.js';
import { defineCustomElement } from '@vaadin/component-base/src/define.js';
import { GridColumn } from './vaadin-grid-column.js';
import { GridRadioSelectionColumnMixin } from './grid-radio-selection-column-mixin.js';

/**
 * `<grid-radio-selection-column>` is a helper element for the `<vaadin-grid>`
 * that provides default renderers and functionality for item selection.
 *
 * #### Example:
 * ```html
 * <vaadin-grid items="[[items]]">
 *  <grid-radio-selection-column frozen auto-select></grid-radio-selection-column>
 *
 *  <vaadin-grid-column>
 *    ...
 * ```
 *
 * By default the selection column displays `<vaadin-radio-button>` elements in the
 * column cells. The radio button in the body rows toggle selection of the corresponding row item.
 *
 * __The default content can also be overridden__
 *
 * @customElement
 * @extends GridColumn
 * @mixes GridSelectionColumnMixin
 */
class GridRadioSelectionColumn extends GridRadioSelectionColumnMixin(GridColumn) {
  static get is() {
    return 'grid-radio-selection-column';
  }
}

defineCustomElement(GridRadioSelectionColumn);

export { GridRadioSelectionColumn };
