package dev.kpada.vhexpanded.mixin;

import iskallia.vault.config.SkillDescriptionsConfig;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SkillDescriptionsConfig.class, remap = false)
public abstract class SkillDescriptionsMixin {
    @Inject(method = "getDescriptionFor", at = @At("HEAD"), cancellable = true)
    private void expanded$description(String name, CallbackInfoReturnable<MutableComponent> cir) {
        if (dev.kpada.vhexpanded.hearts.HeartResearch.NAME.equals(name)) {
            cir.setReturnValue(new TextComponent("Craft Heart Canisters and equip them in a Heart Amulet for up to ")
                    .append(new TextComponent("40 extra hearts").withStyle(s -> s.withColor(0xEF5555)))
                    .append(".\n\nAll four colors are unlocked together. Each color holds ten canisters. Removing the amulet removes its health bonus.\n\nMod Boxes can now reward an empty amulet or miniature red, yellow, and very rarely green hearts."));
            return;
        }
        if (!"Experienced".equals(name)) return;
        MutableComponent description = new TextComponent("Increases XP earned from ")
                .append(new TextComponent("vault runs").withStyle(style -> style.withColor(0x69D68F)))
                .append(".\n");
        for (int rank = 1; rank <= 5; rank++) {
            description.append("\n" + rank + " ")
                    .append(new TextComponent("+" + rank * 10 + "%")
                            .withStyle(style -> style.withColor(0x69D68F)));
        }
        cir.setReturnValue(description);
    }
}
