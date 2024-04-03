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

import com.github.javafaker.Faker;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("serial")
class LazyTestData implements Serializable {

  private static final Faker faker = new Faker();

  private static synchronized Person newPerson() {
    return Person.builder()
        .active(faker.random().nextBoolean())
        .vip(faker.random().nextBoolean())
        .hidden(faker.random().nextBoolean())
        .firstName(faker.name().firstName())
        .lastName(faker.name().lastName())
        .country(generateCountry())
        .city(faker.address().city())
        .streetAddress(faker.address().streetAddress())
        .phoneNumber(faker.phoneNumber().cellPhone())
        .emailAddress(faker.internet().emailAddress())
        .title(faker.name().title())
        .build();
  }

  private final List<Person> data;

  public LazyTestData() {
    data = Stream.generate(LazyTestData::newPerson).limit(400).collect(Collectors.toList());
  }

  public Stream<Person> filter(int offset, int pageSize) {
    return filter(offset, pageSize, null);
  }

  public Stream<Person> filter(int offset, int pageSize, PersonFilter filter) {
    int from = Math.max(0, offset);
    if (filter != null) {
      return data.stream()
          .filter(
              item -> StringUtils.containsIgnoreCase(item.getFirstName(), filter.getFirstName()))
          .filter(item -> StringUtils.containsIgnoreCase(item.getLastName(), filter.getLastName()))
          .skip(from)
          .limit(pageSize);
    }
    return data.stream().skip(from).limit(pageSize);
  }

  public int count() {
    return data.size();
  }

  public int count(PersonFilter filter) {
    return (int) data.stream()
        .filter(item -> StringUtils.containsIgnoreCase(item.getFirstName(), filter.getFirstName()))
        .filter(item -> StringUtils.containsIgnoreCase(item.getLastName(), filter.getLastName()))
        .count();
  }

  private static String generateCountry() {
    String country = faker.address().country();
    if (country.contains("South Georgia") || country.contains("Falkland")) {
      return "Argentina";
    } else {
      return country;
    }
  }

  @Getter
  @Setter
  public static class PersonFilter {
    private String firstName;
    private String lastName;
  }

}
