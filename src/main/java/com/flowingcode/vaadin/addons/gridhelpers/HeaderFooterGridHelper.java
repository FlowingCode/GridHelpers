package com.flowingcode.vaadin.addons.gridhelpers;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.validation.constraints.NotNull;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.Renderer;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("serial")
@RequiredArgsConstructor
class HeaderFooterGridHelper implements Serializable {

  private final GridHelper<?> helper;

  @NotNull
  private static String getTemplate(@NotNull Renderer<?> renderer) {
      try {
          final Field templateField = Renderer.class.getDeclaredField("template");
          templateField.setAccessible(true);
          final String template = (String) templateField.get(renderer);
          return template == null ? "" : template;
      } catch (Exception ex) {
          throw new RuntimeException(ex);
      }
  }

  @NotNull
  private static Renderer<?> getHeaderRenderer(@NotNull Grid.Column<?> column) {
      try {
          final Class<?> clazz = Class.forName("com.vaadin.flow.component.grid.AbstractColumn");
          final Method m = clazz.getDeclaredMethod("getHeaderRenderer");
          m.setAccessible(true);
          return ((Renderer<?>) m.invoke(column));
      } catch (Exception ex) {
          throw new RuntimeException(ex);
      }
  }

  @NotNull
  private static Renderer<?> getFooterRenderer(@NotNull Grid.Column<?> column) {
      try {
          final Class<?> clazz = Class.forName("com.vaadin.flow.component.grid.AbstractColumn");
          final Method m = clazz.getDeclaredMethod("getFooterRenderer");
          m.setAccessible(true);
          return ((Renderer<?>) m.invoke(column));
      } catch (Exception ex) {
          throw new RuntimeException(ex);
      }
  }

  /**
   * See https://github.com/vaadin/flow-components/issues/1496
   */
  @NotNull
  public String getHeader(@NotNull Grid.Column<?> column) {
      final Renderer<?> headerRenderer = getHeaderRenderer(column);
      return getTemplate(headerRenderer);
  }

  public String getFooter(@NotNull Grid.Column<?> column) {
      final Renderer<?> headerRenderer = getFooterRenderer(column);
      return getTemplate(headerRenderer);
  }

}
