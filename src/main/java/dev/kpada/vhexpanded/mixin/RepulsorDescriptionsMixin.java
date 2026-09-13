package dev.kpada.vhexpanded.mixin;
import iskallia.vault.config.AbilitiesDescriptionsConfig;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import iskallia.vault.init.ModConfigs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;
@Mixin(value=AbilitiesDescriptionsConfig.class,remap=false)
public abstract class RepulsorDescriptionsMixin {
    @Inject(method="getDescriptionFor",at=@At("HEAD"),cancellable=true)
    private void expanded$description(String id, CallbackInfoReturnable<MutableComponent> cir) {
        if("Repulsor".equals(id)) cir.setReturnValue(new TranslatableComponent("ability.vhexpanded.repulsor.description",
                keyword("repel","knockback"),keyword("radius","radius"),keyword("cube","absorb"),keyword("duration","duration"))
                .withStyle(s->s.withColor(ModConfigs.COLORS.getColor("text")))
                .append(new TextComponent("\n\n"))
                .append(new TranslatableComponent("ability.vhexpanded.repulsor.cast").withStyle(s->s.withColor(ModConfigs.COLORS.getColor("castType")))));
    }
    private static MutableComponent keyword(String key,String color) {
        return new TranslatableComponent("ability.vhexpanded.repulsor."+key).withStyle(s->s.withColor(ModConfigs.COLORS.getColor(color)));
    }
    @Inject(method="getCurrent",at=@At("HEAD"),cancellable=true)
    private void expanded$current(String id, CallbackInfoReturnable<List<String>> cir) {
        if("Repulsor".equals(id)) cir.setReturnValue(List.of("duration","manaCost","cooldown","adjustedRadius","repelRadius"));
    }
    @Inject(method="getNext",at=@At("HEAD"),cancellable=true)
    private void expanded$next(String id, CallbackInfoReturnable<List<String>> cir) {
        if("Repulsor".equals(id)) cir.setReturnValue(List.of("duration","manaCost","cooldown","adjustedRadius","repelRadius"));
    }
}
