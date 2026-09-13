# Extension mechanisms: initial research

Researched 2026-09-12. Orientation for v0.1 specification; no compatibility test or implementation has been performed. Sources are first-party unless labeled otherwise. Linked default branches may change.

## Wold's Vaults precedent

The project the user calls Wald's Vaults appears to be **Wold's Vaults**, maintained by iwolfking. Its [pack repository](https://github.com/iwolfking/Wolds-Vaults) describes a customized Vault Hunters 3 pack for Minecraft 1.18.2, with a separate [official mod](https://github.com/iwolfking/Wolds-Vaults-Official-Mod) providing new items, blocks, objectives, and behavior changes. The pack contains configs, data, and KubeJS scripts. This is concrete precedent for an expansion consisting of a Java addon plus a pack customization layer.

The official mod README states that new code since April 5, 2025 is GPLv3, older code is MIT, and specified BMO model/texture/animation assets are All Rights Reserved. It encourages reuse subject to its licensing. Check the exact revision and files before copying; do not assume the pack repository or all artwork has the same license. No reuse has occurred. [Author's licensing statement](https://github.com/iwolfking/Wolds-Vaults-Official-Mod#readme)

The author's [VHAPI](https://github.com/iwolfking/VHAPI) is an MIT-licensed mod intended to help extend Vault Hunters 3. It is a candidate to investigate after features are known, not a selected dependency or verified Remastered-compatible API.

## Research unlocks

Wold's live [researches.json](https://raw.githubusercontent.com/iwolfking/Wolds-Vaults/master/config/the_vault/researches.json) contains `MOD_RESEARCHES` entries with mod IDs, research names, knowledge costs, and restrictions for crafting, item use, block interaction, entity interaction, and hittability. For example, its Functional Storage entry names `functionalstorage`, costs 2 knowledge, and restricts crafting; Ars Nouveau costs 6 and also restricts item use and block interaction. These are Wold's balance choices, not proposed Expanded values or Remastered defaults.

**Inference:** adding an existing mod to research is plausibly mostly configuration, alongside its display/group/description entries. However, the chosen Remastered release's actual schema and restrictions must be checked. The research entry alone does not prove every machine, alternate recipe, or automation path obeys the intended lock. Do not promise universal config-only integration before the mod is chosen.

The [official Research wiki](https://wiki.vaulthunters.gg/Research) describes Knowledge Essence from living chests and Knowledge Star crafting using Vault Diamonds and Benitoite. Consuming a star supplies a knowledge point. This connects vault loot to unlocking mod capabilities; recipe material costs are a second progression gate after the unlock.

## Recipes

Wold's has a substantial [KubeJS compatibility-script collection](https://github.com/iwolfking/Wolds-Vaults/tree/master/kubejs/server_scripts/vh_compat), organized by integrated mods, including Functional Storage, Ars Nouveau, Mekanism, and others. Directory evidence establishes the scripting approach; individual scripts were not reliably retrievable in this pass, so no specific ingredient combination is claimed from them.

The [official KubeJS recipe documentation](https://kubejs.com/wiki/tutorials/recipes) supports adding, removing, and replacing recipes in server scripts, including the Minecraft 1.18.2 `onEvent('recipes', ...)` form. This makes JavaScript recipe scripts a credible implementation option. Choose between the target pack's existing recipe tooling and data-driven recipe files after inspecting the exact release; do not add a scripting dependency just because the reference pack uses one.

**Implementation recommendation, provisional:** prefer existing config and recipe mechanisms for costs/unlocks/recipes; use a Java addon for new behavior. A mixin is a targeted way to modify existing mod internals when no usable hook exists, not a separate language or an automatic requirement for every feature. Exact hook selection belongs to the implementation handoff after behavior is decided.

## Minimum later validation

- Pin the exact Remastered pack and core mod release, then check the selected added mod and any dependencies against it.
- Demonstrate research locked and unlocked behavior, expected displayed cost/description, and the intended recipes in-game.
- Check alternate crafting/automation paths for the selected items and multiplayer research behavior if multiplayer is in scope.
- Keep Wold's as a reference until a particular reusable component, its license, and compatibility have been checked.
