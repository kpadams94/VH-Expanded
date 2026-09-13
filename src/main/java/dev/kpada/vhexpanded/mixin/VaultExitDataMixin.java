package dev.kpada.vhexpanded.mixin;

import dev.kpada.vhexpanded.RunReceipt;
import dev.kpada.vhexpanded.client.ClientReceipts;
import dev.kpada.vhexpanded.client.ReceiptView;
import iskallia.vault.client.gui.screen.summary.VaultExitContainerScreenData;
import iskallia.vault.core.vault.stat.VaultSnapshot;
import java.util.UUID;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = VaultExitContainerScreenData.class, remap = false)
public abstract class VaultExitDataMixin implements ReceiptView {
    @Shadow @Final protected VaultSnapshot snapshot;
    @Shadow @Final private UUID asPlayer;
    @Override public RunReceipt expanded$receipt() { return ClientReceipts.find(snapshot, asPlayer); }
    @Redirect(method = "getVaultLevelPercentageWithReward", at = @At(value = "INVOKE",
            target = "Liskallia/vault/client/gui/screen/summary/VaultExitContainerScreenData;applyGreedXpBonus(I)I"))
    private int expanded$progress(int xp) {
        RunReceipt receipt = expanded$receipt();
        if (receipt == null) return VaultExitContainerScreenData.applyGreedXpBonus(xp);
        return receipt.claimed() ? 0 : receipt.awarded();
    }
}
