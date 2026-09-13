# Repulsor verification and player acceptance

Build: `0.1.0-repulsor.3`, targeting Remastered 2.0.4 / Minecraft 1.18.2 / Forge 40.3.11 / Java 17.

## Final acceptance — September 13, 2026

The player reports that all tests pass and the icon and description look good.
The remaining modifier questions were verified in source and the compiled
revision-3 jar extracted from the delivered playtest bundle. Both `getRadius`
and `getRepelRadius` call the pinned native `AreaOfEffectHelper.adjustAreaOfEffect`,
which applies native AoE attributes and limits; both adjusted values feed the
actual cast and their displayed labels. Duration is the unmodified
`durationTicks` field, passed directly to `RepulsorBarrier.cast` and added to
world game time for expiry. No Effect Duration adjustment is applied. Ability
levels still increase duration according to the approved level formula.

The fresh-solo slice is accepted as complete. The testing notes below preserve
the earlier verification history; their requests for player confirmation have
now been satisfied by the player's report. Multiplayer and general save
migration remain outside this slice.

Revision 3 corrects only the icon export, from 32×32 to the native 16×16 dimensions. A focused regression checks the actual pinned specialization renderer's fixed offset and compares the addon texture with the native Mana Barrier icon. It reproduced the incorrect 32-pixel width before the fix. The standalone resource-pack ZIP applies this correction to revision 2 without restarting. The final icon still needs an in-game visual confirmation; startup alone did not catch the original layout defect.

Revision 3 passes **19 automated tests**, including the new layout regression. All **45 main class files are byte-identical to revision 2**. The standalone ZIP was opened and its contained PNG verified at 16×16. No repeat full-pack startup or gameplay certification is claimed for this asset-only correction. See `evidence/repulsor-icon-fix-checks.txt` for artifact hashes and the red/green result.

## Automated verification

Revision 2 results: **18 automated tests passed; 59 Repulsor runtime checks passed; 26 Experienced runtime checks passed.** Both client load checks passed. The dedicated test server saved and stopped normally. Captured results and the exact addon hash are recorded in `evidence/repulsor-runtime-checks.txt`.

The Gradle suite covers raw formulas, boundary levels and continued overlevels; the exact pinned Vault hash and native hook contracts; and the inherited Experienced arithmetic and one-use run-award scope. The native-runtime command exercises actual pack classes, serialization, gear calculations, mobs, block collision and the normal cast/payment entry point.

The Repulsor runtime checks cover:

- Idempotent configuration merge, both original specializations, eight normal purchases, ninth-rank rejection, and native refunds.
- Effective levels 1, 3, 4, 7, 8, 12, 15, 16, 17, 20, 31 and 64; NBT/network/copy preservation; native group and icon registration.
- Distinct half-block collision geometry; outward destinations including mob width; overlapping cast ownership, independent expiration and owner removal.
- Real near/edge zombie clearance without damage; full knockback resistance; insufficient-mana rejection; exactly 29 mana paid at rank eight; immediate cooldown and rejection of another charge during cooldown.
- Real block raycasts: ordinary player-owned arrows and snowballs block, while the player, Javelin and Fireball pass. Mob collision stays solid and caster movement leaves the wall stationary.
- A native Vault helmet grants nine family overlevels to purchased rank eight. Native 80% CDR produces a 16-tick cooldown at effective level 17.
- Independent barrier/repel radii survive native serialization; previous-version NBT remains readable. The real rank-eight cast keeps the old 4.5 repel clearance while the wall ends at 2.5.
- Live retuning updates future cast values on existing objects while preserving purchased rank, specialization, learned state, remaining cooldown and current mana. Already placed barrier geometry and expiry remain unchanged.

The temporary test arena is in Overworld chunk 0,0 near the build ceiling. The command temporarily loads that chunk and restores its forced state, rejects occupied space and removes its temporary mobs/blocks afterward. Use a disposable world. It does not award Repulsor ranks to the real player.

The inherited Experienced runtime suite covers its actual native award/configuration pipeline. The addon is also started in the full client pack to verify client-side hook loading and registration. See `evidence/repulsor-runtime-checks.txt` for final build results and captured runtime lines.

## Earlier player-acceptance checklist (now accepted)

The full step-by-step guide is `START-HERE.md` in the bundle, or `docs/repulsor-install-and-test.md` in the workspace. Client startup and server assertions do not establish visual gameplay correctness. Automated window capture failed on this Windows installation with `SetIsBorderRequired: No such interface supported (0x80004002)`; no successful screenshot or visual inspection is claimed.

Please check the actual ability menu, current/next labels with modifiers, rendered barrier geometry, hotkeys, pursuing mobs, ordinary projectile flight, player ability flight, terrain obstruction, representative vault rooms, repeated casting across real-time expirations, and save/logout/death/reset behavior. Native data and collision assertions support these paths but do not replace an end-to-end player playtest. Repeat the short Experienced regression in the guide, including its retained UI cleanup.

For revision 2, also check the blue-ring icon and native palette against your supplied examples, the smaller barrier with unchanged repel reach, and a config reload followed by reopening the menu. Resource-pack hot reload instructions follow the native resource path; an interactive F3+T player check remains part of acceptance.

Existing-world migration, dedicated multiplayer, and long-running performance with extreme overlevels remain outside this slice. Player acceptance for issue #19 is recorded above.
