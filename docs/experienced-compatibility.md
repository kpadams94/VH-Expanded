# Experienced compatibility and implementation record

Target: issue #18 under the shared issue #17 contract. Addon ID `vhexpanded`; slice version `0.1.0-experienced.2`. No later-slice dependency. Independent implementation; no Wold's source or assets copied.

## Verified pins

- Official [Remastered client release 2.0.4, file 8502758](https://www.curseforge.com/minecraft/modpacks/vault-hunters-third-edition-remastered/files/8502758), downloaded from its ForgeCDN artifact. SHA-256 `e951c9c5a3d7f5e8a61be3d3fe565e2076f1a8df9c94e3830282bf53bc7d71a2`.
- Its `manifest.json` declares Minecraft `1.18.2`, loader `forge-40.3.11`, and Vault project `458203` / file `8502584`. The archive's internal pack-version string is stale (`2.0.3`); the official release and file ID identify the selected 2.0.4 distribution.
- Vault `the_vault-1.18.2-20.0.3-remastered.6872.jar`: SHA-256 `fc6adfeb76071d61e633027334fc95e8efb7faf8a4cb57d646f8176b4f75390b`. Verified against the workspace artifact, the copy installed from the [official server package 8502840](https://www.curseforge.com/minecraft/modpacks/vault-hunters-third-edition-remastered/files/8502840), and the original build dependency.
- Java: Eclipse Temurin JDK `17.0.17+10`; Java target 17. Gradle `8.8`, wrapper distribution checksum checked; ForgeGradle `6.0.54`, Forge `1.18.2-40.3.11`, official `1.18.2` mappings, Mixin `0.8.5` (provided by Forge).
- Reproducible Vault coordinate: `curse.maven:vault-hunters-official-mod-458203:8502584` from `https://www.cursemaven.com`. Build verifies the original artifact's SHA-256. If acquiring manually, use the official [Vault file 8502584](https://www.curseforge.com/minecraft/mc-mods/vault-hunters-official-mod/files/8502584). Never ship this dependency inside the addon.

## Hook trace and boundaries

| Pinned class/method | Observed behavior and addon adaptation |
|---|---|
| `ModConfigs.register` / `SetupEvents.setupCommon` | Loads `EXPERTISES` before generation. TAIL merges only the existing `Experienced` node. Native `reloadcfg` calls `register`, so it reapplies. No whole-file replacement or disk config write. |
| `ExpertisesConfig.tree`, `TieredSkill.readJson` | Existing `Experienced` ID/name/parent and GUI style remain. Replace `tiers` and `maxLearnableTier=5`; native deserialization attaches child parents. Classpath `vhexpanded/experienced.json` owns the five 0.1–0.5 values, cost 1, unlock level 0 and regret cost 0. |
| `SkillDescriptionsConfig.getDescriptionFor` | Only Experienced receives the new English description, including when a stale locale description exists. Other descriptions remain native. |
| `ExperiencedExpertise.onOrbPickup` | Cancel the expertise handler at HEAD; do not cancel Forge's orb event or alter orb pickup itself. |
| `VaultPlayerStats.consume(ServerPlayer, UUID)` | Native pending-ID and snapshot guards run first. Solo/personal branch calculates `StatCollector.getExperience`, then Greed scaling. Redirect only its call to `PlayerVaultStatsData.addVaultExp`; dolls and companions continue receiving the original local `experience`. |
| `PlayerVaultStatsData.addVaultExp` | Native delegation, dirty marking and persistence retained. Ordinary callers do not establish an Expanded run scope. |
| `PlayerVaultStats.addVaultExp` | Native Level Sync early return, level-cap early return, and global XP multiplication remain. A one-use UUID-checked thread-local token applies Experienced at the single XP accumulation write **after** native global multiplication and integer conversion. Native level advancement, points, events and sync then consume the adjusted XP. Scope is always restored in `finally`. |
| `VaultPartyExpData.distributeXp` | Separate shared-party online/offline paths confirmed. They are untouched and outside this solo slice. Level Sync teams are likewise not certified or enhanced. |
| `VaultPlayerStats.prompt` | Sends a server-authored receipt before the native snapshot packet. Receipts are recalculated when an eligible run is actually consumed; history retains the consumed rank and amount. |
| `RunReceipts` | Per-world SavedData `vhexpanded_run_receipts.dat`, keyed by player UUID and vault UUID. Receipts sent to their owner on login; client cache cleared on disconnect. These are display records, never an alternative claim mechanism. |
| `VaultEndScreen` full five-argument constructor, `lambda$new$23`, `lambda$new$26` | Reward label and claim tooltip use the same server receipt. The added explanatory hover tooltip is suppressed at the player’s request in revision 2. Constructor is matched by its complete descriptor, verified in the packaged client. |
| `VaultExitContainerScreenData.getVaultLevelPercentageWithReward` | Uses the receipt's award before claim; adds no extra pending reward when viewing an already claimed receipt. |
| `ClientVaultXpTracker.updateBreakdown`, `addNotification`; `VaultXpTrackerModule.buildLines` | Current personal-rank estimate; native modifiers/Greed/global XP precede the bonus. Tracker heading calls it a run estimate and shows the bonus. Final outcome and native cap can change the eventual award. Floating per-source breakdowns are estimates; the authoritative receipt uses integers. |

## Arithmetic and inherited rules

The existing run pipeline rounds stat/objective components, then Greed, then the global XP multiplier. Those conversions are retained. Expanded takes the otherwise-earned **integer** award and calculates `(long) award * (10 + rank) / 10`, flooring the fractional bonus once. This avoids floating-point errors such as an almost-integer 1.1 multiplication rounding down incorrectly. Positive results saturate at the Java integer limit; normal gameplay values are much smaller.

Zero stays zero; no new outcome eligibility is introduced. Failed and bailed objective completion values are zero in the supplied `vault_stats.json`; the native collector can still account for other earned stats. Pending summary IDs and their removal are untouched. Dolls and companions are called separately after the scoped player call and never receive the adjusted local value. XP items, bounties and ordinary commands have no run token. Native level caps and any XP discarded at a cap remain in force. A receipt is the nominal awarded XP before excess at a reached cap is discarded.

The pinned jar's other `addVaultExp` callers were enumerated: `DollMiniMeEntity`, `MentorsBrewItem`, `VaultXPFoodItem.Flat`, `GreedyMealItem`, `bounty.TaskReward`, `command.progression.ProgressionCommand`, and `quest.base.Quest.QuestReward`, besides the separate party and run paths above. None establishes an Expanded token. The doll completion and companion completion functions also receive the unchanged original run value at their separate call sites.

Rank is read from the purchased, unmodified Experienced tier on the server for every award. A reset therefore removes the bonus immediately. No new skill serializer type is required. No existing-character migration or multiplayer certification is provided. The historical linked view for another player's run has no private receipt and retains its native display; this slice supports the local player's own history.

## Rebuild

Install a Java 17 JDK, set `JAVA_HOME` to it, then run `gradlew.bat build` on Windows or `./gradlew build` elsewhere. The wrapper obtains Gradle and pinned dependencies. The distributable is `build/libs/vh-expanded-0.1.0-experienced.2.jar`. `build/reports/tests/test/index.html` contains the automated results. The pinned-artifact checks default to the original downloaded dependency; `-PvaultJar=/absolute/path/to/the_vault.jar` can select a manually acquired copy for those checks.

`gradlew.bat playtestBundle` creates the handoff ZIP, containing only the addon, player guide, compatibility/evidence notes and checksums. The `.local` directory contains local inspection and runtime-test dependencies and is excluded from source control and packaging. The official Vault jar is neither changed nor redistributed.
