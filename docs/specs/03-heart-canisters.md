# Slice 3 — Baubley Heart Canisters progression integration

**RECIPE HANDOFF RECEIVED.** The user supplied all twelve recipe designs. Static verification passed without recipe changes. This slice is ready for implementation after Repulsor; runtime acceptance remains required.

## Shared contract and prerequisite

Shared foundation: [#17](https://github.com/kpadams94/VH-Expanded/issues/17). Delivery prerequisite: [Repulsor #19](https://github.com/kpadams94/VH-Expanded/issues/19). Begin this slice only after the preceding handoff and the user requests implementation.

Read `docs/specs/00-core.md` before implementation. Build on the delivered Experienced and Repulsor slices in their pinned Remastered 2.0.4 fresh solo environment. Retain both features. Multiplayer and existing-world migration are deferred. This is the third independent playable handoff.

## Player behavior

- Add **Baubley Heart Canisters** with one research unlock named for the mod, covering all four canister colors.
- Research costs **2 knowledge points** and belongs to the existing **Quality of Life (QOL)** category. It is accessible early. Later colors require no additional research unlocks; crafting costs control advancement.
- Preserve the mod's equipped **Heart Amulet** mechanic. Canisters are lasting reusable equipment investments; extra health applies while the amulet is equipped and is removed on unequip. They are not consumed innate character upgrades.
- Retain four colors and ten canisters per color, totaling **40 additional full hearts**, conditional on confirming the target artifact's native behavior. If that behavior differs, report the mismatch before changing the agreed health progression.
- Use expensive crafting with the user's approved vault materials plus the explicitly approved research-gated Mod Box rewards below. Disable ordinary mob heart drops; Mod Box rewards are an intentional additional source, not a bypass to remove. Native recipes that bypass the approved costs must be removed or replaced. Exact intermediate items and recipes belong to the appendix.
- Initial engineering policy for research: gate both crafting and gaining the amulet's health benefit, using native research restriction mechanisms where they cover Curios equip/use. Verify that finding or receiving an amulet without research does not grant the locked benefit. This enforces the intended unlock even in solo test commands/loot paths; it is not multiplayer certification.
- Preserve the current pack's health display and normal interaction with gear, talents, healing, save/reload, and death/respawn. Additional health-display mods are not required unless an observed incompatibility needs one.
- A future **Enhancement** research category is only a tentative roadmap idea. Keep this slice in QOL.

## Dependency and integration plan

Candidate: [Baubley Heart Canisters 1.18.2-1.0.0, file 3772892](https://www.curseforge.com/minecraft/mc-mods/baubley-heart-canisters/files/3772892). Verify this jar and its dependencies against the pinned pack. The mod requires Curios; reuse the pack's compatible installation where present. Record artifact identity and hashes in the shared compatibility note.

The exact staged 1.18.2 jar was inspected for this recipe handoff: SHA-256 `fada1a53f18a3fdc5199f759e634b364837559be987b5a8536d1da74aad5f988`. It confirms four color slots, ten canisters per color and two health points per canister, for +40 full hearts. Its eight native crafting recipes and drop configuration were inspected directly. Full evidence is in `docs/research/bhc-recipe-route-audit.md`. Recheck installed artifact identity before implementation.

1. Verify the candidate, Curios slot behavior, native cap, health arithmetic, drop controls, and recipes. Inventory all relevant recipes/drop sources by exact item/recipe ID. Completion evidence: a concrete integration inventory tied to the installed jar.
2. Add the single QOL research entry and required display/group/restriction entries through the shared owned-config mechanism. Confirm the displayed and charged cost of two and inspect category cost-modifier interactions; do not silently change that cost or unrelated QOL research.
3. Install the approved appendix recipes through the pack's established recipe tooling, removing bypass recipes and ordinary heart drops. Preserve required JSON structures even when setting drop chances to zero. No mixin is needed solely because a script or config file is involved.
4. Verify research enforcement on both crafting and equipment benefit. Use a narrow addon integration only if native restrictions cannot enforce the intended rule.
5. Merge the native Mod Box research pool described below through the existing owned-configuration mechanism. This loot addition requires configuration, not a custom chance hook.
6. Exercise health stacking/persistence and Mod Box behavior, then deliver a packaged build with focused regressions for Experienced and Repulsor.

## Mod Box integration — native weighted research pool

After the player unlocks **Baubley Heart Canisters**, native Mod Boxes can award one empty Heart Amulet or one miniature red, yellow, or green heart. Before research, none of these BHC rewards is eligible. This concerns `the_vault:mod_box`, not mystery boxes, Pandora's Boxes, or other loot containers. Blue hearts, filled canisters, wither bones and apples are not added to the Mod Box pool.

The user's initial approximate targets were amulet 1%, red 10%, yellow 1%, and green 0.01%. After reviewing the actual system, the user explicitly chose **native weighted pools through configuration only**, preserving relative rarity while allowing per-box percentages to vary with unlocked research. Those original percentages are relative targets, not fixed per-box acceptance thresholds.

### Verified native behavior

The pinned Vault jar's `GatedLootableItem.use` reads the opening player's `ResearchTree.getResearchesDone()`. `ModBoxConfig.collectPools` finds entries by exact research name, selects one eligible mod pool by its weight, then selects one product from that pool by item weight. The `None` pool is a fallback when no product can be selected; it is not an always-eligible extra competitor. The method generates/delivers the selected stack and consumes one box after a nonempty reward. Retain these native semantics.

The actual Remastered 2.0.4 archive contains `overrides/config/the_vault/mod_box.json`, installed as `config/the_vault/mod_box.json`. All existing mod pools in this file have weight 1. Add only the owned BHC pool, with weight 1, and retain all other pools and the fallback unchanged. The pool key must exactly match the registered research name **Baubley Heart Canisters**, not the `bhc` mod namespace or the QOL category name.

| Reward | Registry ID | Quantity | Item weight | Chance when BHC pool is selected |
| --- | --- | --- | --- | --- |
| Empty Heart Amulet | `bhc:heart_amulet` | 1 | 100 | 8.3264% |
| Miniature Red Heart | `bhc:red_heart` | 1 | 1000 | 83.2639% |
| Miniature Yellow Heart | `bhc:yellow_heart` | 1 | 100 | 8.3264% |
| Miniature Green Heart | `bhc:green_heart` | 1 | 1 | 0.083264% |

Total BHC item weight is **1201**. Unconditional probability is `(BHC pool weight / total eligible mod-pool weight) * (item weight / 1201)`. With N equally weighted eligible pools including BHC, divide the last column by N. For N=8, probabilities per box are approximately 1.0408% amulet, 10.4080% red, 1.0408% yellow, and 0.010408% green. If BHC is the only eligible pool, every successful Mod Box draws a BHC reward. Merely unlocking a research with no Mod Box pool does not increase N. Do not label these as fixed percentages in UI or documentation.

### Exact configuration fragment

The following fragment is to merge into `config/the_vault/mod_box.json`, not replace the whole file. It is also saved at `docs/recipes/heart-canisters/mod-box-fragment.json`. Apply it after configuration loading and before box use through the shared owned-config integration, or through the documented pack overlay installation process. Repeated application must be idempotent. A valid pool includes its `entries` list and pool-level `weight`; no new item registration is required.

```json
{
  "POOL": {
    "Baubley Heart Canisters": {
      "entries": [
        {"value": {"id": "bhc:heart_amulet", "amountMin": 1, "amountMax": 1}, "weight": 100},
        {"value": {"id": "bhc:red_heart", "amountMin": 1, "amountMax": 1}, "weight": 1000},
        {"value": {"id": "bhc:yellow_heart", "amountMin": 1, "amountMax": 1}, "weight": 100},
        {"value": {"id": "bhc:green_heart", "amountMin": 1, "amountMax": 1}, "weight": 1}
      ],
      "weight": 1
    }
  }
}
```

Use the existing native selection algorithm rather than adding independent rolls, a bonus second drop, or a fixed-chance replacement hook. Native research gating supplies eligibility. The amulet reward has no preloaded heart inventory. Ordinary mob heart drops remain zero as already specified; recipes remain unchanged.

The inspected native Unboxing Station accepts booster packs and jewel pouches, not Mod Boxes; adding Mod Box station support is outside scope. Verify manual native opening and any already-supported player-context opening path without introducing new automation features. Research evidence and exact bytecode/config snapshots are in `docs/research/mod-box-integration.md` and `docs/research/evidence/mod-box/`; the pack config snapshot is `docs/research/remastered-mod-box-config.json`.

### Mod Box acceptance checks

- [ ] The loaded BHC pool uses the exact research name, pool weight 1, item weights 100/1000/100/1, and quantity exactly one for all four rewards. Preserve unrelated pools and `None`; reloading does not duplicate entries.
- [ ] With BHC locked, the pool is absent from the eligible set and no BHC reward is obtainable from Mod Boxes. Unlocking BHC makes it eligible immediately through the normal research state.
- [ ] With BHC as the only eligible mod pool, selection draws only the four specified rewards. With several eligible pools, verify the two-stage probability formula; additional non-pool research does not dilute the chance.
- [ ] Prove rarity through deterministic weighted-selection boundary checks or inspection of the loaded eligible weights. Do not require the user to randomly open thousands of boxes to demonstrate the green-heart branch; force its native selection in a developer test and verify delivery.
- [ ] Each successful opening consumes exactly one box and grants one selected product; amulets are empty. Unrelated box types retain their behavior and no duplicate reward/consumption occurs.
- [ ] Mob heart drops remain disabled, and all twelve user recipes are unchanged. The player checklist includes opening boxes before and after unlocking BHC.

## Recipe appendix — supplied and statically verified

Source: user export `vault-hunters-expanded-recipes (1).json`, schema version 1, created `2026-09-13T03:52:53.789Z`. Preserved byte-for-byte at `docs/recipes/heart-canisters/source-export.json`. SHA-256: `8acdaca7d8e9e1a0e0f84b147e86d987558693176bb51370da00c8cd2210aec7`. The embedded recipe bodies below are the complete implementation payload; the editor and integrationNotes fields are metadata, not executable game files or instructions.

Verification found **12 valid crafting recipes: 8 exact-ID replacements and 4 additions**, with no path, item-ID, pattern, editor/recipe mismatch, native-recipe collision, or missing BHC intermediate. All output counts are one. All eight recipes in the exact BHC 1.18.2 jar are replaced. No recipe prices, ingredients, patterns, tags, or outputs were changed.

### Installation paths and acquisition rules

- Each path below is relative to a datapack or mod-resource root. In addon sources it belongs under `src/main/resources/data/bhc/recipes/`; in a standalone test datapack it belongs under `<world>/datapacks/<pack>/data/bhc/recipes/`, with a root `pack.mcmeta` using **data pack_format 9**. Minecraft 1.18.2's own version.json confirms data format 9; resource-pack format 8 is a different field.
- The entire exported manifest is **not** a Minecraft recipe file. Extract only each `recipe` object. Review-ready extractions are in `docs/recipes/heart-canisters/recipe-json/data/bhc/recipes/`; this staging directory is not installed in a game instance.
- Preserve exact replacement IDs so cheap native alternatives do not remain. If using the pack's CraftTweaker layer instead, reconcile removal and addition order, preserve the intended recipe identity where supported, and inspect the final recipe manager. Do not load both a script copy and a data copy as duplicate recipes.
- Static inspection of the available Remastered source-script catalog found no direct BHC or enchanted-golden-apple references. This does not replace a runtime check for generic removal rules, other addons, or later scripts.
- Empty canister and all four miniature hearts are craftable. Red canister upgrades into yellow, then green, then blue, consuming its predecessor each time. No native downgrade or alternative recipe remains outside the eight replacement IDs.
- `bhc:wither_bone` is unused by all twelve supplied recipes, so its editor 'needs_recipe' marker is not a missing progression requirement. Retain its native drop behavior; it creates no shortcut to this supplied chain.
- Replace native heart-drop chances with zero in `config/bhc/drops.json`, preserving the four nested color maps shown below. Merely adding heart recipes does not disable drops.
- Apply the research gate to the supplied crafting recipes, including `bhc:god_apple`, whose output is the existing vanilla `minecraft:enchanted_golden_apple`. Output-namespace checks alone are insufficient for that recipe. Do not register a new god-apple item or restrict unrelated vanilla acquisition of enchanted golden apples.
- Retain native miniature-heart healing behavior and amulet-based maximum health. This recipe handoff changes crafting and heart drops, not those mechanics.

```json
{"heartEntries":{"red":{"hostile":0.0},"yellow":{"boss":0.0},"green":{"dragon":0.0},"blue":{"minecraft:evoker":0.0}}}
```

### Recipe inventory

| Recipe ID | Action | Ingredients (each count shown; # means item tag) |
| --- | --- | --- |
| `bhc:canister` | replace | 4 × `the_vault:chromatic_iron_ingot`; 1 × `the_vault:perfect_larimar` |
| `bhc:red_heart` | add | 1 × `the_vault:vault_essence`; 1 × `minecraft:apple`; 1 × `the_vault:vault_meat` |
| `bhc:red_heart_canister` | replace | 1 × `bhc:red_heart`; 1 × `bhc:canister`; 1 × `the_vault:chromatic_iron_ingot` |
| `bhc:yellow_heart_canister` | replace | 1 × `bhc:yellow_heart`; 1 × `bhc:red_heart_canister`; 1 × `the_vault:chromatic_steel_ingot` |
| `bhc:green_heart_canister` | replace | 1 × `bhc:green_heart`; 1 × `bhc:yellow_heart_canister`; 1 × `the_vault:black_chromatic_steel_ingot`; 1 × `minecraft:nether_star` |
| `bhc:blue_heart_canister` | replace | 1 × `bhc:blue_heart`; 1 × `bhc:green_heart_canister`; 1 × `the_vault:echoing_ingot`; 1 × `minecraft:netherite_block` |
| `bhc:heart_amulet` | replace | 2 × `#forge:string`; 3 × `#forge:glass`; 1 × `the_vault:gem_pog` |
| `bhc:yellow_heart` | add | 1 × `the_vault:extraordinary_larimar`; 1 × `minecraft:golden_carrot`; 1 × `the_vault:vault_meat_block` |
| `bhc:green_heart` | add | 1 × `the_vault:gem_pog`; 1 × `minecraft:golden_apple`; 1 × `the_vault:vault_diamond_block` |
| `bhc:blue_heart` | add | 1 × `the_vault:echo_pog`; 1 × `the_vault:extraordinary_larimar`; 1 × `the_vault:extraordinary_benitoite`; 1 × `the_vault:extraordinary_alexandrite`; 1 × `the_vault:extraordinary_painite`; 1 × `the_vault:extraordinary_black_opal`; 1 × `the_vault:sour_orange` |
| `bhc:god_apple` | replace | 4 × `the_vault:dreamstone`; 4 × `#forge:storage_blocks/gold`; 1 × `bhc:relic_apple` |
| `bhc:relic_apple` | replace | 2 × `the_vault:perfect_larimar`; 2 × `#forge:gems/diamond`; 2 × `the_vault:perfect_alexandrite`; 2 × `#forge:gems/emerald`; 1 × `minecraft:golden_apple` |

The relic-apple and enchanted-golden-apple recipes are included as supplied, even though the new canister chain does not consume them. Upgrading consumes lower-tier canisters: filling all four amulet slots with ten each requires 40 red hearts, 30 yellow hearts, 20 green hearts, 10 blue hearts and 40 empty canisters in total, plus the listed upgrade ingredients. This is a derived cost, not an adjustment.

### Exact recipe payloads

#### bhc:canister

Action: **replace**. Path: `data/bhc/recipes/canister.json`. Output: `bhc:canister` × 1.

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    " A ",
    "ABA",
    " A "
  ],
  "key": {
    "A": {
      "item": "the_vault:chromatic_iron_ingot"
    },
    "B": {
      "item": "the_vault:perfect_larimar"
    }
  },
  "result": {
    "item": "bhc:canister",
    "count": 1
  }
}
```

#### bhc:red_heart

Action: **add**. Path: `data/bhc/recipes/red_heart.json`. Output: `bhc:red_heart` × 1.

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    {
      "item": "the_vault:vault_essence"
    },
    {
      "item": "minecraft:apple"
    },
    {
      "item": "the_vault:vault_meat"
    }
  ],
  "result": {
    "item": "bhc:red_heart",
    "count": 1
  }
}
```

#### bhc:red_heart_canister

Action: **replace**. Path: `data/bhc/recipes/red_heart_canister.json`. Output: `bhc:red_heart_canister` × 1.

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    {
      "item": "bhc:red_heart"
    },
    {
      "item": "bhc:canister"
    },
    {
      "item": "the_vault:chromatic_iron_ingot"
    }
  ],
  "result": {
    "item": "bhc:red_heart_canister",
    "count": 1
  }
}
```

#### bhc:yellow_heart_canister

Action: **replace**. Path: `data/bhc/recipes/yellow_heart_canister.json`. Output: `bhc:yellow_heart_canister` × 1.

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    {
      "item": "bhc:yellow_heart"
    },
    {
      "item": "bhc:red_heart_canister"
    },
    {
      "item": "the_vault:chromatic_steel_ingot"
    }
  ],
  "result": {
    "item": "bhc:yellow_heart_canister",
    "count": 1
  }
}
```

#### bhc:green_heart_canister

Action: **replace**. Path: `data/bhc/recipes/green_heart_canister.json`. Output: `bhc:green_heart_canister` × 1.

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    {
      "item": "bhc:green_heart"
    },
    {
      "item": "bhc:yellow_heart_canister"
    },
    {
      "item": "the_vault:black_chromatic_steel_ingot"
    },
    {
      "item": "minecraft:nether_star"
    }
  ],
  "result": {
    "item": "bhc:green_heart_canister",
    "count": 1
  }
}
```

#### bhc:blue_heart_canister

Action: **replace**. Path: `data/bhc/recipes/blue_heart_canister.json`. Output: `bhc:blue_heart_canister` × 1.

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    {
      "item": "bhc:blue_heart"
    },
    {
      "item": "bhc:green_heart_canister"
    },
    {
      "item": "the_vault:echoing_ingot"
    },
    {
      "item": "minecraft:netherite_block"
    }
  ],
  "result": {
    "item": "bhc:blue_heart_canister",
    "count": 1
  }
}
```

#### bhc:heart_amulet

Action: **replace**. Path: `data/bhc/recipes/heart_amulet.json`. Output: `bhc:heart_amulet` × 1.

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "A A",
    "BCB",
    " B "
  ],
  "key": {
    "A": {
      "tag": "forge:string"
    },
    "B": {
      "tag": "forge:glass"
    },
    "C": {
      "item": "the_vault:gem_pog"
    }
  },
  "result": {
    "item": "bhc:heart_amulet",
    "count": 1
  }
}
```

#### bhc:yellow_heart

Action: **add**. Path: `data/bhc/recipes/yellow_heart.json`. Output: `bhc:yellow_heart` × 1.

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    {
      "item": "the_vault:extraordinary_larimar"
    },
    {
      "item": "minecraft:golden_carrot"
    },
    {
      "item": "the_vault:vault_meat_block"
    }
  ],
  "result": {
    "item": "bhc:yellow_heart",
    "count": 1
  }
}
```

#### bhc:green_heart

Action: **add**. Path: `data/bhc/recipes/green_heart.json`. Output: `bhc:green_heart` × 1.

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    {
      "item": "the_vault:gem_pog"
    },
    {
      "item": "minecraft:golden_apple"
    },
    {
      "item": "the_vault:vault_diamond_block"
    }
  ],
  "result": {
    "item": "bhc:green_heart",
    "count": 1
  }
}
```

#### bhc:blue_heart

Action: **add**. Path: `data/bhc/recipes/blue_heart.json`. Output: `bhc:blue_heart` × 1.

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    {
      "item": "the_vault:echo_pog"
    },
    {
      "item": "the_vault:extraordinary_larimar"
    },
    {
      "item": "the_vault:extraordinary_benitoite"
    },
    {
      "item": "the_vault:extraordinary_alexandrite"
    },
    {
      "item": "the_vault:extraordinary_painite"
    },
    {
      "item": "the_vault:extraordinary_black_opal"
    },
    {
      "item": "the_vault:sour_orange"
    }
  ],
  "result": {
    "item": "bhc:blue_heart",
    "count": 1
  }
}
```

#### bhc:god_apple

Action: **replace**. Path: `data/bhc/recipes/god_apple.json`. Output: `minecraft:enchanted_golden_apple` × 1.

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "ABA",
    "BCB",
    "ABA"
  ],
  "key": {
    "A": {
      "item": "the_vault:dreamstone"
    },
    "B": {
      "tag": "forge:storage_blocks/gold"
    },
    "C": {
      "item": "bhc:relic_apple"
    }
  },
  "result": {
    "item": "minecraft:enchanted_golden_apple",
    "count": 1
  }
}
```

#### bhc:relic_apple

Action: **replace**. Path: `data/bhc/recipes/relic_apple.json`. Output: `bhc:relic_apple` × 1.

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "ABC",
    "DED",
    "CBA"
  ],
  "key": {
    "A": {
      "item": "the_vault:perfect_larimar"
    },
    "B": {
      "tag": "forge:gems/diamond"
    },
    "C": {
      "item": "the_vault:perfect_alexandrite"
    },
    "D": {
      "tag": "forge:gems/emerald"
    },
    "E": {
      "item": "minecraft:golden_apple"
    }
  },
  "result": {
    "item": "bhc:relic_apple",
    "count": 1
  }
}
```

### Verification boundary

Static path/schema/editor agreement, catalog IDs, exact BHC recipe inventory, native health cap and drop controls were checked. Forge 1.18 source definitions corroborate the five tags; current loaded tag contents, ingredient obtainability, research gates and actual crafting still require the runtime acceptance tests below. No game instance was launched and no recipe was installed by this handoff.

## Acceptance and player test

- [ ] Candidate mod and dependencies load with the packaged Expanded overlay in the pinned fresh solo instance.
- [ ] One QOL research unlock displays and charges two knowledge points and covers all colors. No extra tier unlocks are introduced; unrelated research remains unchanged.
- [ ] Locked crafting and amulet benefit are prevented; purchasing research enables the intended paths. Repeated starts do not duplicate research entries.
- [ ] Every approved recipe matches the appendix in the recipe viewer and actual crafting; removed recipes and ordinary drop routes cannot bypass it. Ingredients are obtainable in the target pack.
- [ ] Each color and maximum capacity grant the approved health total. The equipped amulet requirement, unequip removal, and capacity limits work.
- [ ] Verify extra health with representative Vault gear/talents, wounded equip/unequip, healing, death/respawn, vault entry/exit, and save/reload; no duplicate permanent bonuses or health corruption appears.
- [ ] Experienced and Repulsor pass focused regression checks.
- [ ] Deliver jar/overlay/dependency instructions, recipe manifest, evidence, and player checklist: unlock research, craft the amulet and each tier, equip, compare health, remove, reload, and test a vault.

The recipe-creation hold is satisfied by the supplied export. This slice is not complete until the recipes and integration are implemented and pass the runtime acceptance checks.
