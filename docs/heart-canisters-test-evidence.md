# Heart Canisters validation — issue #20

Status: accepted. The player confirmed the final revision-2 retest was completed on September 13, 2026. This covers the miniature-heart vault-use and six-QOL-icon follow-ups. Revision 2 passed automated and full-pack server verification. Final release packaging is recorded in `RELEASE-0.1.0.md`.

## Revision 2

Build `0.1.0-heart-canisters.2` SHA-256: `3ce09819701f3c69ae8a3da3434e9d10f64df8939c7bdd1f5e69332985fac01f`.

Java 17 build passed with **24 JUnit tests, zero failures**. The layout regression test first reproduced the original staggered columns (390/440/490 versus 415/465/505), then passed with aligned rows. The new native checks cover final GUI positions, the four hearts' actual vault blacklist membership, retained outside-vault consumption/healing, relic-apple and amulet exclusion, and preservation of existing blacklist rules.

The full Remastered 2.0.4 server passed **116 Heart Canisters checks**, **26 Experienced checks**, and **59 Repulsor checks**, then saved and stopped normally. Native source inspection confirms that the vault runner's player-interaction listener and the existing disabled-item tooltip both query this blacklist. The automated checks invoke its real membership lookup and native outside-vault consumption; actual in-vault input and visual appearance remain in the focused player retest.

Focused player retest, subsequently reported completed by the user: aligned research rows; native tooltip and blocked consumption of all four hearts in an actual vault in survival mode; outside-vault healing; relic-apple use inside a vault. No new standalone client session has been run for revision 2. See the upgrade section in `heart-canisters-install-and-test.md`.

## Revision 1 baseline

Build `0.1.0-heart-canisters.1` SHA-256: `478c4fbab27d03f679ba29da54f5350994ee5c57fc2411af769b01e167671c86`.

`gradlew.bat build` passed with Java 17: **23 JUnit tests, zero failures**. The suite checks exact resolved dependency hashes, the health integration signature, unchanged user-export recipes, complete replacement of native recipes, required drop-map structure, and unchanged Mod Box payload, alongside the prior Experienced and Repulsor tests.

The full Remastered 2.0.4 dedicated-server harness loaded the exact BHC and existing Curios jars plus the drop overlay and the addon above. It passed **101 Heart Canisters checks**, **26 Experienced checks**, and **59 Repulsor checks**, then saved and stopped with exit code 0.

The opt-in native runtime suite verified actual loaded research/groups/pools, all twelve final recipes and ingredient tags, locked/unlocked recipe acceptance, craft-only restrictions with vanilla apple use and miniature-heart healing left available, all 1201 native item-selection boundaries, eight-pool weighting, forced native green delivery and consume-one behavior, native health arithmetic, wounded removal, additive health, Curios inventory persistence, and research removal. These are native API checks with a fake player, not end-to-end player interactions or automation-machine certification.

The final run confirms two integration corrections: the required built-in recipe pack wins over BHC's original cheap recipes after pack scripts load, and the research restriction sets crafting alone rather than the broader native item-restriction helper. Evidence excerpts are in `evidence/heart-canisters-runtime-checks.txt`.

The full local client loaded the same addon and dependencies. Its load-complete checks passed for the two-point BHC research, icon style, health and recipe hooks, and the retained Experienced and Repulsor client integrations. This confirms startup and class loading; it does not certify the visible research screen, icon appearance, or a played single-player session.

Player feedback: "everything else works great" apart from the two requested revision-2 changes. This is user-reported acceptance, not a claim that every individual gameplay checklist step was independently observed by the developer.
