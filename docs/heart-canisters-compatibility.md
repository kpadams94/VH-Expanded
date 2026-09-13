# Heart Canisters compatibility — issue #20

Addon `vhexpanded`, release version `0.1.0` (accepted gameplay from `0.1.0-heart-canisters.2`). Built on the accepted Experienced and Repulsor slices. Target: Remastered 2.0.4, Minecraft 1.18.2, Forge 40.3.11, Java 17. The shared contract is `specs/00-core.md`; source inventory and exact native mechanics are in `research/bhc-recipe-route-audit.md` and `research/mod-box-integration.md`.

## Artifact identity

| Artifact | Pin | SHA-256 |
| --- | --- | --- |
| Vault | CurseForge project 458203 / file 8502584; 1.18.2-20.0.3-remastered.6872 | `fc6adfeb76071d61e633027334fc95e8efb7faf8a4cb57d646f8176b4f75390b` |
| BHC | CurseForge project 282947 / file 3772892; 1.18.2-1.0.0 | `fada1a53f18a3fdc5199f759e634b364837559be987b5a8536d1da74aad5f988` |
| Curios | Existing pack dependency; project 309927 / file 4985315; 1.18.2-5.0.9.2 | `327b6a611cdd10be9d789e082cb7c91ef8afa822dcb3daf112aee71012fdfd13` |
| Approved recipe export | 2026-09-13T03:52:53.789Z | `8acdaca7d8e9e1a0e0f84b147e86d987558693176bb51370da00c8cd2210aec7` |

Build: set `JAVA_HOME` to a Java 17 JDK, then run `gradlew.bat build releaseBundle`. Dependencies are pinned through CurseMaven; tests hash the actual resolved artifacts. The bundle contains the addon, owned drop overlay, recipe manifest, guides, and checksums. It does not redistribute Vault, BHC, or Curios jars. The official Vault jar is unchanged. The new research icon is an original 16-pixel heart matching the existing atlas format.

## Owned integration

`ModConfigs.register` tail merges exactly one named BHC research, QoL membership, research style, and Mod Box pool. Reapplying preserves unrelated entries and the `None` fallback. The native QoL group has zero incoming cost increases and contributes zero increases to other research. Single-player research costs two regardless of prior research; native research-sharing multipliers remain outside this solo certification. An incompatible QoL cost configuration fails clearly rather than silently rebalance other research.

The single native `CustomResearch` uses craft-only item restrictions for all twelve recipe outputs, explicitly including `minecraft:enchanted_golden_apple`. This covers the native supported network-owner crafting gates as well as manual crafting. It does not restrict apple loot, inventory transfers, eating, or miniature-heart healing. The only enchanted-golden-apple crafting recipe in the pinned integration is `bhc:god_apple`; additional modded apple crafting recipes would inherit the native output restriction and need separate review.

`ResultContainer` also overrides its inherited recipe-acceptance method to deny BHC recipe identities before an output is generated for an unresearched player. It delegates unrelated recipes and unlocked recipes to native `RecipeHolder` behavior. Native result-slot and crafting-event restrictions remain active. The twelve unchanged JSON files replace all eight native BHC recipe IDs and add four hearts. No duplicate script copy is installed.

Runtime testing demonstrated that the combined native mod-resource pack gives BHC's original same-ID recipes priority over ordinary addon resources. Therefore Forge's `AddPackFindersEvent` registers an automatically enabled, required built-in data pack, `vhexpanded:heart_recipes`, with top priority. No recipe-manager injection or extra user-installed datapack is needed. The source recipes remain under `src/main/resources/data/bhc/recipes`; packaging places them only in the built-in data pack. Its data format is 9; the addon's separate client resource metadata retains format 8. Runtime checks compare the final recipe manager after pack scripts run against a unique, non-executable copy of each approved resource, avoiding classpath ambiguity between same-named files.

Native Curios/Vault restrictions do not research-gate BHC's health callback. A narrow mixin gates only its `addHealth` argument using native research state. A server player-tick reconciliation reads the actually equipped amulet and calls the native update routine, which returns without mutation when the bonus is unchanged. This covers research removal, inventory restoration, and stale saved modifiers. BHC owns the health arithmetic, modifier UUID, capacity clamp, and wounded-health semantics. No duplicate health system is introduced. BHC's optional starting-health override remains disabled by default.

`config/bhc/drops.json` is an explicit installation overlay read by BHC during common setup. It retains all four required nested color maps with zero chances. Wither-bone configuration and native miniature-heart healing remain intact. No mixin implements loot chances or mob-drop suppression.

Revision 2 appends only `bhc:red_heart`, `bhc:yellow_heart`, `bhc:green_heart`, and `bhc:blue_heart` to the existing `VaultGeneralConfig.ITEM_BLACKLIST` at config registration. An accessor exposes that list and its item cache; merging is idempotent and invalidates only those four cached entries. All unrelated blacklist entries remain intact. The native `Runner.initServer` player-interaction listener blocks their use in vaults, with the same creative exemption as other disabled items. Native `TooltipEvents.addBlacklistOnTooltip` supplies the existing dark-red warning. No custom consumption, healing, tooltip, or vault-detection hook is added. Relic apples, the amulet, and canisters are not added to the blacklist; outside-vault miniature-heart healing remains native.

The QoL layout resource places the six visible icons in columns 390/440/490 and rows 120/160, preserving each existing icon and frame. The previous bottom row retained the pack's centered two-icon positions plus the added heart, producing the uneven alignment reported during playtesting. The layout regression test checks the actual packaged coordinate resource; the runtime suite checks the final native GUI styles.

## Native Mod Boxes

Pool key: **Baubley Heart Canisters**, weight **1**. Empty amulet/red/yellow/green entries have weights **100/1000/100/1**, quantity one, and no inventory NBT. Total weight is 1201. No blue heart, filled canister, bone, or apple is added.

Native opening uses the player's completed research names, selects a pool, selects a product, delivers it, and consumes one box on success. `None` is only a fallback. With N equally weighted eligible pools, each BHC item's per-box probability is its weight divided by 1201 and then N. Unlocking research with no pool does not change N. No fixed-per-box rates, second rolls, reward hooks, or new Unboxing Station support are added.

Dedicated server use is a developer harness, not multiplayer certification. Full visual health, death/respawn, vault transitions, and player interaction acceptance are recorded separately in `heart-canisters-test-evidence.md`.
