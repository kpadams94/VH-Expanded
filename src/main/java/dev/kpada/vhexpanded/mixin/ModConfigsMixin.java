package dev.kpada.vhexpanded.mixin;

import dev.kpada.vhexpanded.ExperiencedConfig;
import iskallia.vault.init.ModConfigs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ModConfigs.class, remap = false)
public abstract class ModConfigsMixin {
    @Inject(method = "register", at = @At("TAIL"))
    private static void expanded$merge(CallbackInfo ci) {
        ExperiencedConfig.merge();
        dev.kpada.vhexpanded.repulsor.RepulsorConfig.merge();
        dev.kpada.vhexpanded.hearts.HeartResearch.merge();
    }
}
