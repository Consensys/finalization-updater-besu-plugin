# Finalization Updater Besu Plugin

A [Besu plugin][1] that provides an RPC method to record and update the finalized block that can be utilized by L2.

![GitHub Actions Workflow Status](https://github.com/Consensys/finalization-updater-besu-plugin/actions/workflows/ci.yml/badge.svg?branch=main)

## Build Instructions
You can either use pre-built jar from Assets section in [releases][2] or build it yourself.

> [!NOTE] 
> This project requires Java 21 or later. If it is not available, the gradle build will attempt to download one and use it.

- Checkout the project:
```shell
git clone https://github.com/Consensys/finalization-updater-besu-plugin.git
```

- Check [Besu releases][3] for latest stable version and update it in [`gradle/libs.versions.toml`](gradle/libs.versions.toml). For example:

```toml
[versions]
besu = "24.6.0"
```

- Build the plugin:

```shell
./gradlew clean build
```

The plugin jar will be available at `build/libs/finalization-updater-besu-plugin-<version>.jar`.

## Usage

Drop the `finalization-updater-besu-plugin-<version>.jar` in the `/plugins` folder under Besu installation. This plugin 
will expose following RPC methods:
```shell
linea_ipc_updateFinalizedBlockV1(finalizedBlockNumber: Long): Boolean
```

## License
`SPDX-License-Identifier: Apache-2.0`
- Apache License, Version 2.0, ([LICENSE-APACHE](LICENSE) or <http://www.apache.org/licenses/LICENSE-2.0>) 

### Contribution

Unless you explicitly state otherwise, any contribution intentionally submitted for inclusion in the work by you, as 
defined in the Apache-2.0 license, shall be licensed as above, without any additional terms or conditions.

See [CONTRIBUTING.md](CONTRIBUTING.md) for more details.

[1]: <https://besu.hyperledger.org/private-networks/reference/plugin-api-interfaces>
[2]: <https://github.com/Consensys/finalization-updater/releases>
[3]: <https://github.com/hyperledger/besu/releases>