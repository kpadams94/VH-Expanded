package dev.kpada.vhexpanded.mixin;
import dev.kpada.vhexpanded.repulsor.RepulsorAbility;
import iskallia.vault.init.ModAbilityLabelBindings;
import iskallia.vault.skill.ability.component.AbilityLabelFormatters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Map;
@Mixin(value=ModAbilityLabelBindings.class,remap=false)
public abstract class RepulsorBindingsMixin {
    @Inject(method="register()V",at=@At("TAIL"))
    private static void expanded$bindings(CallbackInfo ci) {
        ModAbilityLabelBindings.register(RepulsorAbility.class,Map.of(
                "adjustedRadius",a->AbilityLabelFormatters.decimal(a.getUnmodifiedRadius()),
                "repelRadius",a->AbilityLabelFormatters.decimal(a.getUnmodifiedRepelRadius()),
                "duration",a->AbilityLabelFormatters.ticks(a.getDurationTicks())));
    }
}
