# Lunge visual handoff

User-selected direction, 2026-09-15. This brief completes the animation/particle design choice; production implementation and in-game acceptance remain in #25.

## Icons

Use the four exact [approved PNGs and hashes](../../art/README.md). No additional artwork is needed for the selected animation or particles.

## Player presentation

All branches use a horizontal, headfirst dive resembling the vanilla glide pose, with both arms extended forward in a crawl-like reach. Hold the reach during the dive rather than running a swimming stroke cycle. First person receives no bespoke hand animation, overlay or cinematic effect.

The visual lasts for the active dive and restores safely on landing/contact or lifecycle interruption. Chained casts continue the active presentation without stacking overrides. Preserve existing movement, collision, ordinary fall damage and actual Elytra behavior; obtaining the pose must not enable real fall-flying. Synchronize the visual so observers see the same active dive. Armor and sleeves must follow the arms.

## Contact effects

Use vanilla `ParticleTypes.CRIT` at actual impact for the family. These are visual particles only and do not make the attack a gameplay critical hit or change its damage/proc rules.

Impact additionally uses the existing native Nova explosion appearance at a smaller scale, preserving its existing colors. Center it on the actual contact point, including enemy and terrain contacts. Recast, cancellation and a missed dive do not synthesize a burst. Keep the effect local to the approved Impact burst area (base radius 2 with approved area scaling); exact visual scale and particle density are implementation tuning, not a newly selected balance value.

## Checked feasibility and implementation guidance

Pinned local source was inspected, not run in-game:

- `Player.setForcedPose(FALL_FLYING)` and actual `isFallFlying()` are distinct; the forced pose alone does not select vanilla Elytra rotation/physics. Existing pose research covers compact hitbox and restoration limits. Use Lunge-specific third-person presentation where the default interpolation is unsuitable.
- `HumanoidModel` exposes arm rotations. Its swim animation cycles through a 26-unit stroke rather than holding both arms forward. A narrowly scoped Lunge model adjustment after ordinary animation can hold the requested reach; include sleeves/armor and exclude first-person hand rendering.
- `PlayerRenderer.renderHand` has its own setup and arm resets. Preserve that ordinary first-person rendering. Pose-driven eye-height/camera behavior still needs testing; no custom camera animation is requested.
- `NovaParticleMessage` takes a position and radius, and `spawnParticles` uses the radius for spawn offsets and velocities. It emits native Nova wave/cloud/main particles. This provides a reuse path for a smaller spatial effect. It does not itself scale each sprite; `NovaExplosionCloudParticle` has a fixed initial `quadSize` of 1.0. If radius reduction alone remains visually oversized, tune particle size/count locally without modifying normal Nova or introducing new art.

Local evidence: `.local/lunge-visual-check/net/minecraft/client/model/HumanoidModel.java`, `.local/lunge-visual-check/iskallia/vault/network/message/NovaParticleMessage.java`, `.local/lunge-minecraft-decompiled/net/minecraft/client/renderer/entity/player/PlayerRenderer.java`, `.local/kill-credit-research/iskallia/vault/client/particles/NovaExplosionCloudParticle.java`. Third-party decompilation stays local and is not redistributed.

## Remaining verification

In #25, check prompt horizontal posing and forward arms, short and prolonged dives, ground/air/water/Elytra cases, valid pose restoration, chained casts, armor/sleeves, normal first-person hands and camera behavior, multiplayer visibility, crit placement, and compact Nova appearance matching the contact area. The user's design approval is not an in-game visual test.
