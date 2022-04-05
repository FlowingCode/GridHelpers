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
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("serial")
final class GridHelperClassNameGenerator<T> implements SerializableFunction<T, String> {

  private Map<Class<?>, SerializableFunction<T, String>> helperClassNameGenerators =
      new HashMap<>();

  @Getter @Setter private SerializableFunction<T, String> gridClassNameGenerator;

  private transient boolean invoked;

  void setHelperClassNameGenerator(Class<?> clazz, SerializableFunction<T, String> generator) {
    if (generator != null) {
      helperClassNameGenerators.put(clazz, generator);
    } else {
      helperClassNameGenerators.remove(clazz);
    }
  }

  private Stream<SerializableFunction<T, String>> generators() {
    return Stream.concat(
        Optional.ofNullable(gridClassNameGenerator).map(Stream::of).orElseGet(Stream::empty),
        helperClassNameGenerators.values().stream());
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
