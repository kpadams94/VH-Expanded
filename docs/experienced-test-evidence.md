# Experienced verification record

Current build: `0.1.0-experienced.2`, issue #18. The player reported all other checks good on September 13, 2026 and requested only presentation cleanup. Revision 2 shortens the description to one sentence plus green rank bonuses, using the reference color `#69D68F`, and removes the added explanatory XP tooltip. Gameplay calculations are unchanged.

The recorded runtime results below are for revision 1. Revision 2 is checked by compilation, packaging and the existing automated suite; no new visual pass is claimed.

## Passed

- **Pinned artifacts:** official release archive manifest confirms Minecraft 1.18.2, Forge 40.3.11, and Vault file 8502584. Workspace, official installed server artifact, and build dependency match the recorded Vault SHA-256.
- **Build:** Java 17 compilation, reobfuscation, and addon packaging. No Vault dependency embedded.
- **Automated suite:** 14 tests passed. Six rank values; 2,000 → 3,000 stacking; exact fractional flooring; zero and negative preservation; rank bounds; recipient-only and one-use scope; exception cleanup; nested contexts; and actual pinned bytecode for the award, doll/companion, summary and tracking hooks.
- **Official server baseline:** unmodified official 2.0.4 server package reached `Done (75.305s)` on September 12, 2026. This is a server startup baseline, not a solo client gameplay baseline.
- **Packaged addon server launch:** the separate addon jar installed alongside the original Vault jar reached `Done (16.821s)` on the first addon launch. Universal mixins loaded successfully; client classes did not prevent dedicated-side startup.
- **Native runtime verification:** five one-point ranks, sixth-rank rejection, native purchase/refund behavior, native NBT and network rank round trips, deterministic config merging, actual XP storage at ranks 0–5, actual global 2× multiplier followed by rank 5, fractional and zero awards, normal helper isolation, native cap, and receipt serialization.
- **Final jar verification:** SHA-256 `9b00a4ecaacbba1641cb19da1bec65b90ee0b24310aabe5ad6514271e6d76e57`. The final packaged server reached `Done (11.228s)`. Its **26 runtime checks passed**, including an explicit rank-five character whose orb remained worth seven XP. After native `/the_vault reloadcfg`, all 26 passed again. No test failure or detached level-up listener error occurred in these final runs.
- **Unrelated config preservation:** the installed `config/the_vault/expertises.json` remained byte-for-byte identical to the official pack file, SHA-256 `e1b80bf1074a77adff785688b37f5b877e781efe94e2551fd7cf0eaa466e5bc4`. Runtime rank merging does not rewrite it.
- **Client constructor repair:** the first client attempt detected a constructor-selector error in the summary mixin. It was corrected to the explicit five-argument constructor. The corrected packaged client completed resource reload and reached its main-menu stage in the log. The pinned-bytecode test now checks that constructor descriptor.
- **Final packaged client:** logged `Expanded client load check PASS: summary and tracker hooks loaded; five Experienced ranks configured`, then completed resource reload and registered the main menu. This checks all four client mixin target classes, including the final tracker heading. It is loading evidence, not a visual gameplay pass.

## Checks originally handed to the player

- Original handoff limitation (subsequently player-tested): visual rendering of all five ranks, description, tracker, receipt and history. The Windows capture tool failed twice with `SetIsBorderRequired failed: No such interface supported (0x80004002)`; no screenshot or visual pass is claimed.
- A real player's vault completion/bail/death/timeout through `VaultPlayerStats.consume`, the synchronized receipt and the actual claim UI.
- A real vault award crossing a level boundary, with matching UI, stored XP and native level-up events. A detached runtime probe did produce level 1 / 500 XP from 1,500 XP, but unrelated native listeners logged errors for the absent online player. That probe is not counted as a player-event pass; the shipped self-test avoids that detached crossing. Use the player instructions to finish this check.
- Physical orb pickup, consuming an actual Vault XP item at equal levels, and companion/doll/bounty behavior during gameplay. The injection boundary leaves their original input intact; that structural evidence is not a gameplay certification.
- Save/quit/reopen with a real player's rank, unclaimed/claimed summary, history after refund, and normal Neuralizer interaction. Native serialization/refund primitives were tested; the full UI/reload sequence still needs playtesting.
- Full untouched client baseline and fresh solo-world gameplay. The isolated client was assembled from the exact client manifest and official launcher libraries, without using or changing the user's existing profiles. It was used for packaged client loading, not certified gameplay.

## Boundaries and evidence locations

Dedicated multiplayer and old-world migration are outside this delivery. Shared-party and Level Sync paths retain native behavior; this build's bonus is for the supported personal solo award path. Other players' linked history uses the native view. Runtime log warnings already present in the baseline are not attributed to Expanded without evidence.

Automated report: `build/reports/tests/test/index.html`. The ZIP includes selected unedited final log lines in `evidence/experienced-runtime-checks.txt`. Full local runtime logs: `.local/baseline-server.log`, `.local/addon-server-first.log`, `.local/client-launch.log`, and `.local/server-pack/logs/latest.log`. Original dependency inspection and isolated game files remain under ignored `.local`; they are not distributed. Follow `START-HERE.md` in the ZIP for the remaining player checks.
