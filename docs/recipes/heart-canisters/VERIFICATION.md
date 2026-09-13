# Heart Canisters recipe handoff verification

Result: **static checks passed; no submitted recipe changed**. No game instance was launched and these files have not been installed.

The original export is preserved as `source-export.json`, SHA-256 `8acdaca7d8e9e1a0e0f84b147e86d987558693176bb51370da00c8cd2210aec7`. It contains 12 recipes: 8 replacements of all native BHC recipes and 4 new miniature-heart recipes. All outputs are existing items, all paths match their recipe IDs, all patterns and counts are valid, and recipe payloads agree with the editor data. All BHC intermediate ingredients have supplied recipes. The unused wither-bone editor warning does not prevent progression.

The paths use `data/bhc/recipes/<name>.json`, correct relative to an addon resource root or a Minecraft 1.18.2 datapack root. The whole export is a handoff manifest, not a directly installable datapack. Native recipe payloads are staged under `recipe-json/`; the builder must integrate those plus the specified drop/research controls. The local Minecraft 1.18.2 client jar's `version.json` confirms data-pack format **9**.

The staged Vault jar hash matches the pinned catalog: `fc6adfeb76071d61e633027334fc95e8efb7faf8a4cb57d646f8176b4f75390b`. All 19 distinct Vault inputs in the export have registered-item or registered-block-item evidence in that catalog. Vanilla and BHC IDs likewise match the staged catalogs. This is static evidence, not a runtime registry dump.

All five tags have matching [Forge 1.18 definitions](https://github.com/MinecraftForge/MinecraftForge/tree/1.18.x/src/generated/resources/data/forge/tags/items): string, glass, diamond gems, emerald gems, and gold storage blocks. Definitions and Git blob hashes are retained in `../forge-tag-evidence.json`; loaded modpack tag membership remains a runtime check.

The exact BHC 1.18.2 jar confirms all eight replacement IDs, absence of native downgrade recipes, the four-color +40-heart ceiling, and the heart-drop configuration. See [the artifact route audit](../../research/bhc-recipe-route-audit.md). The integration must zero ordinary heart-drop probabilities while preserving the four color maps. The `bhc:god_apple` recipe outputs a vanilla enchanted golden apple; research enforcement must cover that recipe without blocking unrelated vanilla acquisition.

User recipes are fully embedded in [Slice 3](../../specs/03-heart-canisters.md) and [GitHub #20](https://github.com/kpadams94/VH-Expanded/issues/20), formally blocked by Repulsor #19. Exact prices and output counts were preserved, including the relic-apple and enchanted-golden-apple recipes. Current Remastered script catalog inspection found no direct BHC/enchanted-golden-apple references, but final load order and generic removal rules still need in-game checks.

Remaining builder verification: installed artifact identity, nonempty loaded tags, ingredient obtainability, final JEI/recipe-manager entries, actual crafting, research gating, disabled drops, and health behavior. A static pass is not a claim that gameplay validation has passed.

Subsequent approved change: Mod Boxes now provide a research-gated alternative source for an empty amulet and miniature red/yellow/green hearts. The native weighted pool is specified in Slice 3 and `mod-box-fragment.json`. This does not change any of the twelve verified recipes or re-enable mob heart drops. Verify native Mod Box eligibility and weighted selection as part of the slice's runtime acceptance.
