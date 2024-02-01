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

import { addListener } from '@vaadin/component-base/src/gestures.js';

/**
 * A mixin that provides basic functionality for the
 * `<grid-radio-selection-column>`. This includes properties, cell rendering,
 * and overridable methods for handling changes to the selection state.
 *
 * **NOTE**: This mixin is re-used by the Flow component, and as such must not
 * implement any selection state updates for the column element or the grid.
 * Web component-specific selection state updates must be implemented in the
 * `<grid-radio-selection-column>` itself, by overriding the protected methods
 * provided by this mixin.
 *
 * @polymerMixin
 */
export const GridRadioSelectionColumnBaseMixin = (superClass) =>
  class GriRadioSelectionColumnBaseMixin extends superClass {
    static get properties() {
      return {
        /**
         * Width of the cells for this column.
         */
        width: {
          type: String,
          value: '58px',
        },

        /**
         * Flex grow ratio for the cell widths. When set to 0, cell width is fixed.
         * @attr {number} flex-grow
         * @type {number}
         */
        flexGrow: {
          type: Number,
          value: 0,
        },

        /**
         * When true, the active gets automatically selected.
         * @attr {boolean} auto-select
         * @type {boolean}
         */
        autoSelect: {
          type: Boolean,
          value: false,
        },

      };
    }

    static get observers() {
      return [
        '_onHeaderRendererOrBindingChanged(path)',
      ];
    }

    /**
     * Renders the Select Row radio button to the body cell.
     *
     * @override
     */
    _defaultRenderer(root, _column, { item, selected }) {
      let radioButton = root.firstElementChild;
      if (!radioButton) {
        radioButton = document.createElement('vaadin-radio-button');
        radioButton.setAttribute('aria-label', 'Select Row');
        root.appendChild(radioButton);
        // Add listener after appending, so we can skip the initial change event
        radioButton.addEventListener('checked-changed', this.__onSelectRowCheckedChanged.bind(this));
      }

      radioButton.__item = item;
      radioButton.__rendererChecked = selected;
      radioButton.checked = selected;
    }

    /**
     * Selects the row when the Select Row radio button is clicked.
     * The listener handles only user-fired events.
     *
     * @private
     */
    __onSelectRowCheckedChanged(e) {
      // Skip if the state is changed by the renderer.
      if (e.target.checked === e.target.__rendererChecked) {
        return;
      }
      
      this._onItemClicked(e.target.__item);
    }

    _onItemClicked(item){
        this._selectItem(item);
    }

  };
