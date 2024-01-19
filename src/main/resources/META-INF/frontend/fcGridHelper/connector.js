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

/* 
 * This file incorporates work licensed under the Apache License, Version 2.0
 * from Vaadin Cookbook https://github.com/vaadin/cookbook
 *  Copyright 2020-2022 Vaadin Ltd.
 */

import { Grid } from '@vaadin/grid/src/vaadin-grid.js';

(function () { 
  window.Vaadin.Flow.fcGridHelperConnector = {
    initLazy: grid => {
    
    	//https://cookbook.vaadin.com/grid-arrow-selection
    	grid.addEventListener('keyup', function(e) {
    		if (e.keyCode == 32) return;
    		if (!grid._fcghArrowSelection || grid._fcghEnhancedSelection) return;
    		if (grid.selectedItems){
    			grid.activeItem=grid.getEventContext(e).item;
    			grid.selectedItems=[grid.getEventContext(e).item];
    			grid.$server.select(grid.getEventContext(e).item.key);
    		}
    	});

		const __updateHorizontalScrollPosition = grid.__updateHorizontalScrollPosition.bind(grid); 
		grid.__updateHorizontalScrollPosition = function() {
			__updateHorizontalScrollPosition();
			this.querySelectorAll("[fcgh-footer]").forEach(footer=>{
				const slot = footer.closest('vaadin-grid-cell-content').assignedSlot;
				if (slot) {
					slot.parentElement.parentElement.style.transform = `translate(${this._scrollLeft}px, 0)`;
				}
			});
		}.bind(grid);

		grid.fcGridHelper = {
			setHeightByRows : function(n) {
				if (grid.fcGridHelper._heightByRowsObserver) {
					grid.fcGridHelper._heightByRowsObserver.unobserve(grid);
				}
				
				grid.fcGridHelper._heightByRowsObserver = new ResizeObserver(() => {
					var height = grid.fcGridHelper.computeHeightByRows(n);
					grid.style.setProperty('--height-by-rows',height+'px');
				});
				
				grid.fcGridHelper._heightByRowsObserver.observe(grid);
			},
			
			computeHeightByRows : function(n) {
				var height = 0;
				var rows = grid.shadowRoot.querySelectorAll("tbody tr");
				
				if (rows.length>0) {
					for(var i=0;i<n && i<rows.length;i++) {
						height += rows[i].offsetHeight;
					}
				
					if (rows.length<n) {
						height *= n/rows.length;
					}
				}
				
				height += grid.shadowRoot.querySelector("thead").offsetHeight;
				height += grid.shadowRoot.querySelector("tfoot").offsetHeight;
				
				var table = grid.shadowRoot.querySelector("table");
				
				var clientHeight = table.clientHeight;
				table.style.overflowX = 'hidden';
				height += table.clientHeight - clientHeight;
				table.style.overflowX = '';
				
				height += grid.offsetHeight-grid.clientHeight;
				return height;
			},

			_resizeObserver : new ResizeObserver((entries) => {
				const observer = grid.fcGridHelper._resizeObserver;
				for (const e of entries) {
					let width = e.contentBoxSize[0].inlineSize;
					width = observer.widths.findLast(step=>step<width);
					if (width!==observer.width) {
						observer.width = width;
						grid.dispatchEvent(new CustomEvent("fcgh-responsive-step", { detail: {step: width===undefined ? -1 : width} }));
					}
				 }
  			}),
  			
			_setResponsiveSteps : function(widths) {
				const observer = grid.fcGridHelper._resizeObserver;
				observer.widths=widths.sort((a, b) => a - b);
				observer.unobserve(grid);
				
				if (widths.length>0) {
					observer.observe(grid);		
				} else {
					observer.width=undefined;
					grid.dispatchEvent(new CustomEvent("fcgh-responsive-step", { detail: {step: -1} }));
				}
			}
			
		};
    }
	
  }
})();
