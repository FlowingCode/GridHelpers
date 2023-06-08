[![Published on Vaadin Directory](https://img.shields.io/badge/Vaadin%20Directory-published-00b4f0.svg)](https://vaadin.com/directory/component/grid-helpers-add-on)
[![Stars on vaadin.com/directory](https://img.shields.io/vaadin-directory/star/grid-helpers-add-on.svg)](https://vaadin.com/directory/component/grid-helpers-add-on)
[![Build Status](https://jenkins.flowingcode.com/job/GridHelpers-addon/badge/icon)](https://jenkins.flowingcode.com/job/GridHelpers-addon)

# Grid Helpers Add-on

Several grid recipes for Vaadin 23+ (and 22), ready to use. DOES NOT require extending `Grid`.

## Features

- Remove multiselect selection column
- Freeze a grid's selection (checkbox) column
- Create Grid with conditional selection (with the following limitations: [#11](https://github.com/FlowingCode/GridHelpers/issues/11), [#12](https://github.com/FlowingCode/GridHelpers/issues/12))
- Select Grid rows automatically using up/down arrow keys
- Show a Vaadin Grid with compact row styling
- Show a meaningful message instead of an empty Grid
- Show a menu to toggle the visibility of grid columns

## Online demo

[Online demo here](http://addonsv23.flowingcode.com/grid-helpers)

## Download release

[Available in Vaadin Directory](https://vaadin.com/directory/component/grid-helpers-add-on)

## Building and running demo

- git clone repository
- mvn clean install jetty:run

To see the demo, navigate to http://localhost:8080/

## Release notes

See [here](https://github.com/FlowingCode/GridHelpers/releases)

## Issue tracking

The issues for this add-on are tracked on its github.com page. All bug reports and feature requests are appreciated. 

## Contributions

Contributions are welcome, but there are no guarantees that they are accepted as such. Process for contributing is the following:

- Fork this project
- Create an issue to this project about the contribution (bug or feature) if there is no such issue about it already. Try to keep the scope minimal.
- Develop and test the fix or functionality carefully. Only include minimum amount of code needed to fix the issue.
- Refer to the fixed issue in commit
- Send a pull request for the original project
- Comment on the original issue that you have implemented a fix for it

## License & Author

This add-on is distributed under Apache License 2.0. For license terms, see LICENSE.txt.

Grid Helpers Add-on is written by Flowing Code S.A.

# Developer Guide

## Getting started

The class `GridHelper` provides several static methods that receive a `Grid` or `Column` as the first parameter:

```
grid.setSelectionMode(SelectionMode.MULTI);
grid.addThemeName(GridHelper.DENSE_THEME);
GridHelper.setSelectOnClick(grid, true);
GridHelper.setArrowSelectionEnabled(grid, true);
GridHelper.setSelectionColumnHidden(grid, true);

Column<Person> firstNameColumn = grid.addColumn(Person::getFirstName).setHeader("First name");
GridHelper.setHidingToggleCaption(firstNameColumn, "First name");
```

If you use [Project Lombok](https://projectlombok.org/), you can benefit from the [extension method](https://projectlombok.org/features/experimental/ExtensionMethod) feature:

```
@ExtensionMethod(GridHelper.class)
public class LombokDemo extends Div {

  public LombokDemo() {
    Grid<Person> grid = new Grid<>();
    
    grid.setSelectionMode(SelectionMode.MULTI);

    grid.setSelectionColumnHidden(true);
    grid.setSelectOnClick(true);
    grid.setSelectionFilter(Person::isActive);

    grid.addColumn(Person::getFirstName)
        .setHeader("First name")
        .setHidingToggleCaption("First name");

    add(grid);
  }
}
```
