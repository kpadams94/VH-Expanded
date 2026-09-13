# Repulsor compatibility and implementation record

Issue #19 implements the shared issue #17 contract on top of the accepted issue #18 Experienced slice. Addon ID: `vhexpanded`. Version: `0.1.0-repulsor.3`.

## Target and build

Use Vault Hunters Third Edition — Remastered **2.0.4**, Minecraft **1.18.2**, Forge **40.3.11**, Java **17**. The official client distribution is CurseForge file **8502758** and its corresponding server package is **8502840**. The pack manifest pins Vault project **458203**, file **8502584**: `the_vault-1.18.2-20.0.3-remastered.6872.jar`.

Vault SHA-256: `fc6adfeb76071d61e633027334fc95e8efb7faf8a4cb57d646f8176b4f75390b`.
Pack archive SHA-256: `e951c9c5a3d7f5e8a61be3d3fe565e2076f1a8df9c94e3830282bf53bc7d71a2`.
The pack archive's internal version text says 2.0.3; the official release/file ID identifies this 2.0.4 distribution. These pins and the acquisition record are inherited from the completed Experienced slice.

The wrapper uses Gradle 8.8, ForgeGradle 6.0.54, official 1.18.2 mappings, and Java 17. Set `JAVA_HOME` to a Java 17 JDK and run `gradlew.bat build`, then `gradlew.bat playtestBundle`. The build fetches the exact Vault dependency through `curse.maven:vault-hunters-official-mod-458203:8502584`; tests verify its hash and native hook bytecode. Outputs are `build/libs/vh-expanded-0.1.0-repulsor.3.jar` and `build/distributions/vh-expanded-repulsor-playtest.zip`.

Only the addon, documentation, optional tuning/resource pack and checksums are distributed. Native jars, pack files, decompiled sources and native assets are not embedded. The blue-ring icon is newly generated art exported as a transparent 16-by-16 game texture. `.local` is an ignored inspection/runtime workspace. Original Vault remains unchanged. This is an independent implementation; no Wold's code or assets were copied.

## Native integration

| Seam | Implementation and reason |
|---|---|
| `ModConfigs.register` tail | Idempotently merges one Repulsor choice into `Mana_Shield`, alongside the existing base and Mana Barrier choices. Retains their data. Native `reloadcfg` reapplies the merge. |
| Native skill adapter | Registers addon-owned ability and tier types before normal deserialization. Native NBT/network serialization, copying, buying, specialization and regret continue to work. |
| `TieredSkill` / `SkillBonusTierCache` | Eight ranks can be purchased. Native family/group/all-ability bonuses choose the effective tier. The owned tier type extends the finite table when a larger effective level is required. |
| `InstantManaAbility` | Native unlock, mana and cooldown gates run before the action. Native post-action pipeline debits modified mana once. Successful casts start cooldown immediately, with no duration-based delay. |
| `ManaCostHelper`, `CooldownHelper`, `AreaOfEffectHelper` | Native modifier ordering is retained. The raw cooldown reaches an 80-tick floor before native CDR; the floor is not reapplied afterward. Radius stays fractional through AoE adjustment and collision generation. |
| Ability descriptions, labels, GUI lookup and group | Uses the pinned color palette, a brief description and native cast line. Current/next fields show duration, mana, cooldown, barrier radius and repel radius. Values include native modifiers. The new ring icon is an addon-owned filename in the native namespace because the pinned ability atlas filters out other namespaces. No native icon is overwritten. |
| Pulse | Queries the native Vault target selector within the original, larger repel cube. Calls native knockback for resistance/events, then applies the generated horizontal displacement using ordinary entity movement, which resolves solid-block collisions. Both target range and outward destination retain the original scaling, independently of the smaller barrier. No damage call, teleport, damage absorption or invulnerability is introduced. |
| Barrier cells | Addon-owned temporary block entities store thin collision faces at the exact fractional boundary. Air/replaceable cells are eligible; occupied terrain is preserved. Caster block center anchors the stationary cube, consistent with native placement. |
| Passage rules | Players and native pet/Vessel exceptions pass. Player-origin Javelin, Fireball, Ice Bolt, Grenade, Toxic Grenade, Decoy projectile, Storm Arrow and Lightning Orb are exempt. Ordinary projectiles, including arrows and snowballs owned by players, collide. Context-free projectile embedding checks do not falsely mark exempt projectiles embedded. |
| Client rendering and zoned-world updates | Renders the native Mana Barrier model on the actual face geometry without bundling its assets. Reuses the pinned native barrier's client chunk refresh helper, coalesced once per changed chunk per tick. Placement/removal uses the native zoned-world bypass. |
| Ownership and expiry | Each cast has a unique ID, caster ID and absolute game-time expiry. Shared cells combine active faces; expiry removes only the relevant contribution. Death, logout and dimension change remove the caster's loaded barriers. Server/world shutdown clears loaded cells; unloaded saved cells prune stale contributions when they tick again. |

Raw effective-level formulas in revision 2 are barrier radius `1 + 0.5*floor((L-1)/2)`, repel radius `1 + 0.5*(L-1)`, duration `10 + 10*floor(L/4)` ticks, cooldown `max(80,420-20*L)` ticks, and mana `15 + 2*(L-1)`. These revisions supersede the initial issue's barrier and mana formulas at the user's request. Both radii pass independently through the native AoE modifier pipeline. Native timers run in ticks; half-second durations remain precise. Fractional faces are 0.125 blocks thick and lie inside the specified outer boundary.

The new `repelRadius` field is serialized in JSON, NBT and native skill network data. Revision-1 NBT without that field remains readable, using its old radius until the native config merge applies revision-2 values. The addon network protocol is bumped to 2 so incompatible addon versions are rejected. Installing the code update requires a restart; replacing a loaded jar is not supported.

## Scope and operating limits

Certification targets fresh solo play. Existing-character migration and dedicated multiplayer gameplay are deferred. A dedicated pack server is used as a runtime test harness; that is not a multiplayer certification claim. Remove old VH Expanded jars when installing this combined build.

This physical barrier does not guarantee safety from enemies already inside, attacks within melee reach, explosions or other non-projectile effects. Resistance and terrain can prevent full clearance. Like the native placement rule, occupied cells are skipped; partial blocks can leave openings. Very large overlevels increase the surface area and block-update cost; no sustained extreme-level performance claim is made.

Optional `config/vhexpanded-repulsor.json` overrides the bundled coefficients. After installing revision 2, `/vhexpanded repulsor reload` validates the full file, retunes the existing template and online players' Repulsor ability objects in place, and sends native ability-tree synchronization. It preserves learned flags, purchased ranks, specialization, points, current mana and running cooldown state. Existing barrier parts keep their geometry and absolute expiry. Login also reapplies tuning for players absent during reload. Invalid input is rejected before live tree mutation. Old overrides still override defaults, so replace an old three-mana-per-level file when adopting this revision. The standard checks assume default tuning.

The optional `VHExpanded-Repulsor-Visuals` resource pack contains the owned icon and translation strings. Once enabled, these can be edited and reloaded with F3+T. Reopen the ability menu after reloading. The resource pack is optional; identical defaults are bundled in the addon. Runtime code changes still require a restart.

Experienced behavior and its accepted presentation fixes remain in this jar. The prior compatibility record is included under `experienced/`; its older artifact name is historical.
