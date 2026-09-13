package dev.kpada.vhexpanded.mixin;

import com.traverse.bhc.common.util.HealthModifier;
import dev.kpada.vhexpanded.hearts.HeartResearch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = HealthModifier.class, remap = false)
public abstract class HeartHealthMixin {
    @ModifyVariable(method = "updatePlayerHealth", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private static boolean expanded$requireResearch(boolean addHealth, Player player, ItemStack stack, boolean original) {
        return addHealth && HeartResearch.unlocked(player);
    }
}
