# Slice 1 — Experienced vault XP expertise

Deliver a playable addon that changes **Experienced** from vanilla experience-orb enhancement to a five-rank bonus to XP earned from vault runs. This is the first user-testable delivery and includes the minimal shared setup. Repulsor and Heart Canisters are not dependencies.

## Shared contract

Published shared foundation: [GitHub #17](https://github.com/kpadams94/VH-Expanded/issues/17). Read its complete contract together with this ticket; no other feature spec is needed to build this slice.

Apply `docs/specs/00-core.md`: Remastered 2.0.4 / Minecraft 1.18.2 / Java 17, confirm Forge and Vault pins, separate addon with minimal configuration, fresh-world solo testing, and a packaged slice handoff. Read that document before implementation. This feature specification controls its gameplay.

## Player behavior

- Keep the name **Experienced** and its existing expertise placement/icon unless the target configuration needs adjustment to accommodate ranks.
- Remove the entire original vanilla XP-orb benefit. Ordinary Minecraft XP behaves as it does without Experienced.
- Provide **five purchasable ranks**, each costing **one expertise point**. Total maximum investment is five points.
- Rank bonuses are +10%, +20%, +30%, +40%, and +50%. Rank zero is +0%. These are total bonuses, not compounding rank-by-rank.
- Apply the bonus to XP the player's vault run would otherwise award **after other applicable XP bonuses/modifiers**, once: `award = otherwiseEarnedRunXp * (1 + 0.10 * rank)`.
- Scope is the player's own vault-run award. XP items, vanilla orbs, commands, bounties, doll rewards, and companion awards must not gain this bonus merely because they use a shared XP helper. A reward that is not the player's own vault-run XP remains unaffected.
- Preserve the pack's eligibility rules for completed, bailed, failed, or other vault outcomes: multiply an otherwise-positive eligible run award, preserve zero, and do not manufacture an award for a run that normally grants none. Preserve level caps and existing award consumption rules.
- Keep normal expertise learning, reset/refund, and save/reload behavior. Remove the bonus immediately when the rank is reset. Migration of old characters is outside this fresh-world slice.
- Update the expertise description and relevant XP tracking/summary views so they explain the vault-run-only bonus and agree with the award actually granted. Keep the total gain and any level-up accounting consistent.

| Rank | Cost for this rank | Multiplier | Award when otherwise earned XP is 1,000 |
| --- | --- | --- | --- |
| 0 | — | 1.0 | 1,000 |
| 1 | 1 | 1.1 | 1,100 |
| 2 | 1 | 1.2 | 1,200 |
| 3 | 1 | 1.3 | 1,300 |
| 4 | 1 | 1.4 | 1,400 |
| 5 | 1 | 1.5 | 1,500 |

Approved stacking example: 1,000 base XP raised to 2,000 by another +100% bonus becomes **3,000** at rank 5, not 2,500. Engineering default for fractional final XP: preserve the pinned award pipeline's established conversion if it supports these semantics; otherwise floor once at the final integer award. Document the selected rule and use it consistently in the UI. Avoid intermediate rounding of this bonus.

## Implementation steps

1. Complete the shared setup and trace the actual solo end-of-vault award, vanilla-orb handler, expertise configuration, and display paths. Completion evidence: named classes/methods and the configuration entries actually used by the pinned artifact.
2. Extend the existing expertise to five ranks, configure the approved values/costs, and disable its vanilla-orb effect. Confirm load/reload preserves the entries and existing unrelated expertises.
3. Apply a recipient-aware run-only multiplier at the narrow eligible award boundary. Do not blindly modify a universal Vault XP method. Existing research names `VaultPlayerStats.consume` and a separate party distribution path; inspect them, but dedicated multiplayer implementation is deferred. Avoid double application if other systems delegate or synchronize XP.
4. Align tracking, summary, and persistent XP values, then build/install the deliverable and run the checks below. A running client with the five ranks visible is not sufficient completion.

Wold's reference removes the vanilla bonus but uses additive stacking at a broader helper. Reuse its approach only where it satisfies the multiplicative, run-only requirements here. [Reference implementation](https://github.com/iwolfking/Wolds-Vaults-Official-Mod/blob/7fb424101e416c6f293746bca2c6cfa0140c01e9/src/main/java/xyz/iwolfking/woldsvaults/mixins/vaulthunters/custom/MixinExperiencedExpertise.java), [award reference](https://github.com/iwolfking/Wolds-Vaults-Official-Mod/blob/7fb424101e416c6f293746bca2c6cfa0140c01e9/src/main/java/xyz/iwolfking/woldsvaults/mixins/vaulthunters/custom/MixinPlayerVaultStats.java).

## Acceptance and player test

- [ ] Packaged addon starts in the pinned fresh solo instance; no later-slice dependency is needed.
- [ ] Five ranks are displayed, each costs one point, and a sixth rank is unavailable.
- [ ] Controlled eligible awards match all six rows above; the stacking example grants 3,000; zero remains zero; fractional handling matches the documented rule.
- [ ] A vanilla-orb comparison at ranks 0 and 5 shows no expertise bonus. A vault-XP item comparison also shows none. Verify unrelated award callers identified during the trace remain unchanged.
- [ ] An actual vault run demonstrates the correct total and consistent tracker, summary, and stored player XP, including an award crossing a level boundary.
- [ ] Exercise relevant solo outcome paths or document why an outcome cannot award XP. Consuming/closing/reopening a summary or reloading must not grant the same award twice.
- [ ] Save/reload retains the rank; a normal reset removes the bonus and refunds points under native rules.
- [ ] Deliver the jar/overlay, install guide, test evidence, and a short player checklist: install, buy a rank, run a vault, compare the displayed award, reset, repeat.

Stop at this handoff. Let the user test Experienced before they request Slice 2. Multiplayer and existing-world certification are outside this ticket.
