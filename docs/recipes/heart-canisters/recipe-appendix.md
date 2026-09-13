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
