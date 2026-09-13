# Experienced playtest — 0.1.0-experienced.2

Final presentation revision for issue #18. Gameplay checks were accepted by the player on September 13, 2026. If upgrading, quit Minecraft and replace the previous Expanded jar with this one; keep only one Expanded jar installed. It does not include Repulsor or Heart Canisters.

## 1. Install in a separate profile

1. In CurseForge, install **Vault Hunters Third Edition — Remastered, release 2.0.4**, Minecraft **1.18.2**, Forge **40.3.11**. This is the separate Remastered pack, not the older Third Edition pack. [Exact official release](https://www.curseforge.com/minecraft/modpacks/vault-hunters-third-edition-remastered/files/8502758).
2. Name the new profile **VH Expanded - Experienced Test**. Keep your existing profile and saves intact.
3. Launch the new profile once without Expanded. Confirm you reach its menu, then quit Minecraft. Use the launcher's Java 17 runtime.
4. Extract the supplied `vh-expanded-experienced-playtest.zip`. In CurseForge, use the test profile's **Open Folder** option. Copy the jar from the ZIP's `mods` folder into the profile's `mods` folder. The destination is `<test profile>/mods/vh-expanded-0.1.0-experienced.2.jar`. With your current launcher location and the suggested name, this is `C:\Users\Kpada\Twitch\Minecraft\Instances\VH Expanded - Experienced Test\mods\`.
5. Leave the original `the_vault-1.18.2-20.0.3-remastered.6872.jar` installed. Do not replace it or install a second Vault jar. There are **no config files to copy**: the addon changes only its owned Experienced entries in memory.
6. Launch the test profile. In **Mods**, confirm **Vault Hunters Expanded: Experienced** appears. Create a **new singleplayer world**, with **Creative mode** and **Allow Cheats: ON**. Complete the pack's normal initial prompts; leave XP multipliers at their defaults. Do not enable shared-level teams for this test.

If startup fails, stop here and send the test profile's `logs/latest.log` and newest file in `crash-reports`. Do not try your real world.

## 2. Check the exact numbers first (about 5–10 minutes)

Press **T** to open chat. Paste one command at a time and press **Enter**. These helpers require cheats and operate only when you invoke them. `testaward` and `testordinary` really grant XP, so use this disposable world.

1. Run `/vhexpanded selftest`. Expect **Expanded runtime checks PASSED: 26**. It tests detached objects, not your character. Send `logs/latest.log` if it reports a failure.
2. Run `/vhexpanded status`. Expect Experienced **rank 0/5**. This command reports your **Vault** level and XP; the green vanilla XP bar above your hotbar is a different system.
3. Run `/vhexpanded testaward 1000`. At rank 0, expect `before Experienced 1000, awarded 1000`.
4. Run `/vhexpanded points` once to receive five test expertise points.
5. Open the Vault player screen and choose **Expertises**. If you do not know its key, open **Options → Controls → Key Binds**, search for the Vault player/skills screen, and use the binding listed there. Select **Experienced**. Its description should read “Increases XP earned from vault runs.” followed by five green rank bonuses. The native Cost row shows one point per rank.
6. Buy **one** rank through this screen, close it, and run `/vhexpanded testaward 1000`. Repeat after buying each further rank:

| Purchased rank | Expected awarded XP |
|---|---:|
| 0 | 1,000 |
| 1 | 1,100 |
| 2 | 1,200 |
| 3 | 1,300 |
| 4 | 1,400 |
| 5 | 1,500 |

At rank 5, a sixth purchase must be unavailable. Each purchase must spend exactly one point. Leveling during these tests can earn more expertise points naturally; use the before/after count for each purchase, not just the final balance.

7. At rank 5, run `/vhexpanded testaward 2000`: expect **3,000**. This supplies an award that is already 2,000 before Experienced. The self-test separately exercises a genuine global multiplier of 2 followed by Experienced.
8. Run `/vhexpanded testaward 101`: expect **151**. Run `/vhexpanded testaward 0`: expect **0**. Expanded keeps the native earlier rounding and floors the final bonus; it never rounds 151.5 up to 152.
9. Run `/vhexpanded testordinary 1000`, then `/vhexpanded status`. Expect only **1,000** XP before normal level-cap handling, despite rank 5. This calls the ordinary Vault XP helper without marking it as a run award.

The test command does **not** simulate completing a vault, its outcome, loot, or claim button. The next section checks those paths.

## 3. Run a real vault and compare the receipt

You do not have to grind an altar for this disposable test:

1. Run `/give @s the_vault:polished_vault_stone 14` and `/give @s the_vault:vault_crystal 1`.
2. Build a standing rectangular frame **4 blocks wide and 5 blocks high**, with an empty **2-by-3 opening**, using the polished vault stone. Include the corners.
3. Hold the crystal in your main hand. Run `/the_vault modify crystal setLevel 0`. This creates a low-level test crystal; your character can be a higher level from the numeric checks.
4. Right-click an inside face of the frame with the crystal to open the portal. Equip armor, a weapon and a pickaxe from Creative inventory, and take food. Switch to `/gamemode survival` before entering so you test ordinary looting and orb pickup. You may switch back to Creative if you get stuck.
5. Before entering, run `/vhexpanded status` and note your Vault level and XP.
6. Enter the portal. Open and loot several chests, mine some Vault ore, and kill a few mobs. To see the XP tracker, find the **XP Tracker** key in **Options → Controls → Key Binds**. The pack supports Hold, Toggle, and Always-visible tracker settings. Its heading should read **Run XP estimate (+50%)** at rank 5.
7. For the first run, return through the entrance portal before the timer expires. This tests a **bailed** run. You can complete an objective in a later run. The live number is an estimate: completing an objective can add XP, and the final outcome can change eligibility.
8. In the end-of-vault screen, note the XP reward. The reward number and the **Claim Rewards** tooltip must agree. The added explanatory XP tooltip has been removed. For the exact before/after calculation, use the `Experienced run` line in `logs/latest.log` after claiming: at rank 5, 2,000 becomes 3,000.
9. Claim the rewards **once**. Run `/vhexpanded status` again. If you did not level up, the stored XP increase must equal the receipt. If you leveled up, XP spent on each level plus remaining XP must equal the receipt. The addon also records a line such as `Experienced run ... rank 5: 2000 -> 3000 XP` in `logs/latest.log`.
10. Reopen that run from the pack's vault history. The award must retain its original number after it has been claimed. Closing history must not add XP. Run `/vhexpanded status` before and after to confirm.

For an especially clear level-boundary check, make another fresh world, grant five expertise points, buy rank 5 without granting any Vault XP, then run `/vhexpanded testaward 1000`. Expect **Vault level 1, 500 XP** from the 1,500 award. Still check a real run that crosses a level boundary as above.

## 4. Reset, reload, and compare excluded rewards

1. At rank 5, **Save and Quit to Title**, reopen the world, and run `/vhexpanded status`. Rank 5 must remain. Reopening an already claimed summary must not grant XP again.
2. Run `/give @s the_vault:neuralizer 1`. Use the Neuralizer normally in Survival to reset your expertises. It resets all expertises, not only Experienced. Check that your invested points are refunded and Experienced is **0/5**.
3. Run `/vhexpanded testaward 1000`. Expect **1,000** immediately. The old rank-5 run in history must still show its original rank-5 award.
4. Repeat a short real vault at rank 0. Its logged before/after award must show no Experienced increase. Compare each run against its **own** base receipt; two random vaults rarely have identical base XP.
5. **Vanilla orb comparison:** at rank 0, run `/experience set @s 0 levels`, then `/experience set @s 0 points`. In Survival, run `/summon minecraft:experience_orb ~ ~1 ~ {Value:10s}` and walk into it. Use `/experience query @s levels` and `/experience query @s points` to record the result. Reset vanilla XP the same way, buy rank 5, and repeat with another 10-XP orb. Both results must match. Remove any nearby XP orbs between trials so you collect only the test orb.
6. **Vault XP item comparison:** use one identical Vault XP item at rank 0 and rank 5 while staying at the same Vault level, or compare matching copies of the world. A **Vault Burger** is one option available through the pack's item browser. Record Vault XP before and after with `/vhexpanded status`. The item must not receive an extra 50%; account for its native rules and any level-dependent value. `/vhexpanded testordinary` is an additional isolation check, not a substitute for consuming an item.
7. Optional configuration check: run `/the_vault reloadcfg`, then open Expertises again. It must still have exactly five Experienced ranks, with other expertises intact. A restart must do the same.

## 5. Outcome checks and what to send back

- **Bailed:** loot first, then return through the entrance before time expires.
- **Completed:** fulfill the objective shown in your next vault and exit as the objective instructs. Check the receipt and claim once.
- **Failed:** in a disposable run after collecting some XP, die or allow the timer to expire. Record whether the pack offers a summary and how much XP it normally permits. If the eligible base is zero, Experienced must also give zero. Failed/bailed objectives have zero completion XP in this pack, but that alone does not mean every stat-based award is zero.
- **Pending claim:** if the pack permits it, leave a summary unclaimed, save/reopen, and then claim. It must award once. Merely viewing a summary must not award twice.

Send me: which steps passed, your rank, the receipt's base and final XP, Vault level/XP before and after, the outcome, and screenshots of any disagreement. Include the test profile's `logs/latest.log` for a crash or wrong award. There is no need to upload your entire world.

## Removal

Quit Minecraft and remove only `vh-expanded-0.1.0-experienced.2.jar` from the disposable profile's `mods` folder. No pack config restoration is needed. Retire this test world rather than continuing it without the addon: its saved characters can contain five-rank expertise data. Your original profile is unaffected.
