package com.flowingcode.vaadin.addons.gridhelpers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode
@Builder
public class Person {
  boolean active;
  String firstName;
  String lastName;
  String country;
  String city;
  String streetAddress;
  String emailAddress;
  String phoneNumber;
  String title;
}
