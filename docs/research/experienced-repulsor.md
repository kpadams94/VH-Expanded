# Experienced and Repulsor research

Research date: 2026-09-12. Scope: evidence for the v0.1 design conversation, not implementation or a final specification.

Subsequent interview update: Q4 keeps 20 seconds at level 1 and the 4-second floor at level 17. Q5 selects a single immediate pulse followed by the barrier. Q6 limits Experienced to vault-run XP and excludes XP items. These decisions supersede corresponding initial open questions below; the planning frontier is authoritative for current decisions.

## Evidence and version boundary

The exact installed Remastered release and its code have not yet been inspected. Wold's Vaults is a separate expansion: its working source is evidence for an integration approach, not proof of compatibility with Remastered. The official wiki pages also predate some Remastered updates.

### Experienced

- The [official Expertise page](https://wiki.vaulthunters.gg/index.php?title=Expertise&oldid=11127), last edited March 26, 2026, describes Experienced as multiplying vanilla experience-orb value, with three displayed ranks. Confirm the target pack's actual configuration before migrating an existing player's investment.
- Wold's explicitly replaces Experienced's `onOrbPickup` with a no-op, removing that vanilla-XP bonus. [MixinExperiencedExpertise.java](https://github.com/iwolfking/Wolds-Vaults-Official-Mod/blob/7fb424101e416c6f293746bca2c6cfa0140c01e9/src/main/java/xyz/iwolfking/woldsvaults/mixins/vaulthunters/custom/MixinExperiencedExpertise.java)
- Its server-side `PlayerVaultStats.addVaultExp` injection reads unlocked Experienced expertise and adds the configured percentage to `VaultLevelsConfig.getExpMultiplier()`. This is an additive change to that multiplier; it is not necessarily equivalent to multiplying the final award by 1.5 when other bonuses exist. It targets a general XP-award method, so its source coverage must be checked before adopting the pattern. [MixinPlayerVaultStats.java](https://github.com/iwolfking/Wolds-Vaults-Official-Mod/blob/7fb424101e416c6f293746bca2c6cfa0140c01e9/src/main/java/xyz/iwolfking/woldsvaults/mixins/vaulthunters/custom/MixinPlayerVaultStats.java)
- Wold's also adjusts client XP tracking and the end-of-vault summary. Our acceptance checks should ensure the displayed award agrees with what the server grants. [Client tracker](https://github.com/iwolfking/Wolds-Vaults-Official-Mod/blob/7fb424101e416c6f293746bca2c6cfa0140c01e9/src/main/java/xyz/iwolfking/woldsvaults/mixins/vaulthunters/custom/MixinClientVaultXpTracker.java), [end screen](https://github.com/iwolfking/Wolds-Vaults-Official-Mod/blob/7fb424101e416c6f293746bca2c6cfa0140c01e9/src/main/java/xyz/iwolfking/woldsvaults/mixins/vaulthunters/custom/MixinVaultEndScreen.java)
- The addon's README distinguishes older MIT code from new code licensed GPLv3 beginning April 5, 2025. Consult file history and the actual applicable license before copying; independent implementation can use these files as behavioral reference. [Author's licensing statement](https://github.com/iwolfking/Wolds-Vaults-Official-Mod/blob/7fb424101e416c6f293746bca2c6cfa0140c01e9/README.md)

User-approved direction: retain the name Experienced, replace its vanilla-XP benefit with vault XP, five ranks granting +10%, +20%, +30%, +40%, +50%. These are cumulative totals, not five multiplicative bonuses. Source coverage and interaction with other XP modifiers remain decisions for the spec; do not silently inherit Wold's stacking semantics.

### Mana Barrier and Repulsor

- The [official Abilities page](https://wiki.vaulthunters.gg/Abilities) describes Mana Barrier as a temporary mana cube that blocks mobs while allowing the player through, and lists ranks 1–8. This is distinct from Mana Shield's damage absorption. Gear can influence displayed cooldown, duration, and radius. Exact overlevel behavior is not established by this page.
- Wold's active ability UI configuration puts `Mana_Shield_Base` and `Mana_Barrier` under the `Mana_Shield` family's `specializationStyles`. This supports implementing Repulsor as another choice in that family, preserving existing choices, if the target pack uses the same structure. [abilities_gui_styles.json](https://github.com/iwolfking/Wolds-Vaults/blob/794d4d9cae540e52443c818078c2a7899a789c06/config/the_vault/abilities_gui_styles.json)
- Wold's generated-configuration source contains a commented-out Mana Barrier builder. It is not executable evidence of current behavior or a reliable source for current cooldowns. No exact Remastered ability class was retrieved in this bounded research pass.

User direction: Repulsor pushes mobs away around the player and creates a short-lived barrier, with eight purchased ranks and further scaling through overlevels. Intended raw radius is `1 + 0.5 × (effective level − 1)` blocks. Effective levels 1, 8, and 16 therefore have radii 1, 4.5, and 8.5 blocks before other modifiers.

Unresolved high-level choices and arithmetic:

- Single outward pulse on cast versus continuous pushing during the barrier. Also specify whether the barrier stays where cast or follows the player when this becomes relevant to the desired feel.
- A starting cooldown of 20 seconds at level 1, falling one second per upgrade, gives `max(4, 21 − level)` seconds: level 16 is 5 seconds; the floor arrives at level 17. This conflicts with the user's level-16 example and needs reconciliation, not silent correction.
- A four-second base cooldown reduced by 80% is 0.8 seconds before tick rounding or any other enforced cooldown rule.
- Duration starts at 0.5 seconds and gains 0.5 every four levels. The first increase could mean level 4 or level 5; record the intended breakpoint before writing a table. Continued duration scaling and short reduced cooldowns may allow sustained barrier uptime; determine whether that is desired.
- Existing barrier geometry is described as cube blocks. Half-block radius increments may require deliberate treatment in the eventual collision/visual implementation; do not promise that changing a radius number in a configuration alone provides exact half-block barriers.
- Mana cost, knockback strength, and protection scope are still unspecified. Preserve the user-facing distinction between physically keeping mobs out and absorbing damage; they are different outcomes.

Suggested early playable sequence: first validate Experienced's five ranks and award/display consistency, then Repulsor's basic pulse-and-barrier feel, then full rank and overlevel scaling. This is a recommendation, not an approved delivery order.
