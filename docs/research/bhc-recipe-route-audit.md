# Baubley Heart Canisters recipe route audit

Audited 2026-09-13 against the exact staged Minecraft 1.18.2 jar and the user's `vault-hunters-expanded-recipes (1).json`. This is static artifact verification, not an in-game compatibility claim. No recipe or specification was changed by this audit.

## Artifact and evidence

- Jar: [baubley-heart-canisters-1.18.2-1.0.0.jar](../../asset-staging/bhc/baubley-heart-canisters-1.18.2-1.0.0.jar).
- Computed SHA-256: `fada1a53f18a3fdc5199f759e634b364837559be987b5a8536d1da74aad5f988`, matching [staged provenance](../../recipe-tweaker/dist/data/bhc-provenance.json).
- Official artifact URL recorded by that provenance: [Modrinth release](https://modrinth.com/mod/baubley-heart-canisters/version/1.18.2-1.0.0).
- [All native recipe JSON extracted directly from this jar](evidence/bhc/native-recipes.json), cross-checked against [recipe-tweaker native recipe catalog](../../recipe-tweaker/dist/data/bhc-recipes.json).
- Direct bytecode evidence: [configuration, initialization, canister, miniature-heart and amulet container classes](evidence/bhc/routes.javap.txt), and [drop handler, drop configuration, health modifier and four-color enum](evidence/bhc/drops-health.javap.txt). The inspected methods/classes were emitted in full; `javap` also reported an archive-close AccessDeniedException after emission. Recipe extraction and hash verification succeeded independently.

## Recipe coverage and collision result

The jar contains exactly eight recipe IDs. The user export replaces every one under its original ID:

| Native ID | User action |
|---|---|
| `bhc:canister` | Replace |
| `bhc:red_heart_canister` | Replace |
| `bhc:yellow_heart_canister` | Replace |
| `bhc:green_heart_canister` | Replace |
| `bhc:blue_heart_canister` | Replace |
| `bhc:heart_amulet` | Replace |
| `bhc:relic_apple` | Replace |
| `bhc:god_apple` | Replace |

The four additions (`bhc:red_heart`, `bhc:yellow_heart`, `bhc:green_heart`, `bhc:blue_heart`) do not collide with native recipe IDs. Their output items already exist. No native crafting conversion, downgrade, unpacking or alternative same-output recipe was found in the exact jar. Thus retaining all *unreplaced native recipe IDs* retains zero recipes in this jar; the originals still present inside the jar must be overridden or removed under the same IDs at runtime. Adding replacement recipes under other IDs would preserve cheap native alternatives. Evidence: full native extraction above.

## Route completeness

The submitted graph has a craftable empty canister and all four miniature-heart inputs. Red canisters use a red heart and empty canister; yellow consumes a red canister; green consumes yellow; blue consumes green. The amulet is separately craftable. There is no BHC ingredient cycle, and all four color routes are supplied. This conclusion assumes the external Vault/vanilla ingredient IDs and tags pass the separate registry audit.

`bhc:wither_bone` has no recipe in the export, but none of the exported recipes consumes it. The old red-canister dependency on `forge:wither_bones` is removed. Therefore the designer's `needs_recipe` status for wither bone does **not** block this progression.

The relic apple now feeds the enchanted golden apple recipe, rather than the red canister. Neither apple recipe is needed by the exported canister chain. `bhc:god_apple` outputs `minecraft:enchanted_golden_apple`, so mod-namespace research restrictions on outputs alone do not automatically gate that recipe; verify the intended research restriction by recipe/crafting behavior during implementation.

Since upgrades consume their predecessor, filling all four slots with ten canisters each requires crafting a total of 40 red, 30 yellow, 20 green and 10 blue canisters along the way; consequently it consumes 40 red miniature hearts, 30 yellow, 20 green, 10 blue and 40 empty canisters. These are derived recipe totals, not a change in price or design.

## Acquisition bypass to disable

Recipes alone do not replace native miniature-heart drops. `BaubleyHeartCanisters.initConfig` creates `config/bhc/drops.json`; `BHCConfig` serializes a `heartEntries` map, with nested `red`, `yellow`, `green`, `blue` maps. Defaults are `red.hostile = 0.05`, `yellow.boss = 1.0`, `green.dragon = 1.0`, and `blue.minecraft:evoker = 1.0`. `DropHandler.handleEntry` iterates each nested map; `addWithPercent` samples against its value, so zero probabilities suppress these rolls. Keep all four nested maps present because the getter can otherwise return null to an unconditional `entrySet()` call. Evidence: initialization in routes bytecode and `BHCConfig`/`DropHandler` in drops-health bytecode.

A suitable implementation shape is the following; generate and verify it in the target instance, rather than assuming an absent file disables drops:

```json
{
  "heartEntries": {
    "red": { "hostile": 0.0 },
    "yellow": { "boss": 0.0 },
    "green": { "dragon": 0.0 },
    "blue": { "minecraft:evoker": 0.0 }
  }
}
```

Wither-bone drops are separate: `General.boneDropRate` defaults to `0.15`. They no longer provide a shortcut to these recipes because wither bones are not ingredients. Suppressing them is cleanup, not a missing recipe requirement. Evidence: `ConfigHandler$General` and `DropHandler.onEntityDrop`.

## Exact-jar health behavior

The jar's `HeartType` has exactly red, yellow, green and blue. The amulet container has four slots and writes `stack count * 2` health points per slot. `General.heartStackSize` defaults to 10, `BaseHeartCanister.getItemStackLimit` returns that value, and `HealthModifier.updatePlayerHealth` clamps each color contribution to twice that value. This confirms the default ceiling of **80 additional health points / 40 full hearts** for this exact jar. The modifier uses `ADDITION` with identifier `bhc:extra_hearts`. Source: the two retained bytecode evidence files.

Miniature hearts also retain a native consumable-healing behavior: `ItemHeart` consumes one item to heal according to its color. That does not grant lasting maximum health and does not bypass the amulet ceiling; the integration must distinguish it from canister equipment. This audit does not propose disabling existing healing behavior or introduce new scope. Source: `ItemHeart` in routes bytecode and `HeartType` in drops-health bytecode.

## Remaining runtime acceptance checks

Verify all twelve submitted crafting rules in JEI and actual crafting; reject the eight cheap native recipes; verify heart drops are absent; verify research-gated crafting/equipment; verify a fully loaded amulet gives +40 full hearts, with removal and re-equip behavior preserved. Existing Remastered scripts or other installed mods could introduce additional recipes/acquisition routes beyond this BHC-only artifact audit.
