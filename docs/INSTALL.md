# Install Vault Hunters Expanded 0.1.0

Release bundle: `vault-hunters-expanded-0.1.0.zip`. Addon: `vh-expanded-0.1.0.jar`.

## Requirements

Use Vault Hunters Third Edition — Remastered **2.0.4**, Minecraft **1.18.2**, Forge **40.3.11**, and **Java 17**. This release was player-tested solo in a fresh test world. Existing-world migration and multiplayer certification are deferred.

Install [Baubley Heart Canisters 1.18.2-1.0.0](https://www.curseforge.com/minecraft/mc-mods/baubley-heart-canisters/files/3772892) separately. Keep the pack's existing Curios **1.18.2-5.0.9.2**. Third-party mod jars are not included in the release bundle.

## Install or update

1. Close Minecraft. Open your separate Remastered test profile's folder through the launcher's Open Folder command; it contains `mods`, `config`, and `saves`.
2. Extract the release ZIP. Replace previous `vh-expanded-*.jar` versions in the profile's `mods` directory with **`mods/vh-expanded-0.1.0.jar`** from the ZIP. Keep exactly one Expanded jar. If updating from the accepted Heart Canisters revision 2 build, this is the final version name for the same accepted gameplay.
3. Keep or install the exact BHC dependency above in `mods`. Keep the existing compatible Curios jar; do not add a duplicate.
4. Copy **`config/bhc/drops.json`** from the ZIP into the profile at the same path. This disables ordinary miniature-heart mob drops while retaining the approved Mod Box source. If updating from the accepted slice build, the existing matching file can remain.
5. Launch the pack. Recipes, research, icons, and the required built-in recipe pack are included in the addon; no manual recipe datapack or script is needed. The mod list should show **Vault Hunters Expanded 0.1.0**.

Keep any accepted Repulsor tuning. `optional-config/vhexpanded-repulsor.json` is a reference/default file, not a mandatory overwrite. After the code is installed, `/vhexpanded repulsor reload` reloads tuning. Optional editable visuals are under `resourcepacks`; the built-in icon works without them. Disable old visual override packs if they obscure the accepted icon.

## Release contents and verification

The bundle contains the addon, BHC drop overlay, approved recipe references, optional tuning/visuals, compatibility and validation notes, and `SHA256SUMS.txt`. Verify the checksums when needed. Feature-specific guides describe earlier slice handoffs as historical evidence; this file controls the final release installation.

The user confirmed the final Heart Canisters retest, including the follow-up vault-use and QOL layout checks. Automated native checks and their limits are described in `TEST-EVIDENCE.md` and `RELEASE.md`. Developer selftests use disposable fixtures and belong only in a disposable test world.

To remove the addon from a disposable test copy, close Minecraft and restore the prior profile or backup. Recreate a disposable world containing removed custom items/skills rather than treating removal as a supported migration. Keep the original pack profile intact.
