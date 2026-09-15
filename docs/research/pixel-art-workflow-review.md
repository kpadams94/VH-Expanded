# Pixel-art workflow: independent review

Date: 2026-09-14. Scope: existing local icon evidence and a proposed review method for #24. This is workflow analysis, not acceptance of new artwork or an in-game test.

## Observed evidence

- `docs/evidence/repulsor-icon-fix-checks.txt` records a shipped 32×32 export where the native widget expected 16×16. The larger export doubled visual size and shifted the center by eight GUI pixels. A targeted native-anchor test caught the mismatch. Export dimensions are part of correctness, not an art preference.
- `docs/cooldown-visual-review.md` records that the user supplied the accepted 16×16 Second Wind icon after generated and hand-drawn proposals. The accepted source was copied byte-for-byte. This establishes the outcome of that iteration; it does not prove either art technique is inherently unsuitable.
- Visually inspected the shipped Repulsor and Second Wind PNGs at their actual small size, the enlarged prior hand-drawn Second Wind draft, and `docs/evidence/cooldown-icon-preview.png`. The icons occupy a very small visual budget. The prior draft uses a diagonal sword and a large lower-right hourglass; their overlapping outlines and the hourglass's wide frame make the two shapes compete. The accepted preview uses a narrower hourglass silhouette. These are review judgments, not user acceptance claims.
- `.local/cooldown-pixel-draft/draw.ps1` does author on a true 16×16 canvas and creates its inspection enlargement using nearest-neighbor scaling. Thus exact-size authoring and crisp enlargement already exist; merely adding either would not address the whole quality problem.
- The draft script describes shapes with loops and overlays, then renders one composition. The inspected scripts do not contain a comparative thumbnail selection or independent visual gate.
- The existing deterministic GUI previews explicitly distinguish themselves from actual gameplay screenshots. Preserve that distinction.

## Proposed workflow and tool roles

1. **Specify the target before art.** Record verified dimensions, transparency, native reference assets, widget background, and intended semantic cue. For this icon family, use the verified 16×16 contract; do not generalize that size to every future asset. Keep extracted native references local.
2. **Choose composition at native size.** Make three substantially different silhouettes with a restrained palette. Judge them together at 1× and expected integer GUI scales before investing in highlights. Every variant must already be an actual 16×16 PNG. A beautifully enlarged image cannot substitute for this gate.
3. **Use image generation for visual exploration when useful.** It can suggest shapes, palette, light direction, or a family treatment. Treat large generated images as references, not final icons. Do not claim that a prompt requesting 16×16 guarantees 16×16 pixel structure. Any reduced candidate must pass the same native-size review; automatic downsampling is not an acceptance method.
4. **Use a native pixel editor for deliberate refinement.** A one-pixel pencil, layers, exact canvas dimensions, palette control, and nearest-neighbor zoom make silhouette revisions visible and reversible. Browser or computer control is useful for direct editor manipulation and inspecting settings. Code is useful for exact exports, palette substitutions, contact sheets, and validators. Changing the input tool alone does not create visual taste: the comparative review loop is the main improvement.
5. **Review the final PNG in context.** A browser review board should display the actual candidate file at 1×, 2×/3× GUI scale, and 12×/16× inspection scale, with pixelated interpolation and no automatic fit-to-card enlargement. Include dark and light native-style backgrounds and nearby native icons. Keep a pixel grid optional and inspection-only. Label simulated UI honestly.
6. **Use two bounded independent reviews.** Give the readability reviewer unlabeled candidates, native-scale views, and a list of intended ability concepts; ask which concept each communicates and which pairs could be confused. Give the integration reviewer dimensions, references, exported files, and preview rules; ask about scale, contrast, alignment, transparency, and family consistency. Neither reviewer should receive the author's justification first. Require concrete pixel/shape observations, not a generic numerical beauty score.
7. **Revise the weakest candidate, then ask for user selection.** Reviewers advise; they do not certify aesthetic approval for the user. Limit each iteration to a small set with one explained improvement. Avoid presenting a pile of near-identical variants.

## Gates before source integration

| Gate | Evidence | Reject when |
| --- | --- | --- |
| Exact file contract | Decoded final PNG dimensions, alpha information, recorded hash | Size is wrong, image is a screenshot/contact sheet, or an enlargement was exported as the asset |
| Native-scale readability | Actual file at 1× and expected GUI scale; blind reviewer observations | Core silhouette becomes noise, or branches differ only through details invisible at those scales |
| Pixel discipline | Nearest-neighbor enlargement, palette and alpha inspection | Unintentional smoothing, muddy reduction artifacts, stray isolated pixels, or clipped forms compromise the intended silhouette |
| Native context | Actual file on relevant backgrounds beside native references | Border/contrast or apparent weight makes the icon disappear or dominate unintentionally |
| Approval provenance | User-selected filename/hash and recorded reviewer concerns | A draft is substituted for the selected file, or reviewer approval is reported as user approval |
| Packaging | Decode PNG from the built jar and compare to the selected asset | Export/package differs from the selected file or the native anchor contract |

Do not impose a universal hard color-count or binary-alpha rule until the native reference family has been inspected. Count and report those properties; treat suspicious differences as review prompts. Mechanical checks establish compatibility, not quality. The final in-game view establishes actual rendering, not just a reconstructed preview.

## Recommendation

Use an exact-grid editor plus an actual-file review board and independent native-scale critique as the default production loop. Keep image generation optional for concepts. Retain existing native-anchor checks and strengthen selection/package provenance. The critical change is rejecting unreadable or wrong-scale work before it reaches user selection, while showing the user the same small asset that will ship.
