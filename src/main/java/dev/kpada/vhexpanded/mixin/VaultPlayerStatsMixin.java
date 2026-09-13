package dev.kpada.vhexpanded.mixin;

import dev.kpada.vhexpanded.RunAwards;
import iskallia.vault.core.vault.stat.VaultSnapshot;
import iskallia.vault.world.data.PlayerVaultStatsData;
import iskallia.vault.world.data.VaultPlayerStats;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = VaultPlayerStats.class, remap = false)
public abstract class VaultPlayerStatsMixin {
    @Redirect(method = "consume", at = @At(value = "INVOKE", target = "Liskallia/vault/world/data/PlayerVaultStatsData;addVaultExp(Lnet/minecraft/server/level/ServerPlayer;I)Liskallia/vault/world/data/PlayerVaultStatsData;"))
    private static PlayerVaultStatsData expanded$ownRun(PlayerVaultStatsData data, ServerPlayer recipient,
                                                       int xp, ServerPlayer player, UUID vault) {
        return RunAwards.grant(data, recipient, xp, vault);
    }
    @Redirect(method = "prompt", at = @At(value = "INVOKE", target = "Liskallia/vault/world/data/VaultPlayerStats;getSnapshot(Ljava/util/UUID;)Ljava/util/Optional;"))
    private static Optional<VaultSnapshot> expanded$receipt(UUID vault, ServerPlayer player) {
        Optional<VaultSnapshot> snapshot = VaultPlayerStats.getSnapshot(vault);
        snapshot.ifPresent(s -> RunAwards.preview(player, vault, s));
        return snapshot;
    }
}
