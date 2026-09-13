# Slice 2 — Repulsor Mana Barrier specialization

## Approved follow-up — September 13, 2026

After the first handoff, the user revised this slice: barrier radius is now
`1 + 0.5 * floor((L - 1) / 2)` (L1=1, L3=1.5, L5=2); the repel effect retains
its original range and displacement scaling `1 + 0.5 * (L - 1)`. Mana is now
`15 + 2 * (L - 1)`. Duration and cooldown are unchanged. Description and stat
colors should match the supplied native ability examples; the icon should show
blue rings expanding from a central point with no arrow. Revision 2 implements
these changes and adds live balance reload plus optional resource-pack visual
reload after the one-time code installation/restart.

The original issue text below is retained as a historical snapshot; this
follow-up supersedes its shared-radius and three-mana-per-level formulas.

Deliver **Repulsor**, a new specialization choice that clears nearby mobs with one outward pulse and leaves a short stationary barrier. Build on the delivered Experienced foundation. This ticket ends in its own playable build; Heart Canisters is not a dependency.

## Shared contract and prerequisite

Published shared foundation: [GitHub #17](https://github.com/kpadams94/VH-Expanded/issues/17). Read its complete contract together with this ticket.

Delivery prerequisite: [Experienced, GitHub #18](https://github.com/kpadams94/VH-Expanded/issues/18). This dependency establishes the shared build and first player handoff; it does not authorize starting both features in one task.

Read `docs/specs/00-core.md` before implementation. Target the same pinned Remastered 2.0.4 environment and fresh solo testing. Begin when the user requests this slice after receiving Experienced. Retain and regression-check Experienced. Add Repulsor within the existing Mana Shield/Mana Barrier family rather than replacing its other choices; verify actual menu/group identifiers against the pinned artifact.

## Activation and protection

- Use native specialization selection, learning/reset, keybinding, mana validation, and cooldown conventions. Eight ranks can be purchased normally; effective levels above eight come from the pack's over-level system.
- At activation, produce **one outward knockback pulse** around the player. It should move affected mobs just beyond the new barrier edge in ordinary unobstructed terrain, clearing the interior rather than flinging them far away. No repeated pulse and no added damage.
- Erect the temporary barrier at the cast location. It stays there when the player moves.
- Preserve the Mana Barrier configuration/behavior baseline: players and player abilities pass through; mobs and projectiles are blocked. A player ability represented internally as a projectile still passes. Ordinary player-fired projectiles do not gain an exemption solely from ownership.
- Use the parent ability's verified target and geometry conventions wherever they satisfy the explicit behavior. Preserve native boss/resistance and obstruction handling; do not teleport enemies through solid terrain to force the clearance target. Document any material inability to clear the interior.
- The barrier provides physical exclusion, not a separate damage-absorption pool or invulnerability. Reuse native visuals and sounds with the name **Repulsor**, an accurate description, and a distinguishable specialization icon. New commissioned art is not a dependency.

## Scaling

Let L be effective ability level, including over-levels. These initial balance values remain tunable:

| Property | Raw rule |
| --- | --- |
| Radius for pulse and barrier | `1 + 0.5 * (L - 1)` blocks |
| Barrier duration | `0.5 + 0.5 * floor(L / 4)` seconds |
| Base cooldown | `max(4, 21 - L)` seconds |
| Mana per cast | `15 + 3 * (L - 1)` |

| L | Radius | Duration | Base cooldown | Mana |
| --- | --- | --- | --- | --- |
| 1 | 1 | 0.5 s | 20 s | 15 |
| 4 | 2.5 | 1 s | 17 s | 24 |
| 8 | 4.5 | 1.5 s | 13 s | 36 |
| 12 | 6.5 | 2 s | 9 s | 48 |
| 16 | 8.5 | 2.5 s | 5 s | 60 |
| 17 | 9 | 2.5 s | 4 s | 63 |
| 20 | 10.5 | 3 s | 4 s | 72 |

Apply normal cooldown reduction **after** the four-second base floor. At L17 with 80% reduction, the target is 0.8 seconds (16 normal game ticks). There is no extra mandatory gap between barriers: repeated casting may provide continuous protection if mana and the build sustain it. Higher levels continue radius, duration, and mana scaling after the cooldown reaches its floor.

Engineering defaults: inherit the parent specialization's purchase costs, level gates, and applicable radius/duration/mana modifier pipeline, except where these explicit formulas override base values. Show final modified values accurately. Confirm the inheritance in the implementation note; it is not a claim that every modifier exists on the current parent.

Half-block radius increments must have a meaningful gameplay effect for both pulse and barrier. Native cube-block placement may discretize geometry; investigate before committing to an approach. Do not silently round away upgrades or substitute a different progression. If exact geometry requires a material change in the player's experience, return that conflict for a decision.

## Implementation steps

1. Inspect the live specialized skill tree, Mana Barrier implementation, collision/placement and cleanup rules, serialization, and over-level modifier pipeline. Establish exact registration points before deserializing a new type.
2. Add a stable owned specialization/type, its coordinated tree/config/UI entries, and the pulse/barrier behavior. Keep other family choices functional and repeated config initialization idempotent.
3. Implement all formulas through effective levels and native modifiers. Reject an unaffordable or cooling-down cast through normal rules without creating a free pulse/barrier.
4. Handle repeated casts and cleanup with ownership per barrier instance so expiration of an older overlapping barrier cannot prematurely remove a newer barrier. Reuse existing lifecycle handling where it provides that behavior. Maintain native cleanup across world exit/death; temporary barriers must not become permanent world blocks.
5. Package and test the full slice, including a small Experienced regression. Deliver the user playtest build before starting any later feature.

Research lead: [ability internals #15](https://github.com/kpadams94/VH-Expanded/issues/15) names the live `AbilityTree` / `SpecializedSkill` / `TieredSkill` hierarchy and early `Adapters.SKILL` registration. Verify this against the target jar. [Wold's family configuration](https://github.com/iwolfking/Wolds-Vaults/blob/794d4d9cae540e52443c818078c2a7899a789c06/config/the_vault/abilities_gui_styles.json) places Mana Barrier beside Mana Shield, but does not prove exact Remastered runtime behavior.

## Acceptance and player test

- [ ] Select, purchase, reset, and save/reload Repulsor using native controls; eight purchased levels and over-levels work, and original choices remain usable.
- [ ] In ordinary open space, one cast clears eligible mobs just beyond the edge and erects the stationary barrier; no subsequent pulse or added damage occurs.
- [ ] Players and player abilities, including a projectile-based ability, pass through. Mobs and ordinary projectiles are blocked. Moving away leaves the barrier behind.
- [ ] Verify table rows and boundaries L3/4, L7/8, L15/16/17, including mana costs. Demonstrate half-block radius upgrades and continued over-level scaling.
- [ ] Duration, radius, cooldown, and mana displays agree with observed values and any native modifiers. L17 with 80% reduction demonstrates the 0.8-second cooldown where the modifier is obtainable through a documented test setup.
- [ ] Insufficient mana and active cooldown produce no successful cast. Successful casting charges once.
- [ ] With sufficient mana and reductions, repeated casts maintain protection without forced downtime. Older barrier expiry does not invalidate newer overlap; all barriers clean up after their lifetimes and normal world lifecycle transitions.
- [ ] Test in representative vault terrain as well as open test space. Report collision/placement limitations, including resistant targets, without inventing a new knockback bypass mechanic.
- [ ] Experienced still awards the approved run-XP bonus. Packaged installation and normal save/reload work.
- [ ] Deliver jar/overlay, evidence, and player checklist: select Repulsor, surround the player with test mobs, cast and move, test projectile passage, compare rank 1/rank 8/an over-level build, and try repeated casting.

Stop at the Repulsor handoff. Dedicated multiplayer and existing-world migration remain deferred.
