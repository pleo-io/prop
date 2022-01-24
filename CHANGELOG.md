# [4.0.0](https://github.com/pleo-io/prop/compare/v3.0.1...v4.0.0) (2022-01-24)


### Bug Fixes

* **ci:** refactor workflow ([#83](https://github.com/pleo-io/prop/issues/83)) ([956c39c](https://github.com/pleo-io/prop/commit/956c39ca332a5d64f3f09ebbd3c7da0a0ee3e0cb))
* **ci:** use self-hosted but only release on master ([#84](https://github.com/pleo-io/prop/issues/84)) ([798278f](https://github.com/pleo-io/prop/commit/798278f50906b4b704cba564895efa080406534b))


* Move to Kotlin ([f506e74](https://github.com/pleo-io/prop/commit/f506e7407f77d9cdbbeb8ca7a64a464a47f1bb30))


### Features

* **core:** More idiomatic Kotlin ([f82dc35](https://github.com/pleo-io/prop/commit/f82dc3541a1744a99cceda8b123255462bd1da1f))
* **core:** More idiomatic Kotlin ([0dc60a8](https://github.com/pleo-io/prop/commit/0dc60a87edb404dae852ef424c7b3ee2c2438605))
* **core:** More idiomatic Kotlin ([d8d1c63](https://github.com/pleo-io/prop/commit/d8d1c63ef714ba011b6b3d0e3d17c4453e625521))
* **core:** More idiomatic Kotlin ([#78](https://github.com/pleo-io/prop/issues/78)) ([ad1005f](https://github.com/pleo-io/prop/commit/ad1005f2da7a1b0be652fb7c013c1643087fc9e2))
* **core:** More idiomatic Kotlin ([#80](https://github.com/pleo-io/prop/issues/80)) ([35b120f](https://github.com/pleo-io/prop/commit/35b120f3e1acfb9c22d8782e76a54b1998107e47))


### BREAKING CHANGES

* **core:** use Kotlin lambda instead of Runnable, Function

Use Kotlin lambda instead of Runnable for Prop.addCallback, and instead of Function for PropFactory.parse.
* **core:** use Kotlin lambda instead of Runnable, Function

Use Kotlin lambda instead of Runnable for Prop.addCallback, and instead of Function for PropFactory.parse.
* **core:** use Kotlin lambda instead of Runnable, Function

Use Kotlin lambda instead of Runnable for Prop.addCallback, and instead of Function for PropFactory.parse.
* **core:** use Kotlin lambda instead of Runnable, Function

Use Kotlin lambda instead of Runnable for Prop.addCallback, and instead of Function for PropFactory.parse.
* **core:** use Kotlin lambda instead of Runnable, Function

Use Kotlin lambda instead of Runnable for Prop.addCallback, and instead of Function for PropFactory.parse.
* `JacksonParserFactory` had mistakenly been given the class `io.pleo.prop.guice`; it has been corrected to `io.pleo.prop.jackson`; minimum JDK version has been bumped to JDK11.

## [3.0.1](https://github.com/pleo-io/prop/compare/v3.0.0...v3.0.1) (2021-09-22)


### Bug Fixes

* update readme with directions on how to contribute to this repo ([#60](https://github.com/pleo-io/prop/issues/60)) ([77a7284](https://github.com/pleo-io/prop/commit/77a7284e425237cdd181feccf88b0bb9db5d02e6))

# [3.0.0](https://github.com/pleo-io/prop/compare/v2.4.0...v3.0.0) (2021-09-21)


* add support for assisted inject (#59) ([4c5681d](https://github.com/pleo-io/prop/commit/4c5681da6f0ed107622c917b14693aecb9818d3c)), closes [#59](https://github.com/pleo-io/prop/issues/59)


### BREAKING CHANGES

* InjectionPointExtractor will now return Iterable<InjectionPoint>

Co-authored-by: Harry <85480431+harrylevick@users.noreply.github.com>

# [2.4.0](https://github.com/pleo-io/prop/compare/v2.3.0...v2.4.0) (2021-08-31)


### Features

* bump several dependencies (minor version bumps only) ([e3ae49e](https://github.com/pleo-io/prop/commit/e3ae49e50857e503cfe76e6bc26866b4fb7c423d)), closes [#57](https://github.com/pleo-io/prop/issues/57)

# [2.3.0](https://github.com/pleo-io/prop/compare/v2.2.0...v2.3.0) (2021-04-15)


### Features

* bump guice from 4.2.2 to 5.0.1 ([#17](https://github.com/pleo-io/prop/issues/17)) ([7c60fdf](https://github.com/pleo-io/prop/commit/7c60fdf8e07142823d1f11ac0dc65d3f794406f0))

# [2.2.0](https://github.com/pleo-io/prop/compare/v2.1.0...v2.2.0) (2021-04-15)


### Features

* add invoke method on Prop to enable Kotlin invocation usage ([#35](https://github.com/pleo-io/prop/issues/35)) ([9bb67bf](https://github.com/pleo-io/prop/commit/9bb67bf50d0eab8c2626cced3d7680818db0287e))
