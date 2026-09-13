# Development and release guide

These are the durable project decisions for updates after 0.1.0. The code is the authority for current mechanisms; this guide records why and how we deliver them. Earlier GitHub #17 and `docs/specs` preserve the initial release contract, not an evergreen backlog.

## Feature work

- Write a bounded behavior/specification and acceptance checks for each requested feature. Deliver a playable vertical slice before beginning the next requested slice. Use GitHub blocking relationships for actual prerequisites.
- The user owns gameplay and balance. Make routine engineering choices independently; bring back material conflicts with the approved player behavior. Preserve later playtest decisions over older draft specs.
- Keep one addon (`vhexpanded`) and stable persisted skill/type IDs. Keep gameplay server-authoritative and client presentation separate. Initial multiplayer harness results do not equal multiplayer player certification.
- Prefer verified native integration and owned, idempotent config merges. Preserve unrelated entries. Use narrow version-specific mixins only where necessary; retain the official Vault jar unchanged.
- Keep recipes under their intended IDs. Check the final loaded recipe manager, not only source JSON; the required built-in BHC recipe pack exists because ordinary resource ordering preserved cheap native recipes.
- Keep research names synchronized across research, GUI, and Mod Box configuration. Native Mod Boxes use two-stage weights, not fixed percentages. Preserve four BHC drop maps when disabling mob drops.
- Treat the pinned pack/dependency versions and hashes as a compatibility boundary. Inspect and test hooks when changing any pin. Current pins live in `build.gradle` and `docs/heart-canisters-compatibility.md`.

## Repository boundaries

Commit source, build wrapper, owned assets/config, user-approved recipe exports, specifications, and concise test evidence. Ignore local runtime instances, caches, downloaded jars, extracted/decompiled third-party material, and generated archives. `asset-staging` is local research material. `recipe-tweaker` is a separate, already-versioned local authoring repository; do not accidentally add it as an embedded Git repository or bundle its extracted mod assets with this addon.

## Releasing a version

1. Reconcile the shipped behavior with accepted playtest decisions and record remaining support limits. Keep historical evidence clearly labeled; never overwrite an old result as if it was rerun.
2. Set the version in `build.gradle`. Keep the display name **Vault Hunters Expanded**; public versions/bundles use the version alone, without the last feature's name.
3. Add a newest-first version to `CHANGELOG.md`, with **Added**, **New Mods**, **Balance**, and **Bug Fixes** sections. Leave unused sections empty. Initial-release development corrections are not fixes to an earlier public release.
4. Update `README.md`, `docs/INSTALL.md`, and a version-specific release record. Run `gradlew.bat build releaseBundle` with Java 17. Verify the packaged metadata, one addon jar, required overlay/resources, and checksums. Run targeted runtime/player checks when behavior or integration changes justify them.
5. Record automated results and user-reported playtests separately. Carry forward unchanged gameplay evidence explicitly for packaging-only releases; never call it a new game test.
6. Review the staged file inventory, commit, and push to the configured repository. Tag an accepted release version after the release commit. Keep binaries in release assets rather than source history. Publishing GitHub release assets is a separate user-directed step.
7. Close release tracking only when the agreed checks/handoff are done. Future versions use their own tickets; retain this guide instead of keeping an old setup issue open indefinitely.
