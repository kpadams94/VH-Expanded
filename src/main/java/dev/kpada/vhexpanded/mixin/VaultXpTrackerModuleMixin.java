package dev.kpada.vhexpanded.mixin;

import dev.kpada.vhexpanded.client.ClientReceipts;
import iskallia.vault.client.render.hud.module.vault.VaultXpTrackerModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = VaultXpTrackerModule.class, remap = false)
public abstract class VaultXpTrackerModuleMixin {
    @ModifyConstant(method = "buildLines", constant = @Constant(stringValue = "Vault XP: %s"))
    private String expanded$runEstimateLabel(String original) {
        int rank = ClientReceipts.rank();
        return rank == 0 ? "Run XP estimate: %s" : "Run XP estimate (+" + rank * 10 + "%%): %s";
    }
}
