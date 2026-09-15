# v0.2.0 Prototype: Lunge icons and dive visual direction

Prototype Lunge imagery before production integration. Reference [#21 — v0.2.0: Player enhancements delivery plan and decision record](https://github.com/kpadams94/VH-Expanded/issues/21); approved family is heavy single-target Lunge plus rapid-fire, area-impact, and Vault-style leech. Conduct/guaranteed on-hit imagery is obsolete.

## Work

- Use the `prototype` workflow for a disposable visual comparison; use image-generation capabilities only where appropriate for raster asset exploration.
- Inspect the actual pinned Vault ability icon sizes, palette, silhouettes, frames, and existing addon assets. The prior Repulsor icon needed corrections; do not claim a visually plausible image matches the native atlas without inspection.
- Present distinct icon directions for base and three specializations outside Minecraft, at actual in-game size and enlarged for review, against native-like skill backgrounds.
- Explore headfirst dive/impact imagery or storyboards: compact body, direct first-enemy hit, recognizable damage/rapid/AOE/leech identities.
- Preserve the final #23 movement choice: 90% of Bullet impulse with native Dash upward tilt and pitch handling, enemy-contact stop, and no free elytra flight. This supersedes the older exact-aim draft. Do not treat an image/mock animation as evidence of collision correctness.
- Capture user-selected direction, asset dimensions/transparency/export requirements, and a short animation brief for the implementation.
- Keep final animation timing and feel open to user iteration in the real Lunge build. This task is not a production ability implementation.

## Completion

- [x] User can compare alternatives and approves a coherent icon family at native size.
- [x] Record the selected direction and rejected/ambiguous readings, with actual preview artifacts.
- [x] Provide the implementation task an asset/animation brief and clearly distinguish provisional art from accepted exports.
- [x] Prototype sources and approved assets captured on `codex/lunge-art-approved`, linked from #24/#25; production integration remains in #25.

## How to work with the user

Use the named skill when this task is started. Keep research, findings, possible overlaps, and prototype results in the central chat. **The user's workflow overrides skill defaults for asking questions:** maintain `docs/planning/v0.2-open-questions.md` by feature with stable IDs; the user answers any subset in chat when ready. Do not replace that with a rapid series of question boxes. Reuse settled decisions; do not interview the user again about resolved questions. Fold approved answers into the plan and downstream issue bodies; retain unresolved qualifications. Research facts yourself, using bounded research agents where the skill calls for them. Do not invent balance/recipes from unanswered choices.

A completed design task must leave a concrete, user-approved handoff and explicit remaining deferrals. Do not implement production gameplay in this task. Prototype assets/code are disposable exploration, not automatic production acceptance.

## Workflow investigation and first experiment

Started by user request to improve pixel-art quality. Proposed method is recorded in `docs/pixel-art-workflow.md`; independent critique is in `docs/research/pixel-art-workflow-review.md`. The first base-Lunge trial is now user-approved; see the approval record below.

Verified native Dash/Bullet/Nova/Leech/Barrier icons are 16×16. Native colors/alpha vary, so do not impose a universal low-color-count or binary-alpha rule. The old hand-drawn workflow already used true 16×16 and nearest-neighbor enlargement; those alone do not solve composition quality. Piskel was opened through browser control and a blank canvas successfully set to 16×16. No art, export round-trip, or production replacement was performed.

Proposed workflow: create three genuinely different 16×16 base-Lunge silhouettes, reject weak readings at actual size, refine the strongest in a pixel editor with a live native-size preview, then extend its visual language to Flurry/Impact/Siphon. Image generation is optional concept/storyboard support; automatic reduction of large generated art is not the finishing step. Use Piskel as the immediate no-install editor candidate; Aseprite with computer use is an alternative if available. Editor choice must prove itself in the pilot.

Review exact exported PNGs at 1×, integer GUI scales and enlarged nearest-neighbor inspection, on native-like backgrounds beside native references. Independent readability review receives neutral filenames without the author's explanation; integration review separately checks dimensions, alpha, alignment, context and provenance. Reviewers advise; the user selects the final family. Preserve editable source, versioned candidates, reviewer objections and selected hashes. Later verify the packaged PNGs and actual game rendering.

Completed artifact checkpoint: three base silhouettes, one refined candidate, editable source, native-size comparison and independent findings. All four exact native PNGs are approved; see the family manifest below. Actual disposable prototype sources/previews will be captured on an isolated throwaway branch with a verdict as required by the prototype workflow; native extracted assets and unrelated working changes remain excluded.
## Base Lunge approval — 2026-09-14

The user approved the exact 16×16 candidate, including its existing spacing, and reported a substantial quality improvement over the previous workflow. See [approved PNG, editable source, preview, and hash](../../art/lunge/README.md). Base approval does not complete the remaining specialization or animation work, so #24 remains open. The reusable process is installed as the personal `slash-art` skill.


## Icon family approval — 2026-09-15

Lunge, Flurry, Impact and Siphon are approved. [Canonical assets, sources, previews and exact hashes](../../art/README.md). Animation brief, prototype capture, implementation handoff and later game verification remain separate outstanding work.


## Animation and particles selected — 2026-09-15

[Visual handoff](lunge-visual-handoff.md): horizontal dive, forward-reaching arms, ordinary first-person view, vanilla crit contact particles and smaller native Nova for Impact. All icon and animation design choices are recorded. Prototype branch capture remains outstanding; rendering implementation and playtests remain in #25.


## Completed handoff

All decisions and icon approvals are settled. The approved artifacts and owned prototype sources are captured on `codex/lunge-art-approved`. Issue #24 can close; #25 owns production integration, movement/pose/particle tuning, packaged-byte checks and multiplayer/gameplay acceptance.

