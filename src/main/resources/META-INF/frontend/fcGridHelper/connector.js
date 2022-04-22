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

/* 
 * This file incorporates work licensed under the Apache License, Version 2.0
 * from Vaadin Cookbook https://github.com/vaadin/cookbook
 *  Copyright 2020-2022 Vaadin Ltd.
 */
 
/* 
 * This file incorporates work licensed under the Apache License, Version 2.0
 * from Selection Grid https://vaadin.com/directory/component/selection-grid
 *  Copyright 2020 Vaadin Ltd.
 */

import { Grid } from '@vaadin/grid/src/vaadin-grid.js';

import {
    _selectionGridSelectRow,
    _selectionGridSelectRowWithItem
} from '@vaadin/flow-frontend/src/helpers.js';

(function () { 
  window.Vaadin.Flow.fcGridHelperConnector = {
    initLazy: grid => {
    	
    	if (!grid.$server.selectRange) {
    		grid.$server.selectRange = (fromIndex, toIndex) => grid.dispatchEvent(new CustomEvent('fc-selectRange', {detail: {fromIndex, toIndex}})); 
    	}
    
    	if (!grid.$server.selectRangeOnly) {
			grid.$server.selectRangeOnly = (fromIndex, toIndex) => grid.dispatchEvent(new CustomEvent('fc-selectRangeOnly', {detail: {fromIndex, toIndex}}));
    	}
    	
        if (!window.Vaadin.Flow.fcGridHelperConnector.gridConnectorOverriden) {
          let gridConnectorInitLazy = window.Vaadin.Flow.gridConnector.initLazy;
          window.Vaadin.Flow.gridConnector.initLazy = grid => {
            gridConnectorInitLazy(grid);
            let setSelectionMode = grid.$connector.setSelectionMode;
            grid.$connector.setSelectionMode = selectionMode => {
              setSelectionMode(selectionMode);
              grid.__fcSelectionMode=selectionMode;
            }; 
          };
          window.Vaadin.Flow.fcGridHelperConnector.gridConnectorOverriden=true;
        }
        
        grid._selectionGridSelectRow = _selectionGridSelectRow.bind(grid);
        grid._selectionGridSelectRowWithItem = _selectionGridSelectRowWithItem.bind(grid);
        
    	//https://cookbook.vaadin.com/grid-arrow-selection
    	grid.addEventListener('keyup', function(e) {
    		if (grid.__fcSelectionMode=='NONE') return;
    		
    		if (grid.__fcSelectionMode=='SINGLE') {
    			if (e.keyCode == 32) return;
    			if (!grid._fcghArrowSelection) return;
    			if (grid.activeItem===grid.getEventContext(e).item) return;
    		
    			grid.activeItem=grid.getEventContext(e).item;
    			grid.selectedItems=[grid.getEventContext(e).item];
    			grid.$server.select(grid.getEventContext(e).item.key);
    		}
    	});
    	
    	const oldClickHandler = grid._onClick.bind(grid);
        grid._onClick = function _click(e) {
            oldClickHandler(e);
            
            if (!grid._fcghArrowSelection) return;
            if (grid.__fcSelectionMode!='MULTI' ) return;
            this._selectionGridSelectRow(e);
        }.bind(grid);
		
        const old_onNavigationKeyDown = grid._onNavigationKeyDown.bind(grid);
        grid._onNavigationKeyDown = function _onNavigationKeyDownOverridden(e, key) {
            old_onNavigationKeyDown(e,key);
            if (!grid._fcghArrowSelection) return;
            if (grid.__fcSelectionMode!='MULTI' ) return;
            
            const ctrlKey = (e.metaKey)?e.metaKey:e.ctrlKey;
            if (e.shiftKey || !ctrlKey) {
                // select on shift down on shift up
                if (key === 'ArrowDown' || key === 'ArrowUp') {
                    const row = Array.from(this.$.items.children).filter(
                        (child) => child.index === this._focusedItemIndex
                    )[0];
                    if (row && typeof row.index != 'undefined') {
                        this._selectionGridSelectRowWithItem(e, row._item, row.index);
                    }
                }
            }
        }.bind(grid);

        const old_onSpaceKeyDown = Grid.prototype._onSpaceKeyDown.bind(grid);
        grid._onSpaceKeyDown = function _onSpaceKeyDownOverriden(e) {
            old_onSpaceKeyDown(e);
            if (!grid._fcghArrowSelection) return;
            if (grid.__fcSelectionMode!='MULTI' ) return;
            
            const tr = e.composedPath().find((p) => p.nodeName === "TR");
            if (tr && typeof tr.index != 'undefined') {
                const item = tr._item;
                const index = tr.index;
                if (this.selectedItems && this.selectedItems.some((i) => i.key === item.key)) {
                    if (this.$connector) {
                        this.$connector.doDeselection([item], true);
                    } else {
                        this.deselectItem(item);
                    }
                } else {
                    if (this.$server) {
                        this.$server.selectRangeOnly(index, index);
                    } else {
                        this.selectedItems = [];
                        this.selectItem(item);
                    }
                }
            }
        }.bind(grid);
        
    }
  }
})();
