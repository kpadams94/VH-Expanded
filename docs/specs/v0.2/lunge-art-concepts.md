# Lunge icon concepts — round 1

User requested image-generated pixel-art-inspired concepts before native-grid work: their launch-ripple idea plus two alternatives. These are composition studies, not 16×16 assets, accepted exports, or animation evidence. The warm gold/orange palette is a comparison choice, not an approved family palette.

| Concept | Local artifact | Intended reading | Initial author critique |
|---|---|---|---|
| A — Launch ripple | `.local/lunge-art-prototype/concepts/A-launch-ripple.png` | User's compressed ripple with a curved streak launching from its center | Strong starting direction; ripple must remain a takeoff cue rather than imply the base ability deals AOE. Simplify rings and preserve the curved launch at native size. |
| B — Driving strike | `.local/lunge-art-prototype/concepts/B-driving-strike.png` | Heavy diagonal thrust with compact motion trails | Simple direction, but can read as a projectile or arrow. |
| C — Diving silhouette | `.local/lunge-art-prototype/concepts/C-diving-silhouette.png` | Compact armored figure launching headfirst | Literal subject, but detail and pose could read as flying/running armor and need substantial native-grid simplification. |

All three were produced with the built-in image generator as separate requests and displayed in chat. Original outputs remain in the session's generated-images directory; local copies above preserve them for the next stage. No resampling, pixel editing, or production replacement was performed. The critiques are the author's judgment, not independent blind review or user selection.

Selected direction: user chose A and approved its colors and general shape. They requested a stronger curve. The initial revision incorrectly transitioned into a straight diagonal thrust; the user rejected that interpretation. Their final instruction is a continuous curve throughout the entire streak, including its tip. Keep the launch ripple and approved palette.

Revisions: `.local/lunge-art-prototype/concepts/A1-rejected-straight-tip.png` preserves the rejected transition; `.local/lunge-art-prototype/concepts/A2-continuous-curve.png` is the corrected concept, approved by the user before authorizing native pixel work. This correction supersedes earlier curve-then-straight wording. B/C are not selected.

Native pilot: three actual 16×16 silhouettes were compared by an independent blind reviewer. Candidate 03 was preferred for its clearer ellipse opening and taller, narrower curve. Candidate 01 suggested a handle/pedestal; 02 crowded the lower junction. All retained some cup/steam or curled-letter ambiguity. Candidate 03 received warm shading and thirteen recorded pencil edits in Piskel, strengthening highlights and taper and subduing the ring's side projections.

Review artifact: `.local/lunge-art-prototype/lunge-curve-v1-review.png`. Actual candidate: `.local/lunge-art-prototype/native/lunge-curve-v1.png`. Editable pixel rows and recorded edits: adjacent `lunge-curve-v1.source.json`. Verified 16×16, seven visible colors, alpha values 0/255, and zero discrepancies between PNG pixels and saved source rows. SHA256: `CACCF9D3B0872D24191E79BC136DDB79C048897C18D660BEB766E64825A07ADB`.

Piskel's browser download did not complete. The delivered PNG was recovered by replaying the exact recorded pencil actions against the original native draft; it is not a successfully downloaded Piskel export. The editor tab is preserved for inspection. This establishes a usable fallback, while reliable editor export remains a workflow limitation.

User approved this exact native PNG on 2026-09-14, including the existing spacing. The durable approved asset, source, preview, and checksum are recorded in [the approval record](../../art/lunge/README.md). Earlier reviewer ambiguity is retained as history; in-game rendering and family consistency remain untested. No production asset was replaced.

Final independent review: continuous curve and palette pass as a selection draft; the reduced shape suggests a hooked flare/flame above an oval. The reviewer confirmed 146 transparent and 110 opaque pixels. Prototype source is preserved locally with a README; throwaway branch capture was attempted but repository object writes were denied by filesystem permissions. Branch capture and issue pointer remain pending.


## Impact concept — first study

User direction: straight descending central streak with stacked rings expanding outward, suggesting a strike falling from the sky and bursting on impact. Blue is the proposed palette; yellow belongs to approved base Lunge and red is reserved for Flurry by the user. The generated study is `.local/lunge-art-prototype/concepts/Impact-A-blue-stacked-rings.png`.

This is a composition study awaiting user selection, not a native 16×16 asset. It uses three increasingly wide elliptical rings and an icy-blue core. Native translation should simplify the bottom splash, preserve gaps between rings, and provide a top margin (the generated streak touches the upper edge). Base Lunge's approved PNG remains unchanged.

Impact palette revision: the user accepted the stacked-ring composition but found icy blue implied a cold effect. They requested a comparison using base Lunge's ivory/gold/orange/brown palette; shared colors may be acceptable if the silhouettes are distinct enough. Recolored study: `.local/lunge-art-prototype/concepts/Impact-B-lunge-palette.png`. Palette choice and final native Impact PNG remain pending; this request does not change the approved base asset or Flurry's proposed red.

## Impact native draft — warm concept accepted

The user selected the recolored warm concept and authorized native pixel work. Three 16×16 silhouettes are preserved in `.local/impact-art/native/01.png` through `03.png`; blind review preferred 01 for separated balanced tiers, while noting tree/antenna associations. The suggested removal of a below-base central pixel did not correspond to a pixel in the source, so it was not applied.

Candidate `.local/impact-art/native/impact-v1.png` adds shared Lunge shading and eleven Piskel pencil/eraser actions: eight highlight pixels and three erased top-edge pixels. The exact edits and all RGBA rows are saved in the adjacent source JSON. Editor download timed out again; final PNG was recovered through exact edit replay, then verified against source. The editor tab is preserved.

Preview: `.local/impact-art/impact-v1-review.png`, showing unchanged approved Lunge beside Impact at native and enlarged scales. SHA256: `4D2DA376E87D36974E017F90A65C5D6393FD8590E2E379DDF270BA3FFF2CA4BE`. Native Impact approval remains pending. No production asset was replaced; game rendering remains untested.

Final independent review: 16×16 dimensions, binary alpha, and all 256 source pixels match. The reviewer finds Impact distinct from Lunge at 1×/3×, with rings clear at 3×; at 1× the connected dark shading can suggest a tiered cone, weakening the sense of separated shockwaves. This concern is presented for user review rather than treated as approval.

Impact approval: the user approved the displayed native draft and moved on to Siphon. Exact asset/source/preview are preserved in [the Impact approval record](../../art/impact/README.md); this supersedes pending-native-approval wording above.

## Siphon concept direction

User requests a line ending in a mid-air burst: the line may use the existing yellow/gold palette, the burst should be red, with roughly three or four green healing pluses/twinkles around it. Use clear chunky marks that can survive native pixel translation; the user corrected hearts to pluses. First concept will use three separated green plus-shaped twinkles. Concept and native PNG approval remain pending.

First generated Siphon study: `.local/lunge-art-prototype/concepts/Siphon-A-red-burst-green-pluses.png`. The diagonal lower-left-to-upper-right orientation is an author proposal. Native translation should simplify the burst spikes and remove stray golden particles, preserving three separate green healing marks. Awaiting concept selection.

Siphon revision, 2026-09-15: user accepted the concept direction subject to a slightly curved yellow beam and a thinner base. Revised study: `.local/lunge-art-prototype/concepts/Siphon-B-curved-tapered-beam.png`, retaining red burst and three green pluses. Carry the curve and tapered origin into native-grid work; simplify stray gold particles. This is concept direction approval with specified corrections, not approval of a final native PNG.

Siphon curve correction: user rejected B's S-bend. Require one pronounced continuous arc matching primary Lunge, with no change in curvature direction; retain tapered tail, red burst and three green healing pluses. Revised study `.local/lunge-art-prototype/concepts/Siphon-C-single-arc.png` uses the approved base-Lunge concept as a curve reference. B is rejected; C awaits visual review. This supersedes the earlier gentle-curve interpretation.

Siphon curvature refinement: C's arc was too aggressive. User wants a gentler single bend, like a palm tree bent by wind. D (`.local/lunge-art-prototype/concepts/Siphon-D-palm-arc.png`) keeps a thin, mostly upright base that gradually leans into the red burst. Preserve a single curvature direction, no S bend, and avoid C's semicircular hook. Red burst and three green pluses unchanged in intent. D awaits user review.

Siphon concept approved: user selected D's gentle palm-like single arc. Preserve the thin upright origin gradually leaning into the red burst and three green pluses. This supersedes pending-D status above; native 16×16 artwork is now being developed separately.

## Siphon native draft

Three silhouettes and colored drafts are preserved under `.local/siphon-art/native/`. Blind reviewer preferred 02's thinner shaft but noted all could resemble a magic wand; author retained 01's more curved trajectory to honor the approved palm-arc shape and thinned its base in Piskel. Six recorded edits also connect the beam highlight to the burst and add coral highlights. Actual candidate: `.local/siphon-art/native/siphon-v1.png`; editable RGBA rows and edit log are adjacent in `siphon-v1.source.json`. Preview: `.local/siphon-art/siphon-v1-review.png`.

Verified 16×16 RGBA, ten visible colors, alpha 0/255, zero mismatches with all saved pixel rows. SHA256: `D11A91E2A9D77786C6125016269D4A24CF990A1F480C14F355D268045CA627BF`. Browser export timed out; PNG recovered from recorded Piskel edits, with editor tab preserved. Native PNG approval and in-game review remain pending; no production assets changed.

Final independent Siphon review: distinct from Lunge/Impact at 1× and 3×; gentle single arc and all three pluses remain recognizable. Red burst dominates and may read as an attack/explosion; green pluses carry the healing cue. Reviewer independently confirmed dimensions, alpha and all source pixels. Native approval remains with the user.

Siphon native approval: user accepted the displayed draft and moved to Flurry. Exact asset/source/preview are preserved in [the Siphon approval record](../../art/siphon/README.md). This supersedes pending native approval above.

## Flurry concept direction

User requests a zigzag pattern of streaks, each successive streak becoming more red. First composition study will show a sequential gold-to-orange-to-scarlet-to-deep-red progression, with separated readable strokes. Concept and native PNG approval remain pending.

First Flurry composition: `.local/lunge-art-prototype/concepts/Flurry-A-zigzag-red-progression.png`. Four alternating diagonal strokes progress bottom-to-top from gold to orange to scarlet to crimson. Four strokes and upward ordering are author proposals, pending user concept selection; native PNG remains unmade.

Flurry refinement: user requests connected movement between streaks, with small bursts at the contacts, reading as an attack bouncing between targets. Revised concept `.local/lunge-art-prototype/concepts/Flurry-B-connected-bounces.png` connects the four zigzag legs and adds compact flashes at the three turns and final contact, retaining increasing redness. This supersedes A's separated-streak interpretation. Concept approval remains pending; imagery does not add automatic bouncing or targeting to gameplay.

Flurry curve refinement: user requests a gentle arc on every connecting path. Preserve connected bounce flashes and increasing redness. C left the gold starter too straight; D (`.local/lunge-art-prototype/concepts/Flurry-D-curved-bounces.png`) strengthens the gentle single arc on each leg, including the gold origin. No S-bend within a leg. D awaits concept selection; native PNG not yet authored.

Flurry concept approved: user selected D, the second and more curved revision. Preserve gentle arcs on every leg, connected contact flashes, and gold-to-red progression. This supersedes pending-D status; native pixel approval remains separate.

## Flurry native draft

Three native silhouettes and colored studies are preserved under `.local/flurry-art/native/`. Blind review preferred 01's separated bends, while noting the silhouette could read as lightning rather than successive motions. The draft uses gold/orange/scarlet/crimson legs and ivory contact centers. Ten recorded Piskel edits refine highlights and taper the origin. At this grid size, shallow concept arcs become stepped bends; smooth-curve fidelity is limited.

Candidate `.local/flurry-art/native/flurry-v1.png`; source rows and edits in adjacent `flurry-v1.source.json`; review board `.local/flurry-art/flurry-v1-review.png` compares the three approved icons. Verified 16×16 RGBA, six colors, alpha 0/255, exact match to all saved source rows. SHA256: `B19F7F195111FDF648981E5788FA653F3084D7537D5CA11982E876846C067AA9`. Browser download timed out; the final PNG is recovered from recorded edits. Editor tab preserved. Native Flurry approval remains pending; no production integration or in-game check.

Final independent Flurry review: connected four-leg zigzag and gold-to-red progression clear at 3×, distinct from the approved family. At 1×, lightning-like silhouette remains and individual arcs/flashes are less apparent. Reviewer confirmed 16×16 dimensions, binary alpha and all 256 source pixels. User selection remains pending.

Flurry native approval: user accepted the exact displayed PNG. See [Flurry approval](../../art/flurry/README.md) and [approved family manifest](../../art/README.md). All four native icons are now approved; this supersedes pending-native selection above.
