// Copyright 2024, Consensys Software Inc.
// SPDX-License-Identifier: Apache-2.0
package net.consensys.linea;

import com.google.auto.service.AutoService;
import org.hyperledger.besu.plugin.BesuContext;
import org.hyperledger.besu.plugin.BesuPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Besu plugin that provides an RPC method to record and update the finalized block that can be
 * utilized by L2.
 */
@AutoService(BesuPlugin.class)
public class FinalizationUpdaterPlugin implements BesuPlugin {
  private static final Logger LOG = LoggerFactory.getLogger(FinalizationUpdaterPlugin.class);

  @Override
  public void register(final BesuContext besuContext) {
    LOG.trace("Registering plugin ...");
  }

  @Override
  public void start() {
    LOG.trace("Starting plugin ...");
  }

  @Override
  public void stop() {
    LOG.trace("Stopping plugin ...");
  }
}
