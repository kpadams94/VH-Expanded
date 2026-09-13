package dev.kpada.vhexpanded.mixin;
import dev.kpada.vhexpanded.repulsor.RepulsorAbility;
import iskallia.vault.skill.ability.component.*;
import iskallia.vault.util.calc.*;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import iskallia.vault.init.ModConfigs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Locale;
@Mixin(value=AbilityLabelFactory.class,remap=false)
public abstract class RepulsorLabelsMixin {
    @Inject(method="create",at=@At("HEAD"),cancellable=true)
    private static void expanded$values(String key, AbilityLabelContext<?> context, CallbackInfoReturnable<MutableComponent> cir) {
        if(!(context.config() instanceof RepulsorAbility a)) return;
        var p=Minecraft.getInstance().player;
        String label; double value; String suffix=""; String color=key;
        switch(key) {
            case "cooldown" -> { label="Cooldown"; value=(p==null?a.getCooldownTicks():CooldownHelper.adjustCooldown(p,a,a.getCooldownTicks()))/20d; suffix="s"; }
            case "manaCost" -> { label="Mana Cost"; value=p==null?a.getManaCost():a.up((skill,cost)->ManaCostInvoker.expanded$adjust(iskallia.vault.snapshot.AttributeSnapshotHelper.getInstance().getSnapshot(p),skill.getId(),cost),a.getManaCost()); }
            case "adjustedRadius" -> { label="Barrier Radius"; color="radius"; value=p==null?a.getUnmodifiedRadius():a.getRadius(p); }
            case "repelRadius" -> { label="Repel Radius"; color="radius"; value=p==null?a.getUnmodifiedRepelRadius():a.getRepelRadius(p); }
            case "duration" -> { label="Duration"; value=a.getDurationTicks()/20d; suffix="s"; }
            case "level" -> { label="Min Level"; color=context.vaultLevel()<a.getUnlockLevel()?"levelLo":"levelHi"; value=a.getUnlockLevel(); }
            default -> { return; }
        }
        String number=String.format(Locale.ROOT,"%.2f",value).replaceAll("0+$","").replaceAll("\\.$","");
        final String valueColor=color;
        cir.setReturnValue(new TextComponent("\n "+label+": ").withStyle(s->s.withColor(ModConfigs.COLORS.getColor("text")))
                .append(new TextComponent(number+suffix).withStyle(s->s.withColor(ModConfigs.COLORS.getColor(valueColor)))));
    }
}
