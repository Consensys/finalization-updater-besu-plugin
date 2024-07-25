// Copyright 2024, Consensys Software Inc.
// SPDX-License-Identifier: Apache-2.0
package net.consensys.linea;

import com.google.auto.service.AutoService;
import org.hyperledger.besu.plugin.BesuContext;
import org.hyperledger.besu.plugin.BesuPlugin;
import org.hyperledger.besu.plugin.services.BlockchainService;
import org.hyperledger.besu.plugin.services.RpcEndpointService;
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

    final RpcEndpointService rpcEndpointService =
        besuContext
            .getService(RpcEndpointService.class)
            .orElseThrow(
                () ->
                    new RuntimeException(
                        "Failed to obtain RpcEndpointService from the BesuContext."));

    final BlockchainService blockchainService =
        besuContext
            .getService(BlockchainService.class)
            .orElseThrow(
                () ->
                    new RuntimeException(
                        "Failed to obtain BlockchainService from the BesuContext."));

    final FinalizationUpdaterRpcMethod finalizationUpdaterRpcMethod =
        new FinalizationUpdaterRpcMethod(blockchainService);
    rpcEndpointService.registerRPCEndpoint(
        FinalizationUpdaterRpcMethod.RPC_NAMESPACE,
        FinalizationUpdaterRpcMethod.RPC_METHOD_NAME,
        finalizationUpdaterRpcMethod::execute);
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
