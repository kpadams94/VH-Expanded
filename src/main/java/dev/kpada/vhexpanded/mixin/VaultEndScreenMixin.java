package dev.kpada.vhexpanded.mixin;

import dev.kpada.vhexpanded.client.ClientReceipts;
import dev.kpada.vhexpanded.client.ReceiptView;
import iskallia.vault.client.gui.screen.summary.VaultEndScreen;
import iskallia.vault.client.gui.screen.summary.VaultExitContainerScreenData;
import iskallia.vault.core.vault.Vault;
import iskallia.vault.core.vault.stat.VaultSnapshot;
import java.util.List;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = VaultEndScreen.class, remap = false)
public abstract class VaultEndScreenMixin {
    @Shadow @Final private VaultSnapshot snapshot;
    @Shadow @Final private UUID asPlayer;

    @Redirect(method = {"<init>(Liskallia/vault/core/vault/stat/VaultSnapshot;Lnet/minecraft/network/chat/Component;Ljava/util/UUID;ZZ)V", "lambda$new$26"}, at = @At(value = "INVOKE",
            target = "Liskallia/vault/client/gui/screen/summary/VaultExitContainerScreenData;applyGreedXpBonus(I)I"), require = 2)
    private int expanded$summary(int xp) {
        return ClientReceipts.award(snapshot, asPlayer, VaultExitContainerScreenData.applyGreedXpBonus(xp));
    }

    @Inject(method = "lambda$new$23", at = @At("HEAD"), cancellable = true)
    private static void expanded$receipt(VaultExitContainerScreenData data, Vault vault,
                                         CallbackInfoReturnable<List<Component>> cir) {
        var receipt = ((ReceiptView) data).expanded$receipt();
        // The native tooltip would show the unadjusted award; omit it for our receipts.
        if (receipt != null) cir.setReturnValue(List.of());
    }
}
