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
        .country(faker.address().country())
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

}
