package dev.kpada.vhexpanded;

import java.util.UUID;
import net.minecraft.nbt.CompoundTag;

/** Server-authored, persisted receipt; historical views never use the viewer's current rank. */
public record RunReceipt(UUID player, UUID vault, int rank, int beforeBonus, int awarded, boolean claimed) {
    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("player", player);
        tag.putUUID("vault", vault);
        tag.putInt("rank", rank);
        tag.putInt("beforeBonus", beforeBonus);
        tag.putInt("awarded", awarded);
        tag.putBoolean("claimed", claimed);
        return tag;
    }
    public static RunReceipt load(CompoundTag tag) {
        return new RunReceipt(tag.getUUID("player"), tag.getUUID("vault"), tag.getInt("rank"),
                tag.getInt("beforeBonus"), tag.getInt("awarded"), tag.getBoolean("claimed"));
    }
}
