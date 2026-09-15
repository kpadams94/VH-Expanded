# Pixel-art production workflow

Proposed working method for #24, grounded in the existing addon failures and pinned native assets. This is a process recommendation, not user approval of any new icon. New Lunge art has not been produced or selected yet.

## What changes

The main change is a composition-and-review loop: compare several actual-size silhouettes, refine the strongest in a pixel editor, then review the exported file independently before presenting it for approval. Exact dimensions and nearest-neighbor enlargement already existed in the older hand-drawn scripts. Repeating those steps alone will not fix visual quality.

Avoid both a large generated illustration shrunk into an icon and a single coordinate-script composition polished without comparison. Image generation remains useful for discovering a visual idea. The production decision happens on the real pixel grid.

## Verified target and evidence

Pinned Vault jar: `.local/server-pack/mods/the_vault-1.18.2-20.0.3-remastered.6872.jar`. Directly inspected ability textures:

| Native reference | Dimensions | Visible RGBA colors | Alpha observations |
|---|---|---:|---|
| Dash | 16×16 | 63 | Includes 253 alpha |
| Bullet (`dash_damage`) | 16×16 | 73 | Includes several partial-alpha values |
| Nova | 16×16 | 97 | Transparent/opaque only |
| Rampage Leech | 16×16 | 47 | Transparent/opaque only |
| Mana Barrier | 16×16 | 96 | Transparent/opaque only |

Counts are measurements, not quality targets. Start with a few readable value/color groups, then add purposeful shading. Do not force every native-style icon into a tiny fixed palette or prohibit all partial alpha. Reject unintended filtering and muddy edges based on inspection.

Repulsor's earlier 32×32 export doubled its visual size and shifted its center by eight GUI pixels. The current native anchor test checks 16×16. The accepted Second Wind image came from the user after prior proposals. See [Repulsor evidence](evidence/repulsor-icon-fix-checks.txt), [Second Wind provenance](cooldown-visual-review.md), and [independent critique](research/pixel-art-workflow-review.md).

## Tool choices

| Tool | Best role | Limitation / evidence |
|---|---|---|
| Browser + Piskel | Draw/refine on the native grid with pen, layers, palette, undo and live small preview; export source and PNG | Browser control was connected and a blank canvas changed from 32×32 to 16×16. Drawing/export round-trip still needs the pilot. Public editor includes ads. |
| Computer use + Aseprite | Alternative desktop pixel editor, especially for animation and editable layer/frame sources | Not found in PATH or the checked common install locations; this is not proof it is absent everywhere. No installation or purchase performed. |
| Image generation | Optional composition/palette exploration or larger dive storyboards | A prompt saying “16×16” does not establish an actual 16-pixel structure. Generated concepts are not accepted exports. |
| Browser review board | Display exact exported PNGs beside native references at controlled scales/backgrounds; switch alternatives | A simulated UI is not an in-game screenshot. Control browser zoom/DPI and label source-pixel scale separately from physical display pixels. |
| Independent subagents | Readability critique without the author's explanation; separate integration/provenance review | Reviewers can share biases. Concrete findings support user selection; votes or beauty scores cannot certify quality. |
| File tools / export validation | Inspect dimensions, alpha, bounds, compare file hashes and built-jar contents | Mechanically correct art can still look poor. Code-generated shapes are not automatically a substitute for visual revision. |

Piskel is the immediate no-install candidate; retain editable `.piskel` plus exact PNG exports. If an accessible Aseprite installation is available later, retain `.aseprite` sources and export PNG at 100%. Tool choice should follow the measured pilot, not a claim that mouse clicks inherently make better art. Official references: [Piskel editor](https://www.piskelapp.com/), [Aseprite sprite dimensions](https://www.aseprite.org/docs/sprite-size/), [source preservation/export](https://www.aseprite.org/docs/save/), [batch export](https://www.aseprite.org/docs/cli/).

## Production loop

1. **Record the asset contract.** For this family: one 16×16 transparent RGBA PNG per icon, one frame, square pixels, native registration/anchor, no baked UI frame unless native integration calls for one. Names and gameplay come from #23: Lunge, Flurry, Impact, Siphon. Inspect native background, lighting direction, visual weight and padding before choosing them. Future asset types require their own dimensions.
2. **Explore shape before detail.** Produce three substantially different base-Lunge silhouettes at 16×16. Distinguish a thrust/impact from generic running, a sword item, or flight. Keep each concept to one dominant shape and at most one supporting motion/impact cue. Optional generated references may inspire shapes; they do not count as these native-grid candidates.
3. **First rejection gate.** View the three silhouettes at 1× and representative integer GUI scales, without labels first. Reject unclear candidates before highlights, gradients, borders or specialization work. Do not assume all failures can be repaired through more detail.
4. **Refine the best candidate deliberately.** Work in short passes: silhouette, separation between shapes, light/dark groups, color, then highlights. Keep live 1× visible. Review after each meaningful change; use undo/history and versioned sources. Avoid long blind sequences of pixel placement. Draw opaque structural shapes first; deliberate translucent effects come last if useful.
5. **Extend a coherent family.** Only after the base is readable, develop Flurry/Impact/Siphon from the same visual language. Differentiate by silhouette or action cue as well as color. A tiny added glyph that vanishes at native size is insufficient. Color choices and motifs remain proposals until user selection.
6. **Review actual exports.** One board uses the exact candidate PNGs at 1×, 2×/3× representative GUI scale and 12× or 16× nearest-neighbor inspection. Show native-style dark/light panels and neighboring native icons, an optional pixel grid at inspection scale, and a grayscale comparison. Enlarged previews must be clearly labeled and never used as ship files.
7. **Independent review.** A fresh readability reviewer gets neutral filenames and no author explanation: first describe what each icon looks like, then map the family to the intended actions and report confusions. A separate integration reviewer checks actual files, background contrast, centering, optical size, alpha and family consistency. Require specific observations and a proposed correction. Reject and revise; do not send every weak draft to the user.
8. **User selection and preservation.** Present a compact comparison of the strongest directions. Record the selected exact file, source, hash, rejected readings and any qualifications. Reviewer preference is not user approval. Keep source and preview together so the next edit does not recreate the image from memory.
9. **Export and integration verification.** Decode final PNGs, verify 16×16 RGBA, bounds and expected transparency, inspect every file after export, then later compare packaged bytes with approved hashes. Native-scale screenshot review in the actual game is the final rendering check during #25, separately recorded from HTML/editor previews.

## First experiment for #24

Question: does editor refinement plus early independent review produce a base-Lunge icon that reads more clearly than the previous single-composition script workflow?

Build three base silhouettes, screen at native size, refine the strongest, and show the before/after at identical scale. If no silhouette survives review, change the visual concept instead of generating a larger batch of polished variants. After two unsuccessful refinement rounds, report the specific readability failure and try a different motif/tool route. This is a proposed iteration budget, not acceptance by elapsed time.

The first checkpoint delivers three native-size silhouette files, one refined candidate, editable source, a comparison artifact and reviewer findings. Then the user selects a direction before the four-icon family is finalized. Larger dive storyboards are a separate artifact with their own canvas size; they show Dash-like transformed aim and compact first-contact impact, without implying collision correctness or free elytra flight.

Keep disposable work under ignored `.local/lunge-art-prototype/`; extracted native references under `.local/lunge-art-reference/`. Save concise research/decisions in `docs/`. For actual prototype capture, archive only owned prototype sources/previews in an isolated throwaway branch and link the branch/verdict from #24/#25, following the prototype skill; do not capture third-party textures or unrelated staged changes. The current turn evaluated the workflow and blank editor controls; it did not create an art prototype requiring branch capture.

## Pass criteria

- File dimensions, export scale, mode, source linkage and eventual package identity are correct.
- Main visual reads at native/GUI scale without a caption rescuing it; variants remain distinguishable without relying solely on color.
- Outlines, highlights and negative spaces have a purpose; no accidental clipping, stray pixels or resampling haze.
- Visual weight and contrast fit the inspected native family.
- User approves the exact exported family, with remaining objections recorded rather than dismissed by reviewer consensus.

This method has been researched and the browser editor's size control tested. Improved Lunge art quality remains a claim to demonstrate through the pilot, not an outcome already achieved.
