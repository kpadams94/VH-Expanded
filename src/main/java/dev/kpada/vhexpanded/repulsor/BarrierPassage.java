package dev.kpada.vhexpanded.repulsor;
import iskallia.vault.entity.boss.TheVesselEntity;
import iskallia.vault.entity.entity.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
public final class BarrierPassage {
    private BarrierPassage() {}
    public static boolean passes(Entity entity) {
        if (entity instanceof Player || entity instanceof PetEntity || entity instanceof TheVesselEntity) return true;
        // Ability identity AND player origin: a player-owned arrow/trident is still blocked.
        return entity instanceof Projectile p && p.getOwner() instanceof Player
                && (p instanceof VaultThrownJavelin || p instanceof VaultFireball || p instanceof IceBoltEntity
                || p instanceof VaultGrenade || p instanceof ToxicGrenadeEntity || p instanceof VaultDecoyProjectile
                || p instanceof VaultStormArrow || p instanceof LightningOrbEntity);
    }
}
