# Lunge balance — approved starting values

Design answers for #23, 2026-09-14. LU-17 sets Flurry's two-second base cap at rank eleven, superseding earlier endpoints; LU-18 sets all specialization flat starts to 3. These are arithmetic projections of user-selected rules, not tested gameplay balance or a complete approved implementation table.

Let `L` be effective tier, clamped to 1–30, and `AD` be the player's attack damage. There are eight learnable ranks, one skill point each, with no level/prerequisite gate. Defined damage scaling continues through tier 30; values above 30 remain at the last configured tier.

## Damage

- Lunge strike: `5 + 2 × (L−1) + (0.10 + 0.10 × (L−1)) × AD`. The coefficient is the AD contribution to the ability's damage; no extra ordinary melee hit is added.
- Flurry, Impact and Siphon strike: `3 + (L−1) + (0.10 + 0.05 × (L−1)) × AD`. LU-18 sets each starting flat value to 3; each gains 1 flat damage per rank.
- Impact burst: `(0.30 + 0.05*(L-1)) × configured Impact strike damage`. It reaches 100% at rank 15 and 175% at rank 30. Base radius stays 2 blocks, with native percentage area scaling/limits. Nova falloff is 100/80/60/40/20% over five equal distance bands from collision origin. Walls block the burst; there is no numerical target cap. Main target receives main plus its burst share. Target/terrain contact only; no burst on expiry, recast or cancellation.
- Siphon leech fraction: `min(0.80, 0.25 + 0.05*floor((L-1)/2))`. Ranks 1–2:25%, 3–4:30%, cap 80% at rank 23. Damage continues scaling afterward. Preserve native leech semantics and healing modifiers/restrictions; no global healing-effectiveness bonus.
- LU-14 accepts impact-time AD and independence from melee swing charge. No AP component or ordinary melee crit/sweep bonus is added; primary native proc chances/counts/modifiers remain governed by the deliberate ability integration.

## Cooldowns

Cooldowns below are base values before ordinary cooldown reduction/refunds. Half-second steps equal ten ticks at 20 ticks per second and are representable by the native integer cooldown field.

- Lunge: rank 1 starts at 20 seconds, then −0.5 seconds per rank, ending cooldown progression at rank 12: `20 − 0.5 × (min(L,12)−1)`. Rank 8 is 16.5 seconds; rank 12+ is 14.5 seconds.
- Impact and Siphon: rank 1 starts at 30 seconds, then −0.5 seconds per rank, ending cooldown progression at rank 12: `30 − 0.5 × (min(L,12)−1)`. Rank 8 is 26.5 seconds; rank 12+ is 24.5 seconds. The user's final half-second instruction supersedes the briefly suggested one-second Siphon step.
- Flurry: `max(2, 12−(L−1))` seconds. Rank 1 = 12, rank 8 = 5, rank 10 = 3, rank 11+ = 2. LU-17 explicitly prioritizes the starting value and one-second-per-rank scaling over earlier endpoint estimates.
- Mana: Lunge `15+3*(L-1)`; Flurry `12+(L-1)`; Impact/Siphon 25 at every tier. The old Flurry rank-eight 20–25 target is superseded by 19 mana.

## All defined tiers

Damage, cooldown, mana, burst percentage and leech fraction follow resolved user-selected formulas. Exact movement tuning and Impact target cap is removed. All damage values precede target mitigation and other applicable native modifiers. Cooldown caps do not cap damage or leech progression.

| Tier | Lunge flat | Lunge AD | Spec flat | Spec AD | Lunge CD | Impact/Siphon CD | Flurry CD | Lunge mana | Flurry mana | Impact/Siphon mana | Burst | Leech |
|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| 1 | 5 | 10% | 3 | 10% | 20 | 30 | 12 | 15 | 12 | 25 | 30% | 25% |
| 2 | 7 | 20% | 4 | 15% | 19.5 | 29.5 | 11 | 18 | 13 | 25 | 35% | 25% |
| 3 | 9 | 30% | 5 | 20% | 19 | 29 | 10 | 21 | 14 | 25 | 40% | 30% |
| 4 | 11 | 40% | 6 | 25% | 18.5 | 28.5 | 9 | 24 | 15 | 25 | 45% | 30% |
| 5 | 13 | 50% | 7 | 30% | 18 | 28 | 8 | 27 | 16 | 25 | 50% | 35% |
| 6 | 15 | 60% | 8 | 35% | 17.5 | 27.5 | 7 | 30 | 17 | 25 | 55% | 35% |
| 7 | 17 | 70% | 9 | 40% | 17 | 27 | 6 | 33 | 18 | 25 | 60% | 40% |
| 8 | 19 | 80% | 10 | 45% | 16.5 | 26.5 | 5 | 36 | 19 | 25 | 65% | 40% |
| 9 | 21 | 90% | 11 | 50% | 16 | 26 | 4 | 39 | 20 | 25 | 70% | 45% |
| 10 | 23 | 100% | 12 | 55% | 15.5 | 25.5 | 3 | 42 | 21 | 25 | 75% | 45% |
| 11 | 25 | 110% | 13 | 60% | 15 | 25 | 2 | 45 | 22 | 25 | 80% | 50% |
| 12 | 27 | 120% | 14 | 65% | 14.5 | 24.5 | 2 | 48 | 23 | 25 | 85% | 50% |
| 13 | 29 | 130% | 15 | 70% | 14.5 | 24.5 | 2 | 51 | 24 | 25 | 90% | 55% |
| 14 | 31 | 140% | 16 | 75% | 14.5 | 24.5 | 2 | 54 | 25 | 25 | 95% | 55% |
| 15 | 33 | 150% | 17 | 80% | 14.5 | 24.5 | 2 | 57 | 26 | 25 | 100% | 60% |
| 16 | 35 | 160% | 18 | 85% | 14.5 | 24.5 | 2 | 60 | 27 | 25 | 105% | 60% |
| 17 | 37 | 170% | 19 | 90% | 14.5 | 24.5 | 2 | 63 | 28 | 25 | 110% | 65% |
| 18 | 39 | 180% | 20 | 95% | 14.5 | 24.5 | 2 | 66 | 29 | 25 | 115% | 65% |
| 19 | 41 | 190% | 21 | 100% | 14.5 | 24.5 | 2 | 69 | 30 | 25 | 120% | 70% |
| 20 | 43 | 200% | 22 | 105% | 14.5 | 24.5 | 2 | 72 | 31 | 25 | 125% | 70% |
| 21 | 45 | 210% | 23 | 110% | 14.5 | 24.5 | 2 | 75 | 32 | 25 | 130% | 75% |
| 22 | 47 | 220% | 24 | 115% | 14.5 | 24.5 | 2 | 78 | 33 | 25 | 135% | 75% |
| 23 | 49 | 230% | 25 | 120% | 14.5 | 24.5 | 2 | 81 | 34 | 25 | 140% | 80% |
| 24 | 51 | 240% | 26 | 125% | 14.5 | 24.5 | 2 | 84 | 35 | 25 | 145% | 80% |
| 25 | 53 | 250% | 27 | 130% | 14.5 | 24.5 | 2 | 87 | 36 | 25 | 150% | 80% |
| 26 | 55 | 260% | 28 | 135% | 14.5 | 24.5 | 2 | 90 | 37 | 25 | 155% | 80% |
| 27 | 57 | 270% | 29 | 140% | 14.5 | 24.5 | 2 | 93 | 38 | 25 | 160% | 80% |
| 28 | 59 | 280% | 30 | 145% | 14.5 | 24.5 | 2 | 96 | 39 | 25 | 165% | 80% |
| 29 | 61 | 290% | 31 | 150% | 14.5 | 24.5 | 2 | 99 | 40 | 25 | 170% | 80% |
| 30 | 63 | 300% | 32 | 155% | 14.5 | 24.5 | 2 | 102 | 41 | 25 | 175% | 80% |

Cooldown columns are seconds before CDR/refunds. Burst is the pre-falloff fraction; leech is before native damage-relative limits/healing modifiers. Spec denotes each of Flurry, Impact and Siphon.

Movement: `0.9 * (11 + floor(L/2)) * 0.15` before native Dash pitch handling, at all 30 tiers. Use native upward tilt and angle-based strength. No midair timeout; land/impact/lifecycle cleanup ends the dive. No Impact target cap. All player-facing design questions are answered; movement feel is explicitly deferred to the first playable build. Heart fragmentation is a native on-kill effect: the pinned PlayerEvents death listener checks the native Lucky Hit flag guard (including AOE/chaining), Vault location, player damage-source owner, positive HIT_HEARTS and its native random chance. Primary Lunge may generate it through an eligible lethal strike at native chance; specialization damage must retain the suppressing flags. This does not remove ordinary player kill credit or kill-cooldown talent events. Runtime checks remain required. No runtime acceptance is claimed.


## Approved movement curve

Pre-directional impulse; the native Dash directional coefficient and upward tilt apply afterward. Smaller growth uses Bullet's same step cadence at 90% magnitude. These are starting test values, not measured travel distances.

| Tier | Impulse |
|---:|---:|
| 1 | 1.485 |
| 2 | 1.620 |
| 3 | 1.620 |
| 4 | 1.755 |
| 5 | 1.755 |
| 6 | 1.890 |
| 7 | 1.890 |
| 8 | 2.025 |
| 9 | 2.025 |
| 10 | 2.160 |
| 11 | 2.160 |
| 12 | 2.295 |
| 13 | 2.295 |
| 14 | 2.430 |
| 15 | 2.430 |
| 16 | 2.565 |
| 17 | 2.565 |
| 18 | 2.700 |
| 19 | 2.700 |
| 20 | 2.835 |
| 21 | 2.835 |
| 22 | 2.970 |
| 23 | 2.970 |
| 24 | 3.105 |
| 25 | 3.105 |
| 26 | 3.240 |
| 27 | 3.240 |
| 28 | 3.375 |
| 29 | 3.375 |
| 30 | 3.510 |
