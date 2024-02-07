# Prop 

Pleo prop is a dynamic property library. It allows you to configure your application using properties that are not hardcoded and can be easily modified at runtime.

It is made of 4 modules that when combined give you a full, flexible and powerful solution.

## Usage

If you're okay with using Guice, Apache Commons Config2 and Jackson, add a dependency on `prop-all` (Gradle):

```groovy
implementation "io.pleo:prop-all:7.0.0"
```

All you need is to initialize the `AutoPropModule` by passing it all the Guice Modules you'd like it to scan for `Prop<X>` dependencies.

```kotlin
val modules: List<Module> = ...
val autoPropModule = AutoPropModule(
    "io.pleo", // Package prefix
    modules,
    CommonsConfigPropFactory(),
    JacksonParserFactory(),
)
modules.add(autoPropModule)
Guice.createInjector(modules)
```

And then you can add a `@Named("myPropName") Prop<X>` to your class, like this:

```kotlin
// The @Named annotation is required. A detailed exception will be thrown when 
// bootstrapping the Guice injector if it is missing.
class MyServiceClient @Inject constructor(
    @Named("service.url") Prop<String> private val serviceUrl: Prop<String>
) {
    // ...
    fun getHealth(): Health =
        Unirest.get(serviceUrl.get() + "/rest/health")
            .asObject(Health.class)
            .getBody()
}
```

You can also use default values using the `@Default` annotation

```kotlin
class MyThing @Inject constructor(
    @Default("localhost") @Named("service.host") val serviceHost: Prop<String>
) {
    // ...
}
```

You can use many type parameters for your `Prop<X>`. Out of the box, the following types are supported:

* Boolean
* Integer
* Float
* Double
* BigDecimal
* java.time.Duration
* java.time.Instant

As well as all types that can be deserialized by Jackson.

### How does it work

`AutoPropModule` will scan the modules that you provide and will find all InjectionPoints that require a `Prop<X>` instance. 

It will determine the type parameter of the `Prop<X>` and dynamically generate a parser for this `Prop<X>`.

It will then initialize a property based on the `@Named` annotation and the parser.

Finally, it dynamically binds this new `Prop<X>` instance in Guice. Guice does the rest of the magic. 

## The Modules

### prop-core

The classes that your application will use. It has no dependencies and is extremely lightweight.

### prop-guice

The Google Guice integration. Will automatically detect all `Prop<X>` that are required by your application and bind these to the right `Prop<X>` instance.

### prop-commons-config

The Apache Commons Config integration. Will fetch `Prop<X>` values using [Apache Commons Config v2](https://commons.apache.org/proper/commons-configuration) which can be configured to read properties from many configuration repositories.

The `CommonsConfigPropFactory` takes an optional ConfigurationBuilder argument which defines how the configuration
is read. By default `config.properties` is read from the class path and read; many other options exist; see the [Commons Config v2 User Guide](https://commons.apache.org/proper/commons-configuration/userguide/user_guide.html).

### prop-jackson

The Jackson integration. Allows using serialized JSON as `Prop<X>` values, so you can use `Prop<MyComplexObject>` as long as `MyComplexObject` can be deserialized from a JSON String.

The `JacksonParserFactory` optionally takes a ObjectMapper instance as an argument; by default a vanilla ObjectMapper is set.

## Extending

You can easily customize the behavior of prop. The two main extension points are `PropFactory` and `ParserFactory`.

### PropFactory

`PropFactory` takes a property name, a parse function and a default value, and must return a `Prop<X>`. The default implementation is `CommonsConfigPropFactory`.

### ParserFactory

`ParserFactory` takes a `java.reflect.Type` and returns a lambda that can transform a `String` into an instance of the right Type. The default implementation is `JacksonParserFactory`.

### Other

You could easily create a `prop-spring` module that would dynamically add `Prop<X>` beans depending on what is required. Pull requests are welcome!

### Contributing

To submit a change request, fork this repo and open a pull request with a detailed description of the changes made.

This lib uses [semantic-release](https://github.com/semantic-release/semantic-release). Follow the `semantic-release` commit conventions, and the lib will be automatically published with a new version based on the content of the commit messages. 
The `CHANGELOG` will also be automatically updated with a list of all commits.   
 
