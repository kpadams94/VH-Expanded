package dev.kpada.vhexpanded.mixin;
import dev.kpada.vhexpanded.repulsor.*;
import iskallia.vault.skill.base.Skill;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(value = Skill.Adapter.class, remap = false)
public abstract class SkillAdapterMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void expanded$register(CallbackInfo ci) {
        var adapter = (Skill.Adapter)(Object)this;
        adapter.register("vhexpanded:repulsor", RepulsorAbility.class, RepulsorAbility::new);
        adapter.register("vhexpanded:repulsor_tiers", RepulsorTiers.class, RepulsorTiers::new);
    }
}
