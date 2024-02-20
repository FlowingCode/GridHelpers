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

import '@vaadin/grid/vaadin-grid-column.js';
import { GridColumn } from '@vaadin/grid/src/vaadin-grid-column.js';
import { GridRadioSelectionColumnBaseMixin } from './grid-radio-selection-column-base-mixin.js';

class GridFlowRadioSelectionColumn extends GridRadioSelectionColumnBaseMixin(GridColumn) {

  static get is() {
    return 'grid-flow-radio-selection-column';
  }

  static get properties() {
    return {
      /**
       * Override property to enable auto-width
       */
      autoWidth: {
        type: Boolean,
        value: true
      },

      /**
       * Override property to set custom width
       */
      width: {
        type: String,
        value: '56px'
      },
      
      /**
       * The previous state of activeItem. When activeItem turns to `null`,
       * previousActiveItem will have an Object with just unselected activeItem
       * @private
       */
      __previousActiveItem: Object,
    };
  }


  constructor() {
    super();
    this.__boundOnActiveItemChanged = this.__onActiveItemChanged.bind(this);
  }

  /** @protected */
  disconnectedCallback() {
    this._grid.removeEventListener('active-item-changed', this.__boundOnActiveItemChanged);
    super.disconnectedCallback();
  }

  /** @protected */
  connectedCallback() {
    super.connectedCallback();
    if (this._grid) {
      this._grid.addEventListener('active-item-changed', this.__boundOnActiveItemChanged);
    }
  }
    
  /**
   * Override a method from `GridRadioSelectionColumnBaseMixin` to handle the user
   * selecting an item.
   *
   * @param {Object} item the item to select
   * @protected
   * @override
   */
  _selectItem(item) {
    this._grid.$connector.doSelection([item], true);
  }

  /**
   * Override a method from `GridRadioSelectionColumnBaseMixin` to handle the user
   * deselecting an item.
   *
   * @param {Object} item the item to deselect
   * @protected
   * @override
   */
  _deselectItem(item) {
    this._grid.$connector.doDeselection([item], true);
  }
  
  
  /** @private */
  __onActiveItemChanged(e) {
    const activeItem = e.detail.value;
    if (activeItem) {
      if(this.__previousActiveItem !== activeItem) {
	    this._deselectItem(this.__previousActiveItem);
	  }      
      this._selectItem(activeItem);
      this.__previousActiveItem = activeItem;
    } else {
	  this.__previousActiveItem = undefined;
    }
  }

  /**
   * Override a method from `GridRadioSelectionColumnBaseMixin` to handle the user clicked on an item.
   *
   * @param {Object} item the clicked item
   * @protected
   * @override
   */
  _onItemClicked(item) {
    super._onItemClicked(item);
    this.__previousActiveItem = item;
  }

}

customElements.define(GridFlowRadioSelectionColumn.is, GridFlowRadioSelectionColumn);
