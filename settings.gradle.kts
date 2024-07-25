// Copyright 2024, Consensys Software Inc.
// SPDX-License-Identifier: Apache-2.0
plugins {
  // Apply the foojay-resolver plugin to allow automatic download of JDKs
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "finalization-updater-besu-plugin"
