## [6.0.3](https://github.com/pleo-io/prop/compare/v6.0.2...v6.0.3) (2022-09-08)


### Bug Fixes

* **deps:** update dependency org.jmailen.gradle:kotlinter-gradle to v3.11.1 ([#122](https://github.com/pleo-io/prop/issues/122)) ([08fe25f](https://github.com/pleo-io/prop/commit/08fe25fd8a1d8ee8b3164e37ea7dece66b47daa0))

## [6.0.2](https://github.com/pleo-io/prop/compare/v6.0.1...v6.0.2) (2022-08-15)


### Bug Fixes

* **deps:** update junit5 monorepo to v5.9.0 ([#124](https://github.com/pleo-io/prop/issues/124)) ([2bba6fb](https://github.com/pleo-io/prop/commit/2bba6fb7f38f1360dcfaf4923ae511e877f5c977))

## [6.0.1](https://github.com/pleo-io/prop/compare/v6.0.0...v6.0.1) (2022-07-29)


### Bug Fixes

* **deps:** Correct renovate semantic release type ([#125](https://github.com/pleo-io/prop/issues/125)) ([b247551](https://github.com/pleo-io/prop/commit/b247551855b43a6e3d5bdf9d341b6948a901dc37))

# [6.0.0](https://github.com/pleo-io/prop/compare/v5.0.0...v6.0.0) (2022-05-23)


### Performance Improvements

* **core:** Removed old CODEOWNERS file in favour of .github/CODEOWNERS ([#121](https://github.com/pleo-io/prop/issues/121)) ([f3dbd4d](https://github.com/pleo-io/prop/commit/f3dbd4df878f14d67e4670324da670d1ac80e385))


### BREAKING CHANGES

* **core:** Java version updated from Java11 to Java17

# [5.0.0](https://github.com/pleo-io/prop/compare/v4.0.0...v5.0.0) (2022-01-26)


### Features

* **core:** Exit Archaius, Enter Apache Commons Config2 ([c58945a](https://github.com/pleo-io/prop/commit/c58945aa6918afc5d03d656878b3e9af57ee4f0a))


### BREAKING CHANGES

* **core:** Archaius support is removed, including `ConfigurationManager()`

# [4.0.0](https://github.com/pleo-io/prop/compare/v3.0.1...v4.0.0) (2022-01-24)


We have had some issues cutting releases; pardon the confusion.

### Bug Fixes

* **ci:** refactor workflow 

### BREAKING CHANGES

* **core:** More idiomatic Kotlin ([#77](https://github.com/pleo-io/prop/issues/77)) ([d8d1c63](https://github.com/pleo-io/prop/commit/d8d1c63ef714ba011b6b3d0e3d17c4453e625521))\
	* Use Kotlin lambda instead of Runnable for Prop.addCallback, and instead of Function for PropFactory.parse.
* **core:** Move to Kotlin ([#76](https://github.com/pleo-io/prop/issues/76)) ([f506e740](https://github.com/pleo-io/prop/commit/f506e7407f77d9cdbbeb8ca7a64a464a47f1bb30))
	* `JacksonParserFactory` had mistakenly been given the class `io.pleo.prop.guice`; it has been corrected to `io.pleo.prop.jackson`.
	* minimum JDK version has been bumped to JDK11.

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
