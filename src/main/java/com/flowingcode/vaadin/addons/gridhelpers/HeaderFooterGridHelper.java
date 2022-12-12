package com.flowingcode.vaadin.addons.gridhelpers;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.data.renderer.Renderer;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("serial")
@RequiredArgsConstructor
class HeaderFooterGridHelper implements Serializable {

  private final GridHelper<?> helper;

  private static final Method getHeaderText = getMethod(Column.class, "getHeaderText").orElse(null);

  private static final Method getFooterText = getMethod(Column.class, "getFooterText").orElse(null);

  private static Optional<Method> getMethod(Class<?> clazz, String name) {
    try {
      return Optional.of(clazz.getMethod(name));
    } catch (NoSuchMethodException e) {
      return Optional.empty();
    }
  }

  private static String getTemplate(Renderer<?> renderer) {
      try {
          final Field templateField = Renderer.class.getDeclaredField("template");
          templateField.setAccessible(true);
          final String template = (String) templateField.get(renderer);
          return template == null ? "" : template;
      } catch (Exception ex) {
          throw new RuntimeException(ex);
      }
  }

  private static Renderer<?> getHeaderRenderer(Grid.Column<?> column) {
      try {
          final Class<?> clazz = Class.forName("com.vaadin.flow.component.grid.AbstractColumn");
          final Method m = clazz.getDeclaredMethod("getHeaderRenderer");
          m.setAccessible(true);
          return ((Renderer<?>) m.invoke(column));
      } catch (Exception ex) {
          throw new RuntimeException(ex);
      }
  }

  private static Renderer<?> getFooterRenderer(Grid.Column<?> column) {
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
  public String getHeader(Grid.Column<?> column) {
    if (getHeaderText != null) {
      // Vaadin 23.2+
      try {
        return (String) getHeaderText.invoke(column);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    } else {
      Renderer<?> headerRenderer = getHeaderRenderer(column);
      return getTemplate(headerRenderer);
    }
  }

  public String getFooter(Grid.Column<?> column) {
    if (getFooterText != null) {
      // Vaadin 23.2+
      try {
        return (String) getFooterText.invoke(column);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    } else {
      Renderer<?> headerRenderer = getFooterRenderer(column);
      return headerRenderer==null?null:getTemplate(headerRenderer);
    }
  }

}
