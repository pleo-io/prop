# Prop

Prop is a dynamic property library. It allows you to configure your application using properties that are not hardcoded and can be easily modified at runtime.

It is made of 4 modules that when combined give you a full, flexible and powerful solution.

# The Modules

## prop-core

The classes that your application will use. It has no dependencies and is extremely lightweight.

## prop-guice

The Google Guice integration. Will automatically detect all `Prop` that are required by your application and bind these to the right `Prop` instance.

## prop-archaius

The Netflix Archaius integration. Will fetch `Prop` values using Archaius which can be configured to read properties from many many configuration repositories.

## prop-jackson

The Jackson integration. Allows using serialized JSON as `Prop` values so you can use `Prop<MyComplexObject>` as long as `MyComplexObject` can be desrialized from a JSON String.

