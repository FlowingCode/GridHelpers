(function(){
  window.Vaadin.Flow.fcGridHelperDemoConnector = {
	
	getViewportRowCount : grid => {
		//bisection method, f is monotonically increasing 
		var f= x=>grid.fcGridHelper.computeHeightByRows(x) - grid.clientHeight;
		var a=1,b=1;
		if (f(a)>=0) return 1;
		while (f(b)<0) fb=f(b+=b);
		var find = (a,b) => {
			var m=Math.ceil((a+b)/2), fm=f(m);
			if (fm==0 || b-a<=2) return (fm>0) ? m-1 : m;
			return fm>0 ? find(a,m) : find(m,b);
		} 
		return find(a,b);
	}		
		
  }
})();