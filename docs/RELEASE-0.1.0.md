# Vault Hunters Expanded 0.1.0 — release record

Release date: 2026-09-13. Product name: **Vault Hunters Expanded**. The final 0.1.0 release consolidates the accepted Experienced, Repulsor and Heart Canisters slices; it introduces no further gameplay changes relative to the accepted `0.1.0-heart-canisters.2` build.

## Acceptance

The user confirmed that the final Heart Canisters retest was completed, covering the remaining QOL alignment and miniature-heart vault-use follow-up checks. Earlier Experienced and Repulsor acceptance is preserved in their feature evidence files. This is user-reported acceptance, not a claim that this release-finalization task independently replayed those scenarios.

The accepted gameplay build previously passed 24 JUnit checks and a full-pack native harness with 116 Heart Canisters, 26 Experienced and 59 Repulsor checks. Historical evidence and limitations remain in `heart-canisters-test-evidence.md` and `docs/evidence`.

## Finalization scope

- Version and bundle name normalized to 0.1.0; mod display name remains Vault Hunters Expanded.
- One release installation guide, changelog and durable development guide added.
- Final user retest marked accepted; initial release changelog has no Balance or Bug Fixes entries.
- Release packaging no longer depends on the ignored asset-staging directory. Optional visuals use tracked original resources.
- Runtime downloads/caches, extracted third-party material and the separate recipe-authoring repository remain outside the addon Git history.

## Release verification

`gradlew.bat build releaseBundle` passed with Java 17. All **24 JUnit tests passed**, with zero failures or errors. The final jar manifest reports `Implementation-Version: 0.1.0`; its SHA-256 is `cc0621b8844c2442a461931fd9dc8463c58dc05ac900ce792fff0dd97e85c02b`.

Comparing the final jar to the accepted `0.1.0-heart-canisters.2` jar found only `META-INF/MANIFEST.MF` changed: the gameplay classes and resources are byte-identical. The archive contains one addon jar and the required BHC drop overlay; staged file checksums were verified. The final bundle is `build/distributions/vault-hunters-expanded-0.1.0.zip`, with its own external SHA-256 file and internal `SHA256SUMS.txt`.

No new game session was needed for this metadata/documentation-only finalization. The accepted gameplay evidence is carried forward explicitly; this does not expand multiplayer or existing-world support.

## Support boundary

Fresh-world solo play is the accepted target. Dedicated-server harness checks are not multiplayer player certification. Existing-world migration and removal of persisted custom content are not certified. Third-party jars are acquired separately; their pins/hashes are in the compatibility record.
