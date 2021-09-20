# [3.0.0](https://github.com/pleo-io/prop/compare/v2.4.0...v3.0.0) (2021-09-17)

### Features

* add support for google guice assistedInject bindings and the use of `FactoryModuleBuilder`
see [the official documentation](https://github.com/google/guice/wiki/AssistedInject#inspecting-assistedinject-bindings-new-in-guice-30) for more context.

### Breaking

* `InjectionPointExtractor` now returns `Iterable<InjectionPoint>` to allow injecting props for multiple factory functions.
Anyone using the `InjectionPointExtractor` on its own will need to adjust to handle this change.

# [2.4.0](https://github.com/pleo-io/prop/compare/v2.3.0...v2.4.0) (2021-08-31)


### Features

* bump several dependencies (minor version bumps only) ([e3ae49e](https://github.com/pleo-io/prop/commit/e3ae49e50857e503cfe76e6bc26866b4fb7c423d)), closes [#57](https://github.com/pleo-io/prop/issues/57)

# [2.3.0](https://github.com/pleo-io/prop/compare/v2.2.0...v2.3.0) (2021-04-15)


### Features

* bump guice from 4.2.2 to 5.0.1 ([#17](https://github.com/pleo-io/prop/issues/17)) ([7c60fdf](https://github.com/pleo-io/prop/commit/7c60fdf8e07142823d1f11ac0dc65d3f794406f0))

# [2.2.0](https://github.com/pleo-io/prop/compare/v2.1.0...v2.2.0) (2021-04-15)


### Features

* add invoke method on Prop to enable Kotlin invocation usage ([#35](https://github.com/pleo-io/prop/issues/35)) ([9bb67bf](https://github.com/pleo-io/prop/commit/9bb67bf50d0eab8c2626cced3d7680818db0287e))
