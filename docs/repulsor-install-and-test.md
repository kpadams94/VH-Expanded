# Repulsor playtest — 0.1.0-repulsor.3

Issue #19. This build includes the accepted Experienced changes from #18. Heart Canisters is not included. The player accepted all tests, icon and description on September 13, 2026; final code checks confirmed AoE scaling on both radii and no Effect Duration scaling on the barrier. This guide remains available for repeat testing.

## Install the permanent icon correction

Revision 3 includes the corrected icon directly in the addon. This is the standard installation; no icon-fix resource pack is needed.

1. Save and quit Minecraft completely.
2. Open the test profile's `mods` folder. Remove `vh-expanded-0.1.0-repulsor.2.jar` and install `vh-expanded-0.1.0-repulsor.3.jar` from this bundle. Keep exactly one VH Expanded jar.
3. Launch Minecraft. In Options → Resource Packs, disable the standalone Repulsor icon-fix pack and any older `VHExpanded-Repulsor-Visuals` pack. An old visual pack can override the corrected built-in texture. No replacement resource pack is required.
4. Open your existing disposable Repulsor test world and its Abilities screen. The rings should now fit centered inside the specialization slot using the addon's own texture. Your revision-2 tuning, purchased ranks and gameplay behavior are retained.

The native menu assumes a 16×16 icon and uses a fixed eight-pixel offset. The original 32×32 export was twice the intended size and shifted its center down/right. The source texture and packaged jar are now corrected, and a regression check guards that native size requirement. No gameplay code changed from revision 2.

## Updating from Repulsor revision 1

This revision requires **one full Minecraft restart** to install the new jar: barrier and repel now have separate saved/network fields. Quit, replace `vh-expanded-0.1.0-repulsor.1.jar` with `vh-expanded-0.1.0-repulsor.3.jar`, then launch. Do not replace a jar while Minecraft is using it. You may continue the disposable Repulsor test world; old ability records remain readable and receive the new tuning through the native merge. Back up that test save first. General existing-character migration remains uncertified.

If you previously copied `config/vhexpanded-repulsor.json`, replace it with the new optional-config file or remove it to use bundled defaults. An old override with `manaPerLevel: 3` still intentionally overrides the new default of 2.

After this installation, **future balance edits can reload without restarting**:

1. Copy `optional-config/vhexpanded-repulsor.json` into the profile's `config` folder if it is not already there.
2. Edit and save the coefficients. `radiusBase`/`radiusPerLevel` control **repel**. `barrierRadiusBase`, `barrierRadiusStep`, and `barrierEveryLevels` separately control **barrier**. `manaPerLevel` is now 2.
3. In your loaded world, run `/vhexpanded repulsor reload` and reopen the ability menu. Check `/vhexpanded repulsor status`.
4. New casts use the new values. Purchased ranks, selected specialization, current mana, existing barriers and running cooldowns are preserved. Existing barriers retain the geometry and expiration they had when cast. Keep default values when running the automatic selftest.

**Future icon/description edits also reload without restarting:** copy the optional `resourcepacks/VHExpanded-Repulsor-Visuals` folder into the profile's `resourcepacks` folder, enable it above other packs in Options → Resource Packs, then edit its files. The ring icon is `assets/the_vault/textures/gui/abilities/vhexpanded_repulsor.png`; description text is `assets/vhexpanded/lang/en_us.json`. Save, close the ability menu, press **F3+T** (or Fn+F3+T on some keyboards), wait for resource loading, then reopen the menu. Keep the four `%s` placeholders in the description. Neither this optional pack nor a config file is required to see the new built-in defaults.

## 1. Install

1. Quit Minecraft. Use a separate **Vault Hunters Third Edition — Remastered 2.0.4** profile: Minecraft **1.18.2**, Forge **40.3.11**, Java **17**. You can duplicate the disposable Experienced profile and name the copy **VH Expanded - Repulsor Test**. Keep your normal profile intact.
2. Extract `vh-expanded-repulsor-playtest.zip`.
3. In CurseForge, open the test profile's menu and choose **Open Folder**. Open its `mods` folder. Move the previous `vh-expanded-…experienced….jar` out of this folder, then copy in `mods/vh-expanded-0.1.0-repulsor.3.jar` from the ZIP. Keep **exactly one Expanded jar** installed. The new jar includes Experienced; do not install both slices together.
4. Keep the official `the_vault-1.18.2-20.0.3-remastered.6872.jar`. There are **no required configuration files to copy**. Expanded merges its owned tree and menu entries when the pack loads.
5. Launch. The Mods screen should list **Vault Hunters Expanded**, version **0.1.0-repulsor.3**.
6. Create a **fresh singleplayer world**, Creative, Allow Cheats ON. A Superflat world makes the first checks easier. Finish the pack's initial prompts. Use default gameplay modifiers and avoid shared-level teams.

If startup fails, send this test profile's `logs/latest.log` and newest crash report. Keep the test world separate from your regular saves.

## 2. Run the automatic checks and buy rank 1

Open chat with **T**. Paste commands one at a time, pressing Enter after each.

1. Run `/vhexpanded repulsor selftest`. After a short preparation message, expect **Repulsor runtime checks PASSED: 59**. It uses a temporary test area near **8, 304, 8** in the Overworld, cleans up after itself, and does not train your character. Leave that high-altitude area empty. If it fails, send the error and `logs/latest.log` before proceeding.
2. Run `/vhexpanded selftest`. Expect **Expanded runtime checks PASSED: 26** for Experienced.
3. Run `/vhexpanded repulsor points`. It grants 16 test skill points. These are ability points, distinct from the expertise points used by Experienced.
4. Open the Vault player screen → **Abilities**. Find **Mana Shield**, then its specialization choices. You should see **Mana Shield**, **Mana Barrier**, and **Repulsor**. Repulsor uses blue rings spreading from a central point, with no arrow.
5. Select **Repulsor** and purchase **one rank**, using the ordinary Learn button. The rank should cost one skill point. Its description should be brief and describe an outward push followed by a stationary barrier.
6. Find your keys in **Options → Controls → Key Binds**. Locate the Vault ability selection and use/cast bindings. Select Repulsor in the ability selector, close the menu, then **press and release** the use-ability key to cast. It is an instant ability and resolves on release. If your pack offers a direct Mana Shield shortcut, bind it to a convenient unused key. The family shortcut should use your selected Repulsor specialization.
7. Run `/vhexpanded repulsor status`. At rank 1, with no modifier gear, expect **purchased 1/8, effective L1; radius 1; repel radius 1; duration 0.50s; cooldown 20s (400 ticks); cost 15**.

The status command reports exact server values. A tooltip's two-decimal number may round a fractional modifier; game timing is measured in 20 ticks per second. Creative casting normally waives mana, so use Survival for payment tests below.

## 3. Build an open test space and check the pulse

These construction commands are for the **fresh disposable world**. They create a platform above the ground.

```text
/gamemode creative
/fill -16 99 -16 16 99 16 minecraft:stone
/tp @s 0.5 100 0.5
/difficulty normal
/gamerule doDaylightCycle false
/time set midnight
/gamerule doMobSpawning false
```

`doMobSpawning false` prevents random spawns; your summon commands still work. Midnight keeps the test zombies from burning. Remain on this platform for the numeric and geometry tests.

1. Summon one stationary test zombie close enough for rank 1:

   `/summon minecraft:zombie 1 100 0.5 {Tags:["repulsor_test"],NoAI:1b,PersistenceRequired:1b,Silent:1b}`

2. Stand at **0.5, 100, 0.5**, select Repulsor and cast once. Expect **one push**, with the zombie moved just beyond the cube's edge. You should see the barrier briefly. Rank 1 lasts only **half a second**, so a blink is expected.
3. Watch the zombie afterward. It should not keep receiving outward pushes. The ability adds no damage. To compare health, run `/data get entity @e[type=minecraft:zombie,tag=repulsor_test,limit=1,sort=nearest] Health` before and after a cast. Use a new zombie for each trial and avoid falls or other damage sources.
4. Remove your marked test zombies with `/kill @e[tag=repulsor_test]`. This selector targets only mobs marked by this guide.
5. Buy ranks 2 through 8 normally. Check each purchase costs one point and a ninth purchased rank is unavailable. Stop at ranks **3, 4, 7 and 8** to compare the table below.
6. At rank 8, summon zombies at **1.5, 100, 0.5**, **3.5, 100, 0.5**, **-2.5, 100, 0.5**, and **0.5, 100, 3.5**, using the same tagged summon command with each position. Return to **0.5, 100, 0.5** and cast.
7. The ordinary zombies should clear the cube, ending just outside it rather than flying far away. **Walk out immediately after casting.** The barrier must remain where you cast it, and you must pass through it freely.

Repulsor keeps the parent's cube convention and anchors at the center of your occupied block. Radius means center to the outside of a cube face. The faces are thin collision surfaces, and fractional radii are preserved. At this test position, ranks 1 and 2 have their east face at **X=1.5**; ranks 3 and 4 at **X=2.0**; rank 5 at **X=2.5**; and rank 8 at **X=3.0**. The repel radius still grows by 0.5 every level, reaching 4.5 at rank 8, so mobs are pushed farther than the smaller barrier. The wall's thin visible/collision thickness lies inside that boundary. The floor beneath your feet remains normal solid terrain.

## 4. Compare every important rank

Remove all gear that changes abilities, area of effect, mana cost or cooldown for the raw-value comparison. Run `/vhexpanded repulsor status` and compare the ordinary ability panel after every change.

| Effective level | Barrier radius | Repel radius | Duration | Base cooldown | Mana |
|---:|---:|---:|---:|---:|---:|
| 1 | 1 | 1 | 0.5 s | 20 s | 15 |
| 2 | 1 | 1.5 | 0.5 s | 19 s | 17 |
| 3 | 1.5 | 2 | 0.5 s | 18 s | 19 |
| 4 | 1.5 | 2.5 | 1 s | 17 s | 21 |
| 5 | 2 | 3 | 1 s | 16 s | 23 |
| 7 | 2.5 | 4 | 1 s | 14 s | 27 |
| 8 | 2.5 | 4.5 | 1.5 s | 13 s | 29 |
| 12 | 3.5 | 6.5 | 2 s | 9 s | 37 |
| 15 | 4.5 | 8 | 2 s | 6 s | 43 |
| 16 | 4.5 | 8.5 | 2.5 s | 5 s | 45 |
| 17 | 5 | 9 | 2.5 s | 4 s | 47 |
| 20 | 5.5 | 10.5 | 3 s | 4 s | 53 |

To test over-levels while keeping **eight purchased ranks**:

1. Run `/vhexpanded repulsor gear 4`. Find the named **Repulsor TEST** helmet in your inventory and **equip it in the helmet slot**. It uses the native Mana Shield bonus-level gear attribute. After a moment, status should show **purchased 8/8, effective L12**.
2. Remove that helmet. Run `gear 7`, `gear 8`, `gear 9`, and `gear 12` using the same full command prefix, equipping **one helmet at a time**. These produce L15, L16, L17 and L20 respectively at purchased rank 8.
3. Compare the radius and duration after casting. The cooldown stops shrinking at L17, but radius, mana cost and later duration steps must continue increasing.
4. Removing the helmet should return you to your purchased rank. The helmet can remain in your inventory without applying its bonus.

You can also test native modifier gear you already know how to obtain. Compare the ability panel with `status`; its radius and mana values should reflect those modifiers. The pinned Mana Barrier does not apply a duration modifier, so Repulsor inherits its unmodified-duration behavior. Chance-based native free-cast or cooldown-refund effects can change an individual payment/cooldown; remove those for controlled tests.

## 5. Check mana payment and rejected casts

1. Remove the test helmet and return to purchased rank 8. Remove any other test gear. Switch to `/gamemode survival`. The stationary NoAI zombies will not pursue you; do not enable their AI until the later combat check.
2. Run `/vhexpanded repulsor mana 100`, then `status`. With normal gear, maximum mana should be 100.
3. Cast once and immediately check status. The cast costs **29 mana**. Natural regeneration can add a little before you finish opening chat, so an exact 71 is not required after a delay. The automatic runtime check tests the exact payment without elapsed regeneration.
4. While the cooldown is visibly running, press/release cast again. There must be no second pulse, no second barrier and no additional payment.
5. Wait for the cooldown to end. Run `/vhexpanded repulsor mana 0` and cast immediately. Expect the native out-of-mana response with **no pulse and no barrier**. If you wait long enough to regenerate the cost, refill zero and try again immediately.
6. Restore mana to 100 and cast again. It should work and pay once.

## 6. Test sustained protection and overlapping casts

1. Keep **eight purchased ranks**. Run `/vhexpanded repulsor sustain`, equip the named sustain-test helmet, and wait a moment for the native gear update.
2. Run `status`. Expect **effective L17**, **barrier radius 5**, **repel radius 9**, **duration 2.5s**, **cooldown 0.80s / 16 ticks**, and **cost 47**. The helmet supplies +9 family levels, 80% cooldown reduction, an adequate reduction cap and extra maximum mana. It is deliberately artificial test gear; it does not claim these rolls are normally obtainable on one helmet.
3. Run `/vhexpanded repulsor mana 100000` to fill your available mana. The command clamps to your actual maximum. Stay in Survival.
4. Stand still and repeatedly **press and release** cast roughly once per second. Several barriers should overlap without a mandatory gap. An old barrier expiring at 2.5 seconds must not make the newer overlapping barrier disappear.
5. Repeat while moving a few blocks between casts. Each cube should remain at its own cast position and expire independently. Some shared faces may overlap; each newer contribution must retain its own lifetime.
6. Stop casting. Within 2.5 seconds of the last cast, every last barrier must disappear. Remove the test helmet afterward.

## 7. Test mobs and projectiles

Use the sustain helmet for its longer barrier and short repeatable cooldown. Return to the platform center and refill mana as needed.

**Mobs:** summon an ordinary pursuing zombie outside the barrier:

`/summon minecraft:zombie 11.5 100 0.5 {Tags:["repulsor_test"],PersistenceRequired:1b,Silent:1b}`

Stay in Survival inside the cube and cast repeatedly. The zombie should be physically held outside. Walk through the wall yourself, then back inside. The barrier does not provide invulnerability: mobs already inside because of resistance/obstruction can still attack, and standing next to a mob on the other side can remain within its melee reach.

**Ordinary player-fired projectiles:** give yourself a bow and arrows with `/give @s minecraft:bow` and `/give @s minecraft:arrow 64`. Cast, then immediately shoot outward through a cube face. The arrow must hit the barrier despite being yours. A skeleton shooting inward must also be blocked while the wall is present; summon it with the same marked syntax, replacing `zombie` with `skeleton`. Clean up marked mobs between comparisons.

**Projectile-based abilities:** use extra skill points to learn **Javelin** or **Fireball** normally. Set convenient selection/cast keys in Controls. Cast Repulsor, quickly select and cast Javelin/Fireball through the wall before it expires. It must pass through and continue toward its normal target. Repeat once with no wall for comparison. The automatic runtime checks separately verify player-owned Javelin and Fireball raycasts through the actual barrier cells.

**Ordinary player movement and other abilities:** walk across each side, jump through the top where practical, and use your normal movement or targeted abilities across a face. They should retain their usual behavior. The native exceptions for pets and The Vessel are retained.

## 8. Check obstruction, resistance and vault terrain

1. Compare a normal zombie with this fully resistant one, both inside the intended cube:

   `/summon minecraft:zombie 1.5 100 0.5 {Tags:["repulsor_test"],NoAI:1b,PersistenceRequired:1b,Attributes:[{Name:"minecraft:generic.knockback_resistance",Base:1.0d}]}`

   The resistant zombie may remain inside. Repulsor must not bypass its resistance or deal damage to force it out.
2. Build a solid wall between a test mob and its outward destination. Cast. The mob must stop at the obstruction, not teleport through it. Ordinary solid blocks must remain intact. Like the native barrier, Repulsor skips occupied, nonreplaceable block cells. Partial blocks such as slabs can therefore leave gaps; inspect these during the terrain check.
3. Enter a real low-level vault to check terrain integration. In Creative, give yourself `/give @s the_vault:polished_vault_stone 14` and `/give @s the_vault:vault_crystal 1`. Build a standing **4-wide by 5-high** polished-vault-stone rectangle with a **2-wide by 3-high** empty opening. Hold the crystal and run `/the_vault modify crystal setLevel 0`, then right-click the inside of the frame with it.
4. Equip normal protection, enter, and switch to Survival for the test. Try Repulsor in an open room and near a wall, doorway, steps and an uneven floor. Observe which mobs clear the cube and which are stopped by terrain or resistance. No extra damage-absorption pool should appear.
5. Cast, move away, then let it expire. No permanent barrier blocks should remain. A `Repulsor cast` line in `logs/latest.log` reports radius, duration, cleared targets and targets that remained inside because of resistance/obstruction.

## 9. Save, reset and clean up

1. With Repulsor selected and ranks purchased, **Save and Quit to Title**, reopen the world, and run `status`. Your selected specialization and purchased rank should remain. Equipped native bonus gear should restore its effective levels after the gear update.
2. With the sustain helmet, cast and immediately Save and Quit. Reopen; no leftover active wall should remain from the previous session. Also cast before leaving a vault and verify no wall follows you to the Overworld.
3. In the disposable world, cast and die using `/kill @s`, then respawn. The prior barrier must not survive the caster's death.
4. Use the ability screen's normal regret/reset controls. If it requires regret points, obtain `/give @s the_vault:regret_orb 8` and consume them under the pack's normal rules. Remove all eight Repulsor ranks. Check the native refund and run `status`; no purchased rank means no usable Repulsor cast.
5. Relearn a rank and switch between the native **Mana Shield**, **Mana Barrier**, and **Repulsor** choices using the normal specialization controls. Their usual selection/refund rules should remain functional. Mana Barrier should still behave like its original, longer-lasting barrier.
6. Run `/the_vault reloadcfg`. Reopen the ability screen. There should still be one Repulsor choice, eight purchasable ranks and both original family choices. Save/reload again if desired.

## 10. Small Experienced regression

1. Run `/vhexpanded points` for five expertise points. Buy Experienced through the ordinary Expertise screen.
2. At rank 1, `/vhexpanded testaward 1000` should report **1,100**; at rank 5, it should report **1,500**. This really grants test XP. `/vhexpanded testordinary 1000` must remain **1,000** before native level-cap handling.
3. Check that the brief green Experienced description is retained and the previously removed explanatory XP hover popup has not returned.
4. During the vault terrain check, loot some chests and return through the entrance. Claim the reward once. Check the Experienced before/after award in `logs/latest.log` and confirm the summary and stored XP agree, allowing for XP spent leveling up. The full prior Experienced guide is included in the bundle for the longer test if needed.

## What to send back

Tell me which numbered sections passed and which failed. For a failure, include the purchased/effective rank, `status` output, whether you were in Creative or Survival, equipped test helmet, and what happened versus the expected behavior. A short video is especially helpful for a short-lived barrier or an overlap gap. Include `logs/latest.log` for crashes, wrong values or stuck blocks.

## Removal and optional tuning

Quit Minecraft and remove only `vh-expanded-0.1.0-repulsor.3.jar` from this disposable profile's `mods` folder. Retire the test world: it contains a new saved specialization type and may contain addon block records. Existing-world migration and dedicated multiplayer certification are outside this slice.

No pack configuration restoration is required. Advanced tuning is optional; use the live reload instructions near the start of this guide. Disable/remove the optional visual resource pack when retiring this profile. The runtime tests and this table assume the approved defaults.
