/*-
 * #%L
 * Grid Helpers Add-on
 * %%
 * Copyright (C) 2022 - 2025 Flowing Code
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

import lombok.experimental.UtilityClass;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

@UtilityClass
class StringUtils {

  /**
   * Removes control characters from both ends of this String returning {@code null} if the String
   * is empty ("") after the trim or if it is {@code null}.
   *
   * <pre>
   * StringUtils.trimToNull(null)          = null
   * StringUtils.trimToNull("")            = null
   * StringUtils.trimToNull("     ")       = null
   * StringUtils.trimToNull("abc")         = "abc"
   * StringUtils.trimToNull("    abc    ") = "abc"
   * </pre>
   *
   * @param str the String to be trimmed, may be null
   * @return the trimmed String, {@code null} if only chars &lt;= 32, empty or null String input
   */
  static String trimToNull(String str) {
    if (str == null) {
      return null;
    }
    str = str.trim();
    if (str.isEmpty()) {
      return null;
    }
    return str;
  }

}
