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

import { Grid } from '@vaadin/grid/src/vaadin-grid.js';

(function () { 
  window.Vaadin.Flow.fcGridHelperConnector = {
    initLazy: grid => {
    
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
        
    	//https://cookbook.vaadin.com/grid-arrow-selection
    	grid.addEventListener('keyup', function(e) {
    		if (e.keyCode == 32) return;
    		if (!grid._fcghArrowSelection) return;
    		if (grid.activeItem===grid.getEventContext(e).item) return;

    		if (grid.selectedItems){
    			grid.activeItem=grid.getEventContext(e).item;
    			grid.selectedItems=[grid.getEventContext(e).item];
    			grid.$server.select(grid.getEventContext(e).item.key);
    		}
    	});
    }
  }
})();
