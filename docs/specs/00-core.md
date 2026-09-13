# Vault Hunters Expanded v0.1.0 — shared foundation and delivery contract

This is the shared specification for three sequential, independently testable vertical slices. It is a reference contract, not a fourth feature or a reason to delay the first playable build. Implement setup together with Experienced.

## Delivery sequence

1. **Experienced:** build the minimal addon foundation and deliver the complete XP expertise replacement.
2. **Repulsor:** extend that foundation and deliver the complete ability specialization after the user has received the Experienced build and requests the next slice.
3. **Heart Canisters:** deliver the research and recipe integration after Repulsor. The user has now supplied the twelve recipes, their static validation passed, and the recipe-publication hold is satisfied.

Each implementation ticket ends with an installable build, a short test guide, and evidence for that slice's acceptance checks. A builder assigned one slice completes that slice and stops at its handoff. Finishing all three is not a prerequisite for user testing. Subsequent builds retain completed features and perform focused regression checks on them.

Published implementation tickets: [Slice 1 — Experienced #18](https://github.com/kpadams94/VH-Expanded/issues/18), then [Slice 2 — Repulsor #19](https://github.com/kpadams94/VH-Expanded/issues/19), then [Slice 3 — Heart Canisters #20](https://github.com/kpadams94/VH-Expanded/issues/20). Native GitHub dependencies enforce #19 blocked by #18 and #20 blocked by #19. Each slice still requires its own handoff and user request before starting the next.

## Target and scope

- Vault Hunters Third Edition — Remastered **2.0.4**, Minecraft **1.18.2**, Forge, **Java 17**.
- Existing repository research records Forge **40.3.11** and `the_vault-1.18.2-20.0.3-remastered.6872.jar`, CurseForge file **8502584**, for this pack. These pins are carried from [closed baseline issue #5](https://github.com/kpadams94/VH-Expanded/issues/5), not independently validated during this specification split. The first builder must confirm them against the official 2.0.4 manifest and installed artifact and record checksums before coding against internals.
- Validate initial play in a **fresh solo test world**. Existing-world migration and dedicated multiplayer validation are deferred. Maintain server-authoritative gameplay on the integrated server and separate client presentation so later multiplayer support remains practical.
- Preserve the official Vault jar. Deliver a separate addon plus the minimal owned configuration/resources required. Do not redistribute proprietary dependency jars.
- The current three slices supersede gameplay assumptions in the closed First Expansion issues, including Shape and Afterimage. Those are not part of v0.1.0.

## Setup, completed inside Experienced

1. Confirm exact pack, loader, Java, and Vault artifact versions. Record reproducible dependency coordinates or legitimate acquisition instructions and artifact hashes in a compatibility note. Verify a clean baseline pack launch.
2. Establish a Java 17 Forge addon build with a checked-in build wrapper and pinned dependencies. Choose a stable addon ID and version convention; record them. No feature framework or future-category implementation is required.
3. Inspect the actual target hooks before selecting runtime injections. Use native hooks where available and narrow mixins when necessary. Keep version-specific adaptation isolated from feature calculations.
4. Apply only owned configuration entries, after the relevant Vault loading step and before consumption/generation. Preserve unrelated entries. Repeated starts must not duplicate ranks or overwrite player/pack settings unrelated to Expanded. An explicit minimal pack overlay is acceptable when runtime merging would add unnecessary complexity; document installation order and the exact changed entries.
5. Produce and install a packaged build in a separate test instance, then exercise Experienced in-game. A development launch alone is insufficient packaging evidence.

Completion: another builder can reproduce the addon, and the user can install the first slice without installing either later feature or waiting for recipes.

## Shared engineering decisions

- Keep authoritative mana, XP, research, and persistence changes server-side; synchronize the existing UI paths correctly. Preserve normal save/reload.
- Keep tuning values in an owned configuration surface where practical. User-approved initial numbers are acceptance targets, not invitations to rebalance silently.
- Reuse native skill purchase/reset, cooldown, modifier, and presentation conventions unless a feature explicitly overrides them. Inspect and document inherited behavior rather than guessing from a newer wiki.
- Use a stable registered skill type if a new behavior requires one. Register before JSON/NBT/network deserialization. Resolve actual registration and load-order names from the pinned runtime.
- Read third-party code as reference. Check the specific file license before copying. Wold's current addon contains mixed licensing; adopting Wold's entire addon or VHAPI is not required.
- Technical implementation choices belong to the builder. Return only material conflicts with the approved player behavior for a product decision; do not ask the user to choose file layout or hook names.

## Per-slice handoff

Deliver the addon jar and required config/resource overlay together, identify their versions and hashes, and list exact destination folders and prerequisite pack version. Include build instructions, meaningful automated checks, observed in-game results, a concise player test checklist, and known limitations. Provide safe removal guidance for the disposable test instance; keep the original pack instance intact.

Record acceptance results as passed, failed, or unverified with evidence. When an environment prevents gameplay verification, supply the build and exact remaining user checks, and label it awaiting validation; do not claim the slice is tested or complete. Report separately when it is ready for the user's playtest. Avoid publishing a combined final v0.1.0 release before all three slices are delivered.

## Sources and remaining verification

- [Official Remastered release](https://www.curseforge.com/minecraft/modpacks/vault-hunters-third-edition-remastered/files/8502758)
- [Recorded Vault dependency](https://www.curseforge.com/minecraft/mc-mods/vault-hunters-official-mod/files/8502584)
- [Forge 1.18 Java/build documentation](https://docs.minecraftforge.net/en/1.18.x/gettingstarted/)
- Closed internal research leads: [bootstrap #13](https://github.com/kpadams94/VH-Expanded/issues/13), [XP internals #14](https://github.com/kpadams94/VH-Expanded/issues/14), [ability internals #15](https://github.com/kpadams94/VH-Expanded/issues/15). Their prototype claims must be checked against available artifacts; their old gameplay decisions do not override these specs.

The local `docs/research` notes contain additional provenance. No game code was implemented or compatibility-tested as part of writing these specifications.
