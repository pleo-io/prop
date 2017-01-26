# Prop

Pleo prop is a dynamic property library. It allows you to configure your application using properties that are not hardcoded and can be easily modified at runtime.

It is made of 4 modules that when combined give you a full, flexible and powerful solution.

# Usage

If you're okay with using Guice, Archaius and Jackson, add a dependency on `prop-all`.

```
<dependency>
    <groupId>io.pleo</groupId>
    <artifactId>prop-all</artifactId>
    <version>1.0.0</version>
</dependency>
```

All you need is to initialize the `AutoPropModule` by passing it all of the Guice Modules you'd like it to scan for `Prop<X>` dependencies.

```
    List<Module> modules = ...
    AutoPropModule autoPropModule = new AutoPropModule("io.pleo", // Package prefix
                                                       modules,
                                                       new ArchaiusPropFactory(),
                                                       new JacksonParserFactory());
    modules.add(autoPropModule);
    Guice.createInjector(modules);
```

And then you can simply add a `@Named("myPropName") Prop<X>` to your class, like this

```
public class MyServiceClient {
    private Prop<String> serviceUrl;
    
    // The @Named annotation is required. A detailed exception will be thrown when 
    // bootstrapping the Guice injector if it is missing.
    public MyServiceClient(@Named("service.url") Prop<String> serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    // ...
    public Health getHealth() {
        return Unirest.get(serviceUrl.get() + "/rest/health").asObject(Health.class).getBody();
    }
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

## How does it work

`AutoPropModule` will scan all of the modules that you provide and will find all InjectionPoints that require a `Prop<X>` instance. 

It will determine the type parameter of the `Prop<X>` and dynamically generate a parser for this `Prop<X>`.

It will then initialize a Archaius property based on the `@Named` annotation and the parser.

Finally it dynamically binds this new `Prop<X>` in Guice. Guice does the rest of the magic. 

# The Modules

## prop-core

The classes that your application will use. It has no dependencies and is extremely lightweight.

## prop-guice

The Google Guice integration. Will automatically detect all `Prop<X>` that are required by your application and bind these to the right `Prop<X>` instance.

## prop-archaius

The Netflix Archaius integration. Will fetch `Prop<X>` values using Archaius which can be configured to read properties from many many configuration repositories.

## prop-jackson

The Jackson integration. Allows using serialized JSON as `Prop<X>` values so you can use `Prop<MyComplexObject>` as long as `MyComplexObject` can be desrialized from a JSON String.

# Extending

You can easily customize the behavior of prop. The three main extension points are `PropFactory` and `ParserFactory`.

## PropFactory

`PropFactory` takes a property name and a parse function and must return a `Prop<X>`. The default implementation is `ArchaiusPropFactory`.

## ParserFactory

`ParserFactory` takes a `java.reflect.Type` and returns a `java.util.Function` that can transform a `String` into an instace of the right Type. The default implementation is `JacksonParserFactory`. 