/*-
 * #%L
 * Grid Helpers Add-on
 * %%
 * Copyright (C) 2022 Flowing Code
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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestData {

  private static final Faker faker = new Faker();

  private static synchronized Person newPerson() {
    return Person.builder()
        .active(faker.random().nextBoolean())
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

  static List<Person> initializeData() {
    return Stream.generate(TestData::newPerson).limit(40).collect(Collectors.toList());
  }

  private static String generateCountry() {
    String country = faker.address().country();
    if (country.contains("South Georgia") || country.contains("Falkland")) {
      return "Argentina";
    } else {
      return country;
    }
  }
}
