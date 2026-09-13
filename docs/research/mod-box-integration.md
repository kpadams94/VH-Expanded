# Mod Box integration research

Inspected 2026-09-13 against the staged target Vault binary `asset-staging/vault.jar`, SHA-256 `fc6adfeb76071d61e633027334fc95e8efb7faf8a4cb57d646f8176b4f75390b`. These are static bytecode findings, not runtime test results. The packaged config is separately preserved at [remastered-mod-box-config.json](remastered-mod-box-config.json).

## Existing behavior

`ModItems.MOD_BOX` is a `GatedLootableItem` registered as `the_vault:mod_box`. Its mapped `use(Level, Player, InteractionHand)` method (binary `m_7203_`) runs reward selection on the server. It calls `PlayerResearchesData.get(ServerLevel).getResearches(player).getResearchesDone()`, so eligibility uses the opening player's completed research names. See [item bytecode](evidence/mod-box/lootable.javap.txt).

1. `ModBoxConfig.collectPools(Iterable<String>)` deduplicates research strings and looks up each exact string in `POOL`. Null, missing, nonpositive-weight, and empty pools are skipped.
2. Select one eligible mod pool according to its integer `weight`.
3. Select one `ProductEntry` from that pool's `entries` according to the entry's integer `weight`.
4. If no product was selected, select from `POOL.None` instead. `None` is a fallback, not normally a pool competing with unlocked mods.
5. Generate the selected item stack, drop its contents by the player, consume one held box, and run success effects. Empty outcomes use failure effects and do not consume a box. The item-count loop splits the one generated stack into legal stack sizes; it is not a multi-box roll.

The config name is `mod_box`; the pack supplies `config/the_vault/mod_box.json`. Its schema is `POOL.<exact research name> = {weight: integer, entries: [{weight: integer, value: {id: namespaced item ID, amountMin: integer, amountMax: integer}}]}`. See [config and selection bytecode](evidence/mod-box/config-weighted-list.javap.txt).

`WeightedList.getRandom(Random)` calls `nextInt(getTotalWeight())` and selects by cumulative weight. Consequently:

`P(item per box) = selected pool weight / sum of eligible pool weights × item weight / sum of selected pool entry weights`.

There is no native fixed-per-box percentage field. Adding more eligible researches normally reduces each mod's share. The staged config uses weight 1 for its mod pools. Four BHC entry weights 100, 1000, 100, 1 would total 1201 and yield about 8.326%, 83.264%, 8.326%, 0.08326% *conditional on selecting BHC*, rather than the user's requested per-box percentages.

## Relevant opening routes

The native `UnboxingStationTileEntity.isAllowedPack(ItemStack)` explicitly accepts only `BoosterPackItem` or `JewelPouchItem`. It is not an alternative Mod Box opening path in the inspected binary. See [station bytecode](evidence/mod-box/unboxing-station.javap.txt). Any separate addon automation must be checked if introduced; this finding does not claim coverage of uninspected addon classes.

## Decision — native weighted integration approved

### Approved native weighted integration

Add one pool keyed by the exact new BHC research name using the owned-config merge mechanism. Register each output at amountMin=amountMax=1. Preserve existing pools and `None`. Use `bhc:heart_amulet`, `bhc:red_heart`, `bhc:yellow_heart`, and `bhc:green_heart`; these heart IDs are ingredients, not finished health canisters. Do not add blue hearts or finished canisters.

This route needs config only for loot, but cannot honestly promise fixed per-open percentages. The user approved native weighting: pool weight 1, red-heart entry weight 1000, amulet 100, yellow-heart 100, green-heart 1, total 1201. Treat the originally requested rates as relative rarity, not fixed per-open percentages. The pool key must exactly match the new completed-research string used elsewhere by the integration.

### Rejected alternative — fixed per-open integration (not implementation scope)

After BHC research is unlocked, use one mutually exclusive server roll with 10,000 equally likely outcomes: 100 amulet, 1000 red-heart, 100 yellow-heart, 1 green-heart, and 8799 ordinary native Mod Box outcomes. This is 1%, 10%, 1%, 0.01%, and 87.99%. Each BHC success returns exactly one item in place of the ordinary reward, never an extra reward. Before unlock, original behavior runs unchanged.

Use a narrow seam in `GatedLootableItem.use` guarded by identity with `ModItems.MOD_BOX`. Prefer replacing the selected local `ProductEntry` before item generation while retaining native delivery, consumption, and effects. The inspected local slot 8 holds the selected product, initially null, then assigned at bytecode offsets 102 and 130; the fallback branch starts at 109. A mapped local capture or equivalent narrow injection must be proved against the pinned dev artifact, not copied from offsets blindly. The existing method has no standalone public reward-selector helper.

A cancellable server-side entry hook that handles only successful BHC rolls and delegates the 87.99% remainder to untouched native behavior is an alternative if local capture is fragile. That route must reproduce native consume-one, drop-stack, and success-effects behavior on success, and must not roll a second time. Avoid global random/WeightedList interception.

Do not also add BHC to the native research pool in fixed mode: that would give extra BHC chances during the 87.99% ordinary branch and inflate specified probabilities. Existing native pool proportions remain unchanged conditional on ordinary outcomes. If BHC is the only unlocked research with no ordinary pool, the native remainder correctly uses `None`.

## Acceptance emphasis

Verify locked-player exclusion, exact item IDs/counts, no blue/finished-canister results, one consumed box per successful reward, no duplicated output, and unchanged ordinary relative selection. For fixed odds, deterministic boundary tests over the 10,000 roll values establish the probabilities; a casual few-box playtest cannot establish a 0.01% rate. Test manual opening on the target pack; rerun existing recipe/drop/research checks because Mod Boxes become an intentional additional acquisition route. Native mob-heart drops remain disabled.
