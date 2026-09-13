package dev.kpada.vhexpanded;

import iskallia.vault.core.vault.Vault;
import iskallia.vault.core.vault.stat.VaultSnapshot;
import iskallia.vault.greed.GreedNodeHelper;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModGameRules;
import iskallia.vault.world.VaultPartyExpSharing;
import iskallia.vault.world.data.LevelSyncData;
import iskallia.vault.world.data.PlayerExpertisesData;
import iskallia.vault.world.data.PlayerGreedTreeData;
import iskallia.vault.world.data.PlayerVaultStatsData;
import iskallia.vault.world.data.VaultPartyExpData;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;

public final class RunAwards {
    private RunAwards() {}
    public static int rank(ServerPlayer player) {
        return ExperiencedConfig.rank(PlayerExpertisesData.get(player.getLevel()).getExpertises(player));
    }
    public static boolean shared(ServerPlayer player, UUID vault) {
        var server = player.getServer();
        var sync = LevelSyncData.get(server);
        return (sync.isEnabled(server) && sync.isInTeam(player.getUUID()))
                || (server.getGameRules().getRule(ModGameRules.PARTY_EXP_SHARING).get() != VaultPartyExpSharing.DISABLED
                    && VaultPartyExpData.get(server).getVaultXpMap(vault).size() > 1);
    }
    public static int nativeAward(int input) { return (int) (input * ModConfigs.LEVELS_META.getExpMultiplier()); }

    public static void preview(ServerPlayer player, UUID vault, VaultSnapshot snapshot) {
        var ledger = RunReceipts.get(player.getServer());
        var existing = ledger.find(player.getUUID(), vault);
        if (existing != null && existing.claimed()) { Expanded.send(player, existing); return; }
        int rank = shared(player, vault) ? 0 : rank(player);
        int input = shared(player, vault) ? 0 : snapshot.getEnd().get(Vault.STATS)
                .get(player.getUUID()).getExperience(snapshot.getEnd());
        float greed = GreedNodeHelper.getXpGainMultiplier(player);
        if (greed > 0) input = (int) (input * (1.0f + greed));
        int before = nativeAward(input);
        var stats = PlayerVaultStatsData.get(player.getLevel()).getVaultStats(player);
        int tier = PlayerGreedTreeData.get(player.getServer()).getGreedTier(player.getUUID());
        int award = stats.getVaultUncappedLevel() >= iskallia.vault.skill.PlayerVaultStats.getLevelCap(tier)
                ? 0 : ExperiencedXp.award(before, rank);
        RunReceipt receipt = new RunReceipt(player.getUUID(), vault, rank, before, award, false);
        ledger.put(receipt);
        Expanded.send(player, receipt);
    }

    public static PlayerVaultStatsData grant(PlayerVaultStatsData data, ServerPlayer player, int input, UUID vault) {
        int rank = shared(player, vault) ? 0 : rank(player);
        try (RunAwardScope scope = new RunAwardScope(player.getUUID(), rank)) {
            data.addVaultExp(player, input);
            RunReceipt receipt = new RunReceipt(player.getUUID(), vault, rank,
                    scope.used() ? scope.beforeBonus() : nativeAward(input), scope.awarded(), true);
            RunReceipts.get(player.getServer()).put(receipt);
            Expanded.send(player, receipt);
            Expanded.LOGGER.info("Experienced run {} player {} rank {}: {} -> {} XP", vault,
                    player.getUUID(), rank, receipt.beforeBonus(), receipt.awarded());
        }
        return data;
    }
}
