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

import { GridRadioSelectionColumnBaseMixin } from './grid-radio-selection-column-base-mixin.js';

/**
 * @polymerMixin
 * @mixes GridSelectionColumnBaseMixin
 */
export const GridRadioSelectionColumnMixin = (superClass) =>
  class extends GridRadioSelectionColumnBaseMixin(superClass) {
    static get properties() {
      return {
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
      this.__boundOnSelectedItemsChanged = this.__onSelectedItemsChanged.bind(this);
    }

    /** @protected */
    disconnectedCallback() {
      this._grid.removeEventListener('active-item-changed', this.__boundOnActiveItemChanged);
      this._grid.removeEventListener('selected-items-changed', this.__boundOnSelectedItemsChanged);
      super.disconnectedCallback();
    }

    /** @protected */
    connectedCallback() {
      super.connectedCallback();
      if (this._grid) {
        this._grid.addEventListener('active-item-changed', this.__boundOnActiveItemChanged);
        this._grid.addEventListener('selected-items-changed', this.__boundOnSelectedItemsChanged);
      }
    }
   
    /**
     * Override a method from `GridSelectionColumnBaseMixin` to handle the user
     * selecting an item.
     *
     * @param {Object} item the item to select
     * @protected
     * @override
     */
    _selectItem(item) {
      this._grid.selectItem(item);
    }

    /**
     * Override a method from `GridSelectionColumnBaseMixin` to handle the user
     * deselecting an item.
     *
     * @param {Object} item the item to deselect
     * @protected
     * @override
     */
    _deselectItem(item) {
      this._grid.deselectItem(item);
    }

    /** @private */
    __onActiveItemChanged(e) {
      const activeItem = e.detail.value;
      this.__previousSelectionCleared = false;
      if (this.autoSelect && activeItem) {
          this._selectItem(activeItem);
          this.__previousActiveItem = activeItem;
      }
    }

    /** @private */
    __onSelectedItemsChanged(e) {     
     if(!this.__previousSelectionCleared && this.__previousActiveItem){
          this.__previousSelectionCleared = true;
          this._deselectItem(this.__previousActiveItem);          
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
      this.__previousSelectionCleared = false;      
      super._onItemClicked(item);
      this.__previousActiveItem = item;
    }

  };
