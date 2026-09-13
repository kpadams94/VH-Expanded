# Vault Hunters Expanded

**Version 0.1.0** — an expansion for Vault Hunters Third Edition — Remastered 2.0.4.

## Features

- **Experienced:** five one-point expertise ranks grant 10–50% more personal vault-run XP, applied after other bonuses. Vanilla XP and XP items remain unaffected.
- **Repulsor:** a Mana Shield-family specialization that pushes mobs outward and leaves a brief stationary barrier. Includes native area-of-effect, mana and cooldown modifiers, over-level scaling, and configurable tuning.
- **Baubley Heart Canisters:** a two-point QOL research unlock, twelve vault-material recipes, up to 40 extra full hearts through an equipped amulet, and native weighted Mod Box rewards. Ordinary miniature-heart mob drops are disabled; consuming miniature hearts is blocked inside vaults.

## Install

Use **Minecraft 1.18.2, Forge 40.3.11, Java 17, and Remastered 2.0.4**. Install Baubley Heart Canisters **1.18.2-1.0.0** separately and retain the pack's Curios **1.18.2-5.0.9.2**.

The release bundle is `build/distributions/vault-hunters-expanded-0.1.0.zip`. It includes `mods/vh-expanded-0.1.0.jar` and the required `config/bhc/drops.json` overlay. Replace older Expanded jars so only one is installed. No separate recipe datapack or script is needed.

Follow the [installation guide](docs/INSTALL.md). See the [changelog](CHANGELOG.md) and [release record](docs/RELEASE-0.1.0.md).

## Validation and support

All three features and the final Heart Canisters follow-up retest were accepted by the player. Automated build and full-pack native checks are documented in the release record. Player acceptance and automated harness results are recorded separately.

This version targets solo play in a fresh test world. Existing-world migration and multiplayer certification remain deferred, even though developer checks use a full-pack server harness.

## Build

Set `JAVA_HOME` to a Java 17 JDK, then run:

```powershell
.\gradlew.bat build releaseBundle
```

Outputs: `build/libs/vh-expanded-0.1.0.jar` and `build/distributions/vault-hunters-expanded-0.1.0.zip`. The bundle includes file checksums. The former `playtestBundle` task remains an alias for the release bundle.

For future changes, read the [development and release guide](docs/DEVELOPMENT.md). Current dependency and hook details are in the [compatibility record](docs/heart-canisters-compatibility.md). The [individual specifications](docs/specs/README.md) and feature test records preserve the initial design and playtest history.

The separate local recipe-tweaker repository is an authoring tool, not a runtime dependency. Downloaded mod jars, extracted assets, local runtime instances, and generated archives are excluded from source control and the addon distribution.
