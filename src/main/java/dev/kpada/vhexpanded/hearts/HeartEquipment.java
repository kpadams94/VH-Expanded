package dev.kpada.vhexpanded.hearts;

import com.traverse.bhc.common.init.RegistryHandler;
import com.traverse.bhc.common.util.HealthModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import top.theillusivec4.curios.api.CuriosApi;

public final class HeartEquipment {
    private HeartEquipment() {}

    /** Reconcile loaded equipment after research changes, respawn, and saved attribute restoration. */
    public static void tick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level.isClientSide || !event.player.isAlive()) return;
        reconcile(event.player);
    }

    public static void reconcile(net.minecraft.world.entity.player.Player player) {
        ItemStack amulet = CuriosApi.getCuriosHelper().findEquippedCurio(RegistryHandler.HEART_AMULET.get(), player)
                .map(entry -> entry.getRight()).orElse(ItemStack.EMPTY);
        if (amulet.isEmpty() && player.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH)
                .getModifier(HealthModifier.HEALTH_MODIFIER_ID) == null) return;
        // Native routine is a no-op if the existing amount is unchanged; retains wounded-health semantics.
        HealthModifier.updatePlayerHealth(player, amulet, HeartResearch.unlocked(player) && !amulet.isEmpty());
    }
}
