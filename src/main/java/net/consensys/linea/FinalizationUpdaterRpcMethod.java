// Copyright 2024, Consensys Software Inc.
// SPDX-License-Identifier: Apache-2.0
package net.consensys.linea;

import java.util.Optional;
import org.hyperledger.besu.ethereum.api.jsonrpc.internal.parameters.JsonRpcParameter;
import org.hyperledger.besu.ethereum.api.jsonrpc.internal.response.RpcErrorType;
import org.hyperledger.besu.plugin.data.BlockContext;
import org.hyperledger.besu.plugin.services.BlockchainService;
import org.hyperledger.besu.plugin.services.exception.PluginRpcEndpointException;
import org.hyperledger.besu.plugin.services.rpc.PluginRpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the RPC method: {@code linea_updateFinalizedBlockV1(finalizedBlockNumber:
 * Long):Boolean }
 */
public class FinalizationUpdaterRpcMethod {
  private static final Logger LOG = LoggerFactory.getLogger(FinalizationUpdaterRpcMethod.class);
  static final String RPC_NAMESPACE = "linea_ipc";
  static final String RPC_METHOD_NAME = "updateFinalizedBlockV1";

  private final BlockchainService blockchainService;
  private final JsonRpcParameter parameterParser = new JsonRpcParameter();

  /**
   * Constructor for the FinalizationUpdaterRpcMethod.
   *
   * @param blockchainService An instance of the BlockchainService.
   */
  public FinalizationUpdaterRpcMethod(final BlockchainService blockchainService) {
    LOG.trace("FinalizationUpdaterRpcMethod constructor called");
    this.blockchainService = blockchainService;
  }

  /**
   * Executes the RPC method. This method will be called by Besu RPC service.
   *
   * @param request Instance of PluginRpcRequest. The request object contains the parameters passed.
   * @return Boolean.TRUE if the finalized block number is successfully updated.
   * @throws PluginRpcEndpointException if the block number is invalid or the block is not found.
   */
  public Boolean execute(final PluginRpcRequest request) {
    LOG.trace("FinalizationUpdaterRpcMethod execute called");

    final Long finalizedBlockNumber = parseResult(request);

    // lookup finalized block by number in local chain
    final Optional<BlockContext> finalizedBlock =
        blockchainService.getBlockByNumber(finalizedBlockNumber);
    if (finalizedBlock.isEmpty()) {
      throw new PluginRpcEndpointException(
          RpcErrorType.BLOCK_NOT_FOUND,
          "Block not found in the local chain: " + finalizedBlockNumber);
    }

    // Persist the finalized block number
    blockchainService.setFinalizedBlock(finalizedBlock.get().getBlockHeader().getBlockHash());

    return Boolean.TRUE;
  }

  private Long parseResult(final PluginRpcRequest request) {
    Long blockNumber;
    try {
      final Object[] params = request.getParams();
      blockNumber = parameterParser.required(params, 0, Long.class);
    } catch (final Exception e) {
      throw new PluginRpcEndpointException(RpcErrorType.INVALID_PARAMS, e.getMessage());
    }

    if (blockNumber <= 0) {
      throw new PluginRpcEndpointException(
          RpcErrorType.INVALID_PARAMS, "Block number must be greater than 0");
    }

    return blockNumber;
  }
}
