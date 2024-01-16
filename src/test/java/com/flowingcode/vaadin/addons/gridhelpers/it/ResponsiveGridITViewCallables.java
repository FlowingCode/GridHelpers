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
package com.flowingcode.vaadin.addons.gridhelpers.it;

import com.flowingcode.vaadin.testbench.rpc.RmiCallable;
import com.flowingcode.vaadin.testbench.rpc.RmiRemote;

public interface ResponsiveGridITViewCallables extends RmiCallable {

  IResponsiveStep responsiveStep(int minWidth);

  int getCurrentStep();

  interface IResponsiveStep extends RmiRemote {
    void show(int colIndex);

    void hide(int colIndex);

    void showAll();

    void hideAll();

    void remove();

    IListenerRegistration addListener();
  }

  interface IListenerRegistration extends RmiRemote {
    int getCount();

    int getLastMinWidth();

    void remove();

    void cumulative();

  }

  default void roundtrip() {}

}
