import java.util.Properties
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.kotlinter.gradle)
    }
}

/**
 * Below suppression can be resolved by upgrading to Gradle 8.x
 * See: https://github.com/gradle/gradle/issues/22797
 */
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    jacoco
    publishing
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.coveralls.jacoco)
    alias(libs.plugins.jacocolog)
    alias(libs.plugins.jmailen.kotlinter)
}

allprojects {
    group = "io.pleo"

    apply(plugin = rootProject.libs.plugins.coveralls.jacoco.get().pluginId)
    apply(plugin = rootProject.libs.plugins.jacocolog.get().pluginId)
    apply(plugin = rootProject.libs.plugins.jmailen.kotlinter.get().pluginId)

    jacoco {
        toolVersion = "0.8.7"
    }

    tasks.named("jacocoLogAggregatedCoverage") {
        enabled = false
    }

    repositories {
        mavenLocal()
        mavenCentral()
    }
}

subprojects {
    apply(plugin = rootProject.libs.plugins.java.library.get().pluginId)
    apply(plugin = rootProject.libs.plugins.kotlin.library.get().pluginId)
    apply(plugin = rootProject.libs.plugins.maven.publish.get().pluginId)
    apply(plugin = rootProject.libs.plugins.jmailen.kotlinter.get().pluginId)

    dependencies {
        implementation(rootProject.libs.slf4j)
        implementation(rootProject.libs.kotlin.reflect)
        implementation(rootProject.libs.kotlin.guice)

        testImplementation(rootProject.libs.google.truth)
        testImplementation(rootProject.libs.logback.classic)
        testImplementation(rootProject.libs.junit.jupiter)
        testImplementation(rootProject.libs.mockk)

        testRuntimeOnly(rootProject.libs.junit.jupiter.engine)
    }

    tasks {
        withType<JavaCompile>().configureEach {
            options.encoding = "UTF-8"
            sourceCompatibility = "17"
            targetCompatibility = "17"
        }

        withType<KotlinCompile>().configureEach {
            kotlinOptions {
                jvmTarget = "17"
                javaParameters = true
                languageVersion = "1.6"
            }
        }

        test {
            useJUnitPlatform()
        }

        check {
            dependsOn("installKotlinterPrePushHook")
            finalizedBy("formatKotlin")
        }

        javadoc {
            if (JavaVersion.current().isJava9Compatible) options
                .let { it as StandardJavadocDocletOptions }
                .addBooleanOption("html5", true)
        }

        register<Jar>("sourcesJar") {
            from(sourceSets["main"].allSource)
            archiveClassifier.set("sources")
        }

        register<Jar>("javadocJar") {
            from(javadoc)
            archiveClassifier.set("javadoc")
        }
    }

    publishing {
        publications {
            create("mavenJava", MavenPublication::class) {
                from(components["java"])
                artifact(tasks["sourcesJar"])
                artifact(tasks["javadocJar"])

                pom {
                    name.set(project.name)
                    description.set("Dynamic properties for your Java/Kotlin app")
                    url.set("http://github.com/pleo-io/prop")

                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("http://www.opensource.org/licenses/mit-license.php")
                        }
                    }

                    developers {
                        developer {
                            name.set("Pleo")
                            email.set("dev@pleo.io")
                        }
                    }

                    scm {
                        connection.set("scm:git:git@github.com:pleo-io/prop.git")
                        developerConnection.set("scm:git:git@github.com:pleo-io/prop.git")
                        url.set("https://github.com/pleo-io/prop")
                    }
                }
            }
        }

        repositories {
            mavenLocal()
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/pleo-io/prop")
                credentials {
                    username = System.getenv("GITHUB_ACTOR")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }
        }
    }
}

// Executed only at root level so code coverage is only reported once to Coveralls
coverallsJacoco {
    reportPath = "build/reports/jacoco/jacocoAggregatedReport/jacocoAggregatedReport.xml"
    reportSourceSets += subprojects.map { it.sourceSets["main"].allSource.srcDirs }.flatten()
}

tasks {
    register("incrementVersion") {
        group = "release"
        description = "Increments the version in this build file everywhere it is used."

        fun generateVersion(): String = version
            .toString()
            .split(".")
            .map(String::toInt)
            .let { (major, minor, patch) ->
                when (rootProject.properties["mode"]?.toString() ?: "internal") {
                    "major" -> "${major + 1}.0.0"
                    "minor" -> "$major.${minor + 1}.0"
                    "patch" -> "$major.$minor.${patch + 1}"
                    else -> "$major.$minor.$patch"
                }
            }

        doLast {
            val gradleProperties = Properties()
            val propertiesFile = file("gradle.properties")
            gradleProperties.load(propertiesFile.inputStream())
            gradleProperties.setProperty("version", rootProject.properties.getOrDefault("overrideVersion", generateVersion()).toString())
            gradleProperties.store(propertiesFile.writer(), null)
        }
    }

    named("coverallsJacoco") {
        dependsOn("jacocoAggregatedReport")
    }
}


