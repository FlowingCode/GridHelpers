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

import com.vaadin.flow.function.SerializableFunction;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
final class GridHelperPartNameGenerator<T> implements SerializableFunction<T, String> {

  private Map<Class<?>, SerializableFunction<T, String>> helperPartNameGenerators =
      new HashMap<>();

  @Getter
  @Setter
  private SerializableFunction<T, String> gridPartNameGenerator;

  private transient boolean invoked;

  void setHelperPartNameGenerator(Class<?> clazz, SerializableFunction<T, String> generator) {
    if (generator != null) {
      helperPartNameGenerators.put(clazz, generator);
    } else {
      helperPartNameGenerators.remove(clazz);
    }
  }

  private Stream<SerializableFunction<T, String>> generators() {

    return Stream.concat(
        Optional.ofNullable(gridPartNameGenerator).map(Stream::of).orElseGet(Stream::empty),
        helperPartNameGenerators.values().stream());
  }

  @Override
  public String apply(T t) {
    if (invoked) {
      return null;
    }

    invoked = true;
    try {
      return StringUtils.trimToNull(
          generators()
              .map(generator -> generator.apply(t))
              .map(StringUtils::trimToNull)
              .filter(Objects::nonNull)
              .flatMap(s -> Stream.of(s.trim().split("\\s+")))
              .distinct()
              .collect(Collectors.joining(" ")));
    } finally {
      invoked = false;
    }
  }
}
