# v0.1.0 specification index

Review and build these slices in order. Each feature specification contains its own complete behavioral requirements, implementation steps, and acceptance contract, together with the shared foundation. GitHub issue snapshots will be recorded here after publication.

## Published tickets

- [#17 — Shared foundation and delivery contract](https://github.com/kpadams94/VH-Expanded/issues/17)
- [#18 — Experienced and playable addon foundation](https://github.com/kpadams94/VH-Expanded/issues/18)
- [#19 — Repulsor](https://github.com/kpadams94/VH-Expanded/issues/19), following #18
- [#20 — Heart Canisters research and supplied vault recipes](https://github.com/kpadams94/VH-Expanded/issues/20), following #19. All twelve recipes are embedded in its body.

## Blocking relationships

GitHub's native dependency is configured and verified: **Repulsor #19 is blocked by Experienced #18**. The shared-core issue #17 is a reference read alongside each ticket, not a standalone prerequisite that must be closed first. Setup is delivered within #18.

The recipe hold is satisfied. **Heart Canisters #20 is blocked by Repulsor #19**, configured and verified through GitHub's native dependency API. The user receives and tests each playable slice before requesting the next; closing a GitHub dependency does not itself authorize an agent to start another slice.

| Document | Role | Delivery |
| --- | --- | --- |
| [00 — Shared foundation](00-core.md) | Environment, setup, boundaries, and handoff contract | Setup is included with Experienced, not a separate delayed milestone |
| [01 — Experienced](01-experienced.md) | Five-rank vault-run XP replacement | First installable and testable build |
| [02 — Repulsor](02-repulsor.md) | Mana Barrier specialization | Second independent feature handoff, retaining Experienced |
| [03 — Heart Canisters](03-heart-canisters.md) | QOL research, health progression, and twelve supplied recipes | Third independent feature handoff, after #19 |

The combined `docs/planning/v0.1-feature-draft.md` is historical. Feature specs control current requirements; research notes are supporting evidence and may describe different historical mechanics. Engineering defaults are identified as such and must satisfy the approved gameplay.

After publication, the issue bodies are complete reviewable snapshots of shared core plus each relevant slice. Keep a spec and its issue synchronized when an approved decision changes; do not let an older snapshot override a later user correction.

No game launch or runtime compatibility test is represented by these documents. All three tickets are specified for implementation in sequence. Heart Canisters now includes the complete user recipe export, with static path, item-catalog, tag-source, exact BHC recipe/drop, and health-cap verification. Its final loaded recipes and actual crafting still require runtime acceptance. The source export is preserved unchanged in `docs/recipes/heart-canisters/source-export.json`.
