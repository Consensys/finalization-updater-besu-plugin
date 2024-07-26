// Copyright 2024, Consensys Software Inc.
// SPDX-License-Identifier: Apache-2.0
import org.jreleaser.model.Active
import org.jreleaser.model.Distribution
import org.jreleaser.model.UpdateSection

plugins {
  `java-library`
  alias(libs.plugins.spotless)
  alias(libs.plugins.jgitver)
  alias(libs.plugins.jreleaser)
  alias(libs.plugins.gradle.errorprone.plugin)
}

project.group = "net.consensys.linea.besu.plugin"

repositories {
  mavenLocal() // for testing locally build Besu plugin

  // Use Maven Central for resolving dependencies.
  mavenCentral()

  // for linea-besu plugin dependencies
  maven {
    url = uri("https://artifacts.consensys.net/public/linea-besu/maven/")
    content { includeGroupByRegex("io\\.consensys\\..*") }
  }

  // For Besu dependencies
  maven {
    url = uri("https://hyperledger.jfrog.io/artifactory/besu-maven/")
    content { includeGroupByRegex("org\\.hyperledger\\.besu($|\\..*)") }
  }

  // for consensys dependencies
  maven {
    url = uri("https://artifacts.consensys.net/public/maven/maven/")
    content { includeGroupByRegex("tech\\.pegasys(\\..*)?") }
  }
}

// project dependencies. Testing dependencies are declared in the testing block.
dependencies {
  api(libs.besu.plugin.api)
  api(libs.besu.internal.api) {
    exclude(group = "org.apache.logging.log4j", module = "log4j-slf4j2-impl")
  }

  // https://github.com/google/auto/tree/main/service
  annotationProcessor(libs.google.auto.service)
  implementation(libs.google.auto.service.annotations)
  implementation(libs.slf4j.api)
  implementation(libs.picocli)

  // errorprone dependencies
  errorprone(libs.google.error.prone)
}

// Apply a specific Java toolchain to ease working on different environments.
java { toolchain { languageVersion = JavaLanguageVersion.of(21) } }

testing {
  suites {
    // shared with every JVM test suite
    withType<JvmTestSuite> {
      useJUnitJupiter()
      dependencies {
        implementation(libs.assertj.core)
        runtimeOnly(libs.slf4j.simple)
      }
    }

    // default test suite
    val test by getting(JvmTestSuite::class)

    val integrationTest by
        registering(JvmTestSuite::class) {
          dependencies { implementation(project()) }

          // make integrationTest suite run after the default test suite
          targets { all { testTask.configure { shouldRunAfter(test) } } }
        }
  }
}

tasks.named("check") { dependsOn(testing.suites.named("integrationTest")) }

spotless {
  java {
    importOrder()
    removeUnusedImports()
    googleJavaFormat()
    licenseHeaderFile(layout.projectDirectory.file("gradle/spotless/java.license.template"))
  }

  kotlinGradle { ktfmt() }
}

// auto-generate project version (semver) based on tags
jgitver { nonQualifierBranches = "main" }

tasks.register("printVersion") {
  group = "Help"
  description = "Prints the project version"
  doLast { println("Version: ${project.version}") }
}

tasks.jar {
  manifest {
    attributes(
        mapOf("Implementation-Title" to project.name, "Implementation-Version" to project.version))
  }
}

jreleaser {
  project {
    description.set("Finalization Updater Besu Plugin")
    authors.set(listOf("Consensys Protocols Team"))
    license.set("Apache-2.0")
    inceptionYear.set("2024")
    copyright.set("2024, Consensys Software Inc.")
    links {
      homepage.set("https://github.com/Consensys/finalization-updater-besu-plugin")
      documentation.set("https://github.com/Consensys/finalization-updater-besu-plugin")
    }
  }
  dependsOnAssemble.set(true)
  gitRootSearch.set(true)
  distributions {
    create("finalization-updater-besu-plugin") {
      distributionType.set(Distribution.DistributionType.SINGLE_JAR)
      artifact {
        path.set(layout.buildDirectory.file("libs/{{distributionName}}-{{projectVersion}}.jar"))
      }
    }
  }

  release {
    github {
      repoOwner = "consensys"
      // append artifacts to an existing release with matching tag
      update {
        enabled = true
        sections.set(listOf(UpdateSection.ASSETS, UpdateSection.TITLE, UpdateSection.BODY))
      }
      // We need to create tag manually because our project version is calculated based on tags.
      skipTag = true
      changelog {
        formatted.set(Active.ALWAYS)
        preset.set("conventional-commits")
        contributors {
          enabled.set(true)
          format.set(
              "- {{contributorName}}{{#contributorUsernameAsLink}} ({{.}}){{/contributorUsernameAsLink}}")
        }
      }
    }
  }
}
