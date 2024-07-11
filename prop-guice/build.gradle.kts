description = "Guice integration for Pleo Prop"

dependencies {
  api(project(":prop-core"))
  api(libs.google.guice)
  api(libs.google.guice.assistedinject)
}
