package dev.kpada.vhexpanded.mixin;

import dev.kpada.vhexpanded.ExperiencedXp;
import dev.kpada.vhexpanded.client.ClientReceipts;
import iskallia.vault.client.data.ClientVaultXpTracker;
import iskallia.vault.core.vault.Vault;
import iskallia.vault.core.vault.stat.StatCollector;
import iskallia.vault.greed.GreedNodeHelper;
import iskallia.vault.init.ModConfigs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientVaultXpTracker.class, remap = false)
public abstract class ClientVaultXpTrackerMixin {
    @Shadow private ClientVaultXpTracker.VaultXpBreakdown breakdown;

    @Inject(method = "updateBreakdown", at = @At("RETURN"))
    private void expanded$estimate(Vault vault, StatCollector stats, CallbackInfo ci) {
        int input = stats.getExperience(vault);
        float greed = GreedNodeHelper.getClientXpGainMultiplier();
        if (greed > 0) input = (int) (input * (1.0f + greed));
        int total = ExperiencedXp.award((int) (input * ModConfigs.LEVELS_META.getExpMultiplier()), ClientReceipts.rank());
        float scale = ModConfigs.LEVELS_META.getExpMultiplier() * (1.0f + ClientReceipts.rank() / 10.0f);
        float chests = breakdown.chests() * scale;
        float ores = breakdown.ores() * scale;
        float mobs = breakdown.mobs() * scale;
        breakdown = new ClientVaultXpTracker.VaultXpBreakdown(total, chests, ores, mobs, total - chests - ores - mobs);
    }

    @ModifyVariable(method = "addNotification", at = @At("HEAD"), argsOnly = true)
    private float expanded$notification(float xp) {
        return xp * ModConfigs.LEVELS_META.getExpMultiplier() * (1.0f + ClientReceipts.rank() / 10.0f);
    }
}
