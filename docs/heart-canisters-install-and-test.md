# Heart Canisters playtest — issue #20

Historical slice guide. Final release installation is in [INSTALL.md](INSTALL.md). The user has completed the final retest described below; retained steps document what was tested.

Build: **0.1.0-heart-canisters.2**, retaining the accepted Experienced and Repulsor features.
Target: Vault Hunters Third Edition — Remastered **2.0.4**, Minecraft **1.18.2**, Forge **40.3.11**, Java **17**. Use a separate copy of the pack and a fresh solo test world with cheats enabled.

## Updating from revision 1

Close Minecraft and replace only `vh-expanded-0.1.0-heart-canisters.1.jar` with `vh-expanded-0.1.0-heart-canisters.2.jar` in your CurseForge test profile's `mods` folder. Keep the installed BHC, Curios, and drop configuration. No new dependencies or settings files are needed.

Check the following in your existing test world:

1. Open QoL research: all six icons should form two straight rows of three.
2. Each miniature heart (red, yellow, green, blue) should show the pack's normal **Disabled in the Vaults** tooltip.
3. In **survival mode inside a vault**, try consuming each color. No healing or item consumption should occur. The native blacklist exempts creative players, so creative mode is not a valid test of this restriction.
4. Outside a vault, hurt the player and consume a miniature heart: its normal healing should still work.
5. Inside a vault, eat a relic apple when normal eating conditions allow. It should remain usable. Equipped amulet health should also continue working.

## Install while Minecraft is closed

1. In **CurseForge → Minecraft**, open your separate Vault Hunters test profile. Choose its **three-dot menu → Open Folder**. This is the folder containing `mods`, `config`, and `saves`; leave it open for the next steps.
2. Download **Baubley Heart Canisters 1.18.2-1.0.0**, [CurseForge file 3772892](https://www.curseforge.com/minecraft/mc-mods/baubley-heart-canisters/files/3772892). Put `baubley-heart-canisters-1.18.2-1.0.0.jar` into that instance's `mods` folder. Do not choose a newer Minecraft version.
3. Keep the pack's existing **Curios 1.18.2-5.0.9.2**. Do not add a second copy. The launcher may name its file differently; the mod list should show that version.
4. Extract `vh-expanded-heart-canisters-playtest.zip`. Move previous `vh-expanded-*.jar` files out of the test instance's `mods` folder, and copy the bundle's `mods/vh-expanded-0.1.0-heart-canisters.2.jar` there. Keep exactly one VH Expanded jar.
5. Copy the bundle's **`config/bhc/drops.json`** into the instance at that same path, creating the `bhc` folder if necessary. Replace BHC's default drops file if it already exists. This disables ordinary miniature-heart mob drops; it preserves wither-bone drops.
6. Launch the test pack. No extra health-display mod, recipe datapack, or recipe script is needed. Recipes and research are included in the addon. Keep any accepted Repulsor tuning you already use.

The addon bundle does not contain third-party mod jars. You do need the BHC jar in your own instance; the developer's test installation does not install it for you.

CurseForge documents the profile menu's Open Folder option in its [official profile guide](https://blog.curseforge.com/how-to-enable-mods-for-a-specific-world-in-minecraft-java/).

## First check: research and locked behavior

1. Create a fresh solo world. In the research screen, open **QoL** and locate **Baubley Heart Canisters**. It should show **2 knowledge points** and one unlock for all four colors.
2. Before buying it, try an approved canister recipe and the approved enchanted-golden-apple recipe in a crafting table. Neither should produce a takeable result. Check JEI for the approved expensive ingredients.
3. Give yourself a filled test amulet: `/give @s bhc:heart_amulet{heart_amount:[I;20,20,20,20]}`. This command creates a disposable health fixture, not a normally filled inventory. Put it in the Heart Amulet Curios slot. Your maximum health must not increase while research is locked. Remove and discard this fixture before testing normal canister insertion.
4. Give yourself Mod Boxes with `/give @s the_vault:mod_box 16` and manually open a few before research. No BHC rewards should appear. Automated checks establish eligibility; a few random boxes alone cannot prove exclusion.
5. Obtain two knowledge points through the pack's normal knowledge-star route, then purchase BHC. Confirm exactly two points are charged and no additional color research appears.
6. With BHC as your only unlocked research that has a Mod Box pool, open boxes again. Rewards should be an empty amulet, miniature red heart, miniature yellow heart, or very rarely miniature green heart. Blue hearts and filled canisters are never part of this pool. No need to wait for a green heart; the developer check forces that native branch.

## Craft and equip

Use creative mode to obtain ingredients, then switch to survival for crafting and equipment checks. Compare all twelve JEI recipes to `recipes/recipe-appendix.md`: empty canister, amulet, four miniature hearts, four canisters, relic apple, and enchanted golden apple. All outputs are one. The eight old cheap recipes must be absent.

Craft the amulet and right-click it in your main hand to open its inventory. Insert canisters in their matching color slots, close the inventory, and equip the amulet in its Curios slot. Each canister adds one full heart, with a maximum of ten per color. Ten of all four colors add **40 full hearts / 80 health points**. Upgrading a canister consumes its previous tier.

Compare `/attribute @s minecraft:generic.max_health get` before and after equipping. With no other bonuses the full amulet changes 20 to 100 health points. Also check the normal pack health display. Removing the amulet must remove its bonus; re-equipping must restore only one bonus.

Test these remaining player interactions:

- Equip and remove while wounded, then heal. Native BHC preserves the amount of missing health; removing more maximum health than your current health can be fatal. Use the disposable test world.
- Equip representative Vault health gear and a health talent; the amulet should combine normally, without permanent duplicate bonuses.
- Save and reload with the amulet equipped; die and respawn, recover/equip it; enter and exit a vault. Compare maximum health at each step and after unequipping.
- Confirm miniature hearts still heal outside vaults, but are blocked inside vaults in survival mode. Relic apples remain usable in vaults. Ordinary mob heart drops remain disabled; wither bones may still drop.
- Check Experienced's XP bonus on a vault run and cast Repulsor to confirm its accepted pulse, barrier, costs, and presentation remain intact.

## Developer checks and removal

In a **disposable world only**, `/vhexpanded hearts selftest` checks loaded recipes/tags, research costs/gates, native weighted selection, forced green delivery, and native health/Curios behavior. It uses an isolated fake player and seeds the world's random stream for deterministic box selection. `/vhexpanded selftest` and `/vhexpanded repulsor selftest` run the prior feature checks. Detailed outputs are written to the game log. These do not replace visual or full gameplay acceptance.

To remove this slice from the disposable test copy, close Minecraft, remove the new Expanded and BHC jars, and restore the prior accepted Expanded jar. Recreate the disposable test world rather than migrating a world containing removed BHC items. The original pack instance stays available.
