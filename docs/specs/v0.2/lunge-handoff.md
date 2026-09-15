# Lunge design handoff — issue #23

All LU-1–LU-22 player-facing decisions are settled. The final answers authorize recording the handoff and closing #23 once its design checks are complete. Implementation and gameplay acceptance remain in #25; icon/visual preparation remains in #24.

## Authoritative records

- [Combat decisions](lunge-combat-decisions.md): targets, first-contact stop, additive movement and chained casts, fall-distance reset, Impact triggers, proc restrictions, native leech semantics, and required checks.
- [Approved 30-tier tables](lunge-balance.md): every branch's flat damage, AD coefficient, base cooldown, mana, burst/leech fractions, and movement impulse.
- [Planning record](../../planning/v0.2-design-frontier.md) and [retired question queue](../../planning/v0.2-open-questions.md): accepted answers and explicit supersessions.
- GitHub #23 and #25 include the full combat contract and tables so the downstream task does not depend on uncommitted local notes being available remotely.

## Final supersessions

The final movement choice uses 90% of Bullet's impulse and Dash's native directional adjustment, replacing exact aim/no upward tilt. Flurry reaches its two-second base cooldown at rank 11, replacing earlier rank-8/rank-10 endpoints. Its rank-8 mana is 19, replacing the original 20–25 target. Impact bursts only on target/terrain contact, never on timeout/recast/cancellation, and has no numerical target cap. Specializations start at 3 flat damage. Native hurt immunity is retained. No older draft number overrides these answers.

## Explicit playtest deferral and implementation obligations

The user approved the complete starting movement curve and explicitly deferred its feel to the first playable build, with adjustments if enemies or terrain make it awkward. Animation interpolation, camera/body alignment and multiplayer synchronization must be tested during implementation. This is not approval to omit compact-body clearance, first-enemy stopping, or lifecycle restoration. The dive lasts until landing/impact with lifecycle cleanup, not a hidden midair timer.

The first playtest must assess ground/air/water and elytra casts, upward/downward Dash-transformed trajectories, overhead enemy clearance, walls/ceilings, landing, prolonged gliding/water cleanup, chained casts without multiple stored impact payloads, enemy-contact fall rescue versus normal fall damage after misses/terrain, radius/area scaling and Nova falloff, main-plus-burst total damage, native same-target immunity and alternating Flurry targets, and Siphon mitigation/boss/overkill/healing restrictions.

Primary Lunge must deliver native direct weapon proc eligibility at unchanged native chances/counts/modifiers, independent of stale melee charge or critical-swing context. Specializations must not enable those direct procs. Retain normal on-kill systems rather than casually suppressing kill credit/events. Engineering must trace and test each requested effect, avoid applying damage multipliers twice, and give Siphon exactly one native-semantics healing path. This is a behavior contract; static research is not transformed-runtime certification.

Heart fragmentation is a native on-kill effect: the pinned PlayerEvents death listener checks the native Lucky Hit flag guard (including AOE/chaining), Vault location, player damage-source owner, positive HIT_HEARTS and its native random chance. Primary Lunge may generate it through an eligible lethal strike at native chance; specialization damage must retain the suppressing flags. This does not remove ordinary player kill credit or kill-cooldown talent events. Runtime checks remain required.

## Evidence classification

- **User-approved:** branch names; all 30-tier formulas and starting values; combat rules; no target cap; Dash-like starting movement; movement-feel deferral.
- **Independently checked:** pinned source/config for native Dash/Bullet movement, Nova radius/falloff, leech calculation/restrictions, damage immunity, and relevant proc guards; table arithmetic and synchronization of the published handoff.
- **Not performed:** Lunge production implementation, in-game playtest, animation acceptance, multiplayer certification, or a Lunge release build. No old cooldown-talent evidence counts as Lunge evidence.

Research: [combat](../../research/lunge-combat-integration.md), [Nova/Bullet follow-up](../../research/lunge-nova-bullet-followup.md), [movement/falling](../../research/lunge-dash-scaling.md), [leech](../../research/lunge-leech-options.md), [pose](../../research/lunge-pose-feasibility.md).

## Visual design handoff — 2026-09-15

The user selected horizontal glide-style diving with arms reaching forward, no bespoke first-person animation, vanilla crit impact particles, and a smaller existing Nova effect for Impact. [Approved visual brief and feasibility notes](lunge-visual-handoff.md); [four approved icons](../../art/README.md). Rendering, scaling and multiplayer checks remain in #25.
