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

import com.flowingcode.vaadin.addons.gridhelpers.GridHelper;
import com.flowingcode.vaadin.addons.gridhelpers.GridResponsiveStep;
import com.flowingcode.vaadin.addons.gridhelpers.GridResponsiveStep.GridResponsiveStepListenerRegistration;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import java.util.List;
import java.util.stream.IntStream;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(GridHelper.class)
@Route(ResponsiveGridITView.ROUTE)
public class ResponsiveGridITView extends VerticalLayout implements ResponsiveGridITViewCallables {

  public static final String ROUTE = "it/responsive";

  private Grid<List<Integer>> grid;

  @Getter
  private int currentStep = -1;

  public ResponsiveGridITView() {
    grid = new Grid<>();
    setPadding(false);
    setSpacing(false);
    add(grid);

    IntStream.range(0, 8)
        .forEach(
            i -> {
              grid.addColumn(list -> list.get(i)).setHeader("Col " + i);
            });
  }

  @Override
  @ClientCallable
  public JsonValue $call(JsonObject invocation) {
    return ResponsiveGridITViewCallables.super.$call(invocation);
  }

  @Override
  public IResponsiveStep responsiveStep(int minWidth) {
    GridResponsiveStep<?> step = grid.responsiveStep(minWidth);
    step.addListener(ev -> {
      currentStep = ev.getMinWidth();
    });
    return new IResponsiveStepImpl(step);
  }

  @RequiredArgsConstructor
  @EqualsAndHashCode
  private class IResponsiveStepImpl implements IResponsiveStep {
    final GridResponsiveStep<?> delegate;

    @Override
    public void show(int colIndex) {
      delegate.show(grid.getColumns().get(colIndex));
    }

    @Override
    public void hide(int colIndex) {
      delegate.hide(grid.getColumns().get(colIndex));
    }

    @Override
    public void showAll() {
      delegate.showAll();
    }

    @Override
    public void hideAll() {
      delegate.hideAll();
    }

    @Override
    public void remove() {
      delegate.remove();
    }

    @Override
    public IListenerRegistration addListener() {
      IListenerContext ctx = new IListenerContext();
      return new IListenerRegistrationImpl(
          ctx,
          delegate.addListener(
              ev -> {
                ctx.count++;
                ctx.minWidth = ev.getMinWidth();
              }));
    }
  }

  private class IListenerContext {
    int count;
    int minWidth = -1;
  }

  @RequiredArgsConstructor
  private class IListenerRegistrationImpl implements IListenerRegistration {

    private final IListenerContext ctx;

    private final GridResponsiveStepListenerRegistration registration;

    @Override
    public int getCount() {
      return ctx.count;
    }

    @Override
    public int getLastMinWidth() {
      return ctx.minWidth;
    }

    @Override
    public void remove() {
      registration.remove();
    }

    @Override
    public void cumulative() {
      registration.cummulative();
    }
  }
}
