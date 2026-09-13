package dev.kpada.vhexpanded.mixin;

import iskallia.vault.skill.expertise.type.ExperiencedExpertise;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ExperiencedExpertise.class, remap = false)
public abstract class ExperiencedExpertiseMixin {
    @Inject(method = "onOrbPickup", at = @At("HEAD"), cancellable = true)
    private static void expanded$ordinaryOrbs(PlayerXpEvent.PickupXp event, CallbackInfo ci) { ci.cancel(); }
}
