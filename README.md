# Prop

Prop is a dynamic property library. It allows you to configure your application using properties that are not hardcoded and can be easily modified at runtime.

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

All you need is to initialize the `AutoPropModule` by passing it all of the Guice Modules you'd like it to scan for `Prop`.

```
    List<Module> modules = ...
    AutoPropModule autoPropModule = new AutoPropModule("io.pleo",
                                                       modules,
                                                       new ArchaiusPropFactory(),
                                                       new JacksonParserFactory(new ObjectMapper()));
    modules.add(autoPropModule);
    Guice.createInjector(modules);
```

And then you can simply add a `@Named("myPropName") Prop<X>` to your class, like this

```
public class MyServiceClient {
    private Prop<String> serviceUrl;
    
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

# The Modules

## prop-core

The classes that your application will use. It has no dependencies and is extremely lightweight.

## prop-guice

The Google Guice integration. Will automatically detect all `Prop` that are required by your application and bind these to the right `Prop` instance.

## prop-archaius

The Netflix Archaius integration. Will fetch `Prop` values using Archaius which can be configured to read properties from many many configuration repositories.

## prop-jackson

The Jackson integration. Allows using serialized JSON as `Prop` values so you can use `Prop<MyComplexObject>` as long as `MyComplexObject` can be desrialized from a JSON String.

