# Baubley Heart Canisters: v0.1 integration research

Researched 2026-09-12. Discovery only; no mod installation, implementation, or game compatibility test performed. User direction: expensive health upgrades crafted with vault resources, integrated into knowledge progression. Exact recipes are deferred to the user's recipe designer.

Subsequent interview update: Q7 explicitly retains the native equipped Heart Amulet requirement. The equipment-versus-innate question below is resolved in favor of equipment; no consumable innate health system is in scope.

## Compatibility and source confidence

- An official **Minecraft 1.18.2 / Forge** release exists: `baubley-heart-canisters-1.18.2-1.0.0.jar`, released April 29, 2022, CurseForge project 282947 / file 3772892. This is a concrete candidate for Remastered, not a tested compatibility claim. [Exact release](https://www.curseforge.com/minecraft/mc-mods/baubley-heart-canisters/files/3772892)
- The current author description requires **Curios** for 1.16+ and describes Mantle/Health Overlay as optional display support. Do not install a second health display mod without checking the existing pack UI. [Author description](https://www.curseforge.com/minecraft/mc-mods/baubley-heart-canisters)
- Source inspection used the author's `1.18` branch, tree `9a1499310f383b84af929e83871ea32468a8f5c4`. Its build targets Minecraft 1.18 / Java 17 and its properties say `1.18-1.0.1`; **it is not proven identical to the later 1.18.2 binary**. Mechanics below are strong historical guidance and must be checked against the selected jar before implementation. [Build](https://github.com/Traverse-Joe/Baubley-Heart-Canisters/blob/1.18/build.gradle), [Properties](https://github.com/Traverse-Joe/Baubley-Heart-Canisters/blob/1.18/gradle.properties)

## Main product distinction: equipment versus innate upgrades

The mod's ordinary mechanic stores canisters inside a **Heart Amulet**, worn through Curios. The examined source applies a maximum-health bonus on equip and removes it on unequip. These are persistent equipment benefits, not consumed upgrades permanently attached to the character. The method name `addPermanentModifier` is an implementation detail and does not change the explicit unequip removal. [HealthModifier](https://github.com/Traverse-Joe/Baubley-Heart-Canisters/blob/1.18/src/main/java/com/traverse/bhc/common/util/HealthModifier.java), [Amulet](https://github.com/Traverse-Joe/Baubley-Heart-Canisters/blob/1.18/src/main/java/com/traverse/bhc/common/items/ItemHeartAmulet.java)

The examined container has four color slots: red, yellow, green, blue. Each canister contributes two health points (one full heart). `heartStackSize` defaults to 10 per color, implying a default maximum of **40 additional full hearts**, subject to binary verification. This is reference behavior, not an approved Expanded cap. [Container](https://github.com/Traverse-Joe/Baubley-Heart-Canisters/blob/1.18/src/main/java/com/traverse/bhc/common/container/HeartAmuletContainer.java), [Configuration](https://github.com/Traverse-Joe/Baubley-Heart-Canisters/blob/1.18/src/main/java/com/traverse/bhc/common/config/ConfigHandler.java)

Decision required: retain this amulet mechanic or create consumable, innate health upgrades. The latter needs additional behavior and character persistence rules; it is more than recipe integration. Parent conversation has opened this question.

## Drops and recipe customization

The examined code creates `config/bhc/drops.json` with color-specific maps. Defaults are red from hostile mobs at 5%, yellow from bosses at 100%, green from dragons at 100%, blue from evokers at 100%. These source categories are broader than a fixed list of vanilla creatures. [Initialization](https://github.com/Traverse-Joe/Baubley-Heart-Canisters/blob/1.18/src/main/java/com/traverse/bhc/common/BaubleyHeartCanisters.java)

The drop handler accepts configured probabilities; setting entries to zero is a plausible config-only way to suppress heart drops. Preserve all four color maps: the examined getter and handler expect them to exist. Wither bones have a separate `boneDropRate` option. **Inference pending selected-jar verification:** no mixin should be necessary solely to disable ordinary heart drops. [Drop handler](https://github.com/Traverse-Joe/Baubley-Heart-Canisters/blob/1.18/src/main/java/com/traverse/bhc/common/util/DropHandler.java), [Configuration](https://github.com/Traverse-Joe/Baubley-Heart-Canisters/blob/1.18/src/main/java/com/traverse/bhc/common/config/ConfigHandler.java)

Recipes are ordinary JSON files under `data/bhc/recipes`. For example, the examined red canister recipe combines a red heart, wither bone tag, relic apple, and empty canister. These can be replaced with the target pack's recipe mechanism. Exact vault ingredients and whether raw hearts remain intermediate ingredients are deliberately undecided. [Example recipe](https://github.com/Traverse-Joe/Baubley-Heart-Canisters/blob/1.18/src/main/resources/data/bhc/recipes/red_heart_canister.json)

Do not assume newer features from the current CurseForge description exist in the 1.18.2 jar. Soul amulets, soul canisters, and the Vitality Blade were not present in the inspected 1.18 source tree. The desired v0.1 integration concerns health canisters; newer mechanics are not added scope. [Historical source tree](https://github.com/Traverse-Joe/Baubley-Heart-Canisters/tree/1.18)

## Proposed integration path and later checks

Based on this evidence and the research-configuration precedent in [extension-mechanisms.md](extension-mechanisms.md), the likely ordinary integration is: install the compatible mod and Curios dependency, add a knowledge research entry, suppress ordinary heart acquisition, and replace selected recipes with expensive vault-material recipes. This remains a recommendation, not a finished technical specification.

The builder should verify research gating for both crafting and actual equipment benefit; crafting restrictions alone may allow an unlocked player to hand an amulet to a locked player. Verify that the added health combines correctly with Vault gear, talents, healing, death/respawn, vault entry/exit, and the existing health display. The historical health modifier preserves missing-health amount when equipped/removed, so changing equipment while wounded is a specific behavior worth checking. These are targeted integration checks, not claims of an existing incompatibility.

Still open: equipment versus innate benefit, eligible colors, health cap, research cost/timing, and intended acquisition restrictions. Exact recipes wait for the separate recipe designer. No Java API or mixin has been selected.

Licensing provenance if code reuse is later considered: the current CurseForge project declares GPLv3 while historical `mods.toml` says MIT. Do not infer a definitive license for copied source from that discrepancy; resolve the exact artifact/revision first. Ordinary pack inclusion and recipe/config integration do not require copying its Java source. [Historical metadata](https://github.com/Traverse-Joe/Baubley-Heart-Canisters/blob/1.18/src/main/resources/META-INF/mods.toml)
