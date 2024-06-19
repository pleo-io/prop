description = "Apache Config Configuration 2 implementation of PropFactory"

dependencies {
  api(project(":prop-core"))
  api(libs.commons.configuration2)
  api(libs.commons.beanutils)

  testImplementation(libs.google.guava)
}
