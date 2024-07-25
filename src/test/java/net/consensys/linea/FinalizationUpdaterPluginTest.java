// Copyright 2024, Consensys Software Inc.
// SPDX-License-Identifier: Apache-2.0
package net.consensys.linea;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class FinalizationUpdaterPluginTest {
  @Test
  void getVersionIsValid() {
    FinalizationUpdaterPlugin plugin = new FinalizationUpdaterPlugin();
    assertThat(plugin.getVersion()).startsWith("FinalizationUpdaterPlugin");
  }
}
