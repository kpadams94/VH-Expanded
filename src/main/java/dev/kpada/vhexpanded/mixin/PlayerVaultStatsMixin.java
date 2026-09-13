package dev.kpada.vhexpanded.mixin;

import dev.kpada.vhexpanded.RunAwardScope;
import iskallia.vault.skill.PlayerVaultStats;
import java.util.UUID;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(value = PlayerVaultStats.class, remap = false)
public abstract class PlayerVaultStatsMixin {
    @Shadow @Final private UUID uuid;
    @Shadow private int exp;

    // Only the accumulation write after native global scaling; all cap/level-up writes stay native.
    @Redirect(method = "addVaultExp", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD,
            target = "Liskallia/vault/skill/PlayerVaultStats;exp:I"),
            slice = @Slice(from = @At(value = "INVOKE", target = "Liskallia/vault/config/VaultLevelsConfig;getExpMultiplier()F"),
                    to = @At(value = "INVOKE", target = "Liskallia/vault/config/VaultLevelsConfig;getGreedTierXpMultiplier(I)D")))
    private void expanded$finalAward(PlayerVaultStats self, int accumulated) {
        this.exp += RunAwardScope.apply(this.uuid, accumulated - this.exp);
    }
}
