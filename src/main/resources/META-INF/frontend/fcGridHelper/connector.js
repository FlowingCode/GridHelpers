import { Grid } from '@vaadin/grid/src/vaadin-grid.js';

(function () { 
  window.Vaadin.Flow.fcGridHelperConnector = {
    initLazy: grid => {
    	//https://cookbook.vaadin.com/grid-arrow-selection
    	grid.addEventListener('keyup', function(e) {
    		if (e.keyCode == 32) return;
    		if (!grid._fcghArrowSelection) return;
    		if (grid.selectedItems){
    			grid.activeItem=grid.getEventContext(e).item;
    			grid.selectedItems=[grid.getEventContext(e).item];
    			grid.$server.select(grid.getEventContext(e).item.key);
    		}
    	});
    }
  }
})();