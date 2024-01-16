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
package com.flowingcode.vaadin.addons.gridhelpers;

/**
 * Represents an HTML element that supports CSS classes.
 */
public interface GridStylesHelper {
  // methods from HasStyle (minus getElement and getStyle)

  /**
   * Adds a CSS class name to this element.
   *
   * @param className the CSS class name to add, not <code>null</code>
   */
  void addClassName(String className);

  /**
   * Removes a CSS class name from this element.
   *
   * @param className the CSS class name to remove, not <code>null</code>
   * @return <code>true</code> if the class name was removed, <code>false</code> if the class list
   *         didn't contain the class name
   */
  boolean removeClassName(String className);

  /**
   * Sets the CSS class names of this element. This method overwrites any previous set class names.
   *
   * @param className a space-separated string of class names to set, or <code>null</code> to remove
   *        all class names
   */
  void setClassName(String className);

  /**
   * Gets the CSS class names for this element.
   *
   * @return a space-separated string of class names, or <code>null</code> if there are no class
   *         names
   */
  String getClassName();

  /**
   * Sets or removes the given class name for this element.
   *
   * @param className the class name to set or remove, not <code>null</code>
   * @param set <code>true</code> to set the class name, <code>false</code> to remove it
   */
  void setClassName(String className, boolean set);

  /**
   * Checks if the element has the given class name.
   *
   * @param className the class name to check for
   * @return <code>true</code> if the element has the given class name, <code>false</code> otherwise
   */
  boolean hasClassName(String className);

  /**
   * Adds one or more CSS class names to this element. Multiple class names can be specified by
   * using spaces or by giving multiple parameters.
   *
   * @param classNames the CSS class name or class names to be added to the element
   */
  void addClassNames(String... classNames);

  /**
   * Removes one or more CSS class names from element. Multiple class names can be specified by
   * using spaces or by giving multiple parameters.
   *
   * @param classNames the CSS class name or class names to be removed from the element
   */
  void removeClassNames(String... classNames);

}
