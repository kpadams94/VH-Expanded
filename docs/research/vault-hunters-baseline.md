# Vault Hunters Remastered baseline

Researched 2026-09-12. Purpose: support the VH Expanded v0.1 product discussion, not approve features or implementation.

## Target platform

The exact pack is **Vault Hunters Third Edition - Remastered**, published by Iskall85Team. It targets **Minecraft Java Edition 1.18.2 with Forge**. Its latest listed main release is **2.0.4**, uploaded July 24, 2026 (CurseForge file 8502758). Pin this release as a proposed research baseline; user installation compatibility has not yet been checked. [Release page](https://www.curseforge.com/minecraft/modpacks/vault-hunters-third-edition-remastered/files/8502758)

Forge's documentation for Minecraft 1.18 requires a **Java 17 JDK** and describes building mods with **Gradle/ForgeGradle** into a `.jar`. The exact Forge build used by Remastered 2.0.4 and the exact bundled Vault mod artifact remain unverified here; retrieve these from the release manifest before establishing the build. [Forge getting started](https://docs.minecraftforge.net/en/1.18.x/gettingstarted/)

## What Remastered changes

Remastered contains the final VH3 Update 21 experience with a revised mod selection and world generation. It is a separate pack intended for fresh worlds. Compared with the prior pack, it removes mods including Storage Drawers, Simple Storage Network, Pipez, and Terralith; additions include Sophisticated Storage, LaserIO, Industrial Foregoing, PneumaticCraft, and Regions Unexplored. This matters when selecting expansion content: older VH3 guides may describe mods absent from Remastered. [Official pack description](https://www.curseforge.com/minecraft/modpacks/vault-hunters-third-edition-remastered)

## Gameplay context

The basic loop is gathering overworld resources, fulfilling a Vault Altar's requirements to make a crystal, entering a procedurally generated vault, and returning with loot to develop the character and base. Higher vault levels increase both challenge and rewards. Custom gear, abilities, talents, bosses, artifacts, and research make up the larger progression. Research gates access to modded infrastructure. Remastered retains the artifact hunt, final challenge, and Greed endgame. [Official pack description](https://www.curseforge.com/minecraft/modpacks/vault-hunters-third-edition-remastered)

**Research** is the mod-unlocking progression. The official wiki describes Knowledge Essence from living chests being combined with Vault Diamonds and Benitoite into Knowledge Stars. Consuming a star grants a knowledge point to spend in the Research tab. Research groups organize functions such as storage, looting, power, production, and large mods. [Official Research wiki](https://wiki.vaulthunters.gg/Research)

**Expertises** are a separate progression choice from abilities and talents. The current official wiki describes earning one expertise point per five vault levels, and resetting expertises with a Neuralizer. Examples include Trinketer (trinket-use preservation), Artisan (crafting-potential preservation), Bounty Hunter (bounty convenience and another active slot), and Crystalmancer (additional crystal capacity). These are useful vocabulary and design context; verify the chosen expertise's exact behavior in the pinned pack before writing acceptance criteria. [Official Expertise wiki](https://wiki.vaulthunters.gg/Expertise)

## Useful first-party design rationale

The Update 9 notes explain that expertise choices were separated because these investments should not compete directly with abilities and talents. Their sparse points and reset costs were meant to make investment consequential. Treat the specific old costs and effects as historical. [Update 9](https://vaulthunters.gg/patch-notes/9.0.0)

The Update 11 notes explain a shift in Marketer toward player-controlled rerolls to reduce frustration from missing a desirable offer. They also discuss making trinkets easier to obtain while preserving the pleasure of discovering them. These provide examples of explicit developer intent, not universal rules for Expanded. [Update 11](https://vaulthunters.gg/patch-notes/11.0.0)

## Boundaries and remaining verification

- The official Research wiki still lists several mods that the Remastered page explicitly removes. Use it for concepts, not as the definitive Remastered research roster or pricing table.
- Historical patch notes describe Mystic; the current Expertise wiki describes Crystalmancer. Names, mechanics, costs, and reset rules must be tied to the selected release.
- Java is the likely language for new runtime behavior. Whether any individual feature needs Java or a mixin cannot be determined until its desired behavior and the existing extension/configuration points are examined. This is an engineering decision, not a required user-facing design question.
- Integrating a new mod requires distinguishing its installation from its research gate and recipe costs. Exact enforcement points and supported configuration mechanisms need source/config inspection; no claim is made here that a single config edit will implement the entire integration.
- No code was implemented and no game client/server was launched as part of this research.
