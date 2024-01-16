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
